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
		<meta property="og:title" content="For Yuum">
		<meta property="og:type" content="website">
		<meta property="og:image" content="http://39.119.158.240:21530//img/site_img.png">
		<meta property="og:description" content="만사가 귀찮은 사랑스러운 유미를 위한">
		
        <title><%=ComConstant.TITLE%></title>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta http-equiv="cache-Control" content="co-cache" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
			

		<link rel="stylesheet" href="<%=ComConstant.CONTEXT_ROOT %>/css/bootstrap.min.css"> 
        <link rel="stylesheet" href="<%=ComConstant.CONTEXT_ROOT %>/jquery/jquery-ui-1.14.1.min.css">
        <link rel="stylesheet" href="<%=ComConstant.CONTEXT_ROOT %>/css/mobileCommon.css">
		
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/jquery/jquery-3.7.1.min.js"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/jquery/jquery-ui-1.14.1.min.js"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/js/common.js"></script>
        <script type="text/javascript" src="<%=ComConstant.CONTEXT_ROOT %>/js/sweetalert.min.js"></script>
	</head>
	<script type="text/javascript">
		const inputList = ["#i_vendor_item_name", "#i_vendor_price", "#i_count", "#i_vendor_item_option", "#i_name", "#i_address", "#i_mobile"];
		const vendorList = ["예원", "리턴", "라잇", "어썸", "모뜨", "어바우트", "썸데이", "큐브", "베이드", "데이시크", "또또", "발랑", "그리블"];
		var productOrderId = "";
		var idx = 1;
		var orderMap = {};

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
		
		function appendOrder() {
			for(var id of inputList) {
				if(isNull($(id).val())) {
					swal({title: $(id).attr('placeholder') + "을 입력하세요.", closeOnClickOutside:false});
					return;
				}
			}
			
			if($("#i_vendor_name").val() == '') {
				swal({title: "매장명을 선택하세요.", closeOnClickOutside:false});
				return;
			}
			
			if(Object.keys(orderMap).length == 0) {
				$('#order_list').html('');
			}
			
			var orderHtml; 
			orderHtml += "<tr id = '" + idx + "' onClick='getCustomerInfo(" + idx + ")'>";
			orderHtml += "<td><a class='remove_bt' onClick='removeOrder(" + idx + ")'><span>X</span></a></td>";
			orderHtml += "<td>" + $("#i_name").val() + "</td>";
			orderHtml += "<td>" + $("#i_vendor_item_name").val() + "[" + $("#i_vendor_item_option").val() + "]" + "</td>";
			orderHtml += "<td>" + $("#i_count").val() + "</td>";
			orderHtml += '</tr>';
			
			orderMap[idx] = itemConvertMap();
			$('#order_list').append(orderHtml);
			resetForm();
			idx++;
		}
		
		function getCustomerInfo(idx) {
			$("#i_name").val(orderMap[idx].P_NAME);
			$("#i_address").val(orderMap[idx].P_ADDRESS);
			$("#i_mobile").val(orderMap[idx].P_MOBILE);
			$("#i_note").val(orderMap[idx].P_NOTE);
		}
		
		function itemConvertMap() {
			return info = {
					P_PRODUCT_ORDER_ID : productOrderId,
					P_COMPANY_NAME : $("#i_company_name").val(),
					P_ITEM_NO : $("#i_search_no").val(),
					P_VENDOR_ITEM_NAME : $("#i_vendor_item_name").val(),
					P_VENDOR_PRICE  : $("#i_vendor_price").val(),
					P_COUNT  : $("#i_count").val(),
					P_VENDOR_NAME : $("#i_vendor_name").val(),
					P_VENDOR_ITEM_OPTION : $("#i_vendor_item_option").val(),
					P_NAME : $("#i_name").val(),
					P_ADDRESS : $("#i_address").val(),
					P_MOBILE : $("#i_mobile").val(),
					P_NOTE : nullCheck($("#i_note").val())
			};
		}
		
		function resetForm() {
			$("#i_search_no").val('');
			$("#i_vendor_item_name").val('');
			$("#i_vendor_price").val('');
			$("#i_count").val('');
			$("#i_vendor_name").val('');
			$("#i_vendor_item_option").val('');
			$("#i_name").val('');
			$("#i_address").val('');
			$("#i_mobile").val('');
			$("#i_note").val('');
			$("#i_orderer_name").val('');
			$("#i_orderer_id").val('');
			$("#i_order_date").val('');
			productOrderId = "";
		}
		
		function removeOrder(idx) {
			if(idx in orderMap) {
				delete orderMap[idx];
				$("#" + idx).html('')
				
				if(Object.keys(orderMap).length == 0) {
					$('#order_list').html("<td colspan='4' style='height:50px;'>주문 항목을 추가해주세요</td>");
				}
			}
		}
		
		function initOrder() {
			mobileLoading();
			var param = [];
			
			for(var idx in orderMap) {
				param.push(orderMap[idx]);
			}
			
			$.ajax({
				method : "POST",
				url : "/item/initOrder.do",
				data : {data : JSON.stringify(param)},
				dataType : "JSON",
				success : sucInitOrder,
				error : function(xhr, status, error) {
					mobileLoadingEnd();
					commonHandleError(xhr, status, error,  "주문 과정에서 오류가 발생 하였습니다.");
				}
			});
		}
		
		function sucInitOrder(data) {
			mobileLoadingEnd();
			var returnData = data.returnData;
			
			if(returnData.RESULT) {
				swal({title:returnData.TITLE, text: returnData.TEXT, icon:"success",closeOnClickOutside:false}).then(() => {$(location).attr('href','/dashboard.do');});
			} else {
				swal({title: returnData.TITLE, text: returnData.TEXT, icon: "warning", closeOnClickOutside: false});
			}
			
		}
		
	</script>
	<body>	
		<header>
			<div class="header_inner">
			<h1 class="al_center">사입 요청</h1>
			<div class="nav_bt_box"><a href="#" class="nav_bt">전체메뉴</a></div>
			</div>
		</header>
		<section class="list_option_wrap">
			<div class="list_option_box">
				<table class="option_tbl">
					<colgroup>
						<col width="20%">
						<col width="20%">
						<col width="20%">
						<col width="10%">
						<col width="10%">
						<col width="10%">
						<col width="10%">
					</colgroup>
					<tbody>
						<tr>
							<td colspan="1">
								<select class="nomal_input" id="i_company_name" style="width:100%;">
									<option value="모던블랑코">모던</option>
