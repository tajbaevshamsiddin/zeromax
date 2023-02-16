FROM python:3.9-alpine

RUN mkdir media_servise

WORKDIR /media_servise

COPY . /media_servise/

RUN pip3 install -r requirements.txt

EXPOSE 8009

CMD python3 manage.py makemigrations && python3 manage.py migrate && python3 manage.py runserver 0.0.0.0:8009 