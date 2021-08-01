//模拟后端返回了多个摄像头
var cameraArray = [{"id": 3, "x": 35, "y": 118},
    {"id": 2, "x": 200, "y": 200},
    {"id": 1, "x": 300, "y": 300}];
// 右键菜单栏
var rm = null
// body根节点
// var body = document.getElementById("bodyBg")
// 创建可拖动元素的div和对应的img，如果有多个要遍历创建
// var c = document.getElementById("myCanvas");
// var ctx = c.getContext("2d");


var mapList = null;
var anomalyRecord = {}//只保存最新的异常
var curMapIndex = 0;
var curMap = null
var hasCoordCams = []
var noCoordCams = []
var edit = false
var isChange = false;
var camSize = 50;
// "/resources/images/xw/gray_camera.png";

const green_src = getWebRootPath() + "/resources/images/xw/" + 'green' + "_camera.png";
const yellow_src = getWebRootPath() + "/resources/images/xw/" + 'yellow' + "_camera.png";
const red_src = getWebRootPath() + "/resources/images/xw/" + 'red' + "_camera.png";
const black_src = getWebRootPath() + "/resources/images/xw/" + 'black' + "_camera.png";
const gray_src = getWebRootPath() + "/resources/images/xw/" + 'gray' + "_camera.png";
const error_src = getWebRootPath() + "/resources/images/xw/" + 'error' + "_camera.png";

var normal_src = green_src;

var status_src = {'play': yellow_src, 'sleep': red_src, 'leave': gray_src, 'gather': black_src}
$(document).ready(function () {
    queryMap();
    icon_operate();
    setInterval(queryAnomalyEvent,3000);
    // addClick();
    // updateClick();
    // deleteClick();
    // cameraEdit();
    rm = document.getElementById("rightMenu");
    rm.style.display = "none";
    bindEventListen();
    mapEdit();
})

function icon_operate() {
    $(document).on('click', '#btnChooseMapImage', function () {
        //让文件选择组件做一次点击
        $("#mapImage").click();
    });
    //为文件组合框绑定值改变事件
    $(document).on('change', '#mapImage', function () {
        var arrs = $(this).val().split('\\');
        var filename = arrs[arrs.length - 1];
        $("#mapImageFileName").val(filename);
    });
}

function loadOneCameraIcon(mainDiv, camObj) {
    var mainPosition = {top: mainDiv.offset().top, left: mainDiv.offset().left}

    // var divNode = document.createElement("div");
    // divNode.id = "draggable" + "_" + camObj["camHiddenId"];
    // divNode.style.position = "absolute";
    let coord = camObj["camCoordinates"].split(',')

    // divNode.style.left = (parseInt(coord[0]) +parseInt(mainPosition.left))+ "px";
    // divNode.style.top = (parseInt(coord[1]) +parseInt(mainPosition.top))+ "px";
    // var imgNode = document.createElement("img");
    // imgNode.src = getWebRootPath() + "/resources/images/xw/black_camera.png";
    // imgNode.height = 100;
    // imgNode.width = 100;
    var divNode = $('<div></div>');
    divNode.attr('id', camObj.camHiddenId);
    divNode.css('position', 'absolute');
    divNode.offset({top: parseInt(coord[1]), left: parseInt(coord[0])});
    var imgNode = new Image(camSize, camSize);
    imgNode.src = green_src;
    divNode.append(imgNode);
    mainDiv.append(divNode);
    // 绑定每个摄像头右键事件
    divNode.contextmenu(function (e) {

        var mx = e.clientX;
        var my = e.clientY;
        rm.style.left = mx + "px";
        rm.style.top = my + "px";
        rm.style.display = "block";
        rightHiidenId = this.id;
        console.log(rightHiidenId)
        return false;
    })

    divNode.click(function (e) {
        console.log("click", this.id);


    })
    // 为每个摄像头设定可拖拽
    // $(function () {
    //     divNode.draggable({
    //         // containment: { containment: "parent" },
    //         containment: "#main", scroll: false,
    //         stop: function (event, ui) {
    //             console.log(ui);
    //             console.log(ui.helper.context.id);
    //             var index = hasCoordCams.findIndex(e => e.camHiddenId === camObj["camHiddenId"]);
    //             if (index != -1) {
    //                 var c = ui.position.left.toString() + ',' + ui.position.top.toString();
    //                 hasCoordCams[index].camCoordinates = c;
    //             }
    //             if (edit) {
    //                 isChange = true;
    //             }
    //         }
    //     });
    // });
}

