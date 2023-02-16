import uuid
from flask_sqlalchemy import SQLAlchemy
from datetime import datetime
from sqlalchemy.dialects.postgresql import UUID
from enum import Enum

db = SQLAlchemy()


class UserToken(db.Model):
    __tablename__ = 'users_tokens'

    userId = db.Column(db.String(), primary_key=True)
    token = db.Column(db.Text())

    def __init__(self, userId, token):
        self.token = token
        self.userId = userId


class NotificationTypes(Enum):

    SPECIFIC = "Specific"
    SIMPLE = "Simple"


class Notification(db.Model):
    __tablename__ = 'notifications'

    id = db.Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)
    userId = db.Column(db.String(), nullable=True)
    senderId = db.Column(db.String(200), nullable=True)
    message = db.Column(db.Text(), nullable=True)
    notificationType = db.Column(db.Enum(NotificationTypes), default=NotificationTypes.SPECIFIC)
    createdAt = db.Column(db.DateTime, default=datetime.now())

    def __init__(self, message, userId, senderId):
        self.message = message
        self.userId = userId
        self.senderId = senderId
