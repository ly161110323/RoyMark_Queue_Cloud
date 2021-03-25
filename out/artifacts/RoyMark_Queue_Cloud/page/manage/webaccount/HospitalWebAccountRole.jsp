<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/page/common/include.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<meta http-equiv="Cache-Control" content="no-siteapp" />
<title>禾麦智能大厅管理系统</title>
<link rel="shortcut icon" href="${ctx}/resources/images/favicon.ico"
	type="image/x-icon" />

<script type="text/javascript">
	var webaccountLs = "";
	var webaccountName = "";
	var allCheckedFlag = "";

	function editRolePermission() {
		//-------------------
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");//获取树对象
		var nodes = zTree.getCheckedNodes(true);//得到被选择的树节点
		var lgh = nodes.length;
		var RoleLss = "";//拼接字符串
		webaccountLs = getUrlParams("webAccountLs");
		webaccountName = getUrlParams("webAccountName");
		for (var i = 0; i < lgh; i++) {
			RoleLss += nodes[i].id+",";
		}

		$
				.ajax({
					type : 'POST',
					url : '${ctx}/HospitalqueueWebaccountpermissions/updatewebaccountpermission',
					data : {
						"webaccountLs" : webaccountLs,
						"roleLss" : RoleLss
					},
					cache : false,
					dataType : 'json',
					success : function(data) {
						if (data.result == "ok") {
							window.parent.closeFramDialog("项目权限设置成功！");
						}
						if (data.result == "no") {
							window.parent.closeFramDialog("项目权限设置失败！");
						}
						if (data.result == "error") {
							window.parent.closeFramDialog("服务器错误！");
						}
					},
					error : function(data) {
						layer.alert('url error!');
					}
				});
	}

	$().ready(function() {
		if (allCheckedFlag == "1") {
			$("#checkAlls").prop("checked", true);
		} else {
			$("#checkAlls").prop("checked", false);
		}
	});

	/**
	 * 全选控制
	 */
	$(function() {
		$("#checkAlls").bind('click', function() {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");//获取树对象
			if ($(this).prop("checked") == true) {
				zTree.checkAllNodes(true);
			} else {
				zTree.checkAllNodes(false);
			}
		});

	});

	//全选或者去掉全选
	function operatAll(flag) {
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");//获取树对象
		zTree.checkAllNodes(flag);
	}

	var setting = {
		view : {
			showLine : false
		},
		check : {
			enable : true
		},
		data : {
			simpleData : {
				enable : true
			}
		},
		callback : {
			onCheck : onCheck
		}
	};

	var zNodes = new Array();//定义ztree数据为简单数据模式

	//加载树，		
	$(document).ready(function() {
		webaccountLs = getUrlParams("webAccountLs");
		webaccountName = getUrlParams("webAccountName");
		jQuery.browser = {};
		(function() {
			jQuery.browser.msie = false;
			jQuery.browser.version = 0;
			if (navigator.userAgent.match(/MSIE ([0-9]+)./)) {
				jQuery.browser.msie = true;
				jQuery.browser.version = RegExp.$1;
			}
		})();
		getTree();
	});

	/* 获取树形结构数据 */
	function getTree() {
		$.ajax({
			url : "${ctx}/HospitalqueueWebaccountpermissions/webaccountpermissionset",
			dataType : "json",
			type : "post",
			data : {
				"webaccountLs" : webaccountLs
			},
			success : function(data) {
				var dataList = data.list;
				roleLs = data.roleLs;
				allCheckedFlag = data.allCheckedFlag;
				$.each(dataList, function(index, eachData) {
					/* var node = {};
					node["id"] = eachData.id;
					node["pid"] = eachData.pId;
					node["name"] = eachData.name; */
					zNodes.push(eachData);
				});
				$.fn.zTree.init($("#treeDemo"), setting, zNodes);
				//展示树节点
				$.fn.zTree.getZTreeObj("treeDemo").expandAll(true);
			}
		});
	}

	function onCheck(e, treeId, treeNode) {
		/* var zTree = $.fn.zTree.getZTreeObj("treeDemo");//获取树对象
		var checkNodes = zTree.getCheckedNodes(true);//得到被选择的树节点
		var checkLen=checkNodes.length;
		var node = zTree.getNodes();//根节点的数量
		var pLen = node.length;
		var nodes = zTree.transformToArray(node);
		var childrenLen= nodes.length;
		if(checkLen<childrenLen){
			$("#checkAlls").prop("checked",false);
		}else{
			$("#checkAlls").prop("checked",true);
		} */
	}

	//获取url传递的参数值
	function getUrlParams(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r != null)
			return unescape(r[2]);
		return null;
	}
</script>

</head>

<body id="Body" class="gray-bg">
	<div id="content">
		<div class="div-inherit">
			<div style="overflow-y: auto; height: 370px; border: 1px solid #ddd;">
				<ul id="treeDemo" class="ztree"></ul>
			</div>
		</div>
	</div>
</body>


</html>


