
$(document).ready(function () {
    init_Date();
    // addClick();
    addUser();
    anomalyConfirm();
    deleteClick();
    searchClick();
    clearClick();
    configClick();
    clearUser();
    queryWindowList();
    queryUserList();
    contactManagerClick();
    // icon_operate();//部门图标处理
    loadTimeSelect();
    // caseAreaClick();
    // selectAreaClick();
});

function loadTimeSelect() {
    laydate.render({
        elem: '#anomalyStartTime'
        ,type: 'datetime'
    });
    laydate.render({
        elem: '#anomalyEndTime'
        ,type: 'datetime'
    });
}

function init_Date() {
    // var time = new Date();
    // var day = ("0" + time.getDate()).slice(-2);
    // var month = ("0" + (time.getMonth() + 1)).slice(-2);
    // var today = time.getFullYear() + "-" + (month) + "-" + (day);
    // $('#selectCommitDate').val(today);
    laydate.render({
        elem: '#selectCommitDate'
        ,value: new Date() //参数即为：2018-08-20 20:08:08 的时间戳
    });

}

function queryWindowList() {

    var rootPath = getWebRootPath();
    var url = rootPath + "/window/queryData"
    var params = {
        "pageNo": 1,
        "pageSize": -1
    }

// //获取问卷调查
    $.ajax({
        type: 'POST',
        url: url,
        cache: false,
        async: true,
        dataType: 'json',
        data: params,
        success: function (data) {
// 调用成功时对返回的值进行解析
            var list = data.pageList.records;
//若未出错，则获取信息设置到控件中
            var str = "";
            for (var i = 0; i < list.length; i++) {
                str += "<option value='" + list[i].windowHiddenId + "'>" + list[i].windowId + "</option>";
            }

            // $("#formCaseAreaLs").empty();
            // $("#formCaseAreaLs").append(str);
            $("#windowId").empty();
            $("#windowId").append("<option value=''>请选择绑定窗口ID</option>");
            $("#windowId").append(str);
            $("#selectCommitWindowId").empty();
            $("#selectCommitWindowId").append("<option value=''>请选择绑定窗口ID</option>");
            $("#selectCommitWindowId").append(str);
        }
    });
}

function queryUserList() {

    var rootPath = getWebRootPath();
    var url = rootPath + "/user/queryData"
    var params = {
        "pageNo": 1,
        "pageSize": -1
    }

// //获取问卷调查
    $.ajax({
        type: 'POST',
        url: url,
        cache: false,
        async: true,
        dataType: 'json',
        data: params,
        success: function (data) {
// 调用成功时对返回的值进行解析
            var list = data.pageList.records;
//若未出错，则获取信息设置到控件中
            var str = "";
            userInfos = []
            for (var i = 0; i < list.length; i++) {
                str += "<option  value='" + list[i].userHiddenId + "'>" + list[i].userName + "</option>";
                userInfos.push({"name":list[i]['userName'].toString(),"hiddenId":list[i]['userHiddenId'].toString()});
            }


            // $("#userName").empty();
            // $("#userName").append("<option value=''>请选择人员</option>");
            $("#userName").append(str);
            $('#userName').editableSelect({
                effects: 'slide',//点击的时候，下拉框的过渡效果  有default，slide，fade三个值，默认是default
                filter: true,//选择option以后，是否过滤  默认 true
                duration: 'fast',//下拉选项框展示的过度动画速度
                onSelect: function (obj) {
                    // console.log("下拉框选项被选中");
                    var userHiddenId = $(obj).val();//获取当前数据的id
                    var userName = $(obj).text();//获取当前数据的name
                    // console.info(userHiddenId + '===' + userName);

                },
                onCreate: function () {
                    // console.log("下拉框创建");
                },
                onShow: function() {
                    // console.log("下拉框显示");

                },
                onHide: function() {
                    // console.log("下拉框隐藏");
                    // selectInfo.userHiddenId = $(obj).val();
                }
            });

        }
    });
}


