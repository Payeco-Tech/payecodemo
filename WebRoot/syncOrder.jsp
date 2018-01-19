<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>同步订单信息</title>
<style type="text/css">
span{
    color:red;
}
</style>

</head>
<body>
<center>
	<form action="/payecodemo/servlet/UnionQrcodeOrderSyncServlet" method="post" ><!-- target="_blank" -->
	<p><label>输入同步订单信息:</label></p>
	<p><select name="url">
  		<option value ="http://10.123.107.236:8081/pay/services/ApiV2ServerRSA">dianey测试：http://10.123.107.236:8081/pay/services/ApiV2ServerRSA</option>
  		<option value ="https://shdr.payeco.com/services/ApiV2ServerRSA">disney生产：https://shdr.payeco.com/services/ApiV2ServerRSA</option>
	</select></p>
	<textarea name="srcXml" id="srcXml" cols="120" rows="25"></textarea>
	<p><input type="submit" value="提交" style="width:120px;height:35px;border-radius:6px;color: #fff;background-color:#F4A100;border-color: #F4A100;font-size: 18px;line-height: 1.33;"></p>
	</form>
</center>
</body>
</html>