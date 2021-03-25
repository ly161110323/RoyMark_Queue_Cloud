<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="renderer" content="webkit">
	<meta http-equiv="Cache-Control" content="no-siteapp"/>
	<title>禾麦智能大厅管理系统</title>
	<!--[if lt IE 9]>
	<meta http-equiv="refresh" content="0;ie.html"/><![endif]-->
	<link rel="shortcut icon" href="${ctx }/resources/images/favicon.ico" type="image/x-icon"/>
	<link rel="stylesheet" href="${ctx }/resources/newHCSSRemoteQueue/css/bootstrap.min.css" >
	<link rel="stylesheet" href="${ctx }/resources/newHCSSRemoteQueue/css/font-awesome.css">
	<link rel="stylesheet" href="${ctx }/resources/newHCSSRemoteQueue/css/style.min-v=4.1.0.css" >
	<script src="${pageContext.request.contextPath }/resources/newHCSSRemoteQueue/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath }/resources/newHCSSRemoteQueue/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath }/resources/newHCSSRemoteQueue/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="${pageContext.request.contextPath }/resources/newHCSSRemoteQueue/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<script src="${pageContext.request.contextPath }/resources/newHCSSRemoteQueue/js/plugins/layer/layer.min.js"></script>
	<script src="${pageContext.request.contextPath }/resources/newHCSSRemoteQueue/js/hplus.min-v=4.1.0.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/resources/newHCSSRemoteQueue/js/contabs.min.js"></script>
	<script src="${pageContext.request.contextPath }/resources/newHCSSRemoteQueue/js/plugins/pace/pace.min.js"></script>
<%--ztree树相关引用开始--%>
	<link rel="stylesheet" href="${ctx}/resources/css/zTreeStyleIndex/css/zTreeStyle.css" >
	<script src="${ctx}/resources/newHCSSRemoteQueue/js/ztree/jquery.ztree.core-3.5.min.js"></script>
	<script src="${ctx}/resources/newHCSSRemoteQueue/js/ztree/jquery.ztree.excheck-3.5.min.js"></script>
	<script src="./indexjs/jquery-migrate-1.2.1.js"></script>

	<script type="text/javascript" src="./indexjs/indextree.js"></script>
	<script src="${ctx}/page/common/js/util.js"></script>
	<%--ztree树相关引用结束--%>
</head>
<style>
	.x-slide_left {
		width: 17px;
		height: 61px;
		background: url(${ctx }/resources/images/icon.png) 0 0 no-repeat;
		position: absolute;
		top: 200px;
		left: 0px;
		cursor: pointer;
	}
	#areaName
	{
		display: inline-block;
		background:#2ecd71;
		color:#fff;
		padding:5px 13px;
		font-weight:bold;
		-webkit-border-radius: 67px;
	}
	.layadmin-flexible{
		list-style: none;
	}
