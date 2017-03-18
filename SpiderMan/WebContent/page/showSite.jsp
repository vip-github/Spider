<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript">
$(function(){
	$('#siteDatagrid').datagrid({
	    url:'getParentSiteList.do',
	    fitColumns:true,
	    pageSize:50,
	    pageList:[50,100],
	    loadMsg:"加载中，请稍后...",
	    emptyMsg:'没有可显示的数据！',
	    rownumbers:true,
	    pagination:true,
	    border:false,
	    collapsible:true,
	    singleSelect:false,
	    columns:[[
	        {field:'id',title:'id',width:100,align:'center',checkbox:true},
	        {field:'name',title:'名称',width:100,align:'center'},
	        {field:'type',title:'类型',width:80,align:'center'},
	        {field:'url',title:'链接',width:300,align:'center'},
	        {field:'addtime',title:'添加时间',width:130,align:'center'},
	        {field:'threads',title:'线程数',width:50,align:'center'},
	        {field:'cycle',title:'采集周期',width:80,align:'center'},
	        {field:'comment',title:'备注',width:200,align:'center'}
	    ]],
	    toolbar: ['-',{
			text: '新增',
			iconCls: 'icon-add',
			handler: function(){
				$('#addParentSiteWindow').window('open');
			}
		},'-',{
			text: '修改',
			iconCls: 'icon-edit',
			handler: function(){
			}
		},'-',{
			text: '删除',
			iconCls: 'icon-remove',
			handler: function(){
				var checkedRows = $('#siteDatagrid').datagrid('getChecked');
				if(null!=checkedRows && checkedRows.length>0){
					if(checkedRows.length==1){
						var id = checkedRows[0].id;
						$.messager.confirm({
							title: '确认操作',
							msg: '你确定要删除选中记录？',
							fn: function(r){
								if (r){
									$.ajax({
										url: "deleteParentSite.do",
										dataType: 'text',
										timeout: 60000,
										data: 'id='+id,
										success: function(result){
								        	if(result=='ok'){
								        		$('#siteDatagrid').datagrid('reload');
								        		$.messager.alert('操作结果','删除成功！','info');
								        	}else{
								        		$.messager.alert('操作结果','删除失败！','warning');
								        	}
								    	}
									});
								}
							}
						});
					}else{
						$.messager.alert('温馨提示','您一次只能操作一行！','warning');
					}
				}else{
					$.messager.alert('温馨提示','您没有选择操作的行！','warning');
				}
			}
		},'-',{
	    	text: '查看子站点',
			iconCls: 'icon-search',
			handler: function(){
				var checkedRows = $('#siteDatagrid').datagrid('getChecked');
				if(null!=checkedRows && checkedRows.length>0){
					if(checkedRows.length==1){
						var pid = checkedRows[0].id;
						$.ajax({
							url: "getChildrenSiteCount.do",
							type: "POST",
							dataType: 'text',
							timeout: 60000,
							data: 'pid='+pid,
							success: function(result){
					        	if(result>0){
					        		$('#viewChildrenSiteWindow').window('open');
					        	}else{
					        		$.messager.alert('温馨提示','您选择的父站点未找到相应的子站点！','warning');
					        	}
					    	}
						});
					}else{
						$.messager.alert('温馨提示','您一次只能操作一行！','warning');
					}
				}else{
					$.messager.alert('温馨提示','您没有选择操作的行！','warning');
				}
			}
		},'-',{
			text: '新增子站点',
			iconCls: 'icon-mini-add',
			handler: function(){
				var checkedRows = $('#siteDatagrid').datagrid('getChecked');
				if(null!=checkedRows && checkedRows.length>0){
					if(checkedRows.length==1){
						var pid = checkedRows[0].id;
						var type = checkedRows[0].type;
						var name = checkedRows[0].name;
						var comment = checkedRows[0].comment;
						var cycle = checkedRows[0].cycle;
						$("#cname").val(name);
						$("#ccomment").textbox("setText", comment);
						if(null!=type && ''!=type){
							$("#ctype").combobox('select', type);
						}
						if(null!=cycle && ''!=cycle){
							$("#ccycle").combobox('select', cycle);
						}
						$('#addChildrenSiteWindow').window('open');
						$("#hpid").val("");
						$("#hpid").val(pid);
					}else{
						$.messager.alert('温馨提示','您一次只能操作一行！','warning');
					}
				}else{
					$.messager.alert('温馨提示','您需要选择一行进行操作！','warning');
				}
			}
		}],
		view: detailview,
	    detailFormatter: function(rowIndex, rowData){
	    	var result = "<table><thead><tr><th style='text-align:center'>类型</th><th style='text-align:center'>值</th></tr></thead>"
	    	var selectors = rowData.selectors;
	    	if(null!=selectors && undefined!=selectors){
	    		for(var selector in selectors){
	    			result+="<tr>&nbsp;<td style='text-align:center'>&nbsp;&nbsp;"+selector+"&nbsp;&nbsp;</td><td style='text-align:center'>&nbsp;&nbsp;"+selectors[selector]+"&nbsp;&nbsp;</td>&nbsp;</tr>";
	    		}
	    	}
	    	result+="</table>";
	    	return result;
	    }
	});
	$('#viewChildrenSiteWindow').window({
		onOpen: function(){
			var checkedRows = $('#siteDatagrid').datagrid('getChecked');
			if(null!=checkedRows && checkedRows.length==1){
				var pid = checkedRows[0].id;
				$('#childrenSiteDatagrid').datagrid({
					url: 'getChildrenSiteList.do',
					fitColumns:true,
				    pageSize:30,
				    pageList:[30,60,90],
				    loadMsg:"加载中，请稍后...",
				    emptyMsg:'没有可显示的数据！',
				    rownumbers:true,
				    pagination:true,
				    border:false,
				    queryParams:{
				    	pid: pid
				    },
				    columns:[[
	              	    {field:'id',title:'id',width:100,align:'center',checkbox:true},
	              	  	{field:'pid',title:'父站点id',width:100,align:'center'},
	              	    {field:'pname',title:'父站点名称',width:100,align:'center'},
	           	        {field:'type',title:'类型',width:80,align:'center'},
	           	        {field:'name',title:'名称',width:100,align:'center'},
	           	        {field:'url',title:'链接',width:200,align:'center'},
	           	        {field:'addtime',title:'添加时间',width:160,align:'center'},
	           	     	{field:'runtime',title:'采集时间',width:160,align:'center'},
	           	     	{field:'selectors',title:'选择器',width:130,align:'center',formatter: function(value,row,index){
		     	        	var res = "";
		     	        	if(null!=value && undefined!=value){
		     	        		for(var selector in value){
		     	        			res+=selector+" "
		     	        		}
		     	        	}
		     	        	return res;
	     	        	}},
	     	        	{field:'cycle',title:'采集周期',width:160,align:'center'},
	           	        {field:'comment',title:'备注',width:200,align:'center'}
		           ]],
		           toolbar: [{
		        	   	text: '启动',
			   			iconCls: 'icon-search',
			   			handler: function(){
			   				
			   			}
		           },'-',{
		        	   	text: '停止',
			   			iconCls: 'icon-search',
			   			handler: function(){
			   				
			   			}
		   			},'-',{
		        	   	text: '删除',
			   			iconCls: 'icon-search',
			   			handler: function(){
			   				
			   			}
		   			}]
				});
			}
		}
	});
});
function saveParentSite(){
	$('#parentSiteForm').form('submit', {
	    url:'saveParentSite.do',
	    onSubmit: function(param){
	    	return $(this).form('validate');
	    },
	    success:function(data){
	        if(data=='ok'){
	        	$.messager.alert('提示','保存成功！','info',function(){
	        		$('#parentSiteForm').form('clear');
	        		$('#addParentSiteWindow').window('close');
	        		$('#siteDatagrid').datagrid('reload');
	        	});
	        }
	    }
	});
}

