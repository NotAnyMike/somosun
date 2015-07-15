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
			var left = $(this).scrollLeft() + ($(window).width()-$(id).width())/2
			if($(window).width() < $(id).width){
				left = 0
			}
			'left': left, 'margin-left': 0
		});
	});
};

function arrangeTopOfSearchBox(){
	$("#searchSubjectMainDiv").css({'top':$(window).scrollTop() + 80});
}

function showTooltip(){
	$('[data-toggle="tooltip"]').tooltip();	
};

function addClickSearchField(){
	$("#searchField").keyup(function(event){
	    if(event.keyCode == 13){
			var click_ev = document.createEvent("MouseEvents");
			click_ev.initEvent("click", true /* bubble */, true /* cancelable */);
			document.getElementById("searchButton").dispatchEvent(click_ev);
	    }
	});
}

function stopPropagationOfClickOnSelectSubject(){
	var els = document.getElementById("accordionContainer").getElementsByTagName("button");
		Array.prototype.forEach.call(els, function(el) {
    		el.addEventListener('click', function(event){event.stopPropagation();});
		});
}