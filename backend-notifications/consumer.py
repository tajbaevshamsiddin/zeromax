import pika
import json, threading
from notify.functions import to_one_user, to_all_users
from notify.database import UserToken


def callback(ch, method, properties, body):
    data = json.loads(body)
    senderId = data['senderId']
    userId = data['userId']
    msg = data['message']
    profile = UserToken.query.filter_by(userId=userId).first()

    if profile:
        to_one_user(profile=profile, message=msg, senderId=senderId, notificationType=None)
    print(data)

def receive_command():
    
    import os
    # url = os.environ.get('CLOUDAMQP_URL','amqp://test:test@localhost/%2f')
    url = os.environ.get('CLOUDAMQP_URL','amqp://zeromaxinc:password@192.168.49.2:31993/%2f')
    params = pika.URLParameters(url)
    params.socket_timeout = 5

    connection = pika.BlockingConnection(params) # Connect to CloudAMQP
    channel = connection.channel()
    
    channel.exchange_declare(exchange='directExchange', exchange_type='direct', durable=True)

    result = channel.queue_declare(queue='notificationQueue', durable=True)
    queue_name = result.method.queue


    channel.queue_bind(exchange='directExchange', queue=queue_name, routing_key='notificationBind')


    print(' [*] Waiting for logs. To exit press CTRL+C')

    channel.basic_consume(queue=queue_name, on_message_callback=callback, auto_ack=True)

    channel.start_consuming()
    channel.stop_consuming()


def start():
    t_msg = threading.Thread(target=receive_command)
    t_msg.daemon = True
    t_msg.start()
    t_msg.join(0)
    #self.receive_command()