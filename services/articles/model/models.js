var mongoose = require('mongoose');

module.exports = function(){
  var db = mongoose.connection;
  db.on('error', console.error);
  db.once('open', function() {
    console.log('connected to mongod');
  });
  var connectionStr = 'mongodb://'
                    + process.env.MONGO_PORT_27017_TCP_ADDR
                    + ':'
                    + process.env.MONGO_PORT_27017_TCP_PORT
                    + '/articles';
  console.log(connectionStr);
  // set up the db layer
  mongoose.connect(connectionStr);

  var Article = mongoose.model('Article',require('./Article'),'articles');

  return {
    Article: Article
  }
};
