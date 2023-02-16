from flask import Response
from notify.database import db, Notification, NotificationTypes
from notify.FCMManager import send_push
from notify.api_exception import exceptions_error



def to_one_user(profile, senderId, message, notificationType):

    if profile:
        token = [profile.token]

        if not message:
            return exceptions_error("Message is empty", 400)

        if notificationType:
            specific = NotificationTypes.SPECIFIC.value
            simple = NotificationTypes.SIMPLE.value
            if notificationType == specific or notificationType == simple:
                if notificationType == specific:
                    data = Notification(
                        message=message,
                        userId=profile.userId,
                        senderId=senderId)
                    db.session.add(data)
                    db.session.commit()
            else:
                return exceptions_error("Type must match", 400)

        send_push(senderId, message, token)
        return Response(), 200
    else:
        return exceptions_error("Item not found", 404)

def to_all_users(profiles, senderId, message, notificationType):


    for profile in profiles:

        if profile:
            token = [profile.token]
            userId = profile.userId

            if not message:
                return exceptions_error("Message is empty", 400)

            if notificationType:
                specific = NotificationTypes.SPECIFIC.value
                simple = NotificationTypes.SIMPLE.value
                if notificationType == specific or notificationType == simple:
                    if notificationType == specific:
                        data = Notification(
                            message=message,
                            userId=userId,
                            senderId=senderId
                        )
                        db.session.add(data)
                        db.session.commit()
                else:
                    return exceptions_error("Type must match", 400)

            send_push(senderId, message, token)
        else:
            return exceptions_error("Item not found", 404)

    return Response(), 200