function loadDivNodes(cameraArray) {
    var mainDiv = $('#main');
    mainDiv.empty();
    for (var i = 0; i < cameraArray.length; i++) {
        loadOneCameraIcon(mainDiv, cameraArray[i]);
    }
    $('#cameraNum').val(hasCoordCams.length);
}

//载入地图与摄像头信息
function loadMapAndCamera(url, mapHiddenId) {
    if (url) {
        var rootPath = getWebRootPath()
        console.log(rootPath + url)
        img = new Image()
        img.src = rootPath + url;
        img.onload = function () {

            // $('#main').empty()
            $('#main').css('width', img.width)
            $('#main').css('height', img.height)
            // $('#main').append(img)
            $('#main').css('background', 'url(' + img.src + ')')
            $('#mapId').val(mapList[curMapIndex]['mapId'])
            $('#mapName').val(mapList[curMapIndex]['mapName'])
            // $('#mapImageFileName').val(mapList[curMapIndex]['mapMapPath'])
        }
    }
    if (mapHiddenId) {
        // initDivNode
        var rootPath = getWebRootPath();
        var url = rootPath + "/camera/queryData"
        var params = {
            "pageNo": 1,
            "pageSize": -1,
            "mapHiddenId": mapHiddenId
        }

        $.ajax({
            type: 'POST',
            url: url,
            cache: false,
            async: true,
            dataType: 'json',
            data: params,
            success: function (data) {
                //调用成功时对返回的值进行解析
                var list = data.pageList.records;
                hasCoordCams = []
                noCoordCams = []
                list.forEach(function (item) {
                    if (item.camCoordinates) {
                        hasCoordCams.push(item)
                    } else {
                        noCoordCams.push(item)
                    }
                });
                loadDivNodes(hasCoordCams);
                flashAddibleCamera(noCoordCams);
            },
            error: function (response) {
                console.log(response);
            }
        });
    }
}

function flashAddibleCamera(noCoordCams) {

    $('#selectCameraAdd').empty();
    if (noCoordCams.length == 0) {
        $("#selectCameraAdd").append("<option value=''>" + "无可添加摄像头" + "</option>");
        return;
    }
    noCoordCams.forEach(function (item) {
        $("#selectCameraAdd").append("<option value='" + item['camHiddenId'] + "'>" + item['camId'] + "</option>");
    });
    $("#selectCameraAdd option:first").prop("selected", 'selected');

}

function validateData(isAdd) {
    if ($("#mapId").val().trim() == "") {
        layer.alert("地图ID不能为空！");
        return false;
    }
    if ($("#mapName").val().trim() == "") {
        layer.alert("地图名称不能为空！");
        return false;
    }
    if (isAdd) {
        if ($("#mapImageFileName").val().trim() == "") {
            layer.alert("图片路径不能为空！");
            return false;
        }
    }


    var chooseId = $("#mapId").val();
    var chooseName = $("#mapName").val();
    // var fileName = $("#mapImageFileName").val();
    // console.log(fileName)
    var isExit = false;
    var idIndex = mapList.findIndex(e => e.mapId == chooseId)
    var nameIndex = mapList.findIndex(e => e.mapName == chooseName)
    if (isAdd) {
        if (idIndex != -1 || nameIndex != -1) {
            isExit = true;
            layer.alert("地图ID或名称已存在！");
        }
    } else {
        if (idIndex != curMapIndex || nameIndex != curMapIndex) {
            isExit = true;
            layer.alert("地图ID或名称已存在！");
        }
    }


    if (isExit) {
        return false;
    }

    return true;
}

