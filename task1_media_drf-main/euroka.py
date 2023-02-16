import random
import string
import os
import py_eureka_client.eureka_client as eureka_client


N = 32
res = ''.join(random.choices(string.ascii_lowercase + string.digits, k=N))


server_port = 30009


def eureka_init():
    print("working eureka")
    eureka_client.init(eureka_server="http://192.168.49.2:30888/eureka",
        app_name="media",
        instance_port=30009,
        instance_id=("media:" + str(res)),
        instance_ip='192.168.49.2',
        instance_host='192.168.49.2',
    )
    