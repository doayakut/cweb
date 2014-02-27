/*
  Bootstrap - File Input
  ======================

  This is meant to convert all file input tags into a set of elements that displays consistently in all browsers.

  Converts all
  <input type="file">
  into Bootstrap buttons
  <a class="btn">Browse</a>

*/
$(function() {
	// Set the word to be displayed on the button
	var buttonWord = 'Browse';
	var cancelLink;
	var buttonLink;
	var disabledWord = '';
	var $elem;
	  
	$('input[type=file]').each(function(i,elem){
	
	  // Maybe some fields don't need to be standardized.
	  if (typeof $(this).attr('data-bfi-disabled') != 'undefined') {
	    return;
	  }
	
	
	  
	
	  if (typeof $(this).attr('title') != 'undefined') {
	    buttonWord = $(this).attr('title');
	  }
	  if (typeof $(this).attr('placeholder') != 'undefined') {
		  placeholderWord = $(this).attr('placeholder');
		  fileExists = true;
	  }
	  if (typeof $(this).attr('cancel') != 'undefined') {
		    cancelWord = $(this).attr('cancel');
	  }
	  if (typeof $(this).attr('disabled')  != 'undefined') {
		  disabledWord = 'disabled';
		  disabledIcon = 'icon-muted'
			 
	  }
		
	  // Start by getting the HTML of the input element.
	  // Thanks for the tip http://stackoverflow.com/a/1299069
	  $elem = $(elem);
	  var input = $('<div>').append( $elem.eq(0).clone() ).html();
	  buttonLink = '<a class="file-input-wrapper btn ' + $elem.attr('class') + ' ' + disabledWord +  ' ">'+buttonWord+input+'</a>';
	  cancelLink = ' &nbsp; <a href="#" class="file-input-cancel text-error' 
			+ '">  <i class="icon-remove-sign icon-large"></i> </a>';
	
	  // Now we're going to replace that input field with a Bootstrap button.
	  // The input will actually still be there, it will just be float above and transparent (done with the CSS).
	  $elem.after('<span class="file-input-name muted">'+placeholderWord+'</span>');
	  $elem.replaceWith(buttonLink);
	  
	})
	// After we have found all of the file inputs let's apply a listener for tracking the mouse movement.
	// This is important because the in order to give the illusion that this is a button in FF we actually need to move the button from the file input under the cursor. Ugh.
	.promise().done( function(){
	
	  // As the cursor moves over our new Bootstrap button we need to adjust the position of the invisible file input Browse button to be under the cursor.
	  // This gives us the pointer cursor that FF denies us
	  $('.file-input-wrapper').bind('mouseover',function(cursor) {
	
	    var input, wrapper,
	      wrapperX, wrapperY,
	      inputWidth, inputHeight,
	      cursorX, cursorY;
	
	    // This wrapper element (the button surround this file input)
	    wrapper = $(this);
	    // The invisible file input element
	    input = wrapper.find("input");
	    // The left-most position of the wrapper
	    wrapperX = wrapper.offset().left;
	    // The top-most position of the wrapper
	    wrapperY = wrapper.offset().top;
	    // The with of the browsers input field
	    inputWidth= input.width();
	    // The height of the browsers input field
	    inputHeight= input.height();
	    //The position of the cursor in the wrapper
	    cursorX = cursor.pageX;
	    cursorY = cursor.pageY;
	
	    //The positions we are to move the invisible file input
	    // The 20 at the end is an arbitrary number of pixels that we can shift the input such that cursor is not pointing at the end of the Browse button but somewhere nearer the middle
	    moveInputX = cursorX - wrapperX - inputWidth + 20;
	    // Slides the invisible input Browse button to be positioned middle under the cursor
	    moveInputY = cursorY- wrapperY - (inputHeight/2);
	
	    // Apply the positioning styles to actually move the invisible file input
	    input.css({
	      left:moveInputX,
	      top:moveInputY
	    });
	  });
	  
	  $('.file-input-wrapper input[type=file]').bind('change',function(){
	    // Remove any previous file names
	    $(this).parent().next('.file-input-name').remove();
	    $(this).parent().next('.file-input-cancel').remove();
	    if ($(this).prop('files').length > 1) {
	      $(this).parent().after('<span class="file-input-name">'+$(this)[0].files.length+' files</span> ' + cancelLink);
	    }
	    else {
	      $(this).parent().after('<span class="file-input-name">'+$(this).val().replace('C:\\fakepath\\','')+'</span>' + cancelLink);
	    }
	  });
	  
	  $(document).on('click', '.file-input-cancel', function(){
		  if(! $(this).hasClass('disabled')){
			  var $elem = $(this).prev().prev().children('input[type=file]').eq(0);
			  $elem.val('');			  
			  $(this).prev().prev().after('<span class="file-input-name muted">'+$elem.attr("placeholder") +'</span>');

			  $(this).prev().remove();
			  $(this).remove();
		  }
	  });

	});
	
	
	
	// Add the styles before the first stylesheet
	// This ensures they can be easily overridden with developer styles
	var cssHtml = '<style>'+
	  '.file-input-wrapper { overflow: hidden; position: relative; cursor: pointer; z-index: 1; }'+
	  '.file-input-wrapper input[type=file], .file-input-wrapper input[type=file]:focus, .file-input-wrapper input[type=file]:hover { position: absolute; top: 0; left: 0; cursor: pointer; opacity: 0; filter: alpha(opacity=0); z-index: 99; outline: 0; }'+
	  '.file-input-name { margin-left: 8px; }'+
	  '</style>';
	$('link[rel=stylesheet]').eq(0).before(cssHtml);

});
