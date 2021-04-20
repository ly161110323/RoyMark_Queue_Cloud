<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<title>禾麦智能大厅管理系统</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" media="screen" href="${ctx}/resources/css/default.css" />
	<link rel="shortcut icon" href="${ctx}/resources/images/favicon.ico" type="image/x-icon"/>

    <style>
        @media screen and (min-width:768px) {
            #index_content{margin: 0 auto;}
        }
        @media screen and (min-width:1366px) {
            #index_content{margin: 0 auto;margin-top: 0 auto;}
        }
        @media screen and (min-width:1400px) {
            #index_content{margin: 0 auto;margin-top: 8%;}
        }
        @media screen and (min-width:1440px) {
            #index_content{margin: 0 auto;margin-top: 4%;}
        }
        @media screen and (min-width:1600px) {
            #index_content{margin: 0 auto;margin-top: 4%;}
        }
        @media screen and (min-width:1680px) {
            #index_content{margin: 0 auto;margin-top: 6%;}
        }
        @media screen and (min-width:1920px) {
            #index_content{margin: 0 auto;margin-top: 6%;}
        }

    </style>
	<script src="${ctx}/resources/newHCSSRemoteQueue/js/jquery.min.js"></script>
    <script src="${ctx}/resources/newHCSSRemoteQueue/js/plugins/layer/layer.min.js"></script>
	<script type="text/javascript" src="${ctx}/page/common/js/util.js"></script>

	<script type="text/javascript">

        if(window.top!=self){
            window.top.location=self.location;
        }
	</script>
	<script type="text/javascript">
	    $(function(){
	    	$("#AreaDiv").hide();
	    });
	    

		function getTree(){
			var targetUrl="${ctx}/page/manage/loginTree.jsp";
        	var argTitle="选择所属大厅";
        	openwindowNoRefresh(targetUrl,argTitle,400,500);
		}
	 
        function checkuser(){
            var username = document.getElementById("loginId").value;
            if(username == ""){
            	layer.alert("用户名不能为空!");
                return false;
            }
            if(username !="roymarkadmin" ){
            	$("#AreaDiv").show();
            }else{
            	$("#AreaDiv").hide();
            }
            return true;
        }

        function checkpwd(){
            var pwd = document.getElementById("passWord").value;
            if(pwd == ""){
            	layer.alert("密码不能为空!");
                return false;
            }
            return true;
        }

        function doLogin() //用ajax获取登录结果，控制页面跳转或者显示错误.
		{
            var url="${ctx}/anomaly/updateAnomalyFromServer";
            //封装数据
            var formData = new FormData();
            var username = document.getElementById("loginId").value;

            var pwd = document.getElementById("passWord").value;
            formData.append("anomalyEvent", "离岗");
            formData.append("anomalyStartDate", username);
            if (pwd != "")
            	formData.append("anomalyEndDate", pwd);
            formData.append("anomalyConfidence", 0.8);
            formData.append("anomalyLink", "/test");

            formData.append("windowHiddenId", 7);
            formData.append("camHiddenId", 0);
            $.ajax({

                type:"POST",
                //提交的网址
                url:url,
                processData : false, // 使数据不做处理				// 以下两行仅在formData使用
                contentType : false, // 不要设置Content-Type请求头
                //提交的数据   该参数为属性值类型的参数
                data: formData,

                //返回数据的格式
                dataType: "json",

                success:function(data){
                    // console.log("data:"+JSON.stringify(data));
                    // console.log("data result:"+data.result);
				if(data.result=='success')
                {
                    var indexUrl="${ctx}/page/manage/index.jsp";
                    // console.log("indexUrl:"+indexUrl);
                    window.location.href=indexUrl;
                }
                else {
                    // console.log("data:"+JSON.stringify(data));
                    var msg=data.msg;
                    layer.alert(msg);
                }
                }   ,
                //调用执行后调用的函数
                complete: function(XMLHttpRequest, textStatus){

                },
                //调用出错执行的函数
                error: function(){
					console.log("error");
                }
            });
		    return false;

		}
        
        function setInfo(){
        	layer.open({
        	    id:1,
        	        type: 1,
        	        title:'配置文件设置',
        	        skin:'layui-layer-rim',
        	        area:['450px', 'auto'],
        	        content: 
        	     ' <div align="center" class="row" style="width: 420px;  margin-left:7px; margin-top:10px; ">'
        	        +'<div class="col-sm-12">'
        	          +'<div class="input-group">'
        	            +'<span class="input-group-addon">&nbsp&nbsp&nbsp&nbspFTP账号   ：</span>'
        	            +'<input id="ftpuserid" type="text" class="form-control" placeholder="请输入用户名">'
        	            +'</div>'
        	          +'</div>'
        	        +'<div class="col-sm-12" style="margin-top: 10px">'
        	          +'<div class="input-group">'
        	            +'<span class="input-group-addon">&nbsp&nbsp&nbsp&nbspFTP密码   ：</span>'
        	            +'<input id="ftppassword" type="text" class="form-control" placeholder="Ftp密码">'
        	          +'</div>'
        	        +'</div>'
        	        +'<div class="col-sm-12" style="margin-top: 10px">'
      	              +'<div class="input-group">'
      	                +'<span class="input-group-addon">&nbsp&nbsp&nbsp&nbspFTP地址   ：</span>'
      	                +'<input id="ftppath" type="text" class="form-control" placeholder="请输入地址">'
      	              +'</div>'
      	            +'</div>'
      	            +'<div class="col-sm-12" style="margin-top: 10px">'
  	                  +'<div class="input-group">'
  	                    +'<span class="input-group-addon"> 中间键端口   ：</span>'
  	                    +'<input id="udpport" type="text" class="form-control" placeholder="请输入中间键接收端口">'
  	                  +'</div>'
  	                +'</div>'
        	     +'</div>'
        	        ,
        	        btn:['保存','取消'],
        	        btn1: function (index,layero) {
        	        	save();
        	        	layer.close(index);
        	        },
        	        btn2:function (index,layero) {
        	             layer.close(index);
        	        }
        	 
        	    });
        		$.ajax({
            		url:"${ctx}/FtpConfigController/getFtpInfo",
    				type:"post",
    				dataType:"json",
    				success:function(data){
    					$("#ftpuserid").val(data.ftpuserid);
    					$("#ftppassword").val(data.ftppassword);
    					$("#ftppath").val(data.ftppath);
    					$("#udpport").val(data.udpport);
    				}
            	});
        }
        
        function save(){
        	var ftpuserid = $("#ftpuserid").val();
        	var ftppassword = $("#ftppassword").val();
        	var ftppath = $("#ftppath").val();
        	var udpport = $("#udpport").val();
			$.ajax({
				url:"${ctx}/FtpConfigController/saveFtpInfo",
				type:"post",
				dataType:"json",
				data:{
					"ftpuserid":ftpuserid,
					"ftppassword":ftppassword,
					"ftppath":ftppath,
					"udpport":udpport
				},
				success:function(data){
					if(data.result=="ok"){
						layer.alert("配置保存成功！");
					}else{
						layer.alert("配置保存失败！");
					}
				}
			});
        }

        
	</script>

