<%--
  Created by IntelliJ IDEA.
  User: gulante
  Date: 2021/4/12
  Time: 22:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/page/common/includewithnewztreestyle.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <title>禾麦智能大厅管理系统</title>
  <style type="text/css">
    .gray-bg{
      background: url("${ctx}/resources/images/backimg.jfif") no-repeat;
    }
  </style>
  <link rel="shortcut icon" href="${ctx}/resources/images/favicon.ico"
        type="image/x-icon" />
  <script src="${ctx}/resources/jquery/jquery-ui-1.12.1/jquery-ui.js"></script>
  <script>
  window.onload = function(){
      //模拟后端返回了多个摄像头
    var cameraArray = [{"id":1,"x":100,"y":100},
      {"id":2,"x":200, "y":200},
      {"id":3,"x":300,"y":300}];
    // 右键菜单栏
      var rm=document.getElementById("rightMenu");
      rm.style.display="none";
    // body根节点
    var body = document.getElementById("bodyBg")
      // 创建可拖动元素的div和对应的img，如果有多个要遍历创建
      for (var i =0; i < cameraArray.length; i++) {
        var divNode = document.createElement("div");
        divNode.id = "draggable" +"_"+cameraArray[i]["id"];
        divNode.style.position = "absolute";
        divNode.style.left = cameraArray[i]["x"] + "px";
        divNode.style.top = cameraArray[i]["y"] + "px";
        var imgNode = document.createElement("img");
        imgNode.src = "${ctx}/resources/images/camera.png";
        imgNode.height = 100;
        imgNode.width = 100;
        divNode.append(imgNode);
        body.append(divNode);
        // 绑定每个摄像头右键事件
        divNode.oncontextmenu = function (e) {
          var mx = e.clientX;
          var my = e.clientY;
          rm.style.left = mx + "px";
          rm.style.top = my + "px";
          rm.style.display="block";
          return false;
        }
        // 为每个摄像头设定可拖拽
        $(function() {
          $( "#"+ divNode.id).draggable({
            stop: function( event, ui ) {console.log(ui);
            console.log(divNode.id.split("_")[1]);}
          });
        });
      }
      document.documentElement.onclick=function () {
        if (rm.style.display == "block"){
          rm.style.display="none";
        }
      }
  }
  // 上传地图
  function uploadMap() {
    var file_data = $('#file').prop('files')[0];
    console.log(file_data);
    var form_data = new FormData();
    form_data.append('file', file_data);
    // $.ajax({
    //   url: "",
    //   dataType: 'text', // what to expect back from the server
    //   cache: false,
    //   contentType: false,
    //   processData: false,
    //   data: form_data,
    //   type: 'post',
    //   success: function (response) {
    //     console.log(response);
    //   },
    //   error: function (response) {
    //     console.log(response);
    //   }
    // });
  }
  // 保存地图设置
  function saveMap() {

  }
  // 点击监控函数
  function surveillance() {

  }
  // 录像回放函数
  function reviewVideo() {

  }
  // 异常情况确认
  function abnormal() {

  }
  // 设定某元素可拖拽，如果有多个元素，应该要遍历，通过name确定是哪一个摄像头，做更新


  </script>

<body class="gray-bg" id="bodyBg">
<div class="wrapper wrapper-content animated fadeInRight">
  <div class="row">
    <div class="col-sm-12">
      <div style="float: right;">
        <input type="file"
                id = "file"
                name = "file"
                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"></input>
        <button type="button"
                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                onclick="uploadMap()">上传地图</button>
        <button type="button"
                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                nclick="saveMap()">保存地图设置</button>
      </div>
    </div>
  </div>
</div>
<ul id="rightMenu" style="width: 100px; position: absolute;">
  <li><button  type="button" onclick="surveillance()" value="实时监控">实时监控</button></li>
  <li><button  type="button" onclick="reviewVideo()" value="录像回放">录像回放</button></li>
  <li><button  type="button" onclick="abnormal()" value="异常确认">异常确认</button></li>
</ul>
</body>

</html>