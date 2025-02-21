<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.foryuum.frontend.common.vo.UserInfoVo"%> 
<%@ page import="com.foryuum.frontend.common.util.SessionUtil"%>
<%@ page import="com.foryuum.frontend.common.ComConstant"%>

<!DOCTYPE html>
<%
	session = request.getSession();
	UserInfoVo userInfo = SessionUtil.getUserInfo(session);
%>
<script type="text/javascript">
	var arrScreenMenu = new Array();
	
	$(document).ready(doMenuSrch);
	
	function doMenuSrch() {
		$.ajax({
			method : "post",
			url : "<%=ComConstant.CONTEXT_ROOT %>/getMenuList.do",
			dataType : "JSON",
			async : false,
			success : sucDoMenuSrch,
			error : function(xhr, status, error) {
				mobileLoadingEnd();
				commonHandleError(xhr, status, error,  "메뉴 조회중 오류가 발생 하였습니다.");
			}
		});
	}
	
	function sucDoMenuSrch(result) {
		arrScreenMenu = result.returnData; 
	
		var inHtml = '';
		var firstInHtml = '';
		var secondInHtml = '';
		var thirdInHtml = '';
	
		if(arrScreenMenu.length > 0) {
			firstInHtml += '<ul class="dep1">';
			for(var i = 0; i< arrScreenMenu.length; i++){
				var val = arrScreenMenu[i];
				if(val.LEVELNO == 1){
				    var urlPath = val.URLPATH == undefined ? '#' : val.URLPATH;
                    firstInHtml += '<li onMouseEnter="showSubMenu(\'' + val.MENU_ID + '\')"><a href="' + urlPath + '">' + val.MENU_NAME + '</a></li>';
	
					inHtml += '<li>';
					inHtml += '<div>';
					inHtml += '<div class="sub_nav_wrap">';
					inHtml += '<ul id=\''+val.MENU_ID+'\' class="dep2"></ul>';
					inHtml += '</div>';
					inHtml += '</div>';
					inHtml += '</li>';
				}
			}
		}
		firstInHtml += '</ul>';
		
		$("#lnb_p").html(firstInHtml);
		$("#ulLnb_depth").html(inHtml);
	}
	
	function showSubMenu(strScreenId) {
		var secondInHtml ='';
		for (var i = 0; i < arrScreenMenu.length; i++) {
			if (arrScreenMenu[i].P_MENU_ID == strScreenId) {
				secondInHtml +=	'<li><a href="#">'+ arrScreenMenu[i].MENU_NAME+'<span class="icon"></a>';
				
				secondInHtml +=	'<ul class="dep3">';
				for (var z = 0; z < arrScreenMenu.length; z++) { 
					if (arrScreenMenu[z].P_MENU_ID == arrScreenMenu[i].MENU_ID) {
						if (null != arrScreenMenu[z].URL_PATH) {
							secondInHtml +='<li><a href="#" onClick="movePage(\''+arrScreenMenu[z].URL_PATH+'\', \''+arrScreenMenu[z].MENU_ID+'\')">'+ arrScreenMenu[z].MENU_NAME+'<span class="icon"></a>';
						} else {
							secondInHtml +='<li><a href="#">'+ arrScreenMenu[z].MENU_NAME+'<span class="icon"></a></li>';
						}
					}
				}
				secondInHtml +='</ul>';
				secondInHtml +='</li>';					
			}
			$("#"+strScreenId).html(secondInHtml);
		}
	}
	
	function movePage(url, menuId) {
		var $form = $('<form></form>');
		$form.attr('action', url);
		$form.attr('target', '_self');
		$form.attr('method', 'post');
		$form.submit();
	}
</script>

<div class="inr">
    <div id="lnb_p" class="lnb_p"></div>
    <div class="gnb_p">
        <ul class="gnb_info">
            <li class="name">
                <a href=""><%=userInfo.getUserName()%> 님</a>
            </li>
            <li class="name2"></li>
        </ul>
        <ul class="gnb_list">
            <li class="login">
                <button type="button" name="" class="btn btnR btn002" onclick="$(location).attr('href', '<%=ComConstant.CONTEXT_ROOT %>/logout.do')">로그아웃</button>
            </li>
        </ul>
    </div>
</div>

<div class="lnb_depth">
    <ul id="ulLnb_depth">
    </ul>
</div>
