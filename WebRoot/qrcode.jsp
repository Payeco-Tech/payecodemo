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
<center>
<h2>三码合一（微信/支付宝）</h2>
<div id="qrcode"></div>
</center>
<script type="text/javascript">
/* jQuery('#qrcode').qrcode("http://10.123.74.102:8082/qr?tk=vPXlymKhTqaNHyY7PlM7aA");	 */
var url = jQuery("#qrcodeurl").val();
jQuery('#qrcode').qrcode(url);			
</script>
</body>
</html>