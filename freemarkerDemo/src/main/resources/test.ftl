<html>
<head>
	<title>freemarker入门小demo</title>
	<meta charset="utf-8">
</head>

<body>
	<#-- 我是一个注释，不会输出 -->
	<!-- html注释，会输出 -->
	
	<#-- 加载别的模板 -->
	<#include "head.ftl">
	
	${name}，你好，${message}<br>
	
	<#-- #assign是定义一个变量，与在.java中的map中put一个键值对一致 -->
	<#assign linkman="张先生">
	联系人：${linkman}<br>
	
	<#if success=true>
		你已经通过了实名认证
	<#else>
		你未通过实名认证<br><br>
	</#if>
	
	-----------商品列表-----------<br>
	<#list goodsList as goods>
		${goods_index+1} 商品名称：${goods.name} 商品价格：${goods.price}<br>
	</#list>
	
	一共${goodsList?size}条记录<br><br>
	
	<#assign text="{'bank':'工商银行', 'account':'123456789'}">
	<#assign data=text?eval>
	开户行：${data.bank} 账号：${data.account} <br><br>
	
	当前日期：${today?date}<br>
	当前时间：${today?time}<br>
	当前日期加时间：${today?datetime}<br>
	日期格式化：${today?string('yyyy年MM月dd日')}<br><br>
	
	
	当前积分：${point?c}<br><br>
	
	<#if aaa??>
		aaa变量存在：${aaa}<br><br>
	</#if>
		aaa变量不存在<br><br>
	
	${bbb!'不存在'}<br><br>
	
	<#if point gt 100}
		黄金会员
	</#if>
</body>
</html>