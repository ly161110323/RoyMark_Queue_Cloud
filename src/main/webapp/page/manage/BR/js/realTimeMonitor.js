$(function (){
    $.ajax({
        type: 'POST',
        url: getWebRootPath()+'/camera/queryData',
        cache: false,
        async: true,
        dataType: 'json',
        data: {pageSize:-1,pageNo:1},
        success: function (result) {
            var maxVideoNum =parseInt(grid.x)*parseInt(grid.y);
            var pageList = result.pageList;
            var datainfos = pageList.records
            var videoPathList = [];
            if (typeof (datainfos) == "undefined" ) {
                layer.msg("查询摄像头数据异常！");
                return ;
            }
            datainfos.some(function (item){
                if(item.camVideoAddr){
                    videoPathList.push(item.camVideoAddr);
                    if(videoPathList.length>=maxVideoNum){
                        return true;
                    }
                }
            });
            videoPath = "";
            videoPathList.forEach(function (item,index){
                videoPath+=item.toString();
                if(index!=maxVideoNum){
                    videoPath+=',';
                }
            })
            if(videoPath){
                showVideoImg();
            }else {
                layer.msg("无可用摄像头！");
            }
        }
    });
})
var videoPath = "";
var grid = {x:3,y:3};
var width = '960';
var height = '540';
var ws1 = null;
function showVideoImg(){
    if(videoPath==""){
        layer.msg("视频地址为空！");
        return ;
    }
    var wsUrl = getWebRootPath()+"/webSocketService"+'?video_address='+videoPath+'&x='+grid.x+'&y='+grid.y+'&width='+width+'&height='+height;

    ws1 = new Ws({
        host: window.location.host.split(":")[0]
        , port: window.location.host.split(":")[1]
        , path: wsUrl

        // ,dataType: 'json'
        , wsMesEvent: function (message) {
            //将接收到的图片数据进行刷新显示
            var data = JSON.parse(message);
            // console.log(data);
            if (data.code === 0) {
                console.log(message)
            } else if (data.code === 201) {

                $("#show_video").attr("src", "data:image/*;base64," + data.data)
            }
        }
    });
}
// var param = {"video_address": videoPath, "x": grid.x, "y": grid.y};
//建立连接
