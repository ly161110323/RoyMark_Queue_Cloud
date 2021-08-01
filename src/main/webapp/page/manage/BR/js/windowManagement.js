$(document).ready(function () {

    addClick();
    updateClick();
    deleteClick();
    searchClick();
    clearClick();
    // configClick();
    // init_areaInfo();
    drawWindow();
    // queryFloorList();
    queryCamList();

    // icon_operate();//部门图标处理
    //
    // caseAreaClick();
    // selectAreaClick();
});
function icon_operate()
{
    $(document).on('click', '#btnChooseDepartmentIcon', function() {
        //让文件选择组件做一次点击
        $("#departmentIcon").click();
    });
    //为文件组合框绑定值改变事件
    $(document).on('change', '#departmentIcon', function() {
        var arrs = $(this).val().split('\\');
        var filename = arrs[arrs.length - 1];
        $("#departmentIconFileName").val(filename);
    });
}
function init_areaInfo() {
    $("#Area_Ls").val(defaultAreaLs);
    $("#Area_Name").val(defaultAreaName);

}
function queryFloorList()
{

    var rootPath = getWebRootPath();
    var url=rootPath+"/floor/getAll"
//获取问卷调查
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
        success: function (data) {
//调用成功时对返回的值进行解析

            var list = data.floors;
//若未出错，则获取信息设置到控件中
            var str ="";
            for (var i = 0; i < list.length; i++) {
                str += "<option value='" + list[i].floorHiddenId + "'>" + list[i].floorName + "</option>";
            }

            $("#floorName").empty();
            $("#floorName").append("<option value=''>请选择楼层</option>");
            $("#floorName").append(str);
        }
    });
}
function queryCamList()
{

    var rootPath = getWebRootPath();
    var url=rootPath+"/camera/queryData"
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
        success: function (data) {
//调用成功时对返回的值进行解析
            var list = data.pageList.records;
//若未出错，则获取信息设置到控件中
            var str = "";
            for (var i = 0; i < list.length; i++) {
                str += "<option value='" + list[i].camHiddenId + "'>" + list[i].camId + "</option>";
            }

            // $("#formCaseAreaLs").empty();
            // $("#formCaseAreaLs").append(str);
            $("#camId").empty();
            $("#camId").append("<option value=''>请选择绑定摄像头ID</option>");
            $("#camId").append(str);
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
            "background" : "rgb(255, 128, 64)",
            "color": "white"
        });
        dataId = $(this).find("td:eq(0) input[type='checkbox']").val();
        // $("#txtDeptLs").val(dataId);
        selectInfo = {
            "camHiddenId":$(this).find("td:eq(9)").text(),
            "windowId":$(this).find("td:eq(2)").text(),
            "windowCoordinates":$(this).find("td:eq(8)").text(),
            "windowHiddenId":dataId
        }

        $("#windowId").val($(this).find("td:eq(2)").text());

        $("#windowName").val($(this).find("td:eq(3)").text());
        $('#camId').val($(this).find("td:eq(9)").text())
        $("#windowDepartment").val($(this).find("td:eq(5)").text());
        $("#windowEvent").val($(this).find("td:eq(6)").text());


        // $("#windowNinePalaces").val(val=($(this).find("td:eq(7)").text()==="开").toString())
        $("#windowActionAnalysis").val(val=($(this).find("td:eq(7)").text()==="开").toString())
        $("#windowCoordinates").val($(this).find("td:eq(8)").text());
        // $("#formCaseAreaLs").val($(this).find("td:eq(9)").text());
        // $("#adjSelectArea").val($(this).find("td:eq(12)").text());
        // $("#otherId").val($(this).find("td:eq(13)").text());
    });

}

