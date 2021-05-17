$(document).ready(function () {

    addClick();
    updateClick();
    deleteClick();
    searchClick();
    clearClick();
    configClick();
    init_areaInfo();
    queryWindowList();

    icon_operate();//部门图标处理

    caseAreaClick();
    selectAreaClick();
});

function icon_operate() {
    $(document).on('click', '#btnChooseStaffPhoto', function () {
        //让文件选择组件做一次点击
        $("#staffPhoto").click();
    });
    //为文件组合框绑定值改变事件
    $(document).on('change', '#staffPhoto', function () {
        var arrs = $(this).val().split('\\');
        var filename = arrs[arrs.length - 1];
        $("#staffPhotoFileName").val(filename);
    });
}

function init_areaInfo() {
    $("#Area_Ls").val(defaultAreaLs);
    $("#Area_Name").val(defaultAreaName);

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

function queryServerList() {

    var rootPath = getWebRootPath();
    var url = rootPath + "/server/queryData"
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
                str += "<option value='" + list[i].serverHiddenId + "'>" + list[i].serverId + "</option>";
            }

            // $("#formCaseAreaLs").empty();
            // $("#formCaseAreaLs").append(str);
            $("#serverId").empty();
            $("#serverId").append("<option value=''>请选择绑定服务器ID</option>");
            $("#serverId").append(str);
            $("#selectCommitServerId").empty();
            $("#selectCommitServerId").append("<option value=''>请选择绑定服务器ID</option>");
            $("#selectCommitServerId").append(str);
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
        imgPath = $(this).find("td:eq(9)").text();
        staffInfo ={
            userName : $(this).find("td:eq(2)").text(),
            userSex : $(this).find("td:eq(3)").text(),
            userId :$(this).find("td:eq(4)").text(),
            windowId :$(this).find("td:eq(8)").text(),
            userDepartment:$(this).find("td:eq(6)").text(),
            userPost:$(this).find("td:eq(7)").text(),
            userPhoto:$(this).find("td:eq(9)").text()

        }
        $("#userName").val($(this).find("td:eq(2)").text());

        $("#userSex").val($(this).find("td:eq(3)").text());
        $("#userId").val($(this).find("td:eq(4)").text());
        $("#windowId").val($(this).find("td:eq(8)").text());
        $("#userDepartment").val($(this).find("td:eq(6)").text());
        $("#userPost").val($(this).find("td:eq(7)").text());



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
    if ($("#userName").val().trim() == "") {
        layer.alert("人员姓名不能为空！");
        return;
    }
    if ($("#userId").val().trim() == "") {
        layer.alert("人员编号不能为空！");
        return;
    }
    if ($("#userSex").val().trim() == "") {
        layer.alert("人员性别不能为空！");
        return;
    }

    var trs = $("#itemResultTable tr:gt(0)");
    // var chooseName = $("#txtDeptName").val();
    var chooseId = $("#userId").val();
    var isExit = false;

//循环列表判断是否已经存在,放在客户端校验
    trs.each(function (index, element) {
        var objLs = $(element).find("td:eq(0)>input").val();

        if ($(element).find("td:eq(2)").text() == chooseId) {
            if (isAdd) {
                isExit = true;
                layer.alert("人员编号已存在！");
                return false;
            } else {
                if (objLs != dataId) {
                    isExit = true;
                    layer.alert("人员编号已存在！");
                    return false;
                }
            }
        }
    });

    if (isExit) {
        return false;
    }

    return true;
}

//清除数据
function clearData() {
    var $file = $("#staffPhoto");
    $file.after($file.clone().val(""));
    $file.remove();

    $("#userName").val("");

    $("#userSex").val("");
    $("#userId").val("");
    $("#windowId").val("");
    $("#userDepartment").val("");
    $("#userPost").val("");

}

function clearSearch(){
    $("#inputCommitUserName").val("");
    $("#inputCommitUserId").val("");
    $("#inputCommitUserDepartment").val("");
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
        var url = rootPath + "/user/insert";
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

function updateClick() {
//为修改绑定点击事件
    $(document).on('click', '#modifyCommit', function () {
        if (dataId == '') {
            layer.alert("请选择要修改的数据！");
            return;
        }
        if (!validateData(false)) {
            return;
        }
        var formData = new FormData();
        formData.append("userHiddenId", dataId);
        formData.append("uploadinfo", $('#staffPhoto')[0].files[0]);
        formData.append("userId", $('#userId').val());
        formData.append("userName", $("#userName").val());
        formData.append("userSex", $("#userSex").val());
        formData.append("userDepartment", $("#userDepartment").val());
        formData.append("userPost", $("#userPost").val());

        formData.append("windowHiddenId", $("#windowId").val());



        var rootPath = getWebRootPath();
        var url = rootPath + "/user/update";

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
function showPhotos(){
    var rootPath = getWebRootPath();
    var url = rootPath + staffInfo['userPhoto'];
    var img = new Image()
    if(dataId==''){
        layer.alert("请选择人员信息！");
        return
    }

    img.onload = function (){
        wh = img.width/img.height
        layerWidth = 300
        layerHeight = layerWidth/wh
        var imgdom = '<img id="img" src='+url+' style="width: 300px">'
        layer.open({
            type: 1,
            title: false,
            area: [layerWidth+'px',layerHeight+'px'],
            // skin: 'layui-layer-nobg', //没有背景色
            shadeClose: true,
            content: imgdom
        });

    }
    img.onerror = function (){
        layer.alert("图片不存在！")
    }
    if(staffInfo['userPhoto']!=""){
        img.src =url
    }else {
        layer.alert("未保存图片！")
    }




}

function deleteClick() {
//为删除按钮绑定点击事件
    $(document).on('click', '#deleteCommit', function () {
        var rootPath = getWebRootPath();
        var url = rootPath + "/user/delete";
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

function searchClick() {

    //为查询按钮绑定点击事件
    $(document).on('click', '#queCommit', function () {
        isSearch = "1";
        table.draw(false);
        clearSearch();
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
        showPhotos()
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