function updateClick(callback) {
//为修改绑定点击事件
    $(document).on('click', '#modifyCommit', function () {

        if (!mapList[curMapIndex]) {
            layer.alert("当前无地图！");
            return;
        }
        if (!validateData(false)) {
            return;
        }
        var formData = new FormData();
        formData.append("mapHiddenId", mapList[curMapIndex].mapHiddenId);
        formData.append("mapId", $('#mapId').val());
        formData.append("mapName", $("#mapName").val());
        if ($('#mapImageFileName').val() != "") {
            formData.append("uploadMap", $('#mapImage')[0].files[0]);
        }


        var rootPath = getWebRootPath();
        var url = rootPath + "/map/update";

        $.ajax({
            url: url,
            type: "post",
            datatype: "json",
            processData: false, // 使数据不做处理
            contentType: false, // 不要设置Content-Type请求头
            data: formData,
            success: function (data) {
                if (data.result == "error") {
                    layer.msg("服务器错误！");
                    return;
                }
                if (data.result == "ok") {
                    layer.msg("修改成功！");
                } else if (data.result == "no") {
                    layer.msg("修改失败！");
                }
                // table.draw(false);
                queryMap();
                clearData();


            }
        });
        saveCamera();
    });//修改事件处理完毕
}

function addClick() {
    $(document).on('click', '#addCommit', function () {
        if (!validateData(true)) {
            return;
        }
        var formData = new FormData();
        formData.append("uploadMap", $('#mapImage')[0].files[0]);
        formData.append("mapId", $('#mapId').val());
        formData.append("mapName", $("#mapName").val());


        var rootPath = getWebRootPath();
        var url = rootPath + "/map/insert";
        $.ajax({
            type: 'POST',
            url: url,
            cache: false,
            processData: false, // 使数据不做处理
            contentType: false, // 不要设置Content-Type请求头
            data: formData,
            success: function (data) {
                console.log(data)
                if (data.result == "error") {
                    layer.alert("服务器错误！");
                    return;
                }
                if (data.result == "ok") {
                    layer.alert("新增成功！");
                } else if (data.result == "no") {
                    layer.alert("新增失败！");
                }
                queryMap();
                // table.draw(false);
                clearData();
            }
        });
    });
}

//清除数据
function clearData() {
    var $file = $("#mapImageFileName");
    $file.after($file.clone().val(""));
    $file.remove();

    $("#mapName").val("");

    $("#mapId").val("");


}

// 上传地图
function uploadMap() {
    var file_data = $('#file').prop('files')[0];
    console.log(file_data);
    var form_data = new FormData();
    form_data.append('file', file_data);
    $.ajax({
        url: "",
        dataType: 'text', // what to expect back from the server
        cache: false,
        contentType: false,
        processData: false,
        data: form_data,
        type: 'post',
        success: function (response) {
            console.log(response);
        },
        error: function (response) {
            console.log(response);
        }
    });
}

//查询所有地图
function queryMap(id = "", name = "") {
    var rootPath = getWebRootPath();
    var url = rootPath + "/map/queryData"
    var params = {
        "pageNo": 1,
        "pageSize": -1
    }

    if (id != "") {
        params["mapId"] = id
    }
    if (name != "") {
        params["mapName"] = name
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
            var datainfos = pageList.records

            mapList = datainfos;
            console.log(mapList)
            if (typeof (datainfos) != "undefined" && datainfos.length > 0) {
                // curMap = mapList[curMapIndex]
                loadMapAndCamera(mapList[curMapIndex].mapPath, mapList[curMapIndex].mapHiddenId); //加载背景图
                loadMapOptions();

            } else if ((typeof (datainfos) == "undefined") && pageNo > 1) {
                layer.alert("查询地图出现未知错误!")
            } else {
                layer.alert("查询地图出现未知错误!")
            }
        }
    });
}

