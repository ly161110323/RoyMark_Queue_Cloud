<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%--<%@ include file="/page/common/includewithnewztreestyle.jsp" %>--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>树展现列子</title>
    <%--ztree树相关引用开始--%>
    <link rel="stylesheet" href="./indextreecss/demo.css" type="text/css">
    <link rel="stylesheet" href="./indextreecss/zTreeStyle.css">
    <script type="text/javascript" src="./indexjs/jquery-1.4.4.min.js"></script>
    <script type="text/javascript" src="./indexjs/jquery.ztree.core.js"></script>

    <script type="text/javascript" src="./indexjs/tree2.js"></script>
    <script src="${ctx}/page/common/js/util.js"></script>
    <%--ztree树相关引用结束--%>
    <style type="text/css">
        .ztree * {font-size: 10pt;font-family:"Microsoft Yahei",Verdana,Simsun,"Segoe UI Web Light","Segoe UI Light","Segoe UI Web Regular","Segoe UI","Segoe UI Symbol","Helvetica Neue",Arial}
        .ztree li ul{ margin:0; padding:0}
        .ztree li {line-height:30px;}
        .ztree li a {width:200px;height:30px;padding-top: 0px;}
        .ztree li a:hover {text-decoration:none; background-color: #E7E7E7;}
        .ztree li a span.button.switch {visibility:hidden}
        .ztree.showIcon li a span.button.switch {visibility:visible}
        .ztree li a.curSelectedNode {background-color:#D4D4D4;border:0;height:30px;}
        .ztree li span {line-height:30px;}
        .ztree li span.button {margin-top: -7px;}
        .ztree li span.button.switch {width: 16px;height: 16px;}

        .ztree li a.level0 span {font-size: 150%;font-weight: bold;}
        .ztree li span.button {background-image:url("./indextreecss/left_menuForOutLook.png"); *background-image:url("./indextreecss/left_menuForOutLook.gif")}
        .ztree li span.button.switch.level0 {width: 20px; height:20px}
        .ztree li span.button.switch.level1 {width: 20px; height:20px}
        .ztree li span.button.noline_open {background-position: 0 0;}
        .ztree li span.button.noline_close {background-position: -18px 0;}
        .ztree li span.button.noline_open.level0 {background-position: 0 -18px;}
        .ztree li span.button.noline_close.level0 {background-position: -18px -18px;}
    </style>
</head>
<body>
<h1>OutLook 样式的左侧菜单</h1>
<h6>[ 文件路径: super/left_menuForOutLook.html ]</h6>
<div class="content_wrap">
<div class="zTreeDemoBackground left">
    <ul id="indextree" class="ztree" ></ul>
</div>
    <div class="right">
        <ul class="info">
            <li class="title"><h2>实现方法说明</h2>
                <ul class="list">
                    <li>帮朋友用 zTree 实现了一个貌似 Outlook.com 的菜单，特拿出来分享给大家</li>
                    <li class="highlight_red">1、请注意本页面源码中的 css 部分</li>
                    <li class="highlight_red">2、请查看源码中 js 的使用，特别是利用 addDiyDom 回调将 展开按钮 转移到 &lt;a&gt; 标签内</li>
                    <li class="highlight_red">3、利用添加 zTree 的 class 实现展开按钮的隐藏、显示</li>
                    <li>4、其他辅助规则，请根据实际情况自行编写</li>
                    <li>5、当前规则说明:<br/>
                        &nbsp;&nbsp;单击根节点可以展开、折叠;<br/>
                        &nbsp;&nbsp;非根节点只有点击 箭头 图标才可以展开、折叠;<br/>
                    </li>
                </ul>
            </li>
        </ul>
    </div>
</div>
</body>
</html>