function saveChildrenSite(){
	$('#childrenSiteForm').form('submit', {
	    url:'saveChildrenSite.do',
	    onSubmit: function(param){
	    	var check = $(this).form('validate');
	    	var pid = $("#hpid").val();
	    	if(null==pid || ''==pid || undefined==pid){
	    		check = false;
	    	}
	        return check;
	    },
	    success:function(data){
	        if(data=='ok'){
	        	$.messager.alert('提示','保存成功！','info',function(){
	        		$('#childrenSiteForm').form('clear');
	        		$('#addChildrenSiteWindow').window('close');
	        		$('#siteDatagrid').datagrid('reload');
	        	});
	        }
	    }
	});
}

function clear1(){
	$("#pname").val("");
	$("#purl").val("");
	$("#pSelectorValue1").val("");
	$("#pSelectorValue2").textbox("clear");
	$("#pSelectorValue3").textbox("clear");
	$("#pcomment").textbox("clear");
}

function clear2(){
	$("#cname").val("");
	$("#curl").val("");
	$("#cSelectorValue1").val("");
	$("#cSelectorValue2").textbox("clear");
	$("#cSelectorValue3").textbox("clear");
	$("#ccomment").textbox("clear");
	$("#hpid").val("");
}

function addPage(){
	var tableNumber = $("#pageArea table[id^='pageTable']").length;
	var content = "<fieldset><legend>页面配置信息</legend><table id='pageTable"+tableNumber+"'><tr><td>优先级：<select id='pagePriority' name='pages["+tableNumber+"].priority'><option value='0' selected='selected'>0</option><option value='1'>1</option><option value='2'>2</option><option value='3'>3</option><option value='4'>4</option><option value='5'>5</option></select>&nbsp;&nbsp;&nbsp;&nbsp;类型：<select name='pages["+tableNumber+"].type'><option value='sub' selected='selected'>子页面</option><option value='next'>下一页</option><option value='detail'>详细页</option></select></td></tr><tr><td><table id='selectorTable"+tableNumber+"'><tr><td>选择器：</td><td width='5px;'></td><td><button type='button' onclick='addSelector("+tableNumber+")'>新增</button><button type='button' style='margin-left:5px;' onclick='removeSelector("+tableNumber+")'>移除</button></td></tr><tr><td><select name='pages["+tableNumber+"].selectors[0].key'><option value='xpath' selected='selected'>xpath</option><option value='css'>css</option><option value='regex'>regex</option></select></td><td width='5px;'></td><td><input name='pages["+tableNumber+"].selectors[0].value' style='width:220px;'></td></tr></table></td></tr></table></fieldset>";
	$("#pageArea").append(content);
	$($("#pageArea table[id^='pageTable']")[tableNumber]).find("select#pagePriority option:eq("+tableNumber+")").attr("selected", "selected");
}

