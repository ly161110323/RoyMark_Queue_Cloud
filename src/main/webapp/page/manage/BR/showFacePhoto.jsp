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
    <link rel="stylesheet" href="${ctx}/resources/layui/css/layui.css" media="all">
    <script src="${ctx}/resources/layui/layui.js"></script>
</head>
<body>
<div class="text">
    <div id="h1">人员照片</div>
</div>

<div class="layui-carousel" id="img-divs" lay-filter="img-divs">
    <div class="one" carousel-item id="imgs">
<%--        <div class="one-left">--%>
<%--            <button id="pre"><b><</b></button>--%>
<%--        </div>--%>
<%--        <div class="one-center" id="img-container">--%>
<%--            <img id="img" src="" height="480" width="720" >--%>
<%--        </div>--%>
<%--        <div class="one-right">--%>
<%--            <button id="next"><b>></b></button>--%>
<%--        </div>--%>
    </div>
</div>
<div>
    <button id="deleteFace">删除</button>
</div>
</body>
<script language="javascript">
    var oriUrls = parent.imgs.split(','); //保存相对路径

    var hiddenId = parent.dataId;
    var curindex = 0;
    var loadedNum = 0;
    var rootPath=getWebRootPath();
    var curIndex = 0;
    var imgsrclist = []//保存绝对路径
    oriUrls.forEach(function (item){
        imgsrclist.push(rootPath+item);
    });
    function loadimages(){
        $('#imgs').empty();
        imgsrclist.forEach(function (item){

            $('#imgs').append('<img src='+item+'  >')
        });
    }
    loadimages();
    layui.use(['carousel'], function(){
        var carousel = layui.carousel;
        ren = carousel.render({
            elem: '#img-divs'
            ,autoplay:false
            ,width: '500px'
            ,height: '500px'
            // ,interval: 5000
        });
        carousel.on('change(img-divs)', function(obj){ //test1来源于对应HTML容器的 lay-filter="test1" 属性值
            console.log(obj.index); //当前条目的索引
            console.log(obj.prevIndex); //上一个条目的索引
            console.log(obj.item); //当前条目的元素对象
            curIndex = obj.index;
        });
        $('#deleteFace').click(function (){
            console.log(oriUrls)

            var formData = new FormData();
            formData.append("userHiddenId", hiddenId);
            formData.append("imgPath",oriUrls[curindex] );

            var url = rootPath + "/user/deleteFace";

            $.ajax({
                url: url,
                type: "post",
                datatype: "json",
                processData: false, // 使数据不做处理
                contentType: false, // 不要设置Content-Type请求头
                data: formData,
                success: function (data) {
                    if (data.result == "error") {
                        layer.alert(data.msg);
                        return;
                    }
                    if (data.result == "ok") {
                        layer.msg(data.msg);
                        imgsrclist.splice(curindex,1);
                        oriUrls.splice(curindex,1);
                        loadimages();
                        ren.reload();
                    } else if (data.result == "no") {
                        layer.alert(data.msg);
                    }


                }
            });
        });
    });



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
