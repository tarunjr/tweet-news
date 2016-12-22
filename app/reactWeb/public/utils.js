function get(url){
  // Return a new promise`
  return new Promise(function(resolve, reject) {
    var xhr = createCORSRequest('GET', url);
    if (!xhr) {
      throw new Error('CORS not supported');
    }
    xhr.onload = function() {
      resolve(xhr.responseText);
    }
    xhr.onerror = function() {
      reject(new Error('failed to get url:' + url))
    };
    xhr.send();
  });
}

function getJSON(url) {
  return get(url).then(JSON.parse);
}
function getArticle(url) {
  return getJSON(url);
}
function getHashTags(url) {
    return getJSON(url);
}
function getArticles(hashtag) {
  return Promise.all(
    hashtag.articleUrls.map(getJSON)
  );
}