//更新可选地图
function loadMapOptions() {
    var idstr = ""
    for (var i = 0; i < mapList.length; i++) {
        idstr += "<option value='" + mapList[i].mapHiddenId + "'>" + mapList[i].mapId + "</option>";
    }
    $("#selectCommitMapId").empty();
    // $("#selectCommitmapId").append("<option value=''>请选择地图ID</option>");
    $("#selectCommitMapId").append(idstr);
    $("#selectCommitMapId").val(mapList[curMapIndex].mapHiddenId);

    var namestr = ""
    for (var i = 0; i < mapList.length; i++) {
        namestr += "<option value='" + mapList[i].mapHiddenId + "'>" + mapList[i].mapName + "</option>";
    }
    $("#selectCommitMapName").empty();
    // $("#selectCommitmapName").append("<option value=''>请选择地图名称</option>");
    $("#selectCommitMapName").append(namestr);
    $("#selectCommitMapName").val(mapList[curMapIndex].mapHiddenId);
    // flashAddibleCamera(noCoordCams);
}

function saveCamera(callback) {
    var map = new Map();

    hasCoordCams.forEach(function (item) {
        map.set(item.camHiddenId, item.camCoordinates);

    })
    noCoordCams.forEach(function (item) {
        map.set(item.camHiddenId, item.camCoordinates);
    })
    let obj = Object.create(null);
    for (let [k, v] of map) {
        obj[k] = v;
    }

    $.ajax({
        url: getWebRootPath() + '/camera/batchUpdateCoordinates',
        type: 'post',
        datatype: 'json',
        contentType: 'application/json;charset=utf-8',
        data: JSON.stringify(obj),
        sync: false,
        success: function (res) {

            if (res.result == 'ok') {
                layer.msg('保存成功!');
                isChange = false;
                if (typeof callback == 'function') {
                    callback();
                }
            } else {
                layer.msg(res.msg);
            }

        }
    });
}
function changeMap(mapHiddenId){
    var map = mapList.find(e => e.mapHiddenId == mapHiddenId)
    curMapIndex = mapList.findIndex(e => e.mapHiddenId == mapHiddenId)
    console.log(curMapIndex)
    if (isChange) {
        layer.confirm('是否保存当前摄像头位置修改？', {
            btn: ['保存', '不保存'],
            icon: 3
        }, function () {
            // $('#modifyCommit').trigger("click");
            saveCamera(function () {
                loadMapAndCamera(map.mapPath, map.mapHiddenId);
                loadMapOptions();
            });

        }, function () {
            loadMapAndCamera(map.mapPath, map.mapHiddenId);
            loadMapOptions();
        })
    } else {
        loadMapAndCamera(map.mapPath, map.mapHiddenId);
        loadMapOptions();
    }
}
function bindEventListen() {
    $('#saveCameraCommit').click(saveCamera);
    $(document).on('click', '#changeCommit', function () {
        var selectId = $('#selectCommitMapId').val();
        var selectName = $('#selectCommitMapName').val();

        if (selectId == mapList[curMapIndex].mapHiddenId) {
            layer.msg("已选择当前地图！")
            return;
        }
        changeMap(selectId);

    })
    $('#selectCommitMapId').change(function () {
        var hiddenId = $(this).children('option:selected').val()
        $('#selectCommitMapName').val(hiddenId)
    })
    $('#selectCommitMapName').change(function () {
        var hiddenId = $(this).children('option:selected').val()
        $('#selectCommitMapId').val(hiddenId)
    })
    document.documentElement.onclick = function () {
        if (rm.style.display == "block") {
            rm.style.display = "none";
        }
    }
    $(document).on('click', '#addCameraCommit', function () {
        var selectHiddenId = $('#selectCameraAdd').val();
        if (!selectHiddenId) {
            layer.msg("已经没有可添加的摄像头！");
            return;
        }
        var index = noCoordCams.findIndex(e => e.camHiddenId == selectHiddenId);
        noCoordCams[index].camCoordinates = "0,0";
        hasCoordCams.push(noCoordCams[index]);
        loadOneCameraIcon($('#main'), noCoordCams[index])
        noCoordCams.splice(index, 1);
        $('#cameraNum').val(hasCoordCams.length);
        flashAddibleCamera(noCoordCams);
        isChange = true;
    });
    $('#clearData').click(clearData);

}

