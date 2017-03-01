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
	        {field:'selectors',title:'选择器',width:130,align:'center',formatter: function(value,row,index){
	        	var res = "";
	        	if(null!=value && undefined!=value){
	        		for(var selector in value){
	        			res+=selector+" "
	        		}
	        	}
	        	return res;
	        }},
	        {field:'cycle',title:'采集周期',width:130,align:'center'},
	        {field:'comment',title:'备注',width:200,align:'center'}
	    ]],
	    toolbar: [{
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
		},'-',{
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
			}
		},'-',{
			text: '保存',
			iconCls: 'icon-save',
			handler: function(){
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
	              	    {field:'pid',title:'pid',width:80,align:'center'},
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
	var pname = $("#pname").val();
	var purl = $("#purl").val();
	var ptype = $("#ptype").val();
	var pSelectorKey1 = $("#pSelectorKey1").val();
	var pSelectorValue1 = $("#pSelectorValue1").val();
	var pSelectorKey2 = $("#pSelectorKey2").val();
	var pSelectorValue2 = $("#pSelectorValue2").val();
	var pSelectorKey3 = $("#pSelectorKey3").val();
	var pSelectorValue3 = $("#pSelectorValue3").val();
	
	$('#parentSiteForm').form('submit', {
	    url:'saveParentSite.do',
	    onSubmit: function(param){
	    	var check = $(this).form('validate');
	        var selectors = {};
	        selectors[pSelectorKey1] = pSelectorValue1;
	        if(selectors.hasOwnProperty(pSelectorKey2) && null!=pSelectorKey2){
	        	$.messager.alert('错误','你选择了多个选择器：'+pSelectorKey2+"！",'error');
	        	return false;
	        }else{
	        	selectors[pSelectorKey2] = pSelectorValue2;
	        }
	        if(selectors.hasOwnProperty(pSelectorKey3) && null!=pSelectorKey3){
	        	$.messager.alert('错误','你选择了多个选择器：'+pSelectorKey3+"！",'error');
	        	return false;
	        }else{
	        	selectors[pSelectorKey3] = pSelectorValue3;
	        }
	        return check;
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
	var cname = $("#cname").val();
	var curl = $("#curl").val();
	var ctype = $("#ctype").val();
	var cSelectorKey1 = $("#cSelectorKey1").val();
	var cSelectorValue1 = $("#cSelectorValue1").val();
	var cSelectorKey2 = $("#cSelectorKey2").val();
	var cSelectorValue2 = $("#cSelectorValue2").val();
	var cSelectorKey3 = $("#cSelectorKey3").val();
	var cSelectorValue3 = $("#cSelectorValue3").val();
	
	$('#childrenSiteForm').form('submit', {
	    url:'saveChildrenSite.do',
	    onSubmit: function(param){
	    	var check = $(this).form('validate');
	    	var pid = $("#hpid").val();
	    	if(null==pid || ''==pid || undefined==pid){
	    		return false;
	    	}
	        var selectors = {};
	        selectors[cSelectorKey1] = cSelectorValue1;
	        if(selectors.hasOwnProperty(cSelectorKey2) && null!=cSelectorKey2){
	        	$.messager.alert('错误','你选择了多个选择器：'+cSelectorKey2+"！",'error');
	        	return false;
	        }else{
	        	selectors[cSelectorKey2] = cSelectorValue2;
	        }
	        if(selectors.hasOwnProperty(cSelectorKey3) && null!=cSelectorKey3){
	        	$.messager.alert('错误','你选择了多个选择器：'+cSelectorKey3+"！",'error');
	        	return false;
	        }else{
	        	selectors[cSelectorKey3] = cSelectorValue3;
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
	var tableNumber = $("#pageArea table#pageTable").length;
	var content = "<fieldset><legend>页面配置信息</legend><table id='pageTable'><tr><td>优先级：<select id='pagePriority' name=''><option value='0' selected='selected'>0</option><option value='1'>1</option><option value='2'>2</option><option value='3'>3</option><option value='4'>4</option><option value='5'>5</option></select>&nbsp;&nbsp;&nbsp;&nbsp;类型：<select id='pageType' name=''><option value='subpage' selected='selected'>子页面</option><option value='nextpage'>下一页</option><option	value='detailpage'>详细页</option></select></td></tr><tr><td><table id='selectorTable'><tr><td>选择器：</td><td width='5px;'></td><td><a class='easyui-linkbutton' data-options='iconCls:'icon-add'' onclick='addSelector()'>新增</a><a class='easyui-linkbutton' data-options='iconCls:'icon-cancel'' onclick='removeSelector()'>移除</a></td></tr><tr><td><select name='pSelectorKey1'><option value='xpath' selected='selected'>xpath</option><option value='css'>css</option><option	value='regex'>regex</option></select></td><td width='5px;'></td><td><input id='pSelectorValue1' name='pSelectorValue1' style='width:220px;'></td></tr></table></td></tr></table></fieldset>";
	$("#pageArea").append(content);
	$($("#pageArea table#pageTable")[tableNumber]).find("select#pagePriority option:eq("+tableNumber+")").attr("selected", "selected");
}

function removePage(){
	var length = $("#pageArea fieldset").length;
	if(length>1){
		$("#pageArea fieldset:last").remove();
	}else{
		$.messager.alert('操作未成功','至少需要留一行！','warning');
	}
}

function addSelector(){
	var length = $("#selectorTable tr").length;
	if(length<4){
		var content = "<tr><td><select name='pSelectorKey1'><option value='xpath' selected='selected'>xpath</option><option value='css'>css</option><option	value='regex'>regex</option></select></td><td width='5px;'></td><td><input id='pSelectorValue1' name='pSelectorValue1' style='width:220px;'></td></tr>";
		$("#selectorTable").append(content);
	}else{
		$.messager.alert('操作未成功','最多只能配置3个选择器！','warning');
	}
}

function removeSelector(){
	var length = $("#selectorTable tr").length;
	if(length>2){
		$("#selectorTable tr:last").remove();
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
        style="width:450px;height:720px;padding:10px;background:#fafafa;"
        data-options="iconCls:'icon-add',closable:true,
                collapsible:false,minimizable:false,maximizable:false,closed:true,modal:true">
     <form id="parentSiteForm" method="post">
	     <fieldset>
	     	<legend>父站点信息录入</legend>
	     	<div>
	     		站点名称：
	     		<input id="pname" name="pname" class="easyui-validatebox" data-options="required:true" style="width:190px;">
	     		<br/><br/>
	     		站点链接：
	     		<input id="purl" name="purl" class="easyui-validatebox" data-options="required:true" style="width:190px;">
	     		<br/><br/>
	     		站点类型：
	    		<select class="easyui-combobox" id="ptype" name="ptype" data-options="panelHeight:'auto',width:120,required:true">
				    <option value="aa" selected="selected">aa</option>
				    <option value="bb">bb</option>
				    <option	value="cc">cc</option>
				</select>
				&nbsp;周期：
				<select id="pcycle" name="pcycle" class="easyui-combobox" data-options="panelHeight:'auto',width:100,required:true">
				    <option value="10" selected="selected">10分钟</option>
				    <option value="60">1小时</option>
				    <option	value="720">12小时</option>
				    <option	value="1440">1天</option>
				    <option	value="10080">7天</option>
				</select>
				<br/><br/>
				备注：
				<input id="pcomment" name="pcomment" class="easyui-textbox" data-options="width:225,height:50,multiline:true">
	     		<br/><br/>
	     		<div align="center">
	     			<a class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="addPage()">新增页面</a>
	     			&nbsp;
	     			<a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="removePage()">移除页面</a>
	     		</div>
	     		<div id="pageArea">
	     		<fieldset>
	     		<legend>页面配置信息</legend>
	     		<table id="pageTable">
	     			<tr>
	     				<td>
	     					优先级：
				     		<select id="pagePriority" name="">
							    <option value="0" selected="selected">0</option>
							    <option value="1">1</option>
							    <option value="2">2</option>
							    <option value="3">3</option>
							    <option value="4">4</option>
							    <option value="5">5</option>
							</select>
				     		&nbsp;&nbsp;&nbsp;&nbsp;类型：
				     		<select id="pageType" name="">
							    <option value="subpage" selected="selected">子页面</option>
							    <option value="nextpage">下一页</option>
							    <option	value="detailpage">详细页</option>
							</select>
	     				</td>
	     			</tr>
	     			<tr>
	     				<td>
							<table id="selectorTable">
								<tr>
									<td>
										选择器：
									</td>
									<td width="5px;"></td>
									<td>
										<button onclick="addSelector()" type="button">新增</button>
										<button onclick="removeSelector()" type="button">移除</button>
									</td>
								</tr>
								<tr>
									<td>
										<select name="pSelectorKey1">
										    <option value="xpath" selected="selected">xpath</option>
										    <option value="css">css</option>
										    <option	value="regex">regex</option>
										</select>
									</td>
									<td width="5px;"></td>
									<td>
										<input id="pSelectorValue1" name="pSelectorValue1" style="width:220px;">
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
				<select id="ccycle" name="ccycle" class="easyui-combobox" data-options="panelHeight:'auto',width:100,required:true">
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