</head>
<body style="background-color:#ffffff;margin:0;padding:0;" onLoad="document.all.loginId.focus();">

<div id="index_content" style="width:1366px;height:auto !important;min-height:600px;background-image:url(${ctx}/resources/images/index3.jpg);" >
	<form id="login" method="post" name="login"  >
		<div style="padding-left:815px;padding-top:350px;">
			<table>
				<tr>
					<td><font size=2 color="#919191"><B>&nbsp;&nbsp;&nbsp;&nbsp;用户名：</B></font></td>
					<td>
						<input type="text" style="height:22px;width:170px;" size="20" maxlength="20" name="loginId" id="loginId" value="" onchange="checkuser()" />
					</td>
				</tr>
				<tr><td colspan="2"></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr>
					<td><font size=2 color="#919191"><B>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;密 码：</B></font></td>
					<td>
						<input type="text" style="height:22px;width:170px;" size="20" maxlength="20" name="passWord" id="passWord" value="" onchange="checkuser()" />
					</td>
				</tr>
				<tr><td colspan="2"></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr id="AreaDiv">
					<td><font size=2 color="#919191"><B>所属大厅：</B></font></td>
					<td>
						<div>
						<input type="text" style="height:22px;width:170px;" size="20" readonly="readonly"  name="Area_Name" id="Area_Name" onclick="getTree()"/>
						<input type="hidden" name="Area_Ls" id="Area_Ls">
						</div>
					</td>
				</tr>
				<tr><td colspan="2"></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr>
					<td colspan="2" align="right">
						<input class="btnFnt" style="box-shadow: 0 1px 1px inset,0 -1px 0 inset,0 1px 1px  inset;
					background-color:#D02090;color:#FFFFFF;" id=submit1 value=" 登 录 " type="button" onclick="return doLogin();" />
					</td>
				</tr>
			</table>
		</div>
	</form>
</div>
<center>
    <div id="info" onclick="setInfo()" style="width:1366px;height:30px;background-color:#ffffff;padding-top:10px;">
        <font style="font-size:12pt">禾麦科技开发(深圳)有限公司&copy2019版权所有，建议分辨率1920*1080</font>
    </div>
</center>
</body>
</html>