function trClick() {

    //为表格行绑定点击事件
    $('#itemResultTable tbody').on('click', 'tr', function () {

        $("#itemResultTable tr:even").css({
            "background": "#f9f9f9",
            "color": "#676a6c"
        });
        $("#itemResultTable tr:odd").css({
            "background": "white",
            "color": "#676a6c"
        });
        $(this).css({
            "background": "rgb(255, 128, 64)",
            "color": "white"
        });
        dataId = $(this).find("td:eq(0) input[type='checkbox']").val();
        var status={'待处理':'pending',"有效":"valid","无效":"invalid"};
        var res = null;
        if (!!$(this).find("td:eq(14)").text()){
            var res = status[$(this).find("td:eq(14)").text()]
        }
        selectInfo = {
            "anomalyHiddenId": dataId,
            "anomalyEvent": $(this).find("td:eq(5)").text(),
            "anomalyStartTime": $(this).find("td:eq(6)").text(),
            "anomalyEndTime": $(this).find("td:eq(7)").text(),
            "windowHiddenId": $(this).find("td:eq(10)").text(),
            "userHiddenIds": $(this).find("td:eq(11)").text().split(','),
            "anomalyConfidence": $(this).find("td:eq(8)").text(),
            "anomalyFaceConfidence": $(this).find("td:eq(9)").text(),
            "anomalyVideoPath": $(this).find("td:eq(12)").text(),
            "anomalyImagePath":$(this).find("td:eq(13)").text(),
            "anomalyStatus":res
        }
        // console.log(selectInfo.userHiddenIds)
        $("#windowId").val($(this).find("td:eq(10)").text());

        $("#windowName").val($(this).find("td:eq(3)").text());
        // $("#userName").val($(this).find("td:eq(11)").text().split(',')[0]);
        $("#anomalyEvent").val($(this).find("td:eq(5)").text());
        $("#anomalyStartTime").val($(this).find("td:eq(6)").text());
        $("#anomalyEndTime").val($(this).find("td:eq(7)").text());

        $("#anomalyStatus").val(status[$(this).find("td:eq(14)").text()])


    });

}

function formatDate(date, format) {
    var o = {
        "M+": date.getMonth() + 1, //month
        "d+": date.getDate(),    //day
        "h+": date.getHours(),   //hour
        "m+": date.getMinutes(), //minute
        "s+": date.getSeconds(), //second
        "q+": Math.floor((date.getMonth() + 3) / 3),  //quarter
        "S": date.getMilliseconds() //millisecond
    }
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o) if (new RegExp("(" + k + ")").test(format))
        format = format.replace(RegExp.$1,
            RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}

//表格选择框操作
function checkBoxStyle_Control() {
    $(".lsCheck").prop("checked", false);
    $("#checkall").prop("checked", false);
//表格头部复选框点击事件
    $("#checkall").unbind("#checkall").bind("click", function () {
        if ($(this).is(":checked")) {
            $(".lsCheck").prop("checked", true);
        } else {
            $(".lsCheck").prop("checked", false);
        }
    });

//表格主体复选框点击事件
    $(".lsCheck").unbind(".lsCheck").bind("click", function () {
        var allCheckNum = $(".lsCheck").length;
        var checkedNum = $(".lsCheck:checked").length;
        if (allCheckNum == checkedNum) {
            $("#checkall").prop("checked", true);
        } else if (checkedNum == 0) {
            $("#checkall").prop("checked", false);
        } else if (checkedNum > 0) {
            $("#checkall").prop("checked", true);
        }
    });
}

