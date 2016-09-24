const mongoose = require('mongoose');
const Schema = mongoose.Schema;

var ArticleSchema = {
  	url: String,
  	title:  String,
  	beginning: String,
    summary: String,
  	keywords: [String]
  }

module.exports = new mongoose.Schema(ArticleSchema);
module.exports.ArticleSchema = ArticleSchema;
