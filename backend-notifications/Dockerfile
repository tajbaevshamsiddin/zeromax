FROM python:3.9

RUN mkdir /notification

COPY . /notification

WORKDIR /notification

RUN pip install -r requirements.txt

EXPOSE 8007

CMD flask run