<!-- 									<option value="야미블링">야미</option> -->
<!-- 									<option value="나금샵">나금</option> -->
								</select>
							</td>
							<td colspan="2">
								<input type="text" class="nomal_input" style="width:100%;" placeholder="상품번호/상품주문번호" id="i_search_no" autocomplete="off" />
							</td>
							<td colspan="2">
								<a class="search_bt" onClick="getItemInfo()" >
									<span>조회</span>
								</a>
							</td>
							<td colspan="2">
								<a class="add_bt" onClick="appendOrder()" >
									<span>추가</span>
								</a>
							</td>
						</tr>
						<tr>
							<th>도매정보</th>
						</tr>
						<tr>
							<td colspan="4">
								<input type="text" class="nomal_input" style="width:100%;" placeholder="상품명" id="i_vendor_item_name" autocomplete="off" />
							</td>
							<td colspan="2">
								<input type="text" class="nomal_input" style="width:100%;" placeholder="가격" id="i_vendor_price" autocomplete="off" />
							</td>
							<td colspan="1">
								<input type="text" class="nomal_input" style="width:100%;" placeholder="수량" id="i_count" autocomplete="off" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<select class="nomal_input" id="i_vendor_name" style="width:100%;">
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
							</td>
							<td colspan="5">
								<input type="text" class="nomal_input" style="width:100%;" placeholder="옵션명" id="i_vendor_item_option" autocomplete="off" />
							</td>
						</tr>
						<tr>
							<th>고객정보</th>
						</tr>
						<tr>
							<td colspan="1">
								<input type="text" class="nomal_input" style="width:100%;" placeholder="주문자" id="i_orderer_name" autocomplete="off" disabled/>
							</td>
							<td colspan="2">
								<input type="text" class="nomal_input" style="width:100%;" placeholder="네이버ID" id="i_orderer_id" autocomplete="off" disabled/>
							</td>
							<td colspan="4">
								<input type="text" class="nomal_input" style="width:100%;" placeholder="주문일자" id="i_order_date" autocomplete="off" disabled />
							</td>
						</tr>
						<tr>
							<th>배송정보</th>
						</tr>
						<tr>
							<td colspan="1">
								<input type="text" class="nomal_input" style="width:100%;" placeholder="수령인" id="i_name" autocomplete="off" />
							</td>
							<td colspan="6">
								<input type="text" class="nomal_input" style="width:100%;" placeholder="주소" id="i_address" autocomplete="off" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<input type="text" class="nomal_input" style="width:100%;" placeholder="연락처" id="i_mobile" autocomplete="off" />
							</td>
							<td colspan="5">
								<input type="text" class="nomal_input" style="width:100%;" placeholder="배송메모" id="i_note" autocomplete="off" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</section>
		<div class="list_box" id="main_table" style="min-height: 200px;">
			<table class="list_tbl">
				<thead>
					<tr>
						<th nowrap>-</th>
						<th nowrap>고객명</th>
						<th nowrap>상품명</th>
						<th nowrap>수량</th>
					</tr>
				</thead>
				<colgroup>
					<col width="15%">
					<col width="20%">
					<col width="55%">
					<col width="10%">
				</colgroup>
				<tbody id="order_list">
					<tr>
						<td colspan="4" style="height:50px;">주문 항목을 추가해주세요</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="list_option_bottom" id="v_btn_table" style="width: 99%; justify-content: center;display: flex;">
			<a class="option_write_bt" onClick="initOrder()" data-toggle="modal" data-target="#v_back_drop" style="width: 300px;"><span>사입/배송 요청</span></a>
	    </div>
		<jsp:include page="/WEB-INF/views/mobile/common/rightMenu.jsp"/>
		<div class="mobile-loading"><div></div></div>
	</body>
</html>