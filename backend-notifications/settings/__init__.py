import os
from flask import Flask
from notify.database import db
from notify.views import views
from consumer import start
from sqlalchemy import create_engine
from sqlalchemy_utils import database_exists, create_database



def create_app(config=None):
    app = Flask(__name__, instance_relative_config=True)

    engine = create_engine(os.environ.get("SQLALCHEMY_DB_URI"))
    if not database_exists(engine.url):
        create_database(engine.url)

    if config is None:
        app.config.from_mapping(
            SQLALCHEMY_DATABASE_URI=engine.url,
            SQLALCHEMY_TRACK_MODIFICATIONS=False,
        ),
    else:
        app.config.from_mapping(config)

    db.app = app
    db.init_app(app)

    with app.app_context():
        db.create_all()

    app.register_blueprint(views)

    start()
    import py_eureka_client.eureka_client as eureka_client
    import random, string
    
    numbers = 32
    res = ''.join(random.choices(string.ascii_lowercase + string.digits, k=numbers))


    if os.environ.get('RUN_MAIN') is None:
        print("working eureka")
        eureka_client.init(eureka_server="http://192.168.49.2:30888/eureka" ,
            app_name="notification",
            instance_port=30007,
            instance_id=("notification:" + str(res)),
            instance_ip='192.168.49.2:',
            instance_host='192.168.49.2:',
        )

    return app
