from django.conf.urls import url

from . import views

urlpatterns = [
    url(r'^extract$', views.extract, name='extract'),
]