//删除地图
function deleteClick() {
//为删除按钮绑定点击事件
    $(document).on('click', '#deleteCommit', function () {
        var rootPath = getWebRootPath();
        var url = rootPath + "/window/delete";
        var items = new Array();
        var cBox = $("[name=choice]:checked");
        if (cBox.length == 0) {
            layer.alert("请勾选您所要删除的数据！");
            return;
        }
        layer.confirm("您确定要" + "删除" + "这" + cBox.length + "条记录吗？",
            {
                btn: ['确定', '取消']
            },
            function () {
                for (var i = 0; i < cBox.length; i++) {
                    items.push(cBox.eq(i).val());
                }
                var data = {"deleteId": items.toString()};

                $.ajax({
                    type: 'POST',
                    url: url,
                    data: data,
                    success: function (data) {
                        if (data.result == "error") {
                            layer.alert("服务器错误！",data.msg);
                            return;
                        }
                        if (data.result == "ok") {
                            layer.msg("删除成功！");
                        }
                        table.draw(false);
                        clearData();
                    },
                    error: function (data) {
                        layer.alert("ajax错误！");
                    }
                });
            }, function () {

            });
    });
}

function cameraEdit() {
//更多配置
    $(document).on('click', '#cameraEdit', function () {
        if (edit) {
            $('#cameraEdit').text("摄像头编辑开启");
            edit = false;
            $("#main").children().draggable({revert: true});
        } else {
            $('#cameraEdit').text("摄像头编辑关闭");
            edit = true;
            $("#main").children().draggable({revert: false});
        }
    });
}

function queryAnomalyEvent() {
    $.ajax({
        type: 'POST',
        url: getWebRootPath() + '/anomaly/getLatestAnomaly',
        cache: false,

        dataType: 'json',
        data: {"mapHiddenId": mapList[curMapIndex].mapHiddenId},
        success: function (data) {
            // console.log(data)
            if (data.result == "error") {
                layer.msg(data.msg);
                return;
            }
            if (data.result == "ok") {
                let mydata = data.data;
                anomalyRecord={}
                $.each(mydata, function (key) {
                    if(key.split(',')[1]=='1'&&key.split(',')[2]=='1'){
                        anomalyRecord[key.split(',')[0]]=mydata[key]
                    }else {
                        console.log(key.split(',')[0],"error")
                        $('#main').children('#' + key.split(',')[0]).children('img').attr('src', error_src);
                    }
                });


            } else if (data.result == "no") {
                // anomalyRecord = data.data;
                // layer.msg(data.msg);

            }
            changeAllCameraColor();
            // setInterval(changeAllCameraColor,1000);
        }
    });
}

function changeAllCameraColor() {

    // $('#main').children().children("img").attr('src', green_src);
    $.each(anomalyRecord, function (key) { //key表示camHIddenId
        console.log(key)
        // anomalyRecord[key.toString()].forEach(function (anomalyEvent) {
        //
        //     // console.log(anomalyEvent.anomalyEvent)
        //     switch (anomalyEvent.anomalyEvent){
        //
        //         case '睡觉':
        //             $('#main').children('#' + anomalyEvent.camHiddenId).children('img').attr('src', red_src);
        //             break;
        //         case '玩手机':
        //             $('#main').children('#' + anomalyEvent.camHiddenId).children('img').attr('src', yellow_src);
        //             break;
        //         case '聚众聊天':
        //             $('#main').children('#' + anomalyEvent.camHiddenId).children('img').attr('src', black_src);
        //             break;
        //         case '离岗':
        //             $('#main').children('#' + anomalyEvent.camHiddenId).children('img').attr('src', gray_src);
        //             break;
        //     }
        //
        // });
        //[21,0,0]


            // console.log(1111111)
            if (anomalyRecord[key.toString()].length > 0) {
                // console.log(3333333)
                let anomalyEvent = anomalyRecord[key.toString()][0]
                console.log(anomalyEvent)
                switch (anomalyEvent.anomalyEvent) {

                    case '睡觉':
                        $('#main').children('#' + anomalyEvent.camHiddenId).children('img').attr('src', red_src);
                        break;
                    case '玩手机':
                        $('#main').children('#' + anomalyEvent.camHiddenId).children('img').attr('src', yellow_src);
                        break;
                    case '聚众聊天':
                        $('#main').children('#' + anomalyEvent.camHiddenId).children('img').attr('src', black_src);
                        break;
                    case '离岗':
                        $('#main').children('#' + anomalyEvent.camHiddenId).children('img').attr('src', gray_src);
                        break;
                }
            }else {
                $('#main').children('#' + key).children('img').attr('src', green_src);
            }


    });


    // setOneCameraColor(1,getWebRootPath() + "/resources/images/xw/"+warning_img+"_camera.png")
}