//页面数据合法性验证
function validateData(isAdd) {
    if ($("#anomalyEvent").val().trim() == "") {
        layer.alert("异常事件不能为空！");
        return;
    }
    // if ($("#windowId").val().trim() == "") {
    //     layer.alert("窗口ID不能为空！");
    //     return;
    // }
    // if ($("#userSex").val().trim() == "") {
    //     layer.alert("人员性别不能为空！");
    //     return;
    // }

//     var trs = $("#itemResultTable tr:gt(0)");
//     // var chooseName = $("#txtDeptName").val();
//     var chooseId = $("#userId").val();
//     var isExit = false;
//
// //循环列表判断是否已经存在,放在客户端校验
//     trs.each(function (index, element) {
//         var objLs = $(element).find("td:eq(0)>input").val();
//
//         if ($(element).find("td:eq(2)").text() == chooseId) {
//             if (isAdd) {
//                 isExit = true;
//                 layer.alert("人员编号已存在！");
//                 return false;
//             } else {
//                 if (objLs != dataId) {
//                     isExit = true;
//                     layer.alert("人员编号已存在！");
//                     return false;
//                 }
//             }
//         }
//     });
//
//     if (isExit) {
//         return false;
//     }

    return true;
}

//清除数据
function clearData() {

    $("#windowName").val("");
    $("#windowId").val("");
    $("#userName").val("");
    $("#anomalyEvent").val("");
    $("#anomalyStartTime").val("");
    $("#anomalyEndTime").val("");
    $("#anomalyStatus").val("");
}

function clearUser(){
    $('#clearUser').click(function (){
        if (dataId=='') {
            layer.alert("请选择要清除的数据！");
            return;
        }
        var rootPath = getWebRootPath();
        var url = rootPath + "/anomaly/manualClearUser";
        var formData = new FormData();
        formData.append("anomalyHiddenId", dataId);
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
                    layer.alert(data.msg);
                    return;
                }
                if (data.result == "ok") {
                    layer.msg("清除成功！");
                } else if (data.result == "no") {
                    layer.alert(data.msg);
                }
                table.draw(false);
                clearData();
            }
        });
    });
}