</style>
<body class="fixed-sidebar full-height-layout gray-bg  pace-done fixed-nav" style="overflow:hidden" onload="gettime()">
<div id="wrapper">
	<!--左侧导航开始-->
	<nav class="navbar-default navbar-static-side" role="navigation">
		<div class="nav-close"><i class="fa fa-times-circle"></i>
		</div>
		<div class="sidebar-collapse">
			<ul class="nav" id="side-menu">
				<%--树状菜单开始--%>
					<li title="请选择办事大厅">
						<a href="#">
						<i class="layui-icon layui-icon-1">
							<img style="margin-top: 3px;" src="${ctx}/resources/images/m.png">
						</i>
						<span class="nav-label">请选择办事大厅</span>
						<span class="fa arrow"></span>
						</a>
					<ul class="nav nav-second-level">
						<div style="padding-left: 32px;">
							<ul id="indextree" class="ztree"></ul>
						</div>
					</ul>
					</li>
				<%--树状菜单结束--%>
				<c:forEach items="${sessionScope.menuList}" var="item">
					<c:choose>
						<c:when test="${item.menuLevel==1}">
							<li title="${item.menuName}">
								<a href="#">
									<c:if test="${empty item.menuImagepath}">
										<i class="layui-icon layui-icon-${item.menuLs}">
											<img style="margin-top: 3px;" src="${ctx}/resources/images/m.png">
										</i>
										<%--<i class="fa fa-desktop"></i>--%>
									</c:if>
									<c:if test="${not empty item.menuImagepath}">
										<i class="layui-icon layui-icon-${item.menuLs}">
											<img style="margin-top: 3px;" src="${pageContext.request.contextPath }${item.menuImagepath}">
										</i>
									</c:if>
									<span class="nav-label">${item.menuName}</span>
									<span class="fa arrow"></span>
								</a>
								<ul class="nav nav-second-level">
									<c:forEach items="${sessionScope.menuList}" var="childitem">
										<c:choose>
											<c:when test="${childitem.menuUp==item.menuLs}">
												<c:choose>
													<c:when test="${childitem.menuLevel==2}">
														<li>
															<%
																int countSize = 0;
															%>
															<c:choose>
																<c:when test="${childitem.isHaveChildren==1}">
																	<a href="#">${childitem.menuName}<span class="fa arrow"></span></a>
																</c:when>
																<c:when test="${childitem.isHaveChildren==0}">
																	<a href="${pageContext.request.contextPath }${childitem.menuUrl}" class="J_menuItem">${childitem.menuName}</a>
																</c:when>
															</c:choose>
															<c:forEach items="${sessionScope.menuList}" var="childitem2">
															<c:choose>
															<c:when test="${childitem2.menuUp==childitem.menuLs}">
															<c:choose>
															<c:when test="${childitem2.menuLevel==3}">
															<%
																countSize = countSize + 1;
															%>
															<%
																if(countSize==1){
															%>
															<ul class="nav nav-third-level">
																<%}%>
																<li>
																	<a class="J_menuItem" href="${pageContext.request.contextPath }${childitem2.menuUrl}">${childitem2.menuName}</a>
																</li>
																</c:when>
																</c:choose>
																</c:when>
																</c:choose>
																</c:forEach>
																<%
																	if(countSize>0){
																%>
															</ul>
															<%}%>
														</li>
													</c:when>
												</c:choose>
											</c:when>
										</c:choose>
									</c:forEach>
								</ul>
							</li>
						</c:when>
					</c:choose>
				</c:forEach>
			</ul>
		</div>
	</nav>
	<!--左侧导航结束-->
	<!--右侧部分开始-->
	<div id="page-wrapper" class="gray-bg dashbard-1">
		<div class="row border-bottom">
			<nav class="navbar navbar-fixed-top" role="navigation" style="margin-bottom: 0">
				<div class="navbar-header">
					<div class="" style="margin-top: 14px;margin-left: 20px;font-size: 21px;color: #2f4b76;">
						禾麦智能大厅管理系统
					</div>
				</div>
				<ul class="nav navbar-top-links navbar-right">
					<li class="hidden-xs" style="font-size: 12px;color: #666666;">
					<c:choose>
						<c:when test="${ sessionScope.DEFAULT_PROJECT!=null }">
							<span id="areaName">当前办事大厅:&nbsp;&nbsp;${DEFAULT_PROJECT.areaName}</span>
						</c:when>
					<c:otherwise>
						<span id="areaName">请先维护办事大厅数据</span>
					</c:otherwise>
					</c:choose>
						<span class="J_menuItem">${LOGIN_USER.username}</span>
						<span class="J_menuItem" id="time_area"></span>
						<span class="J_menuItem">
                    		<a id="LogoutLink"  onClick="return logout();" style="font-size: 12px;color: #666666;">退出</a>
                    	</span>
					</li>
					<li class="dropdown hidden-xs" style="width: 0px;">
						<a  aria-expanded="false">
							<i class="fa fa-tasks"></i>
						</a>
					</li>
				</ul>
			</nav>
		</div>
		<div class="row content-tabs">
			<button class="roll-nav roll-left J_tabLeft"><i class="fa fa-backward"></i>
			</button>
			<nav class="page-tabs J_menuTabs rm_shrink">
				<li class="layui-nav-item layadmin-flexible " lay-unselect>
					<a href="javascript: flex();" layadmin-event="flexible" title="侧边伸缩" class="navbar-minimalize rm_shrink">
						<i class="layui-icon layui-icon-shrink-right x-slide_left" id="LAY_app_flexible"></i>
					</a>
				</li>
				<div class="page-tabs-content">
					<%--<a href="javascript:;" class="active J_menuTab" data-id="${ctx }/page/manage/terminal/HospitalQueue_Terminal.jsp">终端管理</a>--%>
				</div>
			</nav>
			<button class="roll-nav roll-right J_tabRight" style="right: 80px;"><i class="fa fa-forward"></i>
			</button>
			<div class="btn-group roll-nav roll-right" style="right: 0px;">
				<button class="dropdown J_tabClose" data-toggle="dropdown">关闭操作<span class="caret"></span>

				</button>
				<ul role="menu" class="dropdown-menu dropdown-menu-right">
					<li class="J_tabShowActive">
						<a>定位当前选项卡</a>
					</li>
					<li class="divider"></li>
					<li class="J_tabCloseAll">
						<a>关闭全部选项卡</a>
					</li>
					<li class="J_tabCloseOther">
						<a>关闭其他选项卡</a>
					</li>
				</ul>
			</div>
		</div>
		<div class="row J_mainContent" id="content-main">
			<%--无内容--%>
			<iframe class="J_iframe" name="iframe0" width="100%" height="100%"
					src="${ctx }/page/manage/webaccount/Queue_WebAccount.jsp" frameborder="0"
					data-id="${ctx }/page/manage/webaccount/Queue_WebAccount.jsp" seamless></iframe>
		</div>
		<!-- <div class="footer">
			<div class="pull-right">
				<a href="http://www.roymark.com.cn/" target="_blank">Copyright © 2019 RoyMark All Rights Reserved.</a>
			</div>
		</div> -->
	</div>
	<!--右侧部分结束-->
