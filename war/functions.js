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