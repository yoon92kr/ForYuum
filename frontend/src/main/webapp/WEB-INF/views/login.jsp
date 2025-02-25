<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.foryuum.frontend.common.ComConstant"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<!DOCTYPE html>
<%
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
	
        <link rel="stylesheet" href="<%=ComConstant.CONTEXT_ROOT %>/css/common.css?ver=<%=strDate %>">
        <link rel="stylesheet" href="<%=ComConstant.CONTEXT_ROOT %>/jquery/jquery-ui-1.14.1.min.css">
		
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/jquery/jquery-3.7.1.min.js"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/jquery/jquery-ui-1.14.1.min.js"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/js/common.js?ver=<%=strDate %>"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/js/sweetalert.min.js"></script>
        
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/js/rsa/jsbn.js"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/js/rsa/prng4.js"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/js/rsa/rsa.js"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/js/rsa/rng.js"></script>
        
        <script type="text/javascript">
            var keyModulus = "${keyModulus}";
            var keyExponent = "${keyExponent}";

			function loginCheck(){
				if(isNull($("#i_user_id").val())) {
					swal({title: "아이디를 입력하세요.", closeOnClickOutside:false});
					return;
				}
				
				if(isNull($("#i_user_password").val())) {
					swal({title: "비밀번호을 입력하세요.", closeOnClickOutside:false});
					return;
				}

				$.ajax({
					method : "POST",
					url : "loginCheck.do",
					data : {
						P_USER_ID  : EncryptedValue($("#i_user_id").val(), keyModulus, keyExponent),
						P_USER_PASSWORD : EncryptedValue($("#i_user_password").val(), keyModulus, keyExponent)
					},
					dataType : "JSON",
					success : sucLoginCheck,
					error : errLoginCheck
				});
			}

			function sucLoginCheck(data) {
				if (isNotNull(data)) {
					if (data.result) {
						$(location).attr('href','/home.do');
					} else {
                        swal({title: data.resultMsg,  className: "swal-loginResultPop", text: returnData.failReason, closeOnClickOutside: false, buttons: {true: 'OK'}}).then(function (result) {
                            if(result == 'true') {
                                location.reload(true)
                            }
                        });
					}
				} else {
	                swal({title: "로그인 중 오류가 발생하였습니다.", closeOnClickOutside: false, button: {true: 'OK'}}).then(function (result) {
	                    if (result == 'true') {
	                        location.reload(true)
	                    }
	                });
				}
			}

			function errLoginCheck(xhr, status, error) {
                swal({title: "로그인 중 오류가 발생하였습니다.", closeOnClickOutside: false, button: {true: 'OK'}}).then(function (result) {
                    if (result == 'true') {
                        location.reload(true)
                    }
                });
            }

			function EncryptedValue(strValue, keyModulus, rsaPpublicKeyExponent){
				var rsa = new RSAKey();
				rsa.setPublic(keyModulus, rsaPpublicKeyExponent);
				var securedValue = rsa.encrypt(strValue);
				
				return securedValue;
			}
			
		</script>
    </head>
    <body>
	    <div id="viewport" class="login-wrap">
	        <div id="login-view">
	            <div class="login-inner">
	                <main id="login-main" class="login">
	                    <div class="main-inner" style="width: 0% !important">
	                        <div class="login-warp">
	                            <div class="login-area">
	                                <div class="login-logo">
	                                    <div class="txt_box">
	                                        <h2>LOG IN</h2>
	                                        <p>
	                                            Yuum's Family를 위한 시스템에 오신것을 환영해요.
	                                        </p>
	                                    </div>
	                                </div>
	                                <div class="login-form" style="margin: 35px 0;">
	                                    <div class="login-input-box">
	                                        <ul>
	                                            <li>
	                                                <label for="i_user_id"><i class="fas fa-user"></i><span class="srOnly">아이디</span></label>
	                                                <input type="text" name="i_user_id" id="i_user_id" value="" placeholder="아이디" class="text w100p"
	                                                       onkeypress="if(event.keyCode == 13)loginCheck()" required>
	                                            </li>
	                                            <li>
	                                                <label for="i_user_password"><i class="fas fa-lock"></i><span class="srOnly">비밀번호</span></label>
	                                                <input type="password" name="i_user_password" id="i_user_password" value="" placeholder="비밀번호" class="text w100p"
	                                                       onkeypress="if(event.keyCode == 13)loginCheck()" required>
	                                            </li>
	                                        </ul>
	                                    </div>
	                                    <button type="submit" name="" class="btn btnM btn002 w100p" onclick="loginCheck()">로그인</button>
	                                </div>
	
	                                <div class="login-info" id="d_login_info">
	                                    <p style="font-size: 14px; color:#757575;line-height: 1.5em; font-weight: bold;">
	                                        계정신청은 아빠에게 문의하시기 바랍니다.<br/>
	                                    </p>
	                                </div>
	                            </div>
	                        </div>
	                    </div>
	                </main>
	                <jsp:include page="/WEB-INF/views/common/contentsFooter.jsp"/>
	            </div>
	        </div>
	    </div>
    </body>
</html>