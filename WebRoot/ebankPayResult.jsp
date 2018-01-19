<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.payeco.util.Toolkit" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择支付方式</title>
<%
String bankcode = request.getParameter("bankcode");
String orderNo = request.getParameter("orderNo");
String realOrderNo ="";
if(orderNo!=null && orderNo.contains("|")) {
	realOrderNo = orderNo.split("\\|");
}
String request_text = "";
if(null != request.getParameter("request_text")) {
	request_text = new String(request.getParameter("request_text").getBytes("ISO-8859-1"),"UTF-8");
}
String amount = Toolkit.getValue(request_text, "Amount");
String description = Toolkit.getValue(request_text, "Description");
String merchantNo = Toolkit.getValue(request_text, "MerchantNo");
String merchantOrderNo = Toolkit.getValue(request_text, "MerchantOrderNo");
%>

<style type="text/css">
span{
    color:red;
}
</style>
</head>
<body>
<center>
	
	<h2>网银支付结果</h2>
	
	<p><tr><td align="right">金额：</td><td align="left"><%=orderNo %></td></tr></p>
	<p><tr><td align="right">订单号：</td><td align="left"><%=merchantOrderNo %></td></tr></p>
	<p><tr><td align="right">订单信息：</td><td align="left"><%=description %></td></tr></p>
	<p><tr><td align="right">支付结果：</td><td align="left">支付成功</td></tr></p>
	<form action="/payecodemo/servlet/PayecoServlet" method="post" target="_blank">
	<table style="margin-top:50px;">
	<tr><td colspan="2" align="left" style="padding-left:0px;">
		<input type="hidden" name="orderNo" id="orderNo" value="<%=realOrderNo %>>"/>
		<input type="submit" value="返回商户" style="width:120px;height:35px;border-radius:6px;color: #fff;background-color:#F4A100;border-color: #F4A100;font-size: 18px;line-height: 1.33;">
	</td></tr>
	</table>
	</form>
</center>
</body>
</html>