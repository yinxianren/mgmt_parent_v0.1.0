$(document).ready(function(){	
	
	var oldSelectObj = {};
	var oldSelectValue = "";
    var newSelectObj = {};
    var newSelectValue = ""
	
	$("option").mouseover(function(){ 
		
		 newSelectValue = $(this).val();
		 newSelectObj = $(this);
		 $(this).parent().val(newSelectValue);
	   
   });
	
	
	$("option").mousedown(function(){ 
		
		 var newSelectValue =  $(this).parent().val();
		 $(this).parent().attr("size",0);
		 var id = $(this).parent().attr("id");
		 $("#selectInputDiv_" + id ).remove();

   });
	
	$("option").mouseout(function(){ 
		 
		$(this).parent().val(newSelectValue);
		
	});
		
})
		   
