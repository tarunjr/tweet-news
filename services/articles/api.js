var express = require('express');
var bodyparser = require('body-parser');

const artcile = require('./controller/Article')

module.exports = function() {
	var api = express.Router();
	api.use(bodyparser.json());

	api.post('/articles/', artcile.post);
  api.get('/articles/', artcile.get);

  return api;
}