function clearSearch() {
    $("#inputCommitWindowId").val("");
    $("#selectCommitAnomalyEvent").val("");
    $("#selectCommitDate").val("");
}
function addUser(){
    $('#addUser').click(function (){
        let userInputName = $('#userName').val();

        let userIndex = userInfos.findIndex(e=>e.name==userInputName);
        if(userIndex==-1){
            layer.alert(userInputName+" 不存在，请重新输入！");
            return
        }

        let userHiddenId = userInfos[userIndex].hiddenId;

        if(selectInfo.userHiddenIds.indexOf(userHiddenId)!=-1){
            layer.alert(userInputName+" 已存在该异常人员中，请勿重复添加！");
            return
        }
        // return ;
        if (dataId=='') {
            layer.alert("请选择要修改的数据！");
            return;
        }
        if (userHiddenId=='') {
            layer.alert("请选择新增人员姓名！");
            return;
        }
        var formData = new FormData();
        formData.append("anomalyHiddenId", dataId);
        formData.append("userHiddenId", userHiddenId);
        var rootPath = getWebRootPath();
        var url = rootPath + "/anomaly/manualAddUser";
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
                    layer.alert(data.msg);
                    return;
                }
                if (data.result == "ok") {
                    layer.msg("新增成功！");
                } else if (data.result == "no") {
                    layer.alert(data.msg);
                }
                table.draw(false);
                clearData();
            }
        });
    });
}
function addClick() {
    $(document).on('click', '#addCommit', function () {
        if (!validateData(true)) {
            return;
        }
        var formData = new FormData();
        formData.append("uploadinfo", $('#staffPhoto')[0].files[0]);
        formData.append("userId", $('#userId').val());
        formData.append("userName", $("#userName").val());
        formData.append("userSex", $("#userSex").val());
        formData.append("userDepartment", $("#userDepartment").val());
        formData.append("userPost", $("#userPost").val());
        formData.append("windowHiddenId", $("#windowId").val());

        var rootPath = getWebRootPath();
        var url = rootPath + "/anomaly/insert";
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
                table.draw(false);
                clearData();
            }
        });
    });
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
    $('#modifyCommit').click(function (){

        if (dataId == '') {
            layer.alert("请选择要确认的数据！");
            return;
        }

        let status = $('#anomalyStatus').val()
        console.log('status:',status)
        $.ajax({
            type: 'POST',
            url: getWebRootPath() + '/anomaly/updateAnomalyStatus',
            data: {'anomalyStatus': status, 'anomalyHiddenId': dataId.toString()},
            dataType: "json",
            async: true,
            success: function (data) {
                if (data.result == "error") {
                    layer.alert("服务器错误！确认失败：",data.msg);
                    return;
                }
                if (data.result == "ok") {
                    layer.msg("确认成功！");

                    // $('#main').children('#' + rightHiidenId).children('img').attr('src', green_src);

                }

                table.draw(false);
                // clearData();

            },
            error: function (data) {
                layer.msg("网络错误！");
            }
        });
    });



}
function updateClick() {
//为修改绑定点击事件  实际并未使用该函数 因绝大多数异常记录不支持修改 仅确认结果一项，因此单独使用另一个函数修改确认结果
    $(document).on('click', '#modifyCommit', function () {
        if (dataId == '') {
            layer.alert("请选择要修改的数据！");
            return;
        }
        if (!validateData(false)) {
            return;
        }
        var formData = new FormData();
        formData.append("anomalyHiddenId", dataId);
        formData.append("anomalyEvent", $('#anomalyEvent').val());
        formData.append("anomalyStartTime", $('#anomalyStartTime').val());
        formData.append("anomalyEndTime", $("#anomalyEndTime").val());

        // formData.append("anomalyLink", selectInfo.anomalyLink);
        // formData.append("anomalyConfidence", selectInfo.anomalyConfidence);

        formData.append("windowHiddenId", $("#windowId").val());
        formData.append("userHiddenId", $("#userName").val());
        if( $('#anomalyStatus').val()){
            formData.append("anomalyStatus", $('#anomalyStatus').val());
        }
        var rootPath = getWebRootPath();
        var url = rootPath + "/anomaly/update";

        $.ajax({
            url: url,
            type: "post",
            datatype: "json",
            processData: false, // 使数据不做处理
            contentType: false, // 不要设置Content-Type请求头
            data: formData,
            success: function (data) {
                if (data.result == "error") {
                    layer.alert("服务器错误！");
                    return;
                }
                if (data.result == "ok") {
                    layer.alert("修改成功！");
                } else if (data.result == "no") {
                    layer.alert("修改失败！");
                }
                table.draw(false);
                clearData();
            }
        });
    });//修改事件处理完毕
}


function deleteClick() {
//为删除按钮绑定点击事件
    $(document).on('click', '#deleteCommit', function () {
        var rootPath = getWebRootPath();
        var url = rootPath + "/anomaly/delete";
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
                            layer.alert("服务器错误！删除失败");
                            return;
                        }
                        if (data.result == "ok") {
                            layer.alert("删除成功！");
                        }
                        table.draw(false);
                        clearData();
                    },
                    error: function (data) {
                        layer.alert("错误！");
                    }
                });
            }, function () {

            });
    });
}

