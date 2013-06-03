$(document).ready(function() {
		console.log(window.location.hostname);

	var socket = io.connect(window.location.hostname);
	console.log(socket);
	socket.on('bot',function(msg){
		console.log("info received");
		console.log(msg);
		msgReceived(msg);
	});

	function msgReceived(msg){
		console.log(msg);
		$("#bot-text").append($('<blockquote  class="example-twitter"><p>'+msg+'</p></div>').hide().fadeIn(2000));
	}
	//$("#bot-text").append($('<blockquote  class="example-twitter"><p>ola k dise</p></div>').hide().fadeIn(2000));
	

});