function setOneCameraColor(camHiddenId, imgsrc) {

}

//删除摄像头
function deleteCamera() {
    var inx = hasCoordCams.findIndex(e => e.camHiddenId == rightHiidenId);
    if (inx != -1) {
        hasCoordCams[inx].camCoordinates = "";
        noCoordCams.push(hasCoordCams[inx]);
        hasCoordCams.splice(inx, 1);
        $("#main").find("div[id=" + rightHiidenId + "]").remove();
        flashAddibleCamera(noCoordCams);
        layer.msg("已删除！");
    }
}

// 异常确认
function anomalyConfirm() {
    if (anomalyRecord[rightHiidenId]) {
        var anomalyList = anomalyRecord[rightHiidenId];
        var firstAnomaly = anomalyList[0];
        layer.confirm('请确认当前异常，选择确认或忽略！', {
                btn: ['确认', '放弃', '稍后处理']
            },
            function () {

                var anomalyHiddenId = firstAnomaly.anomalyHiddenId;
                console.log(rightHiidenId)
                $.ajax({
                    type: 'POST',
                    url: getWebRootPath() + '/anomaly/updateAnomalyStatus',
                    data: {'anomalyStatus': 'valid', 'anomalyHiddenId': anomalyHiddenId.toString()},
                    dataType: "json",
                    async: true,
                    success: function (data) {
                        if (data.result == "error") {
                            layer.msg("服务器错误！确认失败");
                            return;
                        }
                        if (data.result == "ok") {
                            layer.msg("确认成功！");

                            // $('#main').children('#' + rightHiidenId).children('img').attr('src', green_src);

                        }
                        // table.draw(false);
                        // clearData();

                    },
                    error: function (data) {
                        layer.msg("网络错误！");
                    }
                });
            },
            function () {

            },
            function () {

            }
        )
    }
}

// 点击监控函数
function realtimeMinitor() {
    var addr = hasCoordCams.find(e => e.camHiddenId == rightHiidenId).camVideoAddr;
    if (addr) {
        window.videoPath = addr;
        window.grid = {x: 1, y: 1};
        window.width = '720';
        window.height = "405";
        layer.open({
            type: 2,
            title: false,
            area: ['720px', '405px'],
            // skin: 'layui-layer-nobg', //没有背景色
            shadeClose: true,
            content: getWebRootPath() + '/page/manage/BR/playvideo.jsp'
        });
    }

}

//地图编辑
function mapEdit() {
    $('#mapEdit').click(function () {
        var targetUrl = getWebRootPath() + "/page/manage/BR/mapEdit.jsp";
        var argTitle = "地图编辑";
        window.curMapIndex = curMapIndex;
        layer.open({
            type: 2,
            title: argTitle,
            shadeClose: true,
            shade: 0.8,
            area: ['960px', '540px'],
            content: targetUrl,
            cancel:function (index,layero){
                queryMap();
            }
        });

    });
}

// 录像回放函数
function reviewVideo() {

}

// 异常情况确认
function abnormalyComfirm() {

}

// 设定某元素可拖拽，如果有多个元素，应该要遍历，通过name确定是哪一个摄像头，做更新

