<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@ page import="com.foryuum.frontend.common.ComConstant"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Error</title>
	</head>
	<style type="text/css">
		#error_box {
			text-align: center;
			display: inline-block;
			padding-left: 10px;
			padding-top: 3px;
		}
		
		#error_box h2 {
			margin: 0;
			font-size: 40px;
			font-weight: bold;
			color: #ff6b00;
			padding-bottom: 5px;
		}
		
		#error_box a {
			margin: 0;
			font-size: 13px;
			color: #9E9E9E;
			text-decoration: none;
			color: inherit;
		}
		
		a:hover, a:active, a:focus, a:visited {
			color: inherit; 
			text-decoration: none; 
		}
		
		body, html {
			height: 100%;
			margin: 0;
			display: flex;
			justify-content: center;
			align-items: center;
			width: 100%;
		}
		
		table {
			width: auto;
			max-width: 1200px;
			border-collapse: collapse;
		}
		
		td {
			text-align: center;
			vertical-align: middle;
		}

		@media screen and (max-width: 767px) {
			table {
				width: 95%;
				max-width: none; 
			}
			#error_box h2 {
				font-size: 32px; 
			}
			#error_box p {
				font-size: 16px; 
			}
		}
	</style>
	<body>
		<table id="error_msg">
			<tr>
				<td align="center"><img src="/img/main_img.png" /></td>
				<td>
					<div id="error_box">
						<h2>ERROR</h2>
						<a href="<%=ComConstant.CONTEXT_ROOT %>/home.do">무언가 단단히 잘못됬어요.</a> <br>
						<a href="<%=ComConstant.CONTEXT_ROOT %>/home.do">로그인 화면으로 돌아가요...</a>
					</div>
				</td>
			</tr>
			<tr>
			</tr>
		</table>
	</body>
</html>