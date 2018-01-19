<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>三码合一支付</title>
<script type="text/javascript" src="/payecodemo/js/jquery.min.js"></script>
<script type="text/javascript" src="/payecodemo/js/jquery.qrcode.min.js"></script>
<style type="text/css">
span{
    color:red;
}
</style>

</head>
<body>
<input name="qrcodeurl" id="qrcodeurl" style="display:none" value="${requestScope.QrCodeUrl}"></input>
<input name="orderno" id="orderno" style="display:none" value="${requestScope.OrderNo}"></input>
<input name="merchantno" id="merchantno" style="display:none" value="${requestScope.MerchantNo}"></input>
<input name="remark" id="remark" style="display:none" value="${requestScope.Remark}"></input>
<center>
<h2>三码合一（微信/支付宝）</h2>
<div id="qrcode"></div>
</center>
<center>
<p>非三码合一下单不能刷新二维码</p>
<p>系统商户号：${requestScope.MerchantNo}</p>
<p>系统订单号：${requestScope.OrderNo}</p>
<p>${requestScope.Remark}</p>
<button id="testAjax" type="button">刷新二维码</button>
</center>
<script type="text/javascript">
/* jQuery('#qrcode').qrcode("http://10.123.74.102:8082/qr?tk=vPXlymKhTqaNHyY7PlM7aA");	 */
var url = jQuery("#qrcodeurl").val();
if(url!=null||url.length!=0) {
	jQuery('#qrcode').qrcode(url);
}
/* qrcodeRefreshTask(); */
$(function(){
        //按钮单击时执行
        $("#testAjax").click(function(){
        	alert("刷新订单："+jQuery("#orderno").val()+",商户号为："+jQuery("#merchantno").val());
              refresh();
         });
    });
function qrcodeRefreshTask() {
	window.setInterval("refresh()",10*1000);
}

function refresh() {
	$.ajax({
		type: "POST",
		url: "QrcodeRefreshServlet",	/* 前面不能加'/'和加servlet */
		data: "OrderNo="+jQuery("#orderno").val()+"&MerchantNo="+jQuery("#merchantno").val(),
		success:function(data){ 
			/* alert("data.url: "+data.url); */ 
			jQuery('#qrcode').empty();
    		jQuery('#qrcode').qrcode(data);
   		},
   		error: function(){
			alert("请求出错");
        }
	});
}
			
</script>
</body>
</html>








