package com.zeromax.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeromax.gateway.exeptions.CustomErrorModel;
import io.jsonwebtoken.Jwts;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private static final String TOKEN_SECRET = "mDV6AzwMxcMeOgINw0aVGSLABQYyRQTUYTDYL2yzrvEaE1wMUg3Ad7PnOBuEzZero";
    private static final String ROOT_TOKEN_SECRET = "PDMtzzRJL3fSNRDUWES8W5Fmij48RQGWk2BZakYwAYVBUSWvjqggB87CiRzZeroROOT";

    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (request.getHeaders().containsKey("ROOT")){
                var header = Objects.requireNonNull(request.getHeaders().get("ROOT")).get(0);
                var token = header.replace("Bearer ", "");
                if (!isJWTValid(token, ROOT_TOKEN_SECRET)){
                    return onError(exchange, "JWT token is not valid");
                }
                return chain.filter(exchange.mutate().request(
                                exchange.getRequest().mutate()
                                        .header("userType", getUserType(token))
                                        .build())
                        .build());
            }

            if (!request.getHeaders().containsKey("Authorization")) {
                return onError(exchange, "No authorization header");
            }

            String authorizationHeader = Objects.requireNonNull(request.getHeaders().get("Authorization")).get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");

            if(!isJWTValid(jwt, TOKEN_SECRET)){
                return onError(exchange, "JWT token is not valid");
            }
            return chain.filter(exchange.mutate().request(
                    exchange.getRequest().mutate()
                            .header("userId", getUserId(jwt))
                            .build())
                    .build());
        });
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message) {
        CustomErrorModel errorModel = new CustomErrorModel(message, "0000", HttpStatus.UNAUTHORIZED.value());
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        ObjectMapper objMapper = new ObjectMapper();
        try {
            byte[] bytes = objMapper.writer().withDefaultPrettyPrinter().writeValueAsBytes(errorModel);
            DataBuffer buffer = serverHttpResponse.bufferFactory().wrap(bytes);
            serverHttpResponse.getHeaders().add(HttpHeaders.WWW_AUTHENTICATE, "Bearer realm=\"Not authenticated\"");
            serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return serverHttpResponse.writeWith(Mono.just(buffer));
        } catch (Exception ex) {
            var response = exchange.getResponse();
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return response.setComplete();
        }
    }

    private boolean isJWTValid(String jwt, String TOKEN_SECRET) {
        boolean returnValue = true;

        try {
            String subject = Jwts.parser()
                    .setSigningKey(TOKEN_SECRET)
                    .parseClaimsJws(jwt)
                    .getBody()
                    .toString();


            if(subject == null || subject.isEmpty()) {
                returnValue = false;
            }
        }catch (Exception ex){
            returnValue = false;
        }
        return returnValue;
    }

    public String getUserId(String jwt){
        return Jwts.parser()
                .setSigningKey(TOKEN_SECRET)
                .parseClaimsJws(jwt)
                .getBody()
                .get("id")
                .toString();
    }

    public String getUserType(String jwt){
        return Jwts.parser()
                .setSigningKey(ROOT_TOKEN_SECRET)
                .parseClaimsJws(jwt)
                .getBody()
                .get("userType")
                .toString();
    }
}
