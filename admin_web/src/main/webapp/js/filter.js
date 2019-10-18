/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/4/8 18:31
 * Project: Management
 * 自定义过滤器
 */

function capitalize() {
    return function (input) {
        return (!!input) ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : '';
    };
}

function getValueByList() {
    return function (input, list, keyName, valueName) {
        if (input !== undefined && list !== undefined && keyName !== undefined && valueName !== undefined) {
            var thisInput = input.toString();
            for (var i = 0; i < list.length; i++) {
                var thisKeyName = list[i][keyName].toString();
                if (thisKeyName === thisInput) {
                    return list[i][valueName];
                }
            }
        }
        return input;
    };
}

function getMerIdAndName() {
    return function (input, list) {
        if (input !== undefined && list !== undefined) {
            input = Number(input);
            for (var i = 0; i < list.length; i++) {
                if (list[i].id === input) {
                    return list[i]['id'] + '(' + list[i]['name'] + ')';
                }
            }
        }
        return input;
    };
}

function getMerchantNameByMerId() {
    return function (input,list) {
        if (input !== undefined && list !== undefined) {
            input = Number(input);
            for (var i = 0; i < list.length; i++){
                if (list[i].id === input) {
                    return list[i]['name'];
                }
            }
        }
    }
}

function replace() {
    return function (input, regexp, replacement) {
        return input.replace(regexp, replacement);
    };
}

function enumParamsFilter() {
    return function(input,params) {
    	for( var i = 0 ;i < params.length ;i++){
    		var obj = params[i];
    		for(var key in obj){
    			if(input == key){
	        			return obj[key];
	        		}
    		}
    	}
    	return "";
    }
}


function myRegex() {
    return function (input, regex, flags, replacement) {
        if (input && regex && flags) {
            replacement = replacement || '';
            return input.replace(new RegExp(regex, flags), replacement);
        }
        return input;
    };
}

function trustHtml($sce) {
    return function(data){
        return $sce.trustAsHtml(data);
    }
}
function getStatus(){
    return function (input) {
        if(input){
            var thisInput = input.toString();
            if(thisInput === "1"){
                return "正常";
            }else if(thisInput === "2"){
                return "已到期";
            } else if(thisInput === "3"){
                return "即将到期";
            }
        }
    }
}

angular
    .module('inspinia')
    .filter('capitalize', capitalize)
	.filter('getValueByList', getValueByList)
	.filter('getMerIdAndName', getMerIdAndName)
    .filter('getMerchantNameByMerId',getMerchantNameByMerId)
    .filter('replace', replace)
    .filter('enumParamsFilter', enumParamsFilter)
    .filter('trustHtml', trustHtml)
    .filter('myRegex', myRegex)
    .filter('getStatus', getStatus)
;