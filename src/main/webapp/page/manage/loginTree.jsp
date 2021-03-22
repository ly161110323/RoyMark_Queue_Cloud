<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<link rel="stylesheet"
		  href="${ctx}/resources/css/zTreeStyleNew/css/zTreeStyle.css">

<%--ztree树相关引用开始--%>
<script src="${ctx}/resources/newHCSSRemoteQueue/js/jquery.min.js"></script>
<script
	src="${ctx}/resources/newHCSSRemoteQueue/js/ztree/jquery.ztree.core-3.5.min.js"></script>
<script
	src="${ctx}/resources/newHCSSRemoteQueue/js/ztree/jquery.ztree.excheck-3.5.min.js"></script>
	<script src="${ctx}/resources/newHCSSRemoteQueue/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="./js/login.js"></script>
<script src="${ctx}/page/common/js/util.js"></script>
<%--ztree树相关引用结束--%>
</head>
<body>
	<div id="content">
		<div class="div-inherit">
			<div style="overflow-y: auto; height: 500px; border: 1px solid #ddd;">
				<ul id="indextree" class="ztree"></ul>
			</div>
		</div>
	</div>
</body>
</html>