//表格选择框操作
function checkBoxStyle_Control()
{
    $(".lsCheck").prop("checked", false);
    $("#checkall").prop("checked", false);
//表格头部复选框点击事件
    $("#checkall").unbind("#checkall").bind("click", function(){
        if($(this).is(":checked")){
            $(".lsCheck").prop("checked", true);
        }else{
            $(".lsCheck").prop("checked", false);
        }
    });

//表格主体复选框点击事件
    $(".lsCheck").unbind(".lsCheck").bind("click", function(){
        var allCheckNum = $(".lsCheck").length;
        var checkedNum = $(".lsCheck:checked").length;
        if (allCheckNum == checkedNum) {
            $("#checkall").prop("checked", true);
        } else if(checkedNum == 0){
            $("#checkall").prop("checked", false);
        } else if(checkedNum > 0){
            $("#checkall").prop("checked", true);
        }
    });
}
//页面数据合法性验证
function validateData(isAdd) {
    if($("#windowId").val().trim()==""){
        layer.alert("窗口ID不能为空！");
        return;
    }
    if($("#windowName").val().trim()==""){
        layer.alert("窗口名称不能为空！");
        return;
    }

    if($("#windowActionAnalysis").val().trim()==""){
        layer.alert("请选择行为分析开启或关闭！");
        return;
    }

    var trs = $("#itemResultTable tr:gt(0)");
    var chooseName = $("#windowName").val();
    var chooseId = $("#windowId").val();
    var isExit = false;

//循环列表判断是否已经存在,放在客户端校验
    trs.each(function(index,element){
        var objLs = $(element).find("td:eq(0)>input").val();
        if($(element).find("td:eq(3)").text() == chooseName){
            if(isAdd){
                isExit=true;
                layer.alert("该窗口名称已存在！");
                return false;
            }else{
                if(objLs!=dataId){
                    isExit=true;
                    layer.alert("该窗口名称已存在！");
                    return false;
                }
            }
        }
        if($(element).find("td:eq(2)").text() == chooseId){
            if(isAdd){
                isExit=true;
                layer.alert("该窗口ID已存在！");
                return false;
            }else{
                if(objLs!=dataId){
                    isExit=true;
                    layer.alert("该窗口ID已存在！");
                    return false;
                }
            }
        }
    });

    if(isExit){
        return false;
    }

    return true;
}
//清除数据
function clearData(){


    $("#windowId").val("");
    $("#windowName").val("");
    $("#windowDepartment").val("")
    $("#windowEvent").val("")
    $("#floorName").val("");
    $("#windowActionAnalysis").val("");
    $("#windowNinePalaces").val("");
    $("#camId").val("");

    // $("#txtDeptId").val("");
    // $("#otherId").val("");
}

function clearSearch(){
    $('#inputCommitWindowId').val("");
    $('#inputCommitWindowName').val("");
    $('#inputCommitWindowDepartment').val("");
}

