from flask import Blueprint, request, Response, jsonify
from notify.database import db, UserToken, Notification
from notify.api_exception import exceptions_error
from notify.functions import to_one_user, to_all_users



views = Blueprint("views", __name__)


@views.post('/notification/send/<userId>')
def send_user(userId):
    profile = UserToken.query.filter_by(userId=userId).first()
    senderId = request.headers.get('userId')

    message = request.get_json().get('message', '')
    notificationType = request.get_json().get('notificationType', '')

    if not senderId:
        return exceptions_error("Sender id is empty", 400)
    if not notificationType:
        return exceptions_error("Type is empty", 400)

    return to_one_user(profile, senderId, message, notificationType)



@views.post('/notification/send')
def send_all():
    profiles = UserToken.query.all()
    senderId = request.headers.get('userId')

    message = request.get_json().get('message', '')
    notificationType = request.get_json().get('notificationType', '')

    if not senderId:
        return exceptions_error("Sender id is empty", 400)
    if not notificationType:
        return exceptions_error("Type is empty", 400)

    return to_all_users(profiles, senderId, message, notificationType)



@views.post('/notification/register')
def register_user():
    userId = request.headers.get('userId')
    token = request.get_json().get('token', '')
    profile = UserToken.query.filter_by(userId=userId).first()

    if not userId:
        return exceptions_error("User id is empty", 400)

    if not token:
        return exceptions_error("Token is empty", 400)

    if not profile:
        if UserToken.query.filter_by(token=token).first():
            return exceptions_error("Token already exists", 409)
        data = UserToken(userId=userId, token=token)
        db.session.add(data)
    elif profile:
        profile.token = token

    db.session.commit()
    return Response(), 200


@views.get('/notification/tokens')
def get_all():
    profiles = UserToken.query.all()

    data = []

    for profile in profiles:
        data.append({
            "userId": profile.userId,
            "token": profile.token
        })
    
    return jsonify({'data': data}), 200

@views.get('/notification')
def get_notifications():
    userId = request.headers.get('userId')
    profile = Notification.query.filter_by(userId=userId).order_by().all()
    if profile:

        notifications = []

        for item in profile:
            notifications.append({
                'id': item.id,
                'userId': item.userId,
                'senderId': item.senderId,
                'message': item.message,
                'createdAt': item.createdAt
            })
        return jsonify({'notifications': notifications}), 200
    else:
        return exceptions_error("Item not found", 404)