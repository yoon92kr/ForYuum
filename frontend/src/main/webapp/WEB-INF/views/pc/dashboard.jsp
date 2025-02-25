<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.foryuum.frontend.common.vo.UserInfoVo"%> 
<%@ page import="com.foryuum.frontend.common.ComConstant"%>
<%@ page import="com.foryuum.frontend.common.util.SessionUtil"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<!DOCTYPE html>
<%
	session = request.getSession();
	UserInfoVo userInfo = SessionUtil.getUserInfo(session);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	String strDate = sdf.format(new Date());
%>
<html lang="ko" dir="ltr">
	<head>
		<meta property="og:title" content="For Yuum">
		<meta property="og:type" content="website">
		<meta property="og:image" content="http://39.119.158.240:21530//img/site_img.png">
		<meta property="og:description" content="만사가 귀찮은 사랑스러운 유미를 위한">
		
        <title><%=ComConstant.TITLE%></title>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
			

		<link rel="stylesheet" href="<%=ComConstant.CONTEXT_ROOT %>/css/bootstrap.min.css"> 
        <link rel="stylesheet" href="<%=ComConstant.CONTEXT_ROOT %>/jquery/jquery-ui-1.14.1.min.css">
        <link rel="stylesheet" href="<%=ComConstant.CONTEXT_ROOT %>/css/common.css?ver=<%=strDate %>">
		
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/jquery/jquery-3.7.1.min.js"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/jquery/jquery-ui-1.14.1.min.js"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/js/common.js?ver=<%=strDate %>"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/js/sweetalert.min.js"></script>
	</head>
	<script type="text/javascript">
	</script>
	<body>	
		<div id="viewport" class="dash">
		    <div id="dashboard_view">
		    	<header id="header">
		      		<jsp:include page="/WEB-INF/views/common/topMenu.jsp"/>
				</header>
		
		      	<main id="container" style="background:#F5F7FA; top:75px;height:calc(100% - 65px);">
					<div class="content_wrap" style="padding:10px;">
			        	<div class="contents">
			           		<div class="row">
			             		<div class="white-space">
			               			<div class="cont_2 cont_type2 padding25 tab" id="d_openStatistics">
			                 			<div class="cont_title">
			                 			</div>
			               			</div>
			             		</div>
			           		</div>
						</div>
					</div>
				</main>
				<%--Contents Footer--%>
				<jsp:include page="/WEB-INF/views/common/contentsFooter.jsp"/>
				<%--Contents Footer--%>
			</div>
		</div>
		<div id="loadingDiv"></div>
	</body>
</html>