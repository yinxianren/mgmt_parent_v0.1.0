function checkIllegalCharacters(v) {
	var i1 = v.indexOf("<");
	var i2 = v.indexOf(">");
	var i3 = v.indexOf("&");
	var i4 = v.indexOf("\"");
	var i5 = v.indexOf("'");
	var i6 = v.indexOf("(");
	var i7 = v.indexOf(")");
	var i8 = v.indexOf("#");
	var i9 = v.indexOf("%");
	var i10 = v.indexOf(";");
	var i11 = v.indexOf("+");
	var i12 = v.indexOf("-");
	if (i1 > -1 || i2 > -1 || i3 > -1 || i4 > -1 || i5 > -1 || i6 > -1
			|| i7 > -1 || i8 > -1 || i9 > -1 || i10 > -1 || i11 > -1
			|| i12 > -1) {
		return false;
	} else {
		return true;
	}
}

// 输入卡号时,每四位更换一下背景色.方便核对
function changecardnoshow(base) {
	var cardNo = document.getElementById("cardNo");
	var firstNum = cardNo.value.substr(0, 1);
	var FSNum = cardNo.value.substr(0, 2);
	var FourNum = cardNo.value.substr(0, 4);
	cardNo.value = cardNo.value.replace(/\D/g, "");
	if (cardNo.value.length >= 25) {
		cardNo.value = cardNo.value.substr(0, 25);
	}
	$("#cardNo").nextAll("img").hide();
	if (FSNum == "35" || FourNum == "2131" || FourNum == "1800") {
		$("#vistaLoss").show();
		$("#masterLoss").show();
		$("#jcbPic").show();
		$("#amexLoss").show();
	} else if (FSNum == "34" || FSNum == "37") {
		$("#vistaLoss").show();
		$("#masterLoss").show();
		$("#jcbLoss").show();
		$("#amexPic").show();
	} else if (firstNum == "4") {
		$("#vistaPic").show();
		$("#masterLoss").show();
		$("#jcbLoss").show();
		$("#amexLoss").show();
	} else if (firstNum == "5") {
		$("#vistaLoss").show();
		$("#masterPic").show();
		$("#jcbLoss").show();
		$("#amexLoss").show();
	} else {
		$("#vistaPic").show();
		$("#masterPic").show();
		$("#jcbPic").show();
		$("#amexPic").show();
	}
	var len = cardNo.value.length;
	if (len <= 4) {
		cardNo.style.backgroundImage = 'none';
	}
	if (len > 4 && len <= 12) {
		cardNo.style.backgroundImage = 'url(' + base + '/common/images/cardnoshow_2.gif)';
	}
	if (len > 12) {
		cardNo.style.backgroundImage = 'url(' + base + '/common/images/cardnoshow_3.gif)';
	}
	cardNo.style.backgroundRepeat = 'no-repeat';
}

// 验证卡号
function checkCardNoBy(value) {
	// accept only digits and dashes
	if (/[^0-9-]+/.test(value))
		return false;
	if (value.length != 16 && value.length != 15 && value.length != 13 && value.length != 14)
		return false;
	var startLetter = value.substring(0, 1);
	if (startLetter != null && startLetter != "6" && startLetter != "4" && startLetter != "5" && startLetter != "3" && startLetter != "1" && startLetter != "2")
		return false;
	var nCheck = 0, nDigit = 0, bEven = false;
	value = value.replace(/\D/g, "");
	for (var n = value.length - 1; n >= 0; n--) {
		var cDigit = value.charAt(n);
		var nDigit = parseInt(cDigit, 10);
		if (bEven) {
			if ((nDigit *= 2) > 9)
				nDigit -= 9;
		}
		nCheck += nDigit;
		bEven = !bEven;
	}
	return (nCheck % 10) == 0;
}

// email
function checkEmailBy(value) {
	return /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i.test(value);
}

function checkFormSb() {
	$("#paySend .error_input").addClass("basic_input");
	$("#paySend .error_input").removeClass("error_input");
	var cardNo = document.getElementById("cardNo");
	var cardExpireMonth = document.getElementById("cardExpireMonth");
	var cardExpireYear = document.getElementById("cardExpireYear");
	var cvv2 = document.getElementById("cvv2");
	var issuingBank = document.getElementById("issuingBank");
	var firstName = document.getElementById("firstName");
	var lastName = document.getElementById("lastName");
	var address = document.getElementById("address");
	var city = document.getElementById("city");
	var country = document.getElementById("country");
	var zip = document.getElementById("zip");
	var email = document.getElementById("email");
	var phone = document.getElementById("phone");

	if (cardNo.value == "" || !checkCardNoBy(cardNo.value)) {
		return showError(cardNo);
	}
	if (cardExpireMonth.value == "") {
		return showError(cardExpireMonth);
	}
	if (cardExpireYear.value == "") {
		return showError(cardExpireYear);
	}
	
	var max = 3;
	if(cardNo == "34" || cardNo == "37"){
		max = 4;
	}
	
	/*if (cvv2.value == "" || isNaN(parseInt(cvv2.value)) || (parseInt(cvv2.value) + '').length != cvv2.value.length || cvv2.value.length > max) {
		return showError(cvv2);
	}*/
	//modify  by tan 20170323 判断条件出错，如果前面是0开始，转成int位数不对，修改成直接判断长度是不是大于2.
	if (cvv2.value == "" || isNaN(parseInt(cvv2.value)) ||cvv2.value.length<3 || cvv2.value.length > max) {
		return showError(cvv2);
	}
	if (issuingBank.value == "") {
		return showError(issuingBank);
	}
	if (firstName.value == "") {
		return showError(firstName);
	}
	if (lastName.value == "") {
		return showError(lastName);
	}
	if (address.value == "") {
		return showError(address);
	}
	if (city.value == "") {
		return showError(city);
	}
	if (country.value == "") {
		return showError(country);
	}
	if (zip.value == "") {
		return showError(zip);
	}
	if (email.value == "" || !checkEmailBy(email.value)) {
		return showError(email);
	}
	if (phone.value == "") {
		return showError(phone);
	}
	document.getElementById("errorMsgId").innerHTML = "";
	document.getElementById("buttonSt").disabled = true; // 提交按钮不可用
	return true;
}

function showError(o) {// 显示错误信息，并把焦点移到出错处，并且框变红
	o.focus();
	o.className = "error_input";
	document.getElementById("errorMsgId").innerHTML = document.getElementById(o.name + "Msg").innerHTML;
	return false;
}
function checkcvv() {
}
