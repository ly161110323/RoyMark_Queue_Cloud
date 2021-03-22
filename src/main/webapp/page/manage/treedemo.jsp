<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/page/common/includewithnewztreestyle.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>树展现列子</title>
    <%--ztree树相关引用开始--%>
    <%--<link rel="stylesheet" href="${ctx}/resources/css/zTreeStyleNew/css/zTreeStyle.css">--%>
    <script src="${ctx}/resources/newHCSSRemoteQueue/js/ztree/jquery.ztree.core-3.5.min.js"></script>
    <script src="${ctx}/resources/newHCSSRemoteQueue/js/ztree/jquery.ztree.excheck-3.5.min.js"></script>
    <script src="./indexjs/jquery-migrate-1.2.1.js"></script>
    <%--<script type="text/javascript" src="./indexjs/showtree.js"></script>--%>
    <script type="text/javascript" src="./indexjs/indextree.js"></script>
    <script src="${ctx}/page/common/js/util.js"></script>
    <%--ztree树相关引用结束--%>

</head>
<body>
<div>
    <ul id="indextree" class="ztree"></ul>
</div>
</body>
</html>
