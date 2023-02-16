import datetime
from flask.json import jsonify


def exceptions_error(message, code):

    response = jsonify(
        data={
            'message': message,
            'code': "0000",
            'status': code,
            'timestamp': datetime.datetime.now()
        }
    )
    response.status_code = code
    return response