function showPhotos(imgs) {


    window.imgs = imgs
    layer.open({
        type: 2,
        title: false,
        area: ['820px','520px'],
        // skin: 'layui-layer-nobg', //没有背景色
        shadeClose: true,
        content: getWebRootPath()+'/page/manage/BR/photoShow.jsp'
    });
    // var img = new Image()
    // if(dataId==''){
    //     layer.alert("请选择人员信息！");
    //     return
    // }
    //
    // img.onload = function (){
    //     wh = img.width/img.height
    //     layerWidth = 300
    //     layerHeight = layerWidth/wh
    //     var imgdom = '<img id="img" src='+url+' style="width: 300px">'
    //     layer.open({
    //         type: 1,
    //         title: false,
    //         area: [layerWidth+'px',layerHeight+'px'],
    //         // skin: 'layui-layer-nobg', //没有背景色
    //         shadeClose: true,
    //         content: imgdom
    //     });
    //
    // }
    // img.onerror = function (){
    //     layer.alert("图片不存在！")
    // }
    // if(staffInfo['userPhoto']!=""){
    //     img.src =url
    // }else {
    //     layer.alert("未保存图片！")
    // }


}
function showVideo(videopath){



    var video = '<video width="720" controls="controls"> <source src="'+videopath+'" type="video/mp4"></video>'
    layer.open({
                type: 1,
                title: false,
                area: ['720px','480px'],
                // skin: 'layui-layer-nobg', //没有背景色
                shadeClose: true,
                content: video
            });
}

function searchClick() {

    //为查询按钮绑定点击事件
    $(document).on('click', '#queCommit', function () {
        isSearch = "1";
        var inputWindowId = $("#inputCommitWindowId").val();
        var inputUserName = $("#inputCommitUserName").val();
        // var selectAnomalyEvent = $("#selectCommitAnomalyEvent").find("option:selected").text();
        var selectAnomalyEvent = $("#selectCommitAnomalyEvent").val();
        var selectDate = $("#selectCommitDate").val();
        var showPendingList = [];
        $("input[name='filter']").each(function (index,ele){
            if($(ele).is(":checked")){
                showPendingList.push($(ele).val())
            }
        })
        var showPending = showPendingList.toString();
        let params = {};
        if(inputWindowId != ""){
            params["windowId"] = inputWindowId;
        }
        if(inputUserName!=""){
            params["userName"] = inputUserName;
        }
        if(selectAnomalyEvent !=""){
            params["event"] = selectAnomalyEvent;

        }
        if(selectDate !=""){
            params["date"] = selectDate;
        }
        if(showPending!= ''){
            params["anomalyStatus"] = showPending;
        }
        searchData =params;

        table.draw(false);
        // clearSearch();
    });

}

function clearClick() {
//清除
    $(document).on('click', '#clearData', function () {
        clearData();
    });
}

function configClick() {
//更多配置
    $(document).on('click', '#showPhoto', function () {
        if (dataId=='') {
            layer.alert("请选择要查看的数据！");
            return;
        }

        var imgs = selectInfo.anomalyImagePath.split(',')
        showPhotos(imgs);
    });
    $(document).on('click', '#showVideo', function () {
        if (dataId=='') {
            layer.alert("请选择要查看的数据！");
            return;
        }
        var videopath = selectInfo.anomalyVideoPath;
        showVideo(videopath);
    });
}
function contactManagerClick(){
    $('#contactManagerButton').click(function (){
        var targetUrl = getWebRootPath() + "/page/manage/BR/alarmContact.jsp";
        layer.open({
            type: 2,
            title: "报警联系人管理",
            shadeClose: true,
            shade: 0.8,
            area: ['1280px', '720px'],
            content: targetUrl,
            cancel:function (index,layero){
                // queryMap();
            }
        });
    });
}

function caseAreaClick() {
    var rootPath = getWebRootPath();
//字典类型管理按钮
    $(document).on('click', '#caseArea_administration', function () {
        var targetUrl = rootPath + "/page/manage/dept/Queue_CaseArea.jsp";
        var argTitle = "人员信息管理";
        // openwindowNoRefresh(targetUrl, argTitle, 1020, 480);
        showPhotos()
    });
}

function selectAreaClick() {
    var rootPath = getWebRootPath();
//字典类型管理按钮
    $(document).on('click', '#selectArea_administration', function () {
        var targetUrl = rootPath + "/page/manage/dept/Queue_SelectArea.jsp";
        var argTitle = "行政区域管理";
        openwindowNoRefresh(targetUrl, argTitle, 1020, 480);
    });
}
