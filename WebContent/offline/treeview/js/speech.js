// Small module that manages audio

define(function(){
	return {
		// TODO: add audio element to page

		// Function to say
		say: function(text){
			var soundAudio = $('.sound').get(0);

			var url = 'http://130.85.90.68:8081/synth/synth?q=' + text;
			soundAudio.src = url;
			soundAudio.load();
			soundAudio.play();
		}
	}
});
