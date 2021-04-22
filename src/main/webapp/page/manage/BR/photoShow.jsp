<%--
  Created by IntelliJ IDEA.
  User: liucl
  Date: 2021/4/13
  Time: 7:26 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/page/common/includewithnewztreestyle.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Title</title>
    <style>
        *{
            margin:0;
            padding:0;
            overflow: auto;
            position: relative;
        }
        .text{
            height: 40px;
            padding: 0;
            margin: 0;

            text-align: center;
        }
        #h1{
            line-height:40px;
            text-align:center;
            height: 40px;
            font-size: 20px;
            display: inline-block;
            vertical-align: middle;
            margin: 0;
            padding: 0;

        }
        li{
            list-style:none;
            float:left;
        }
        .container{

            width: 740px;
            height:360px;
            /*margin:0 auto;*/
            position: relative;
            padding: 0;
        }
        .one{
            width:740px;
            height:360px;


            overflow: auto;
        }
        .one-center{
            width:640px;

            height:360px;
            /*text-align:center;*/
            /*overflow: auto;*/
            float: left;
        }
        .one-left{
            width:50px;
            /*overflow: auto;*/
            float:left;
        }
        .one-right{
            width:50px;
            /*overflow: auto;*/
            float:right;
        }
        button{
            width:50px;
            height:360px;
            background-color:#999;
            border:none;
            outline:none;
        }
        button:hover{
            background-color:#666;
        }
    </style>
</head>
<body>
<div class="text">
    <div id="h1">异常行为截图查看</div>
</div>

<div class="container">
    <div class="one">
        <div class="one-left">
            <button id="pre"><b><</b></button>
        </div>
        <div class="one-center">
            <img id="img" src="" height="360" width="640" >
        </div>
        <div class="one-right">
            <button id="next"><b>></b></button>
        </div>
    </div>
</div>
</body>
<script language="javascript">
    var imgsrclist = parent.imgs;
    var len = imgsrclist.length;
    var curindex = 0;

    // imgsrclist.forEach(function (item){
    //     $('#imgs').append('<li style="display:none" id="a"><img src='+item+'  width="600" height="300"></li>')
    // })
    $('#img').attr('src',imgsrclist[curindex])
    $('#next').click(function (){
        if(curindex===len-1){
            curindex=0;
            $('#img').attr('src',imgsrclist[curindex])
        }else {
            curindex+=1;
            $('#img').attr('src',imgsrclist[curindex])
        }
    })
    $('#pre').click(function (){
        if(curindex===0){
            curindex=len-1;
            $('#img').attr('src',imgsrclist[curindex])
        }else {
            curindex-=1;
            $('#img').attr('src',imgsrclist[curindex])
        }
    })

    // var num=4;
    // b1.onclick=function(){
    //     num--;
    //     if(num<1)
    //         num=4;
    //     panduan();
    // }
    // b2.onclick=function(){
    //     num++;
    //     if(num>4)
    //         num=1;
    //     panduan();
    // }
    // function panduan(){
    //     if(num==1){
    //         a.style.display="block";
    //         b.style.display="none";
    //         c.style.display="none";
    //         d.style.display="none";
    //     }
    //     if(num==2){
    //         a.style.display="none";
    //         b.style.display="block";
    //         c.style.display="none";
    //         d.style.display="none";
    //     }
    //     if(num==3){
    //         a.style.display="none";
    //         b.style.display="none";
    //         c.style.display="block";
    //         d.style.display="none";
    //     }
    //     if(num==4){
    //         a.style.display="none";
    //         b.style.display="none";
    //         c.style.display="none";
    //         d.style.display="block";
    //     }
    // }
</script>
<%--<body>--%>
<%--    <div id="left_side">--%>
<%--        <div class="layui-col-md4">--%>
<%--            <img src="${ctx}/resources/images/xw/last.png">--%>
<%--        </div>--%>
<%--        <div id="content">--%>
<%--            <img id="img" src="" >--%>
<%--            <ul>--%>
<%--                <li>1</li>--%>
<%--            </ul>--%>
<%--        </div>--%>
<%--        <div id="right_side">--%>
<%--            <img src="${ctx}/resources/images/xw/next.png">--%>
<%--        </div>--%>
<%--    </div>--%>

<%--    <script type="text/javascript">--%>
<%--        imgpath = getWebRootPath()+'/RemoteQueue/upload/user/19e9afe202924efeac74bb5ca565248a_23009026-C44B-45C3-85BB-359B0F518484.png'--%>
<%--        $('#img').attr('src',imgpath)--%>
<%--    </script>--%>

<%--</body>--%>
</html>
