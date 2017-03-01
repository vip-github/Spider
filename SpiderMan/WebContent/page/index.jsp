<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>爬虫管理后台-首页</title>
<jsp:include page="easyui.jsp"></jsp:include>
<script type="text/javascript">
	$(function(){
		$('#left_tree').tree({
			onClick: function(node){
				if(undefined!=node.attributes){
					var url = node.attributes.url;
					if(undefined!=url){
						if(!$('#tt').tabs('exists', node.text)){
							$('#tt').tabs('add',{
							    title: node.text,
							    href: url,
							    closable:true
							});
						}else{
							$('#tt').tabs('select', node.text);
						}
					}else{
						$.messager.alert('温馨提示','tab的url属性为空！','warning');
					}
				}
			}
		});
	});
</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false" style="height: 50px">
			<center>
				<!-- <font size="6">爬虫管理后台</font> -->
			</center>
		</div>
		<div data-options="region:'south',split:false,border:false" style="height: 50px;"></div>
		<div data-options="region:'east',split:false,collapsed:true" title="操作记录" style="width: 150px;"></div>
		<div data-options="region:'west',split:false" title="菜单栏" style="width: 180px;">
			<ul id="left_tree" class="easyui-tree" url="data/tree.json"></ul>
		</div>
		<div data-options="region:'center',title:''">
			<div id="tt" class="easyui-tabs" data-options="closable:true,fit:true,border:false"></div>
		</div>
	</div>
</body>
</html>