$(window).load(function(){
	//showTooltip();
});

$(window).load(function(){
	arrangeLeft("#footer")
});

$(window).load(function(){
	arrangeLeft("#siaSummary")
});

$(window).load(function(){
	arrangeLeft("#searchSubjectMainDiv")
});

function arrangeLeft(id){
	$(window).scroll(function(){
		$(id).css({
			'left': $(this).scrollLeft() + ($(window).width()-$(id).width())/2, 'margin-left': 0
		});
	});
};

function showTooltip(){
	$('[data-toggle="tooltip"]').tooltip();	
};

$(document).click(function(e) { 
    // Check for left button
    if (e.button == 0) {
    	if($(e.target).attr("id") == "btnAddSpecificSubject" || $(e.target).parent().attr("id") == "btnAddSpecificSubject"){
			alert("took");
			e.stopPropagation(); 		
    	}

    }
});