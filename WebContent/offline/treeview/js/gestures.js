/* twofingergestures.js */
/* Will get two finger gestures and use treeview api to manipulate view */

define(function () {
    /* Do setup work here */
    return {

    	initialize: function(){
        $('*').on('touchend', this.handleEnd);
        $('*').on('touchstart', this.handleStart);
        $('*').on('touchmove', this.handleMove);
        window.touches = new Array();
        window.multipleTouched = false;
        window.bodyZoom = 1;

    		console.log("gesture: initialized");
  		},


  		handleStart: function(evt) {
        /* Prevent default actions */
        evt.preventDefault();
        evt.stopPropagation();
        var e = evt.originalEvent;

        /* If it is not a new touch. Ignore Miss Fire */
        if(window.touches.length == e.touches.length) // Repeat condition
          return;
        else{
          /* New entry for touch history */
          for (var i = e.changedTouches.length - 1; i >= 0; i--) {
            var touch = {
              id: e.changedTouches[i].identifier,
              startX: e.changedTouches[i].pageX,
              startY: e.changedTouches[i].pageY,
              lastX:e.changedTouches[i].pageX,
              lastY:e.changedTouches[i].pageY,
              history: new Array()
            };
            window.touches.push(touch);

          }

          /* if the new touch is with a second touch. */
          if(window.touches.length == 2){
            window.multipleTouched = true;

            var current = {x: 0, y: 0, d:0};

            current.x = (window.touches[0].lastX + window.touches[1].lastX)/2;
            current.y = (window.touches[0].lastY + window.touches[1].lastY)/2;
            current.d = Math.sqrt(Math.pow((window.touches[1].lastX - window.touches[0].lastX) , 2) + Math.pow((window.touches[1].lastY - window.touches[0].lastY) , 2));

            window.scroll.x = current.x;     
            window.scroll.y = current.y;
            window.zoom = current.d;


            // console.log("PX0:" + window.touches[0].lastX + "         PY0:" + window.touches[0].lastY);
            // console.log("PX1:" + window.touches[1].lastX + "         PY1:" + window.touches[1].lastY);
            // console.log("PX:" + window.scroll.x + "         PY:" + window.scroll.y);
          }

        }
  		},

      handleMove: function(evt){
        /* Prevent default actions */
        evt.preventDefault();
        evt.stopPropagation();
        var e = evt.originalEvent;
        var eStamp = evt.timeStamp;
 
        for (var i = e.changedTouches.length - 1; i >= 0; i--) {
          var idx = $.grep(window.touches, function(x){ return x.id == e.changedTouches[i].identifier; });

          $.each(window.touches, function(ix,value){

            /* Remove the entries that occured more than 200 ms ago */
            window.touches[ix].history = $.grep(window.touches[ix].history, function(iy){ 
              return (evt.timeStamp - iy.stamp < 100) ;
            });

            if(window.touches[ix].id == idx[0].id){

              /* Log history  */                
                var t = {
                  x: e.changedTouches[i].pageX,
                  y: e.changedTouches[i].pageY,
                  stamp: evt.timeStamp
                }
                window.touches[ix].history.push(t);
                window.touches[ix].lastX = e.changedTouches[i].pageX;
                window.touches[ix].lastY = e.changedTouches[i].pageY;
            }
          });

        }
        // If it is a multitouch event and currently has two fingers
        // Update zoom and scroll

        if(multipleTouched && window.touches.length == 2){
          
          
          var touch0 = {x: 0, y: 0};
          var touch1 = {x: 0, y: 0};

          $.each(window.touches[0].history,function() {
              touch0.x += this.x;
              touch0.y += this.y;
          });
          touch0.x = touch0.x/ window.touches[0].history.length;
          touch0.y = touch0.y/ window.touches[0].history.length;

          $.each(window.touches[1].history,function() {
              touch1.x += this.x;
              touch1.y += this.y;
          });
          touch1.x = touch1.x / window.touches[1].history.length;
          touch1.y = touch1.y / window.touches[1].history.length;





          var current = {x: 0, y: 0, d:0};
          var delta = {x: 0, y: 0, d: 0};

          current.x = (touch0.x + touch1.x)/2;
          current.y = (touch0.y + touch1.y)/2;
          current.d = Math.sqrt(Math.pow((touch0.x - touch1.x), 2)  + Math.pow((touch0.y - touch1.y), 2));


          delta.x = window.scroll.x - current.x;
          delta.y = window.scroll.y - current.y;
          delta.d = current.d / window.zoom ;



          if(Math.abs(delta.x) > 0){
            $(window).scrollLeft($(window).scrollLeft() + delta.x ); 
            window.scroll.x = current.x;        
          }

          if(Math.abs(delta.y) > 0){
            $(window).scrollTop($(window).scrollTop() + delta.y / 1);
            window.scroll.y = current.y;
          }

          if(Math.abs(delta.d) > 0){
            var bodyZoom = parseFloat($('body').css('zoom'));
            var nDelta = ((Math.abs(delta.d - 1) / 6) * (Math.abs(delta.d - 1) / (delta.d - 1))) + 1;
            //var nBodyZoom = ((Math.abs(delta.d - 1) / 2) * (Math.abs(delta.d) / delta.d) + 1) * bodyZoom;
            var nBodyZoom = nDelta * bodyZoom;

            $('body').css('zoom', nBodyZoom);
            console.log(delta.d + " : " + nDelta + " : " + parseFloat($('body').css('zoom')) );
            window.zoom = current.d;
          }

        }   

      },

      handleEnd: function(evt){
        evt.stopPropagation();
        var e = evt.originalEvent;

        for (var i = e.changedTouches.length - 1; i >= 0; i--) {
          var idx = $.grep(window.touches, function(x){ return x.id == e.changedTouches[i].identifier; });
          window.touches = $.grep(window.touches, function(x){ return x.id != e.changedTouches[i].identifier; });
        

          if(e.touches.length == 0){
            if(!window.multipleTouched){
              var distance = Math.sqrt( Math.pow((idx[0].lastX - idx[0].startX),2) + Math.pow((idx[0].lastY - idx[0].startY),2));
              if(distance < 40){
                if(evt.timeStamp - window.lastTap < 500)
                  $(window).trigger("doubletap");
                window.lastTap = evt.timeStamp;
                $(this).trigger("tap");
              }
              else{
                var direction = "";

                var distanceX = idx[0].lastX - idx[0].startX;
                var distanceY = idx[0].lastY - idx[0].startY;

                if(Math.abs(distanceX) < Math.abs(distanceY))
                  if(distanceY < 0)
                    direction = "up";
                  else
                    direction = "down";
                else
                  if(distanceX < 0)
                    direction = "left";
                  else
                    direction = "right";

                $(window).trigger({
                  type: "swipe",
                  direction: direction
                });
              }
            }
            else
            {
              console.log("ZOOM & SCRL");
            }

            window.multipleTouched = false;
          }
        }


        

      }

	}
});