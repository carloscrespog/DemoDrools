
/**
 * Module dependencies.
 */

var express = require('express')
  , routes = require('./routes')
  , user = require('./routes/user')
  , http = require('http')
  , path = require('path');


var app = express();



app.configure(function(){
  app.set('port', process.env.PORT || 3000);
  app.set('views', __dirname + '/views');
  app.set('view engine', 'jade');
  app.use(express.favicon());
  app.use(express.logger('dev'));
  app.use(express.bodyParser());
  app.use(express.methodOverride());
  app.use(app.router);
  app.use(express.static(path.join(__dirname, 'public')));
});

app.configure('development', function(){
  app.use(express.errorHandler());
});

app.get('/', routes.index);
app.get('/composer', routes.composer);
app.get('/users', user.list);

var server=http.createServer(app).listen(app.get('port'), function(){
  console.log("Express server listening on port " + app.get('port'));
});

var io = require('socket.io').listen(server);
var extSocket;
io.on('connection', function (socket) {
  extSocket=socket;
  socket.emit('news', { hello: 'world' });
  socket.on('hola', function (data) {
    console.log(data);
  });
});
app.post('/event',function(req,res){
  //console.log(req.body.source);
  extSocket.emit(req.body.source, { data: 'New Event' });
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.send("success",200);
});

app.post('/drools',function(req,res){
  extSocket.emit('Rule', req.body.rule);
  //console.log(req.body);
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.send("success",200);
});

// var everyone = require("now").initialize(server);

// everyone.now.logStuff = function(msg){
//     console.log(msg);
// };