</div>

<script>
    var defaultArea;
    function logout() {
        layer.confirm('确定退出吗？', {
            btn: ['确定','取消']
        }, function(){
            var logoutUrl="${ctx}/webaccount/logout";
            $.ajax({
                type:"GET",
                url:logoutUrl,
                //返回数据的格式
                datatype: "json",
                success:function(data){
                    if(data=='success')
                    {
                        var loginUrl="${ctx}/page/manage/login.jsp";

                        parent.location.href=loginUrl;
                    }
                    else {
                        console.log("退出错误");
                    }
                }   ,
                complete: function(XMLHttpRequest, textStatus){

                },

                error: function(){
                    console.log("error");
                }
            });

        }, function(){

        });
        return false;
    }

    function format(n){
        var m = new String();
        var tmp = new String(n);
        if (tmp.length < 2) {
            m = "0" + n;
        }else {
            m = n;
        }
        return m;
    }

    function gettime(){
        var Week = ['日', '一', '二', '三', '四', '五', '六'];
        var now = new Date();
        var year = now.getFullYear();
        var month = format(now.getMonth() + 1);
        var day = format(now.getDate());
        var hours = format(now.getHours());
        var minutes = format(now.getMinutes());
        var seconds = format(now.getSeconds());
        var weeks = "星期" + Week[now.getDay()];
        var L = "" + year + "年" + month + "月" + day + "日" + " " + weeks;
        document.getElementById("time_area").innerHTML = L;
    }

    function flex(){
        if($(".x-slide_left").css("background-position")=="0px 0px"){
            $(".x-slide_left").css("background-position","0px -61px");
        }else{
            $(".x-slide_left").css("background-position","0px 0px");
        }
    }

    $(function() {
    defaultArea ="${sessionScope.DEFAULT_PROJECT.areaLs}";
     console.log("defaultArea in index:"+defaultArea);

    });
</script>
</body>

</html>
