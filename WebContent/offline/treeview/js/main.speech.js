
require(['./treeview', './speech', './gestures'],function(treeview, speech, gestures){

  $('head').append('<link href="http://netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet" type="text/css" />');
  $('body').append('<audio class="sound" style="display: none;" type="audio/wav"> Your browser does not support the audio element.</audio>');


  treeview.initialize();
  gestures.initialize();

  // Expand/Collapse table 
  $('.tag').on("tap", function (e) { // expand

	e.preventDefault();
    console.log('expand/collapse click');

    var number = $(this).attr('href');
    window.focus = $(this).attr('href');
    treeview.toggleTable(number);

    treeview.focus();

  });
  $('.tag').on("click", function (e) { // expand
	e.preventDefault();
	console.log('expand/collapse click');

    var number = $(this).attr('href');
    window.focus = $(this).attr('href');
    treeview.toggleTable(number);

    treeview.focus();
  });

  $(window).on('doubletap', function(e){
    window.location = $('.readable[number="' + window.focus + '"]').attr('href');
  });

  $('.readable').on('click', function(e){
    if ($(this).hasClass('link')){
      // alert($(this).attr('href'));
      // send_info
      window.location = $(this).attr('href');
    }
  });
  $('.readable').on('tap', function(e){
    window.focus = $(this).attr('number');
    treeview.focus();
  });

  // When readable is focused generate a speech
  $('.readable').on(
    "focusin",
    function (event) {
      var link = '';
      if ($(this).hasClass('link')) link = ' link.';

      var expand = '';
      if ($(this).hasClass('expandable')) {
        if (treeview.isExpanded($(this).attr('number'))) expand = ' expanded.';
        else expand = ' expandable.';
    }

    // var text = $(this).attr('number').replace(/\./g, ' point ') + expand()");
    // speech.say(text);

  });


  //
  $(window).on('swipe',function(ev){
    // console.log("swipe" + ev.direction);
    ev.stopPropagation();
    if(ev.direction === "up"){
      treeview.previous();
      treeview.focus();
    }
    if(ev.direction === "down"){
      treeview.next();
      treeview.focus();
    }
    if(ev.direction === "left"){
      treeview.collapse();
    }
    if(ev.direction === "right"){
      treeview.expand();
    }
    console.log(window.focus);
  });

});


var expand = function(name,id){};