function removePage(){
	var length = $("#pageArea fieldset").length;
	if(length>1){
		$("#pageArea fieldset:last").remove();
	}else{
		$.messager.alert('操作未成功','至少需要留一行！','warning');
	}
}

function addSelector(index){
	var length = $("#selectorTable"+index+" tr").length;
	if(length<4){
		var content = "<tr><td><select name='pages["+index+"].selectors["+(length-1)+"].key'><option value='xpath' selected='selected'>xpath</option><option value='css'>css</option><option value='regex'>regex</option></select></td><td width='5px;'></td><td><input name='pages["+index+"].selectors["+(length-1)+"].value' style='width:220px;'></td></tr>";
		$("#selectorTable"+index).append(content);
	}else{
		$.messager.alert('操作未成功','最多只能配置3个选择器！','warning');
	}
}

function removeSelector(index){
	var length = $("#selectorTable"+index+" tr").length;
	if(length>2){
		$("#selectorTable"+index+" tr:last").remove();
	}else{
		$.messager.alert('操作未成功','至少需要留一行！','warning');
	}
}
</script>
</head>
<table id="siteDatagrid"></table>
<div id="viewChildrenSiteWindow" class="easyui-window" title="查看子站点" 
        style="width:1000px;height:590px;padding:10px;background:#fafafa;"
        data-options="iconCls:'icon-search',closable:true,
                collapsible:false,minimizable:false,maximizable:false,closed:true,modal:true">
    <table id="childrenSiteDatagrid"></table>
