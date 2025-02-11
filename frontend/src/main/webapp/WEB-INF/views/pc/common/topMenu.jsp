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
			url : "<%=ComConstant.CONTEXT_ROOT %>/menuList.do",
			dataType : "JSON",
			async : false,
			beforeSend : beforeAjaxCommon,
			success : sucDoMenuSrch,
			error : errDoMenuSrch
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
                    firstInHtml += '<li onMouseEnter="showSubMenu(\'' + val.SCREENID + '\')"><a href="' + urlPath + '">' + val.SCREENNAME + '</a></li>';
	
					inHtml += '<li>';
					inHtml += '<div>';
					inHtml += '<div class="sub_nav_wrap">';
					inHtml += '<ul id=\''+val.SCREENID+'\' class="dep2"></ul>';
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
	
	function errDoMenuSrch(xhr,status,error){
		if(xhr.status == 999) {
			swal({title:"세션이 만료되었습니다. 다시 로그인하시기 바랍니다.",closeOnClickOutside:false}).then($(location).attr('href', 'login.do'));
		} else {
			swal({title:"메뉴 리스트 조회 중 오류가 발생하였습니다.",icon: "error",closeOnClickOutside:false});
		}
	}
	
	function showSubMenu(strScreenId) {
		var secondInHtml ='';
		for (var i = 0; i < arrScreenMenu.length; i++) {
			if (arrScreenMenu[i].PSCREENID == strScreenId) {
				if (null != arrScreenMenu[i].URLPATH && 'NULL' != arrScreenMenu[i].URLPATH) {
					if(arrScreenMenu[i].SCREENID == "B0090_000" || arrScreenMenu[i].SCREENID == "B0110_000" ){
						if(specificUser == true){
							secondInHtml +=	'<li><a href="#" onClick="movePage(\''+arrScreenMenu[i].URLPATH+'\', \''+arrScreenMenu[i].SCREENID+'\', \''+arrScreenMenu[i].PATHNAME+'\')">'+ arrScreenMenu[i].SCREENNAME+'<span class="icon"></a>';
							continue;
						} else {
							continue;
						}
					} else {
						secondInHtml +=	'<li><a href="#" onClick="movePage(\''+arrScreenMenu[i].URLPATH+'\', \''+arrScreenMenu[i].SCREENID+'\', \''+arrScreenMenu[i].PATHNAME+'\')">'+ arrScreenMenu[i].SCREENNAME+'<span class="icon"></a>';
					}
				} else {
					if (arrScreenMenu[i].SCREENID == "B0101_000") {
						secondInHtml +=	'<li style="letter-spacing:-1px;"><a href="#">'+ arrScreenMenu[i].SCREENNAME+'<span class="icon"></a>';
					} else { 
						secondInHtml +=	'<li><a href="#">'+ arrScreenMenu[i].SCREENNAME+'<span class="icon"></a>';
					}
				}
				
				secondInHtml +=	'<ul class="dep3">';
				for (var z = 0; z < arrScreenMenu.length; z++) { 
					if (arrScreenMenu[z].PSCREENID == arrScreenMenu[i].SCREENID) {
						if (null != arrScreenMenu[z].URLPATH) {
							secondInHtml +='<li><a href="#" onClick="movePage(\''+arrScreenMenu[z].URLPATH+'\', \''+arrScreenMenu[z].SCREENID+'\', \''+arrScreenMenu[z].PATHNAME+'\')">'+ arrScreenMenu[z].SCREENNAME+'<span class="icon"></a>';
						} else {
							secondInHtml +='<li><a href="#">'+ arrScreenMenu[z].SCREENNAME+'<span class="icon"></a></li>';
						}
					}
				}
				secondInHtml +='</ul>';
				secondInHtml +='</li>';					
			}
			$("#"+strScreenId).html(secondInHtml);
		}
	}
	
	function movePage(url, screenId, pathName) {
		var $form = $('<form></form>');
		$form.attr('action', url);
		$form.attr('target', '_self');
		$form.attr('method', 'post');
		$form.appendTo('body');
		$form.append($('<input type="hidden" name="SAVE_FLAG" value="M">'));
		$form.append($('<input type="hidden" name="SCREENID" value="'+ screenId +'">'));
		$form.append($('<input type="hidden" name="PATHNAME" value="'+ pathName +'">'));
		$form.append($('<input type="hidden" name="CLIENTIP" value="<%=userInfo.getStrClientIP()%>">'));
		$form.submit();
	}
</script>
<div class="inr">
    <div class="logo">
        <h1>
			<span>
         		<a href="<%=ComConstant.CONTEXT_ROOT %>/home.do" style="display:inline-flex;width:160px;">
         			<span style="color:#18c8c8;float:left;font-weight:bold; margin-left: 15px;">For Yuum</span>
         		</a>
			</span>
        </h1>
    </div>
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
