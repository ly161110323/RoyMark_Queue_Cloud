<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/page/common/include.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" charset="UTF-8" src="${ctx}/static/resources/js/websocket.js"></script>
</head>
<body>
<div >
    <div class="layui-row">
        <div class="layui-col-md6">
            <%--            <div class="grid-demo"></div>--%>
        </div>
    </div>
    <div  style="width: 100%;height: 100%;display: block">
        <%--<video width="1024" height="576" controls>
            <source src="http://localhost:8080/crowdManagement/getVideo?video_address=" +${video_address}
                    type="video/mp4">
        </video>--%>
        <img id="show_video" style="width: auto;height: 100%;object-fit:cover" src="">
    </div>
    <button onclick="test();">Click</button>

</div>
<script>
    var wsUrl = "${ctx}/webSocketService?video_address=rtsp://admin:1234qwer@10.249.41.65:666/Streaming/Channels/101,rtsp://admin:1234qwer@10.249.41.65:554/Streaming/Channels/401&cam_id=test1,test2,test3,test4&width=1280&height=960";
    //建立连接
    var ws1 = new Ws({
        host: window.location.host.split(":")[0]
        , port: window.location.host.split(":")[1]
        , path: wsUrl
        , wsMesEvent: function (message) {
            //将接收到的图片数据进行刷新显示
            var data = JSON.parse(message);
            console.log(data);
            if (data.code === 0) {
                console.log(message)
            } else if (data.code === 201) {

                $("#show_video").attr("src", "data:image/*;base64," + data.data)
            }
        }
    });

    function test() {
        alert("test");
        ws1.send("test");
        ws1.ws.send("testOr");
    };


</script>
</body>
</html>
<script>


</script>