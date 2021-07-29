$(document).ready(function () {

    addClick();
    updateClick();
    deleteClick();
    searchClick();
    clearClick();
    startClick();
    stopClick();
    // configClick();
    // init_areaInfo();
    // queryCaseAreaList();
    // querySelectAreaList();

    // icon_operate();//部门图标处理

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
function queryCaseAreaList()
{

    var rootPath = getWebRootPath();
    var url=rootPath+"/QueueCasearea/listall"
//获取问卷调查
    $.ajax({
        type: 'POST',
        url: url,
        cache: false,
        async: true,
        dataType: 'json',
        success: function (data) {
//调用成功时对返回的值进行解析

            var list = data.returnObject;
//若未出错，则获取信息设置到控件中
            var str ="";
            for (var i = 0; i < list.length; i++) {
                str += "<option value='" + list[i].caseareaLs + "'>" + list[i].caseareaName + "</option>";
            }
            $("#formCaseAreaLs").empty();
            $("#formCaseAreaLs").append(str);
            $("#selectCaseLs").empty();
            $("#selectCaseLs").append("<option value=''>请选择办事区域</option>");
            $("#selectCaseLs").append(str);
        }
    });
}
function querySelectAreaList()
{

    var rootPath = getWebRootPath();
    var url=rootPath+"/QueueSelectarea/listall"
    $.ajax({
        type: 'POST',
        url: url,
        cache: false,
        async: true,
        dataType: 'json',
        success: function (data) {
//调用成功时对返回的值进行解析
            var list = data.returnObject;
//若未出错，则获取信息设置到控件中
            var str = "<option value=''></option>";

            for (var i = 0; i < list.length; i++) {
                str += "<option value='" + list[i].selectareaLs + "'>" + list[i].selectareaName + "</option>";
            }
            //将节点插入
            $("#adjSelectArea").empty();
            $("#adjSelectArea").append(str);
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
        $("#txtDeptLs").val(dataId);
        $("#serverName").val($(this).find("td:eq(2)").text());
        console.log($(this).find("td:eq(2)").text())
        $("#serverId").val($(this).find("td:eq(3)").text());
        $("#serverIp").val($(this).find("td:eq(4)").text());
        $("#serverPort").val($(this).find("td:eq(5)").text());
        $("#txtDeptMaxappointment").val($(this).find("td:eq(6)").text());
        var tempValue=$(this).find("td:eq(0) input[name='deptImagepath']").val();

        if(tempValue!="null" && tempValue!='' && typeof(tempValue)!='undefined')
        {
            $("#departmentIconFileName").val(tempValue);
        }
        $("#txtDeptId").val($(this).find("td:eq(8)").text());
        $("#formCaseAreaLs").val($(this).find("td:eq(9)").text());
        $("#adjSelectArea").val($(this).find("td:eq(12)").text());
        $("#otherId").val($(this).find("td:eq(13)").text());
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
    if($("#serverName").val().trim()==""){
        layer.alert("服务器名称不能为空！");
        return;
    }
    if($("#serverIp").val().trim()==""){
        layer.alert("服务器IP不能为空！");
        return;
    }

    if($("#serverPort").val().trim()==""){
        layer.alert("服务器端口不能为空！");
        return;
    }
    if($("#serverId").val().trim()==""){
        layer.alert("服务器ID不能为空！");
        return;
    }
    var trs = $("#itemResultTable tr:gt(0)");
    // var chooseName = $("#txtDeptName").val();
    var chooseId = $("#serverId").val();
    var isExit = false;

//循环列表判断是否已经存在,放在客户端校验
    trs.each(function(index,element){
        var objLs = $(element).find("td:eq(0)>input").val();
        // if($(element).find("td:eq(2)").text() == chooseName){
        //     if(isAdd){
        //         isExit=true;
        //         layer.alert("该委办局名称已存在！");
        //         return false;
        //     }else{
        //         if(objLs!=dataId){
        //             isExit=true;
        //             layer.alert("该委办局名称已存在！");
        //             return false;
        //         }
        //     }
        // }
        if($(element).find("td:eq(3)").text() == chooseId){
            if(isAdd){
                isExit=true;
                layer.alert("该服务器id已存在！");
                return false;
            }else{
                if(objLs!=dataId){
                    isExit=true;
                    layer.alert("该委办局编号已存在！");
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


    $("#serverName").val("");
    $("#serverIp").val("");
    $("#serverPort").val("")
    $("#serverId").val("")
    // $("#txtDeptMaxtakeno").val("");
    // $("#txtDeptMaxappointment").val("");
    // $("#departmentIconFileName").val("");
    // $("#txtDeptId").val("");
    // $("#otherId").val("");
}
function clearSearch(){
    $('#inputCommitServerId').val("");
    $('#inputCommitServerName').val("");
}
function addClick() {
    $(document).on('click','#addCommit',function(){
        if(!validateData(true))
        {
            return;
        }


        var formData = new FormData();
        formData.append("serverName", $('#serverName').val());
        formData.append("serverIp",$("#serverIp").val());
        formData.append("serverPort",$("#serverPort").val());
        formData.append("serverId",$("#serverId").val());
        // formData.append("deptPrint",$("#serverId").val());
        var rootPath = getWebRootPath();
        var url=rootPath+"/server/insert";
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
        if (dataId=='') {
            layer.alert("请选择要修改的数据！");
            return;
        }
        if(!validateData(false))
        {
            return;
        }
        var formData = new FormData();
        formData.append("serverName", $('#serverName').val());
        formData.append("serverIp",$("#serverIp").val());
        formData.append("serverPort",$("#serverPort").val());
        formData.append("serverId",$("#serverId").val());
        formData.append("serverHiddenId",dataId)
        console.log(formData)

        var rootPath = getWebRootPath();
        var url = rootPath + "/server/update";

        $.ajax({
            url: url,
            type: "post",
            datatype: "json",
            processData : false, // 使数据不做处理
            contentType : false, // 不要设置Content-Type请求头
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
function startClick(){
    $('#startCommit').click(function (){
        var rootPath = getWebRootPath();
        var url=rootPath+"/server/batchStartServers";
        var items = new Array();
        var cBox = $("[name=choice]:checked");
        console.log(dataId)
        if (cBox.length == 0&&dataId=='') {
            layer.alert("请勾选您所要启动的服务器！");
            return;
        }
        var total_len = 0;

        if (cBox.length>0){
            for (var i = 0; i < cBox.length; i++) {
                items.push(cBox.eq(i).val());
            }
            total_len = cBox.length;
        }else {
            items.push(dataId);
            total_len = 1;
        }
        var data = {"startId" : items.toString()};
        layer.confirm("您确定要" +"启动" + "这" + total_len+ "台服务器吗？",
            {
                btn : [ '确定', '取消' ]
            },
            function() {


                $.ajax({
                    type : 'POST',
                    url : url,
                    data : data,
                    success : function(data) {
                        if (data.result == "error") {
                            layer.alert(data.msg,{time:3});
                            return;
                        }
                        if (data.result == "ok") {
                            layer.msg(data.msg,{time:3});
                        }
                        table.draw(false);
                        // clearData();
                    },
                    error : function(data){
                        layer.alert("后台错误！");
                    }
                });
            }, function() {

            });
    })
}
function stopClick(){
    $('#stopCommit').click(function (){
        var rootPath = getWebRootPath();
        var url=rootPath+"/server/batchStopServers";
        var items = new Array();
        var cBox = $("[name=choice]:checked");
        if (cBox.length == 0&& dataId=="") {
            layer.alert("请勾选您所要启动的服务器！");
            return;
        }

        var total_len = 0;

        if (cBox.length>0){
            for (var i = 0; i < cBox.length; i++) {
                items.push(cBox.eq(i).val());
            }
            total_len = cBox.length;
        }else {
            items.push(dataId);
            total_len = 1;
        }
        var data = {"stopId" : items.toString()};
        layer.confirm("您确定要" +"停止" + "这" + total_len+ "台服务器吗？",
            {
                btn : [ '确定', '取消' ]
            },
            function() {

                $.ajax({
                    type : 'POST',
                    url : url,
                    data : data,
                    success : function(data) {
                        if (data.result == "error") {
                            layer.alert(data.msg);
                            return;
                        }
                        if (data.result == "ok") {
                            layer.msg(data.msg);
                        }
                        table.draw(false);
                        // clearData();
                    },
                    error : function(data){
                        layer.alert("后台错误！");
                    }
                });
            }, function() {

            });
    })
}
function deleteClick() {
//为删除按钮绑定点击事件
    $(document).on('click','#deleteCommit',function() {
        var rootPath = getWebRootPath();
        var url=rootPath+"/server/delete";
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
                            layer.alert("服务器错误！删除失败");
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
        clearSearch();
    });

}
function clearClick()
{
//清除
    $(document).on('click','#clearData',function(){
        clearData();
    });
}
function configClick()
{
//更多配置
    $(document).on('click','#configWindow',function(){
        var btn=document.getElementById("configWindow");
        var btnV = btn.innerHTML;
        if(btnV=="显示配置按钮"){
            $("#configTr").slideDown();
            btn.innerHTML="隐藏配置按钮";
        }else{
            $("#configTr").slideUp();
            btn.innerHTML="显示配置按钮";
        }

    });
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
