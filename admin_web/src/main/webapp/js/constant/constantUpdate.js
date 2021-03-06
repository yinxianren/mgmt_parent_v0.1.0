$(document).ready(function(){
	
	function updateConstant(){
		
		var obj = $("#constantObjForm").serializeJson();
		$.ajax({
			url : "/constant/update",
			data : JSON.stringify(obj),
			dataType : "json",
			type : "POST",
			contentType:'application/json;charset=UTF-8',
            beforeSend:function () {
             },
     		complete:function(data) { 
     		}, 
			success : function(result) {
				if(result.success == 1){
					 
					$.success("更新成功！", true, 3000);
					$("#selectConstantEditDialog").remove();
				    $(".jqmOverlay").remove();	   
					location.reload();
					
				}else{
					
					$.success("更新失败！", true, 3000);
					$("#selectConstantEditDialog").remove();
				    $(".jqmOverlay").remove();
					
					
				}

			}
		});
	
	}
	
	 var config={
			    reportMode:"alert",
			    formDiv:"constantForm",
			    props:[
			        {
			            name:"name",
			            label:"常量名称",
			            trim:true,
			            required:true
			        },
			        {
			            name:"firstValue",
			            label:"常量值",
			            trim:true,
			            required:false
			        },
			        {
			            name:"secondValue",
			            label:"常量值",
			            trim:true,
			            required:false
			        },
			        {
			            name:"groupCode",
			            label:"常量组别",
			            trim:true,
			            required:false
			        },
			        {
			            name:"sortValue",
			            label:"排序",
			            trim:true,
			            required:false
			            ,dataType:"int"
			        }
			    ]
	 
			     }
	 
	var checkValid = $.checkValid(config);//构建验证对象
			    $("#updateConstant").click(function () {
			        if(checkValid.checkAll()){
			        	updateConstant();
			        }
			    });
	
    $("#_back").click(function () {
    	
        $("#selectConstantEditDialog").remove();
        $(".jqmOverlay").remove();
        
    });	
	
})