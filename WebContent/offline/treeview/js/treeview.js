// treeview.js
// Will update tree for manipulation and provide set of functions to manipulate 

define(['./speech'],function(speech){
    return {

        // Function to update tree
        initialize: function(){
            var treeRoot = $('body').find('table')
            // this.tag(treeRoot, '');
            window.focus = '1'; 
            console.log("treeview: initialized");
            console.log("treeview: focus: " + window.focus);

            // COMMENT FOR NORMAL USERS
            // UNCOMMENT FOR BLIND USERS
            // $('.tag').each(function(){
            //     var td = $('<td>');
            //     td.append($(this).clone());
            //     $(this).parent().parent().append(td);
            //     $(this).parent().remove();
            // });

            $('.tag').each(function(){
                $(this).attr('aria-label','Expand'); 
                $(this).attr('role','button');
            });

            $('.readable').each(function(){
                $(this).attr('tabindex','-1'); 
                var str = '';
                $(this).attr('role','link');

                str += 'Level ' + $(this).attr('number') + '.';
                $(this).attr('aria-label',str + $(this).text());
            });
        },
//
//      // Function that tags each node according to
//      //      Is a node not a table? (readable)
//      //      Does contain table? (expandable)
//      //      Is a link? (link)
//      tag: function (elem, parent){
//          var readableCount = 0;
//          var trcount = 0;
//
//          var nodes = new Array();
//          var nodenumbers = new Array();
//
//          $(elem).children('tbody').children('tr').each(function (index) {
//              trcount += 1;
//
//              var isexpandable = false; // if first td has a <a>
//              // children then yes
//              var isreadable = false; // if second td do not has a
//              // <table> children then yes
//              var islink = false;
//
//              var td1 = $(this).find('td:eq(0)');
//              var td2 = $(this).find('td:eq(1)');
//
//              if ($(td1).has('a').length != 0) isexpandable = true;
//              if ($(td2).has('table').length == 0) {
//                  isreadable = true;
//                  readableCount += 1;
//              }
//              if ($(td2).has('a').length != 0) islink = true;
//              var yournumber = parent + readableCount;
//              
//              if (isreadable && islink) {
//                  $(td2).children(':first').addClass('readable');
//                  $(td2).children(':first').addClass('link');
//                  $(td2).children(':first').attr('number', yournumber);
//              }
//              if (isreadable && !islink) {
//                  $(td2).html(
//                      '<div class="readable" tabindex="0" number="' + yournumber + '" style="display:inline-block"> ' + $(td2).text() + '</div>');
//              }
//              if (!isreadable) {
//                  $(td2).children(':first').show();
//                  $(td2).children(':first').children(':first').hide();
//                  $(td2).children(':first').children(':first').attr('number',
//                      yournumber);
//              }
//              if (isexpandable) {
//                  var nexttd = $(this).next().find('td:eq(1)').children(
//                      ':first').children(':first');
//
//                  $(td1).children(':first').attr('tabindex', -1);
//                  $(td1).children(':first').addClass('tag');
//                  $(td1).children(':first').css('text-align','center');
//                  $(td1).children(':first').html('<small>'+ '<i class="icon-plus muted icon-border"></i>'+ '</small>');
//                  $(td1).children(':first').attr('href', yournumber);
//                  $(td2).children(':first').addClass('expandable');
//
//                  nodes.push($(nexttd));
//                  nodenumbers.push(yournumber + '.');
//              }
//
//          });
//          
//          for (var i = nodes.length - 1; i >= 0; i--) {
//              this.tag(nodes[i], nodenumbers[i]);
//          };
//
//          $(elem).attr('children', readableCount);
//      },

focus: function () {
    $('.readable[number="' + window.focus + '"]').focus();
},


next: function() {

    try {
        var v = this.isExpanded(window.focus);
    } catch (e) {
        v = false;
    }
    if (v) {
        window.focus = window.focus + '.1';
    } else {
        var elem = $('.readable[number="' + window.focus + '"]');

        var nexts = elem.parents('tr');
        $(nexts).each(function (i) {
            var next = $(this).nextAll(':not(:has(table))').get(0);
            if ($(next).length) {
                window.focus = $(next).find('.readable').attr('number');
                return false;
            } else return true;
        });
    }
},

previous: function () {
    var elem = $('.readable[number="' + window.focus + '"]');

    var prevTR = $($(elem).parents('tr').get(0)).prevAll(
        ':not(:has(table))').first();

    if ($(prevTR).length) {

        var prevTRNumber = $(prevTR).find('.readable').attr('number');
        try {
            var v = this.isExpanded(prevTRNumber);
        } catch (e) {
            v = false;
        }
        if (v) {
            var table = $($(elem).parents('tr').get(0)).prev().find(
                'table:visible').last();
            var lastTR = $(table).children('tbody').children(
                'tr:not(:has(table))').last();
            window.focus = $(lastTR).find('.readable').attr('number');
        } else {
            window.focus = prevTRNumber;
        }
    } else {
        if ($(elem).parents('tr').length > 1) {
            window.focus = $($(elem).parents('tr').get(1)).prev().find(
                '.readable').attr('number');
        }
    }

},


expand: function () {
    try {
        var v = this.isExpanded(window.focus);
        if (!v) {
            this.toggleTable(window.focus);
            speech.say('expanded ' + window.focus.replace(/\./g, ' point '));
        }
    } catch (e) {
        speech.say('is not expandable');
    }
},

collapse: function () {
    try {
        var v = this.isExpanded(window.focus);
        if (v) {
            this.toggleTable(window.focus);
            speech.say('collapsed ' + window.focus.replace(/\./g, ' point '));
        }
    } catch (e) {
        speech.say('is not expandable');
    }
},

isExpanded: function (number) {
    var elem = $('table[number="' + number + '"]');
    if ($(elem).length) {
        if ($(elem).is(':hidden')) {
            return false;
        } else {
            return true;
        }
    } else {
        throw 'non-exist';
    }
},

toggleTable: function (number) {
    $('table').not('#main').not('[number="' + number + '"]').hide();
    $('.tag').html('<small>'+ '<i class="fa fa-angle-right fa-border fa-fw"></i>'+ '</small>');
    $('.tag').attr('aria-label', 'Expand');
    var table = $('table[number="' + number + '"]');
    var arr = number.split('.');
    for (var i = 0; i < arr.length; i++) {
        var parents = arr.slice(0, i).join('.');
        $('table[number="' + parents + '"]').show();
        $('.tag[href="' + parents + '"]').html('<small>'+ '<i class="fa fa-angle-down fa-border fa-fw "></i>'+ '</small>');
    }

    if ($(table).is(':hidden')) {
        $(table).show();
        $('.tag[href="' + number + '"]').html('<small>'+ '<i class="fa fa-angle-down fa-border fa-fw"></i>'+ '</small>');
        $('.tag[href="' + number + '"]').attr('aria-label','Collapse');
    } else {
        $(table).hide();
        $('.tag[href="' + number + '"]').html('<small>'+ '<i class="fa fa-angle-right fa-border fa-fw"></i>'+ '</small>');
        $('.tag[href="' + number + '"]').attr('aria-label','Expand');
    }

    // window.focus = window.focus + '.1';
    // this.focus();

},

sendInfo: function (name, val) {
    var xmlhttp;
            // alert("name="+name+"&value="+val);
            if (window.XMLHttpRequest) { // code for IE7+, Firefox, Chrome, Opera, Safari
                xmlhttp = new XMLHttpRequest();
            } else { // code for IE6, IE5
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            }
            xmlhttp.open("POST", "save_db.jsp", true);
            xmlhttp.setRequestHeader("Content-type",
                "application/x-www-form-urlencoded");
            xmlhttp.send("name=" + name + "&value=" + val);
        }

    }
});




