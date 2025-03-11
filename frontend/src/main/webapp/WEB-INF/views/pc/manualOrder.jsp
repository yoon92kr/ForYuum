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
		<link rel="stylesheet" href="<%=ComConstant.CONTEXT_ROOT %>/fontawesome/css/solid.min.css">
        <link rel="stylesheet" href="<%=ComConstant.CONTEXT_ROOT %>/fontawesome/css/brands.min.css">
        <link rel="stylesheet" href="<%=ComConstant.CONTEXT_ROOT %>/fontawesome/css/fontawesome.min.css">
        
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/jquery/jquery-3.7.1.min.js"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/jquery/jquery-ui-1.14.1.min.js"></script>
		<script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/js/common.js?ver=<%=strDate %>"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/js/sweetalert.min.js"></script>
        
        <style>
     		#appendModal .divBodyScroll::-webkit-scrollbar:vertical { width : 8px; }
		    #appendModal .divBodyScroll::-webkit-scrollbar:horizontal { height : 8px;}
		    #appendModal .divBodyScroll::-webkit-scrollbar-button { width : 0; height : 0; }
		    #appendModal .divBodyScroll::-webkit-scrollbar-thumb { background : #cccccc; border-radius : 10px;}
		    #appendModal .divBodyScroll::-webkit-scrollbar-track { background : rgba(255, 255, 255, 0.1);}		
        </style>
	</head>
	<script type="text/javascript">
		const inputList = ["#i_vendor_item_name", "#i_vendor_price", "#i_count", "#i_vendor_item_option", "#i_name", "#i_address", "#i_mobile"];
		const vendorList = ["예원", "리턴", "라잇", "어썸", "모뜨", "어바우트", "썸데이", "큐브", "베이드", "데이시크", "또또", "발랑", "그리블"];
		var productOrderId = "";
		var idx = 1;
		var orderMap = {};
		
	    function appendOrder() {
	    	$('#appendModal').show();
	    }
	    
	    function closeAppendModal() {
	    	$('#appendModal').hide();
	    }

		function getItemInfo() {
			if(isNull($("#i_search_no").val())) {
				swal({title: "상품번호를 입력하세요.", closeOnClickOutside:false});
				return;
			}

			var searchNo = $("#i_search_no").val().trim();

			if(searchNo.length == 10) {
				getItemInfoByItemNo(searchNo)
			} else if(searchNo.length == 16) {
				getItemInfoByItemOrderNo(searchNo)
			} else {
				swal({title: "상품번호/상품주문번호를 확인하세요.", closeOnClickOutside:false});
			}
		}

		/* 상품번호 기반 조회 */
		function getItemInfoByItemNo(searchNo) {
			mobileLoading();

			$.ajax({
				method : "POST",
				url : "/item/getItemInfoByItemNo.do",
				data : {
					P_ITEM_NO  : searchNo,
					P_COMPANY_NAME : $("#i_company_name").val()
				},
				dataType : "JSON",
				success : sucGetItemInfo,
				error : function(xhr, status, error) {
					mobileLoadingEnd();
					commonHandleError(xhr, status, error,  "상품 정보 조회중 오류가 발생 하였습니다.");
				}
			});
		}

		/* 주문번호 기반 조회 */
		function getItemInfoByItemOrderNo(searchNo) {
			mobileLoading();

			$.ajax({
				method : "POST",
				url : "/item/getItemInfoByItemOrderNo.do",
				data : {
					P_ITEM_ORDER_NO  : searchNo,
					P_COMPANY_NAME : $("#i_company_name").val()
				},
				dataType : "JSON",
				success : sucGetItemInfoByItemOrderNo,
				error : function(xhr, status, error) {
					mobileLoadingEnd();
					commonHandleError(xhr, status, error,  "상품 정보 조회중 오류가 발생 하였습니다.");
				}
			});
		}

		function sucGetItemInfoByItemNo(data) {
			var itemInfo = data.returnData;
			if(isNotNull(itemInfo)) {
				if (!vendorList.includes(itemInfo.VENDOR_NAME)) {
					swal({title: "조회된 상품 정보가 없습니다.", closeOnClickOutside:false});
				} else {
					$("#i_vendor_item_name").val(itemInfo.VENDOR_ITEM_NAME);
					$("#i_vendor_price").val(convertNumber(itemInfo.VENDOR_PRICE));
					$("#i_count").val(1);
					$('#i_vendor_name').val(itemInfo.VENDOR_NAME).change();
					$("#i_vendor_i_vendor_item_optionname").val('');
				}
			} else {
				swal({title: "조회된 상품 정보가 없습니다.", closeOnClickOutside:false});
			}
			mobileLoadingEnd();
		}

		function sucGetItemInfoByItemOrderNo(data) {
			var itemInfo = data.returnData.RESULT_VALUE;
			if(isNotNull(itemInfo)) {
				if (!vendorList.includes(itemInfo.VENDOR_NAME)) {
					swal({title: "조회된 상품 정보가 없습니다.", closeOnClickOutside:false});
				} else {
					productOrderId = $("#i_search_no").val().trim();
					$("#i_vendor_item_name").val(itemInfo.VENDOR_ITEM_NAME);
					$("#i_vendor_price").val(convertNumber(itemInfo.VENDOR_PRICE));
					$("#i_count").val(itemInfo.ORDER_COUNT);
					$('#i_vendor_name').val(itemInfo.VENDOR_NAME).change();
					$("#i_vendor_item_option").val(itemInfo.ORDER_ITEM_OPTION);

					$("#i_orderer_name").val(itemInfo.ORDERER_NAME);
					$("#i_orderer_id").val(itemInfo.ORDERER_ID);
					$("#i_order_date").val(itemInfo.ORDER_DATE);

					$("#i_name").val(itemInfo.RECEIVER_NAME);
					$("#i_address").val(itemInfo.RECEIVER_ADDRES);
					$("#i_mobile").val(itemInfo.RECEIVER_MOBILE);
					$("#i_note").val(itemInfo.SHIPPING_MEMO);

					if(data.returnData.RESULT == true) {
						swal({title: data.returnData.TITLE, text: data.returnData.TEXT, closeOnClickOutside:false});
					}
				}
			} else {
				swal({title: "조회된 상품 정보가 없습니다.", closeOnClickOutside:false});
			}

			mobileLoadingEnd();
		}
		
	</script>
	<body>	
		<div id="viewport" class="dash">
			<div id="dashboard_view">
				<header id="header">
					<jsp:include page="/WEB-INF/views/common/topMenu.jsp" />
					<div class="breadcrumb">
						<div class="inner">
							<span class="breadcrumb_home">home</span> 
							<span class="breadcrumb_txt">스토어관리</span> 
							<span class="breadcrumb_txt">사입/배송</span>
						</div>
					</div>
				</header>
				<main id="container">
					<div class="sub_wrap">
						<div class="contents">
							<div class="search_wrap">
								<h2 class="sub-title">사입/배송</h2>
								<div style="margin: 0 120px;">
									<div class="flexbox" style="margin: 0 auto; justify-content: center;">
										<div class="tbl-normal flexbox" style="height: 660px; margin-left: 40px; width: 40%; background-color: #EAF3EA;">
											<div style="position: relative; margin-left: 10px;">
												<p class="subTitleDot" style="margin: 10px 15px 10px 20px;">요청 서식</p>
												<div class="list_option_box" style='height: 525px;'>
													<table class="option_tbl">
														<colgroup>
															<col span="10">
														</colgroup>
														<tbody>
															<tr>
																<td colspan="3">
																	<div class="search-box right">
																		<h3 class="input-h3">스토어</h3>
																		<div class="div-style">
																			<select class="selectbox-design" id="i_company_name" style='width: 50%'>
																				<option value="모던블랑코">모던</option>
																			</select>
																		</div>
																	</div>
																</td>
																<td colspan="5">
																	<div class="search-box right" style="padding-bottom:4px;">
																		<h3 class="input-h3">상품(주문)번호</h3>
																		<div class="div-style">
																			<input type="text" class="text" id="i_search_no" autocomplete="off" style='width: 62%;'/>
																		</div>
																	</div>
																</td>
																<td colspan="2">
																	<a class="search_bt" onClick="getItemInfo()" style='height: 28px; width: 80px;' >
																		<span style='margin-top: 3px;'>조회</span>
																	</a>
																</td>
															</tr>
															<tr>
																<th>도매정보</th>
															</tr>
															<tr>
																<td colspan="5">
																	<div class="search-box right" style="padding-bottom:4px;">
																		<h3 class="input-h3">매장명</h3>
																		<div class="div-style">
																			<select class="selectbox-design" id="i_vendor_name" style='width: 50%'>
																				<option value="">매장명</option>
																				<option value="예원">예원</option>
																				<option value="리턴">리턴</option>
																				<option value="라잇">라잇</option>
																				<option value="어썸">어썸</option>
																				<option value="모뜨">모뜨</option>
																				<option value="어바우트">어바우트</option>
																				<option value="썸데이">썸데이</option>
																				<option value="큐브">큐브</option>
																				<option value="베이드">베이드</option>
																				<option value="데이시크">데이시크</option>
																				<option value="또또">또또</option>
																				<option value="그리블">그리블</option>
																			</select>
																		</div>
																	</div>
																</td>
																<td colspan="5">
																	<div class="search-box right" style="padding-bottom:4px;">
																		<h3 class="input-h3">상품명</h3>
																		<div class="div-style">
																			<input type="text" class="text" id="i_vendor_item_name" autocomplete="off" style='width: 80%;'/>
																		</div>
																	</div>
																</td>
															</tr>
															<tr>
																<td colspan="5">
																	<div class="search-box right" style="padding-bottom:4px;">
																		<h3 class="input-h3">옵션명</h3>
																		<div class="div-style">
																			<input type="text" class="text" id="i_vendor_item_option" autocomplete="off" style='width: 62%;'/>
																		</div>
																	</div>
																</td>
																<td colspan="3">
																	<div class="search-box right" style="padding-bottom:4px;">
																		<h3 class="input-h3">가격</h3>
																		<div class="div-style">
																			<input type="text" class="text" id="i_vendor_price" autocomplete="off" style='width: 62%;'/>
																		</div>
																	</div>
																</td>
																<td colspan="2">
																	<div class="search-box right" style="padding-bottom:4px;">
																		<h3 class="input-h3">수량</h3>
																		<div class="div-style">
																			<input type="text" class="text" id="i_count" autocomplete="off" style='width: 43%;'/>
																		</div>
																	</div>
																</td>
															</tr>
														</tbody>
													</table>
												</div>
												<div class="list_option_bottom" style="width: 99%; justify-content: center;display: flex;">
													<a class="option_add_bt" onclick="initOrder()" data-toggle="modal" data-target="#v_back_drop" style="width: 300px;"><span>항목 추가</span></a>
												</div>
											</div>
										</div>
										<div class="tbl-normal flexbox" style="height: 660px; margin-left: 40px; width: 56%;">
											<div style="position: relative; margin-left: 10px;">
												<p class="subTitleDot" style="margin: 10px 15px 10px 20px;">요청 항목</p>
												<div id="menuTreeDiv" style="width: 886px; height: 530px; border: 1px solid #BDBDBD; overflow: auto;"></div>
												<div class="list_option_bottom" style="width: 99%; justify-content: center;display: flex;">
													<a class="option_write_bt" onclick="initOrder()" data-toggle="modal" data-target="#v_back_drop" style="width: 300px;"><span>사입/배송 요청</span></a>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</main>
				<jsp:include page="/WEB-INF/views/common/contentsFooter.jsp"/>
			</div>
		</div>
		<div id="loading" style="z-index: 20000 !important;"></div>
	</body>
</html>