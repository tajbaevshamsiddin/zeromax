import firebase_admin
from firebase_admin import messaging, credentials
from config import cred


firebase_admin.initialize_app(cred)


def send_push(title, msg, registrationToken):
    message = messaging.MulticastMessage(
        notification=messaging.Notification(
            title=title,
            body=msg
        ),
        tokens=registrationToken
    )

    response = messaging.send_multicast(message)
    print("Sent:", msg, response)