</div>
<div id="addParentSiteWindow" class="easyui-window" title="新增父站点" 
        style="width:450px;height:550px;padding:10px;background:#fafafa;"
        data-options="iconCls:'icon-add',closable:true,
                collapsible:false,minimizable:false,maximizable:false,closed:true,modal:true">
     <form id="parentSiteForm" method="post">
	     <fieldset>
	     	<legend>父站点信息录入</legend>
	     	<div>
	     		站点名称：
	     		<input name="name" class="easyui-validatebox" data-options="required:true" style="width:275px;">
	     		<br/><br/>
	     		站点链接：
	     		<input name="url" class="easyui-validatebox" data-options="required:true,validType:'url'" style="width:275px;">
	     		<br/><br/>
	     		&nbsp;Cookie：
	     		<input name="cookie" style="width:275px;">
	     		<br/><br/>
	     		字符编码：
	     		<input name="charset" style="width:95px;" value="自动设置">
	     		&nbsp;休眠时间：
	     		<input name="sleep" class="easyui-validatebox" data-options="required:true,validType:'digits'" style="width:95px;" value="500">
	     		<br/><br/>
	     		超时时间：
	     		<input name="timeout" style="width:95px;" data-options="required:true,validType:'digits'" value="500">
	     		&nbsp;重试次数：
	     		<input name="retry" style="width:95px;" data-options="required:true,validType:'digits'" value="3">
	     		<br/><br/>
	     		线程数量：
	     		<input name="threads" style="width:95px;" data-options="required:true,validType:'digits'" value="10">
	     		<br/><br/>
	     		站点类型：
	    		<select class="easyui-combobox" name="type" data-options="panelHeight:'auto',width:120,required:true">
				    <option value="aa" selected="selected">aa</option>
				    <option value="bb">bb</option>
				    <option	value="cc">cc</option>
				</select>
				&nbsp;周期：
				<select name="cycle" class="easyui-combobox" data-options="panelHeight:'auto',width:100,required:true">
				    <option value="10" selected="selected">10分钟</option>
				    <option value="60">1小时</option>
				    <option	value="720">12小时</option>
				    <option	value="1440">1天</option>
				    <option	value="10080">7天</option>
				</select>
				<br/><br/>
				备注：
				<input name="comment" class="easyui-textbox" data-options="width:305,height:60,multiline:true">
	     		<br/><br/>
	     		<div align="center">
	     			<a class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="addPage()">新增页面</a>
	     			&nbsp;
	     			<a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="removePage()">移除页面</a>
	     		</div>
	     		<div id="pageArea">
	     		<fieldset>
	     		<legend>页面配置信息</legend>
	     		<table id="pageTable0">
	     			<tr>
	     				<td>
	     					优先级：
				     		<select id="pagePriority" name="pages[0].priority">
							    <option value="0" selected="selected">0</option>
							    <option value="1">1</option>
							    <option value="2">2</option>
							    <option value="3">3</option>
							    <option value="4">4</option>
							    <option value="5">5</option>
							</select>
				     		&nbsp;&nbsp;&nbsp;&nbsp;类型：
				     		<select name="pages[0].type">
							    <option value="sub" selected="selected">子页面</option>
							    <option value="next">下一页</option>
							    <option	value="detail">详细页</option>
							</select>
	     				</td>
	     			</tr>
	     			<tr>
	     				<td>
							<table id="selectorTable0">
								<tr>
									<td>
										选择器：
									</td>
									<td width="5px;"></td>
									<td>
										<button onclick="addSelector(0)" type="button">新增</button>
										<button onclick="removeSelector(0)" type="button">移除</button>
									</td>
								</tr>
								<tr>
									<td>
										<select name="pages[0].selectors[0].key">
										    <option value="xpath" selected="selected">xpath</option>
										    <option value="css">css</option>
										    <option	value="regex">regex</option>
										</select>
									</td>
									<td width="5px;"></td>
									<td>
										<input name="pages[0].selectors[0].value" style="width:220px;">
									</td>
								</tr>
							</table>
	     				</td>
	     			</tr>
	     		</table>
				</fieldset>
				</div>
	     	</div>
	     </fieldset>
	     <div align="center" style="margin-top: 10px;">
			<a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="saveParentSite()">保存</a>
			&nbsp;&nbsp;
			<a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="clear1()">清空</a>
		 </div>
	 </form>