function addClick() {
    $(document).on('click','#addCommit',function(){
        if(!validateData(true))
        {
            return;
        }
        var formData = new FormData();
        formData.append("windowId", $('#windowId').val());
        formData.append("windowName",$("#windowName").val());
        formData.append("windowDepartment",$("#windowDepartment").val());
        formData.append("windowEvent",$("#windowEvent").val());
        formData.append("camHiddenId",$("#camId").val());
        // formData.append("floorHiddenId",$("#floorName").val());
        formData.append("windowActionAnalysis",$("#windowActionAnalysis").val());
        formData.append("windowNinePalaces",$("#windowNinePalaces").val());
        formData.append("windowCoordinates",$("#windowCoordinates").val());

        var rootPath = getWebRootPath();
        var url=rootPath+"/window/insert";
        $.ajax({
            type: 'POST',
            url:  url,
            cache: false,
            processData : false, // 使数据不做处理
            contentType : false, // 不要设置Content-Type请求头
            data: formData,
            success : function(data) {
                console.log(data)
                if (data.result == "error") {
                    layer.alert("错误！"+data.msg);
                    return;
                }
                if (data.result == "ok") {
                    layer.alert("新增成功！");
                } else if (data.result == "no") {
                    layer.alert("新增失败！"+data.msg);
                }
                table.draw(false);
                clearData();
            }
        });
    });
}
function updateCoord(Coord){
    var formData = new FormData();
    formData.append("windowHiddenId", dataId);
    formData.append("windowCoordinates",Coord);
    var rootPath = getWebRootPath();
    var url = rootPath + "/window/updateCoordinates";
    $.ajax({
        url: url,
        type: "post",
        datatype: "json",
        processData : false, // 使数据不做处理
        contentType : false, // 不要设置Content-Type请求头
        data: formData,
        success: function (data) {
            if (data.result == "error") {
                layer.alert("错误！"+data.msg);
                return;
            }
            if (data.result == "ok") {
                layer.msg("坐标修改成功！");
            } else if (data.result == "no") {
                layer.msg("坐标修改失败！"+data.msg);
            }
            table.draw(false);
            clearData();
        }
    });

}
function updateClick() {
//为修改绑定点击事件
    $(document).on('click', '#modifyCommit', function () {
        if (dataId=='') {
            layer.alert("请选择要修改的数据！");
            return;
        }
        if(!validateData(false))
        {
            return;
        }
        var formData = new FormData();
        formData.append("windowHiddenId", dataId);
        formData.append("windowId", $('#windowId').val());
        formData.append("windowName",$("#windowName").val());
        formData.append("windowDepartment",$("#windowDepartment").val());
        formData.append("windowEvent",$("#windowEvent").val());
        // formData.append("floorHiddenId",$("#floorName").val());
        formData.append("windowActionAnalysis",$("#windowActionAnalysis").val());
        formData.append("windowNinePalaces",$("#windowNinePalaces").val());
        formData.append("camHiddenId",$("#camId").val());
        formData.append("windowCoordinates",$("#windowCoordinates").val());

        var rootPath = getWebRootPath();
        var url = rootPath + "/window/update";

        $.ajax({
            url: url,
            type: "post",
            datatype: "json",
            processData : false, // 使数据不做处理
            contentType : false, // 不要设置Content-Type请求头
            data: formData,
            success: function (data) {
                if (data.result == "error") {
                    layer.alert("错误！"+data.msg);
                    return;
                }
                if (data.result == "ok") {
                    layer.alert("修改成功！");
                } else if (data.result == "no") {
                    layer.alert("修改失败！"+data.msg);
                }
                table.draw(false);
                clearData();
            }
        });
    });//修改事件处理完毕
}
function deleteClick() {
//为删除按钮绑定点击事件
    $(document).on('click','#deleteCommit',function() {
        var rootPath = getWebRootPath();
        var url=rootPath+"/window/delete";
        var items = new Array();
        var cBox = $("[name=choice]:checked");
        if (cBox.length == 0) {
            layer.alert("请勾选您所要删除的数据！");
            return;
        }
        layer.confirm("您确定要" +"删除" + "这" + cBox.length+ "条记录吗？",
            {
                btn : [ '确定', '取消' ]
            },
            function() {
                for (var i = 0; i < cBox.length; i++) {
                    items.push(cBox.eq(i).val());
                }
                var data = {"deleteId" : items.toString()};

                $.ajax({
                    type : 'POST',
                    url : url,
                    data : data,
                    success : function(data) {
                        if (data.result == "error") {
                            layer.alert("服务器错误！删除失败"+data.msg);
                            return;
                        }
                        if (data.result == "ok") {
                            layer.alert("删除成功！");
                        }
                        table.draw(false);
                        clearData();
                    },
                    error : function(data){
                        layer.alert("错误！");
                    }
                });
            }, function() {

            });
    });
}
function searchClick()
{

    //为查询按钮绑定点击事件
    $(document).on('click','#queCommit',function(){
        isSearch="1";
        table.draw(false);

    });

}
function clearClick()
{
//清除
    $(document).on('click','#clearData',function(){
        clearData();
    });
}
function drawWindow()
{
//更多配置

    $(document).on('click','#drawWindow',function(){
        var data = {"camHiddenId":selectInfo.camHiddenId}
        var rootPath = getWebRootPath();
        var url=rootPath+"/camera/getCurrentPic";
        $.ajax({
            type : 'POST',
            url : url,
            data : data,
            success:function (data){
                if(data.result=='ok'){
                    var path = rootPath+data.path
                    window.imgPath = path
                    window.windowId = selectInfo.windowId
                    window.windowCoordinates = selectInfo.windowCoordinates
                    window.windowHiddenId = selectInfo.windowHiddenId
                    layer.open({
                        type: 2,
                        title: false,
                        area: ['720px','445px'],
                        // skin: 'layui-layer-nobg', //没有背景色
                        shadeClose: true,
                        content: rootPath+'/page/manage/BR/drawRect.jsp'
                    });
                }else {
                    layer.alert(data.msg+",请重新检查绑定摄像头！")
                }
            }
        })

    });
}
function setCoordinate(coord){
    selectInfo["windowCoordinates"] = coord;
    $("#windowCoordinates").val(coord);
    updateCoord(coord)
}



function caseAreaClick()
{   var rootPath = getWebRootPath();
//字典类型管理按钮
    $(document).on('click','#caseArea_administration',function(){
        var targetUrl = rootPath + "/page/manage/dept/Queue_CaseArea.jsp";
        var argTitle="办事区域管理";
        openwindowNoRefresh(targetUrl,argTitle,1020,480);
    });
}
function selectAreaClick()
{   var rootPath = getWebRootPath();
//字典类型管理按钮
    $(document).on('click','#selectArea_administration',function(){
        var targetUrl = rootPath + "/page/manage/dept/Queue_SelectArea.jsp";
        var argTitle="行政区域管理";
        openwindowNoRefresh(targetUrl,argTitle,1020,480);
    });
}
