package com.zeromax.users.utils;

import com.zeromax.users.entity.user.UserEntity;
import com.zeromax.users.exeptions.ForbiddenRequestException;
import com.zeromax.users.exeptions.UnauthorizedRequestException;
import com.zeromax.users.models.LoginClaimModel;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.sql.Date;
import java.time.Instant;

public class JWTUtils {

  private JWTUtils(){
    throw new AssertionError();
  }


  public static LoginClaimModel getUserDetails(String token, String secret)
      throws ForbiddenRequestException, UnauthorizedRequestException {
    try {
      var body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
      LoginClaimModel loginClaimModel = new LoginClaimModel();
      loginClaimModel.setId(body.get("id").toString());
      loginClaimModel.setEmail(body.get("email").toString());
      if(body.get("userType") != null)  loginClaimModel.setUserType(body.get("userType").toString());

      return loginClaimModel;
    } catch (SignatureException e) {
      throw new UnauthorizedRequestException("Invalid token, please try logging in again");
    } catch (ExpiredJwtException e) {
      throw new UnauthorizedRequestException("Expired token, please try refreshing");
    } catch (Exception e) {
      throw new UnauthorizedRequestException();
    }
  }

  public static String createToken(UserEntity user, String companyName, String secret,
                                   long interval) {
    return Jwts.builder()
            .setIssuer(companyName)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.ofEpochMilli(Instant.now().toEpochMilli() + interval)))
            .claim("id", user.getId())
            .claim("email", user.getEmail())
            .claim("userType", user.getUserType().getSlug())
            .signWith(
                    SignatureAlgorithm.HS256,
                    secret
            )
            .compact();
  }
}
