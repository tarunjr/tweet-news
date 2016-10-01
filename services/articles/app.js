
var express = require('express');
var bodyParser = require('body-parser')

// diagnostic message
console.log(process.env.MONGO_PORT_27017_TCP_ADDR);
console.log(process.env.MONGO_PORT_27017_TCP_PORT);


var db = require('./model/models')();
// setup the services layer
var app = express();
app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  next();
});
app.use('/api/v1/', require('./api.js')());

app.listen(8081);
console.log('Listetning on 8081');
