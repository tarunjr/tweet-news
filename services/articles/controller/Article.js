const mongoose = require('mongoose');
var async = require('async');
var querystring = require('querystring');
var http = require('http');

exports.post = function(req, res) {

var urls = req.body;
console.log(urls);
var Article = mongoose.model('Article');

async.waterfall([
	  function getFromDB(callback) {
			  console.log('getFromDB');
				console.log(urls);
				Article.find({ 'url': {$in:urls}} ,
					function(err, cachedArticles) {
						if (err)
							callback(err, null, null)
					 	else {
						 		// find the url
								var cachedUrls = [];
								cachedArticles.forEach(function (article, index, array) {
										cachedUrls.push(article.url);
								});
								console.log(cachedUrls);
								// identify url to get information about
								var fetchUrls = [];
								urls.forEach(function(url, index, array) {
										if (false == cachedUrls.includes(url))
												fetchUrls.push(url);
								});
								callback(null, fetchUrls, cachedArticles);
						}
				});
	  },
	  function getFromService(fetchUrls, cachedArticles, callback) {
	      console.log('getFromService');
				console.log(fetchUrls);
				fetchedArticles = []
				async.each(fetchUrls,
  					function(url, callback){
							var serverHost = 	process.env.EXTRACTION_SVC_PORT_27017_TCP_ADDR + ':' +
																process.env.EXTRACTION_SVC_PORT_27017_TCP_PORT
							var options = {
									protocol: 'http:',
									host: '127.0.0.1',
									port: '8000',
									path: '/article/extract?url=' + querystring.escape(url),
									method: 'GET'
							};
							var req = http.request(options, function(res) {
    								res.setEncoding('utf-8');
										var responseString = '';

										res.on('data', function(data) {
      								responseString += data;
    								});

    								res.on('end', function() {
      								console.log(responseString);
      								var responseObject = JSON.parse(responseString);
      								fetchedArticles.push(responseObject)
											callback();
    								});
  							});
  							req.end();
						},
  					function(err){
    					callback(null, fetchedArticles, cachedArticles);
  					}
					);
	  },
	  function saveToDB(fetchedArticles, cachedArticles, callback) {
	      console.log('saveToDB');
				// Loop through some items
				fetchedArticles.forEach(function(article){
						Article.create(article, function(err, data){
							if(err)
								console.log("failed");
							else {
								console.log(article.title);
					   	}
						});
			  });
	      callback(null,cachedArticles, fetchedArticles);
	  }
	],
	function(err, cachedArticles, fetchedArticles) {
	  console.log('all done');
		var articles = cachedArticles.concat(fetchedArticles);
	  console.log(articles);
		res.send(JSON.stringify(articles));
	});
}
exports.get = function(req, res) {


	var Article = mongoose.model('Article');

	async.waterfall([
			function getTopUrls(callback) {
					var fetchUrls = ["https://t.co/Ey2QvkSbdy", "https://t.co/8CB9hDuuXq",
													 "https://t.co/VdBNvKUX4r", "https://t.co/2v6MwojEUx","https://t.co/rCvmatOj08"]
					callback(null, fetchUrls);
			},
			function getFromDB(urls, callback) {
				  console.log('getFromDB');
					console.log(urls);
					Article.find({ 'url': {$in:urls}} ,
						function(err, cachedArticles) {
							if (err)
								callback(err, null, null)
						 	else {
							 		// find the url
									var cachedUrls = [];
									cachedArticles.forEach(function (article, index, array) {
											cachedUrls.push(article.url);
									});
									console.log(cachedUrls);
									// identify url to get information about
									var fetchUrls = [];
									urls.forEach(function(url, index, array) {
											if (false == cachedUrls.includes(url))
													fetchUrls.push(url);
									});
									callback(null, fetchUrls, cachedArticles);
							}
					});
		  },
		  function getFromService(fetchUrls, cachedArticles, callback) {
		      console.log('getFromService');
					console.log(fetchUrls);
					fetchedArticles = []
					async.each(fetchUrls,
	  					function(url, callback){
								var serverHost = 	process.env.EXTRACTION_SVC_PORT_27017_TCP_ADDR + ':' +
																	process.env.EXTRACTION_SVC_PORT_27017_TCP_PORT
								var options = {
										protocol: 'http:',
										host: '127.0.0.1',
										port: '8000',
										path: '/article/extract?url=' + querystring.escape(url),
										method: 'GET'
								};
								var req = http.request(options, function(res) {
	    								res.setEncoding('utf-8');
											var responseString = '';

											res.on('data', function(data) {
	      								responseString += data;
	    								});

	    								res.on('end', function() {
	      								console.log(responseString);
	      								var responseObject = JSON.parse(responseString);
	      								fetchedArticles.push(responseObject)
												callback();
	    								});
	  							});
	  							req.end();
							},
	  					function(err){
	    					callback(null, fetchedArticles, cachedArticles);
	  					}
						);
		  },
		  function saveToDB(fetchedArticles, cachedArticles, callback) {
		      console.log('saveToDB');
					// Loop through some items
					fetchedArticles.forEach(function(article){
							Article.create(article, function(err, data){
								if(err)
									console.log("failed");
								else {
									console.log(article.title);
						   	}
							});
				  });
		      callback(null,cachedArticles, fetchedArticles);
		  }
		],
		function(err, cachedArticles, fetchedArticles) {
		  console.log('all done');
			var articles = cachedArticles.concat(fetchedArticles);
		  console.log(articles);
			res.send(JSON.stringify(articles));
		});
}
