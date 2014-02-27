$(function() {

	
	$(".question").change(function() {
		$("#editButton").html("<i class='icon-pencil'> </i>Edit Question");
		$("#editButton").show();
		$("#deleteButton").html("<i class='icon-trash'> </i>Delete Question");
		$("#deleteButton").show();
	});
	$(".round").change(function() {
		$("#editButton").html("<i class='icon-pencil'> </i>Edit Round");
		$("#editButton").show();
		$("#deleteButton").html("<i class='icon-trash'> </i>Delete Round");
		$("#deleteButton").show();
	});

	$("#roundNewButton").click(function() {
		$("#roundModalLabel").text("Create New Round");
		$("#roundId").val("");
		$("#roundName").val("");
		$("#roundSchema").val("");
		$("#roundForm input[name=action]").val("new");
		$("#roundModal").modal("show");
	});

	$("#qstionNewButton").click(function() {
		$("#qstionModalLabel").text("Create New Question");
		$("#qstionId").val("");
		$("#qstionText").val("");
		$("#qstionLevel").val("");
		$("#qstionMin").val("");
		$("#qstionMax").val("");
		$("#qstionRound").val("");
		$("#qstionForm input[name=action]").val("new");
		$("#qstionModal").modal("show");
	});

	$("input[name=id]").attr('checked', false);

	$("#editButton").click(function() {
		var $elem = $("#experimentForm input[name=id]:checked");
		if ($elem.hasClass("round")) {
			$.get("/cvweb/exp/round", {
				action : "raw",
				id : $elem.val()
			}).done(function(data) {
				var obj = jQuery.parseJSON(data);
				$("#roundModalLabel").text("Edit Round");
				$("#roundId").val(obj.id);
				$("#roundName").val(obj.name);
				$("#roundSchema").val(obj.schema);
				$("#roundForm input[name=action]").val("edit");
				$("#roundModal").modal("show");
			}).error(function(eventData) {
				alert(eventData.responseText);
			});
		}
		if ($elem.hasClass("question")) {
			$.get("/cvweb/exp/question", {
				action : "raw",
				id : $elem.val()
			}).done(function(data) {
				var obj = jQuery.parseJSON(data);
				$("#qstionModalLabel").text("Edit Question");
				$("#qstionId").val(obj.id);
				$("#qstionText").val(obj.text);
				$("#qstionLevel").val(obj.level);
				$("#qstionMin").val(obj.min);
				$("#qstionMax").val(obj.max);
				$("#qstionRound").val(obj.round);
				$("#qstionForm input[name=action]").val("edit");
				$("#qstionModal").modal("show");
			}).error(function(eventData) {
				alert(eventData.responseText);
			});

		}

	});

});
