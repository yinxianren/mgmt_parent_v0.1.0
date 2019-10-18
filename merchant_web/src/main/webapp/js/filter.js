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
            input = input.toString();
            for (var i = 0; i < list.length; i++) {
                list[i][keyName] = list[i][keyName].toString();
                if (list[i][keyName] === input) {
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
                    return '(' + list[i]['id'] + ')' + list[i]['name'];
                }
            }
        }
        return input;
    };
}

function replace() {
    return function (input, regexp, replacement) {
        return input.replace(regexp, replacement);
    };
}

function trustHtml($sce) {
    return function(data){
        return $sce.trustAsHtml(data);
    }
}
angular
    .module('inspinia')
    .filter('capitalize', capitalize)
	.filter('getValueByList', getValueByList)
	.filter('getMerIdAndName', getMerIdAndName)
	.filter('trustHtml', trustHtml)
    .filter('replace', replace)
;