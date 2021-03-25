$(document).ready(function () {

    addClick();
    updateClick();
    searchClick();

    deleteClick();
    clearClick();

    // dicionaryTypeClick();
    init_areaInfo();
    init_dept();
    getDutys();
    user_ImageEvent();

    generate_account();//生成账号处理
    userMatterConfig();//用户关联叫号事项
});

function init_areaInfo() {
    $("#Area_Ls").val(defaultAreaLs);
    $("#Area_Name").val(defaultAreaName);

}
function init_dept()
{
    var rootPath=getWebRootPath();
    var serverUrl=rootPath+"/QueueDept/listall";
    var params;
    params={status:1,areaLs:defaultAreaLs};
    $.ajax({
        type: 'POST',
        url: serverUrl,
        cache: false,
        async: true,
        dataType: 'json',
        data:params,
        success: function (resule) {
            var resultList = resule.returnObject;//树节点数组
            var deptStr= "";
            if(resultList.length>0) {

                for (var index = 0; index < resultList.length; index++) {
                    deptStr += "<option value='" + resultList[index].deptLs + "'>" + resultList[index].deptName + "</option>";
                }
            }

            //将DOM对象设置到节点中
            $("#belongDept").empty();//先清空
            $("#belongDept").append(deptStr);//再插入
            $("#selectCommitOffice").empty();//先清空\
            var firstOptioin="<option value='-1'>请选择委办局</option>";
            $("#selectCommitOffice").append(firstOptioin);
            $("#selectCommitOffice").append(deptStr);//再插入


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
        $("#txtUserInfoLs").val(dataId);
        $("#belongDept").val($(this).find("td:eq(0) input[name='deptLs']").val());
        $("#txtUserInfoName").val($(this).find("td:eq(3)").text());
        $("#txtUserInfoId").val($(this).find("td:eq(0) input[name='userinfoId']").val());
        $("#txtUserInfoCode").val($(this).find("td:eq(4)").text());
        $("#txtUserInfoBirth").val($(this).find("td:eq(6)").text());
        /* $("#txtUserInfoAge").val($(this).find("td:eq(7)").text()); */
        $("#userInfoPosition").val($(this).find("td:eq(0) input[name='userInfoPosition']").val());
        $("#txtUserInfoTel").val($(this).find("td:eq(8)").text());
        $("#txtUserInfoMail").val($(this).find("td:eq(9)").text());
        //$("#workGuideFileName").val($(this).find("td:eq(0) input[name='userInfoImagePath']").val());
        $("#workGuideFileName").val($(this).find("td:eq(14)").text());
        var userInfoSex=$(this).find("td:eq(0) input[name='userInfoSex']").val()
        if(userInfoSex=="1"){
            $("#yes").iCheck('check');
            $("#no").iCheck('uncheck');
        }else{
            $("#yes").iCheck('uncheck');
            $("#no").iCheck('check');
        }
        var userInfoIsTheParty=$(this).find("td:eq(0) input[name='userInfoIsTheParty']").val()
        if(userInfoIsTheParty=="1"){
            $("#partyYes").iCheck('check');
            $("#partyNo").iCheck('uncheck');
        }else{
            $("#partyYes").iCheck('uncheck');
            $("#partyNo").iCheck('check');
        }
        var userInfoIsMatter=$(this).find("td:eq(0) input[name='userInfoIsMatter']").val()
        if(userInfoIsMatter=="1"){
            $("#matterYes").iCheck('check');
            $("#matterNo").iCheck('uncheck');
        }else{
            $("#matterYes").iCheck('uncheck');
            $("#matterNo").iCheck('check');
        }

        userInfoLs = $(this).find("td:eq(0) input[type='checkbox']").val();
        userName = $(this).find("td:eq(3)").text();
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

//清除数据
function clearData(){

    var $file = $("#workGuide");
    $file.after($file.clone().val(""));
    $file.remove();
    $("#belongDept").val("");
    $("#txtUserInfoLs").val("");
    $("#txtUserInfoName").val("");
    $("#txtUserInfoId").val("");
    $("#txtUserInfoCode").val("");
    //所属项目
    $("#txtAreaName").val("");
    $("#txtAreaLs").val("");
    $("#txtUserInfoBirth").val("");
    //$("#txtUserInfoAge").val("");
    $("#userInfoPosition").val("");
    $("#txtUserInfoTel").val("");
    $("#txtUserInfoMail").val("");
    $("#workGuideFileName").val("");
    $("#yes").iCheck('check');
    $("#no").iCheck('uncheck');
}

function addClick() {
    $(document).on('click','#addItem',function(){
        if (!validateForm(true)) {
            return;
        }
        var formData = new FormData();
        //特殊业务，新增时将是否取号和是否预约的值设置
        formData.append("uploadinfo", $('#workGuide')[0].files[0]);
        formData.append("areaLs",$("#Area_Ls").val());
        formData.append("deptLs",$("#belongDept").val());
        formData.append("userinfoName",$("#txtUserInfoName").val());
        formData.append("userinfoId",$("#txtUserInfoId").val());
        formData.append("userinfoCode", $("#txtUserInfoCode").val());
        formData.append("userinfoSex",$('input[name="queueUserInfo.userInfoSex"]:checked').val());
        formData.append("userinfoBirth", $("#txtUserInfoBirth").val());
        formData.append("userinfoPosition", $("#userInfoPosition").val());
        formData.append("userinfoTel", $("#txtUserInfoTel").val());
        // formData.append("matterIsshow", $('input[name="Matter_IsShow"]:checked').val());
        formData.append("userinfoMail", $("#txtUserInfoMail").val());
        formData.append("userinfoIstheparty",$('input[name="queueUserInfo.userInfoIsTheParty"]:checked').val());
        formData.append("userinfoIsmatter",$('input[name="queueUserInfo.userInfoIsMatter"]:checked').val());


// "dictionaryLs" : dataId};
        var rootPath = getWebRootPath();
        var url=rootPath+"/QueueUserinfo/insert";
        $.ajax({
            type: 'POST',
            url:  url,
            cache: false,
            dataType : "json",
            processData : false, // 使数据不做处理
            contentType : false, // 不要设置Content-Type请求头
            data : formData,
            success : function(data) {
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
    $(document).on('click', '#modifyItem', function () {
        if (dataId=='') {
            layer.alert("请选择要修改的数据！");
            return;
        }

//若效验通过，则获取表单数据，发送修改请求
        if (!validateForm(false)) {
            return;
        }
        var formData = new FormData();
        //特殊业务，新增时将是否取号和是否预约的值设置
        formData.append("uploadinfo", $('#workGuide')[0].files[0]);
        formData.append("areaLs",$("#Area_Ls").val());
        formData.append("deptLs",$("#belongDept").val());
        formData.append("userinfoName",$("#txtUserInfoName").val());
        formData.append("userinfoId",$("#txtUserInfoId").val());
        formData.append("userinfoCode", $("#txtUserInfoCode").val());
        formData.append("userinfoSex",$('input[name="queueUserInfo.userInfoSex"]:checked').val());
        formData.append("userinfoBirth", $("#txtUserInfoBirth").val());
        formData.append("userinfoPosition", $("#userInfoPosition").val());
        formData.append("userinfoTel", $("#txtUserInfoTel").val());
        // formData.append("matterIsshow", $('input[name="Matter_IsShow"]:checked').val());
        formData.append("userinfoMail", $("#txtUserInfoMail").val());
        formData.append("userinfoIstheparty",$('input[name="queueUserInfo.userInfoIsTheParty"]:checked').val());
        formData.append("userinfoIsmatter",$('input[name="queueUserInfo.userInfoIsMatter"]:checked').val());
        formData.append("userinfoLs",dataId);


        var rootPath = getWebRootPath();
        var url = rootPath + "/QueueUserinfo/update";

        $.ajax({
            url: url,
            type: "post",
            dataType : "json",
            processData : false, // 使数据不做处理
            contentType : false, // 不要设置Content-Type请求头
            data : formData,
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
    $(document).on('click','#deleteItem',function() {
        var rootPath = getWebRootPath();
        var url=rootPath+"/QueueUserinfo/delete";
        var items = new Array();
        var title = "删除";
        var cBox = $("[name=choice]:checked");
        if (cBox.length == 0) {
            layer.alert("请勾选您所要删除的数据！");
            return;
        }
        layer.confirm("您确定要" + title + "这" + cBox.length+ "条记录吗？",
            {
                btn : [ '确定', '取消' ]
            },
            function() {
                for (var i = 0; i < cBox.length; i++) {
                    items.push(cBox.eq(i).val());
                }
                var data = {"deleteLss" : items.toString()};

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
    $(document).on('click','#index_select',function(){
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



//获取职务列表
function getDutys()
{
    var rootPath=getWebRootPath();
    var serverUrl=rootPath+"/QueueDictionary/listbytype?dictionarytypeId=UserInfo_Position";
    $.ajax({
        type:'POST',
        url:serverUrl,
        cache:false,
        async:true,
        dataType: 'json',
        success: function(data){
            var dutyStr="<option value=''></option>";
            var resultList = data.returnObject;//树节点数组
            if(resultList.length>0) {

                for (var index = 0; index < resultList.length; index++) {
                    dutyStr += "<option value='" + resultList[index].dictionaryLs + "'>" + resultList[index].dictionaryName + "</option>";
                }
                $("#userInfoPosition").empty();//先清空
                $("#userInfoPosition").append(dutyStr);//再插入
            }
        }
    });
}

//头像图标操作
function user_ImageEvent()
{
    $(document).on('click', '#btnChooseWorkGuide', function() {
        $("#workGuide").click();
    });
    //为文件组合框绑定值改变事件
    $(document).on('change', '#workGuide', function() {
        var arrs = $(this).val().split('\\');
        var filename = arrs[arrs.length - 1];
        $("#workGuideFileName").val(filename);
    });
}

function validateForm(isAdd){
    //判断是否有重复的
    var $trs = $("#itemResultTable tr:gt(0)");
    var selectId = $("#txtUserInfoId").val();
    var selectCode = $("#txtUserInfoCode").val();
    var selectLs = $("#txtUserInfoLs").val();
    var isExit = false;

    var reg= /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
    if($("#belongDept").val()==null || $("#belongDept").val()=='' || $("#belongDept").val()=='0'){
        layer.alert("所属委办局不能为空！")
        return false;
    }
    if($("#txtUserInfoName").val().trim()==""){
        layer.alert("人员姓名不能为空！")
        return false;
    }
    if($("#txtUserInfoId").val().trim()==""){
        layer.alert("人员编号不能为空！")
        return false;
    }
    if($("#txtUserInfoCode").val().trim()==""){
        layer.alert("工号不能为空！")
        return false;
    }
    /* if($("#txtUserInfoBirth").val().trim()==""){
        alert("出生年月不能为空！")
        return false;
    }
    if($("#txtUserInfoTel").val().trim()==""){
        alert("手机号码不能为空！")
        return false;
    } */
    var mail = $("#txtUserInfoMail").val();
    if(mail!=""){
        if (reg.test(mail)!=true){
            layer.alert("邮箱不正确！")
            return false;
        }
    }
    //循环列表判断是否已经存在
    $trs.each(function(index,element){
        var windowExeLs = $(element).find("td:eq(0)>input").val();
        var rowId=$(element).find("td:eq(0) input[name='userInfoId']").val();
        if( rowId== selectId){

            if(isAdd){
                isExit=true;
                layer.alert("该人员编号已存在！");
                return false;
            }else{
                if(windowExeLs!=selectLs){
                    isExit=true;
                    layer.alert("该人员编号已存在！");
                    return false;
                }
            }
        }
        if($(element).find("td:eq(4)").text() == selectCode){
            if(isAdd){
                isExit=true;
                layer.alert("该工号已存在！");
                return false;
            }else{
                if(windowExeLs!=selectLs){
                    isExit=true;
                    layer.alert("该工号已存在！");
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
function generate_account()
{
    $(document).on('click','#createAccount',function(){
        var rootPath = getWebRootPath();
        var url=rootPath+"/QueueUserinfo/createwebaccount";
        var items = new Array();
        var $cBox = $("[name=choice]:checked");
        if($cBox.length == 0){
            layer.alert("请勾选您所要生成账号的用户！");
            return;
        }
        //循环获取每个事项的流水号
        for(var i=0;i<$cBox.length;i++){
            var webAccount = $cBox.eq(i).closest('tr').find("td:eq(0)>input[name='webAccountLs']").val();
            var userName = $cBox.eq(i).closest('tr').children().eq(3).text();
            if(webAccount !='null' && webAccount!='0'){
                layer.alert("您勾选的用户"+userName+"已经存在账号了，请不要重复生成！");
                return false;
            }
            items.push($cBox.eq(i).val());
        }
        layer.confirm("你确定要生成账号吗?", {
            btn: ['确定','取消']
        }, function(){
            $.ajax({
                type:'POST',
                url:url,
                cache:false,
                dataType: 'json',
                data:{"deleteLss":items.toString()},
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                success: function(data){
                    if (data.result == "error") {
                        layer.alert("服务器错误！");
                        return;
                    }
                    if (data.result == "ok") {
                        layer.alert("生成成功！");
                    } else if (data.result == "no") {
                        layer.alert("生成失败！");
                    }
                    table.draw(false);
                    clearData();
                }
            });
        });
    });
}

function userMatterConfig()
{
    //为按钮《叫号事项》绑定事件
    $("#userMatterConfig").click(function(){
        var rootPath = getWebRootPath();
        argTitle = "人员关联叫号事项["+userName+"]";
        if(userInfoLs==""||userInfoLs==null){
            layer.alert("请选择一条人员信息再进行配置！");
            return false;
        }
        var length=$("input[name='choice']:checked").length;
        if(length>1){
            layer.alert("只能选择一条数据记录！");
            return false;
        }
        var argUrl= rootPath + "/page/manage/employee/Queue_UserMatter.jsp";
        var targetUrl=argUrl+"?userinfoLs="+userInfoLs;
        openwindowNoRefresh(targetUrl,argTitle,1020,460);

    });
}