</div>
<div id="addChildrenSiteWindow" class="easyui-window" title="新增子站点" 
        style="width:350px;height:480px;padding:10px;background:#fafafa;"
        data-options="iconCls:'icon-add',closable:true,
                collapsible:false,minimizable:false,maximizable:false,closed:true,modal:true">
     <form id="childrenSiteForm" method="post">
	     <fieldset>
	     	<legend align="center">子站点信息录入</legend>
	     	<div>
	     		<input id="hpid" name="pid" type="hidden" value="">
	     		站点名称：
	     		<input id="cname" name="cname" class="easyui-validatebox" data-options="required:true" style="width:190px;">
	     		<br/><br/>
	     		站点链接：
	     		<input id="curl" name="curl" class="easyui-validatebox" data-options="required:true" style="width:190px;">
	     		<br/><br/>
	     		站点类型：
	    		<select class="easyui-combobox" id="ctype" name="ctype" data-options="panelHeight:'auto',width:120,required:true">
				    <option value="aa" selected="selected">aa</option>
				    <option value="bb">bb</option>
				    <option	value="cc">cc</option>
				</select>
	     		<br/><br/>
	     		选择器：
	     		<br/>
	     		<select class="easyui-combobox" id="cSelectorKey1" name="cSelectorKey1" data-options="panelHeight:'auto',width:60,required:true">
				    <option value="xpath" selected="selected">xpath</option>
				    <option value="css">css</option>
				    <option	value="regex">regex</option>
				</select>
				<input id="cSelectorValue1" name="cSelectorValue1" class="easyui-validatebox" data-options="required:true" style="width:195px;">
				<br/><br/>
				<select class="easyui-combobox" id="cSelectorKey2" name="cSelectorKey2" data-options="panelHeight:'auto',width:60">
				    <option value="css" selected="selected">css</option>
				    <option value="xpath">xpath</option>
				    <option	value="regex">regex</option>
				</select>
				<input id="cSelectorValue2" name="cSelectorValue2" class="easyui-textbox" style="width:200px;">
				<br/><br/>
				<select id="pSelectorKey3" name="cSelectorKey3" class="easyui-combobox" data-options="panelHeight:'auto',width:60">
				    <option	value="regex" selected="selected">regex</option>
				    <option value="xpath">xpath</option>
				    <option value="css">css</option>
				</select>
				<input id="cSelectorValue3" name="cSelectorValue3" class="easyui-textbox" style="width:200px;">
				<br/><br/>
				周期：
				<select name="ccycle" class="easyui-combobox" data-options="panelHeight:'auto',width:100,required:true">
				    <option value="10" selected="selected">10分钟</option>
				    <option value="60">1小时</option>
				    <option	value="720">12小时</option>
				    <option	value="1440">1天</option>
				    <option	value="10080">7天</option>
				</select>
				<br/><br/>
				备注：
				<input id="ccomment" name="ccomment" class="easyui-textbox" data-options="width:225,height:50,multiline:true">
	     	</div>
	     </fieldset>
	     <div align="center" style="margin-top: 10px;">
			<a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="saveChildrenSite()">保存</a>
			&nbsp;&nbsp;
			<a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="clear2()">清空</a>
		 </div>
	 </form>
</div>