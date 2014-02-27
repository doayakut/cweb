
require(['./treeview'],function(treeview){

  $('head').append('<link href="http://netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet" type="text/css" />');
  $('body').append('<audio class="sound" style="display: none;" type="audio/wav"> Your browser does not support the audio element.</audio>');


  treeview.initialize();

  // Expand/Collapse table 
  $('.tag').on("click", function (e) { // expand
	e.preventDefault();
	console.log('expand/collapse click');

    var number = $(this).attr('href');
    window.focus = $(this).attr('href');
    treeview.toggleTable(number);

    treeview.focus();
  });


  $('.readable').on('click', function(e){
    if ($(this).hasClass('link')){
      // alert($(this).attr('href'));
      // send_info
      window.location = $(this).attr('href');
    }
  });


});


var expand = function(name,id){};