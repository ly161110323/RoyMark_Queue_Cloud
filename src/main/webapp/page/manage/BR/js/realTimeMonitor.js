$(function (){
    groupManagementClick();
    prevButton();
    nextButton();
    var rootPath = getWebRootPath();
    var url = rootPath + "/group/queryData"
    var params = {
        "pageNo": 1,
        "pageSize": -1
    }


    $.ajax({
        type: 'POST',
        url: url,
        cache: false,
        async: true,
        dataType: 'json',
        data: params,
        success: function (result) {

            var pageList = result.pageList;
            var list = pageList.records
            var str = "";
            for (var i = 0; i < list.length; i++) {
                str += "<option value='" + list[i].groupHiddenId + "'>" + list[i].groupId + "</option>";
            }
            if(list.length==0){
                layer.msg("无可用分组")
                return ;
            }
            $("#selectCommitGroupId").empty();
            // $("#groupId").append("<option value=''>请选择绑定服务器ID</option>");
            $("#selectCommitGroupId").append(str);
            queryCamByGroup($("#selectCommitGroupId").val(),curPage);
        }
    });
})
var videoPath = "";
var grid = {x:3,y:3};
var width = '1280';
var height = '720';
var ws1 = null;
var curPage = 1;
var totalPage = 1;
function groupManagementClick() {
    var rootPath = getWebRootPath();
//字典类型管理按钮
    $(document).on('click', '#groupManagement', function () {
        var targetUrl = rootPath + "/page/manage/BR/groupManagement.jsp";
        var argTitle = "摄像头分组管理";
        openwindowNoRefresh(targetUrl, argTitle, 1020, 480);
    });
}
function prevButton(){
    $('#prevPage').click(function (){
        if(curPage==1){
            layer.msg("已经是第一页了！")
        }else{
            curPage-=1;
            queryCamByGroup($("#selectCommitGroupId").val(),curPage)
        }
    })
}
function nextButton(){
    $('#nextPage').click(function (){
        if(curPage==totalPage){
            layer.msg("已经是最后一页了！")
        }else{
            curPage+=1;
            queryCamByGroup($("#selectCommitGroupId").val(),curPage)
        }
    })
}
function queryCamByGroup(groupHiddenId,pageNo){
    var rootPath = getWebRootPath();
    var url = rootPath + "/camera/getCamByGroup"
    var params = {
        "groupHiddenId": groupHiddenId,
        'pageNo':pageNo,
        "pageSize": 9
    }
    $.ajax({
        type: 'POST',
        url: url,
        cache: false,
        async: true,
        dataType: 'json',
        data: params,
        success: function (result) {
            if(result.result =='error'){
                layer.msg(result.msg);
                return ;
            }
            var pageList = result.pageList;
            var list = pageList.records;
            var urls = [];
            var ids = []
            totalPage = pageList.pages;
            list.forEach(function (item){
                urls.push(item['camVideoAddr']);
                ids.push(item['camId']);
            })

            videoPath = urls.toString();
            logids = ids.toString();
            if(videoPath==""){
                layer.msg("当前分组无摄像头！");
                return ;
            }
            showVideoImg()
        }
    });
}
function showVideoImg(){
    if(videoPath==""){
        layer.msg("视频地址为空！");
        return ;
    }
    var wsUrl = getWebRootPath()+"/webSocketService"+'?video_address='+videoPath+'&x='+grid.x+'&y='+grid.y+'&width='+width+'&height='+height+'&cam_id='+logids;

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
