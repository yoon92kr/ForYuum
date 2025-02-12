function hideLoading(doc) {
	$('#loadingDiv').fadeOut();
}

function showLoading(doc) {
	var innerHtml = "";
	innerHtml += '<main id="guide-main" class="modal fade loader-system" style="width:100%;top:0;">';
	innerHtml += '	<div class="loader-wrapper" style="width:100%; height:100%;">';
	innerHtml += '	    <div class="cell" style="width:100%; height:100%;">';
	innerHtml += '	      <div class="loading load2" style="width:100%; height:100%;">';
	innerHtml += '		<div class="loader">';
	innerHtml += '		  <div class="loader-inner line-scale-pulse-out"><div></div><div></div><div></div><div></div><div></div></div>';
	innerHtml += '		  <span class="tooltip" style="height:44%;"><p>LOADING</p></span></div></div></div></div></div></main>';
	doc.getElementById('loadingDiv').innerHTML = innerHtml;
	$('#loadingDiv').fadeIn(200).css('display', 'table');
}

function isNull(value) {
	return value == null || value == 'null' || value == 'undefined' || value == '';
}

function isNotNull(value) {
	return !isNull(value);
}

function nullCheck(value) {
	return isNull(value) ? "" : value;
}

function commonHandleError(xhr, status, error, errMsg) {
	if (xhr.status == 999) {
		swal({ title: "세션이 만료되었습니다. 다시 로그인하시기 바랍니다.", icon: "error", closeOnClickOutside: false }).then($(location).attr('href', '<%=ComConstant.CONTEXT_ROOT%>/login.do'));
	} else if (xhr.status == 777) {
		swal({ title: "사용자의 권한이 부족한 기능입니다 권한 요청 후 다시 시도해주세요.", icon: 'error', closeOnClickOutside: false });
	} else {
		swal({ title: errMsg, icon: "error", closeOnClickOutside: false });
	}
}

function mobileLoading() {
	var modal = $('.mobile-loading');
	modal.fadeIn(300).css('display', 'table');
	modal.css("top", window.pageYOffset);
	modal.css("height", $(window).height() + 100);
}

function mobileLoadingEnd() {
	var modal = $('.mobile-loading');
	modal.fadeOut(300);
}

function convertNumber(x) {
	return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}
