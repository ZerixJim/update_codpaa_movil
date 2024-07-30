$(function(){
	$('#login').validate({
    	description : {
        	test : {
            	required : '<div class="error">Required</div>',
            	pattern : '<div class="error">Pattern</div>',
            	conditional : '<div class="error">Conditional</div>',
            	valid : '<div class="success">Valid</div>'
        	}
    	}
	});


});