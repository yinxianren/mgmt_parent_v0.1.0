$(document).ready(function(){	
	
	//请设置要限制的select的size
	function getSize(size){
		
		 return size;
		
	}
	
	$("select").change(function(){
		
		$(this).attr("size",0);
		
		
	});
	
	
	$("select").blur(function(){
		
		
		$(this).attr("size",0);
		
		

	});
	
	var selectObj = {};
	
	$("select").mousedown(function(){
		
		if($(this) !== undefined){
			
			selectObj = $(this);
			$(this).attr("size",7);
			
		}else{
			
			selectObj.attr("size",0);
			
		}
		
		
		
		

	});
	
	
	
})