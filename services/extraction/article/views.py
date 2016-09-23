from django.shortcuts import render
from django.http import HttpResponse
from newspaper import Article
import json
import urllib

def extract(request):
    url = urllib.unquote(request.GET.get('url', '')).decode('utf8')
    print(url)
    article = Article(url, language='en')
    article.download()
    article.parse()
    article.nlp()

    data = {}
    data['title'] = article.title
    data['url'] = url
    data['beginning'] = article.text[0:100]
    data['summary'] = article.summary
    data['keywords'] = article.keywords
    json_data = json.dumps(data)

    return HttpResponse(json_data)
