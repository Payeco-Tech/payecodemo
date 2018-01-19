<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择支付方式</title>
<style type="text/css">
span{
    color:red;
}
</style>

</head>
<body>
<center>
	<form action="/payecodemo/servlet/PayecoServletNew" method="post" target="_blank">
	<table style="margin-top:100px;">
	<tr><td align="right">currency</td><td align="left"> CNY-人民币</td></tr>
	<tr><td align="right">merchantNo</td><td align="left"><input type="text" name="merchantNo" id="merchantNo" value=""/><span>*</span></td></tr>
	<tr><td align="right">password</td><td align="left"><input type="text" name="password" id="password" value="123456"/><span>*</span></td></tr>
	<tr><td align="right">url</td><td align="left"><input type="text" name="url" id="url" 
	value="http://test.payeco.com:9080/pay/services/ApiV2ServerRSA"/><span>*</span></td></tr>
	<!-- value="https://10.123.65.35:8443/services/ApiV2ServerRSA"/><span>*</span></td></tr> -->
	<tr><td align="right">amount</td><td align="left"><input type="text" name="money" id="money" value="0.3"/><span>*</span></td></tr>
	<tr><td align="right">currency</td><td align="left">
		<select class="form-control" id="spicalSelect" name="currency" style="width:90%;">
        	<option selected>CNY</option>
            <option>HKD</option>
            <option>MOP</option>
            <option>TWD</option>
            <option>MYR</option>
            <option>KPW</option>
            <option>THB</option>
            <option>USD</option>
            <option>JPY</option>
            <option>KRW</option>
            <option>SGD</option>
            <option>EUR</option>
            <option>GBP</option>
        </select>
		<span>*</span>
	</td></tr>
	<tr><td align="right">orderform</td><td align="left">
		<select class="form-control" id="orderfrom" name="orderfrom" style="width:90%;">
        	<option selected  value="28">PC收银台</option>
            <option value="29">H5收银台</option>
            <option value="51">白条支付</option>
        </select>
		<span>*</span>
	</td></tr>
	<tr><td align="right">order descript</td><td align="left"><input type="text" name="desc" id="desc" value="Test Decription"/><span>*</span></td></tr>
    <tr><td align="right">order remark</td><td align="left"><input type="text" name="remark" id="remark" value=""/></td></tr>
	<tr><td colspan="2" align="left" style="padding-left:120px;">
		<input type="submit" value="Confirm" style="width:120px;height:35px;border-radius:6px;color: #fff;background-color:#F4A100;border-color: #F4A100;font-size: 18px;line-height: 1.33;">
	</td></tr>
	</table>
	</form>
</center>
</body>
</html>