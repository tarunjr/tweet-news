FROM python:2.7.12-slim
 ENV PYTHONUNBUFFERED 1
 RUN mkdir /code
 WORKDIR /code
 ADD requirements.txt /code/
 RUN apt-get update && apt-get install -y curl && apt-get install -y  python-dev && apt-get install -y libxml2-dev libxslt-dev && apt-get install -y  libjpeg-dev zlib1g-dev libpng12-dev && apt-get install -y gcc
 RUN pip install newspaper
 RUN curl https://raw.githubusercontent.com/codelucas/newspaper/master/download_corpora.py | python2.7
 RUN pip install --upgrade django==1.6.1 
 RUN pip install gunicorn
 ADD . /code/
 EXPOSE 8000
 CMD [ "/code/start.sh"]
