
/*
 * GET home page.
 */

exports.index = function(req, res){
  res.render('index', { title: 'Events2Drools' });
};

exports.composer = function(req,res){
	res.render('composer', { title: 'Complex Event Composer' });
};