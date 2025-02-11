<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.foryuum.frontend.common.vo.UserInfoVo"%> 
<%@ page import="com.foryuum.frontend.common.ComConstant"%>
<%@ page import="com.foryuum.frontend.common.util.SessionUtil"%>

<!DOCTYPE html>
<%
	session = request.getSession();
	UserInfoVo userInfo = SessionUtil.getUserInfo(session);
%>
<html lang="ko" dir="ltr">
	<head>
        <title><%=ComConstant.TITLE%></title>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta http-equiv="cache-Control" content="co-cache" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link rel="stylesheet" href="<%=ComConstant.CONTEXT_ROOT %>/css/common.css">
        <link rel="stylesheet" href="<%=ComConstant.CONTEXT_ROOT %>/jquery/jquery-ui-1.14.1.min.css">
		
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/jquery/jquery-3.7.1.min.js"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/jquery/jquery-ui-1.14.1.min.js"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/js/common.js"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/js/sweetalert.min.js"></script>
        
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/js/rsa/jsbn.js"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/js/rsa/prng4.js"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/js/rsa/rsa.js"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/js/rsa/rng.js"></script>
	</head>
	<style type="text/css">
		/*content.css*/
		.user_info .user_box {background:#07B3CF;border-radius: 4px; width:100%;height:150px; box-sizing: border-box; padding:15px; position: relative; z-index: 10;}
		.user_info .user_box:after {content: ''; position: absolute; width:110px; height: 80px; background-size:60%; top:70px;left:320px;}
		.user_info .user_box dl {color:#fff; font-size: 16px; width:400px; margin-top:20px; font-weight: 400;}
		.user_info .user_box dl dt {display: inline-block; width:40px; float: left; margin-right: 12px;}
		.user_info .user_box dl dd {display: inline-block; width:300px;}
		.user_info .user_box dl dt, .user_info .user_box dl dd {padding-top:5px;}
	</style>
	<script type="text/javascript">
		var	userid = '<%=userInfo.getUserId()%>';
	</script>
	<body>
		<div id="viewport" class="dash">
		    <div id="dashboard_view">
		    	<header id="header">
		      		<jsp:include page="/WEB-INF/views/pc/common/topMenu.jsp"/>
				</header>
		
		      	<main id="container" style="background:#F5F7FA; top:75px;height:calc(100% - 65px);">
					<div class="content_wrap" style="padding:10px;">
			        	<div class="contents">
			           		<div class="row">
			           		</div>
						</div>
					</div>
				</main>
				<%--Contents Footer--%>
				<jsp:include page="/WEB-INF/views/pc/common/contentsFooter.jsp"/>
				<%--Contents Footer--%>
			</div>
		</div>
		<div id="loadingDiv"></div>
	</body>
</html>