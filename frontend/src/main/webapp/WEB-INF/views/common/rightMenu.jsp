<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script type="text/javascript">
	var tDoc = this.document;
	
	
	$(document).ready(docReady);
	
	function docReady() {
		$(".nav_bt").click(navBtClick);
		$(".close_nav").click(closeNavClick );
	}
	
	function closeNavClick() {
		$(".modal_box").animate({
			opacity:'0',
			right:'-1000px'
		});
		$(".modal_box").attr('style', "display:none;");
		$(".right_wrap_mobile").animate({
			right:'-1000px',
			opacity:'0'
		});
	}
	
	function navBtClick() {
		$(".right_wrap_mobile").animate({
			right:'0px',
			opacity:'1'
		});
		$(".modal_box").animate({
			opacity:'0.7',
			right:'0'
		});
		$(".modal_box").attr('style', "display:block;"); 
	}
</script>
<html>
	<body>
		<div class="modal_box"></div>
		<div class="right_wrap_mobile">
			<div class="right_nav_wrap">
				<h1>For Yuum</h1>
				<div class="right_nav_box">
					<ul>
						<li><a href="dashboard.do">사입/배송</a></li>
						<li><a href="logout.do">로그아웃</a></li>
					</ul>
				</div>
				<a href="#" class="close_nav">메뉴닫기</a>
			</div>
		</div>
	</body>
</html>
