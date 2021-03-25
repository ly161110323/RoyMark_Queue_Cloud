$(document).ready(function () {
    initHiddenParams();
    // init_areaInfo();

    addClick();
    updateClick();
    deleteClick();
    init_dept();
    mulitiselect_init();
    levelCssChange();
});
function mulitiselect_init()
{
    $('#upMatterLs').multiselect({
        header: true,
        height: 175,
        minWidth: 197,
        classes: '',
        noneSelectedText: "==请选择==",
        checkAllText: "全选",
        uncheckAllText: '全不选',
        selectedText: '# 选中',
        selectedList: 1,
        show: null,
        hide: null,
        autoOpen: false,
        multiple: true,
        position: {},
        appendTo: "body",
        menuWidth:null
    });

    $('#belongMatter').multiselect({
        header: true,
        height: 175,
        minWidth: 197,
        classes: '',
        noneSelectedText: "==请选择==",
        checkAllText: "全选",
        uncheckAllText: '全不选',
        selectedText: '# 选中',
        selectedList: 1,
        show: null,
        hide: null,
        autoOpen: false,
        multiple: true,
        position: {},
        appendTo: "body",
        menuWidth:null
    });
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
            var deptStr= "<option value=''></option>";
            if(resultList.length>0) {

                for (var index = 0; index < resultList.length; index++) {
                    deptStr += "<option value='" + resultList[index].deptLs + "'>" + resultList[index].deptName + "</option>";
                }
            }

            //将DOM对象设置到节点中
            $("#belongDept").empty();//先清空
            $("#belongDept").append(deptStr);//再插入



        }
    });
}
//改变部门获取上级事项
function changeDeptGetUpMatter(){
    //清除一级事项和二级事项
    $("#upMatterLs").empty();
    $('#upMatterLs').multiselect('refresh');
    $("#belongMatter").empty();
    $('#belongMatter').multiselect('refresh');
    //调用根据等级获取事项
    changeLevelGetUpMatter();
}
//改变事项层级激活上级事项
function changeLevelGetUpMatter(){
    var level = $("#matterLevel").val();

    levelCssChange();
    if(level =="2"){
        getOneLevelMatterList('');
        getTwoLevelMatterList('');
    }else {
        //获取1级事项
        getOneLevelMatterList('');
    }
}
function levelCssChange(){
    var level = $("#matterLevel").val();
    if(level =="2"){
        $("#belongMatter").removeAttr("disabled");
        $("#belongMatter").css({  background: "#fff" });
    }else {
        //获取1级事项
        $("#belongMatter").empty();
        $("#belongMatter").attr("disabled","disabled");
        $("#belongMatter").css({background: "#f0f0f0" });
        $("#belongMatter").val("");
        $('#belongMatter').multiselect('refresh');
    }
}
//获取选中下拉项的值
function showValues(id) {
    var values = $("#"+id).multiselect("getChecked").map(function(){
        return this.value;
    }).get();
    return values;
}
//获取一级事项列表 一级事项的取值范围 = 取号事项 +  事项层级 =1级  +  是否取号 =是
function getOneLevelMatterList(selectVal){
    var rootPath=getWebRootPath();
    var url=rootPath+"/QueueMatter/selectmatters?";
    var params = "matterIsqueue=1";
    var dept = $("#belongDept").val();
    var level = $("#matterLevel").val();
    //根据层级来取一级事项 是否取号
    if(level == "2"){
        params = params+"&matterIstakeno=0";
    }else if(level == "1"){
        params = params+"&matterIstakeno=1";
    }
    if(dept!="" && dept!=null  && dept!=undefined && dept!="undefined"){
        params = params+"&deptLs="+dept;
    }
    params = params+"&matterLevel=1";
    $.ajax({
        type:'POST',
        url:url+params,
        cache:false,
        async:false,
        dataType: 'json',
        success: function(data){
            var list = data.returnObject;
            //若未出错，则获取信息设置到控件中
            var str = "";
            for(var i = 0;i < list.length; i++){
                str += "<option value='"+list[i].matterLs+"'>"+list[i].matterName+"</option>";
            }
            //将节点插入
            $("#upMatterLs").empty();
            $("#upMatterLs").append(str);
            if(selectVal!=""){
                $("#upMatterLs").children("option").each(function(){
                    var temp_value = $(this).val();
                    if(temp_value == selectVal){
                        $(this).attr("selected","selected");
                    }
                });
            }
            $('#upMatterLs').multiselect('refresh');
        }
    });
}
//获取二级事项列表 二级事项的取值范围= 所选 一级事项 的 所有二级事项。
function getTwoLevelMatterList(selectVal){
    var rootPath=getWebRootPath();
    var url=rootPath+"/QueueMatter/selectmatters?";
    var params = "matterIsqueue=1";

    var dept = $("#belongDept").val();
    var upMatterLs = showValues("upMatterLs");//$("#upMatterLs").val();
    var level = $("#matterLevel").val();
    if(dept!="" && dept!=null  && dept!=undefined && dept!="undefined"){
        params = params+"&deptLs="+dept;
    }
    params = params+"&matterLevel=2&matterUpMatter="+upMatterLs;
    if(level == "2" && upMatterLs!="" && upMatterLs!=null){
        $.ajax({
            type:'POST',
            url:url+params,
            cache:false,
            async:false,
            dataType: 'json',
            success: function(data){
                //调用成功时对返回的值进行解析
                var list = data.returnObject;
                //若未出错，则获取信息设置到控件中
                var str = "";
                for(var i = 0;i < list.length; i++){
                    str += "<option value='"+list[i].matterLs+"'>"+list[i].matterName+"</option>";
                }
                //将节点插入
                $("#belongMatter").empty();
                $("#belongMatter").append(str);
                if(selectVal!=""){
                    $("#belongMatter").children("option").each(function(){
                        var temp_value = $(this).val();
                        if(temp_value == selectVal){
                            $(this).attr("selected","selected");
                        }
                    });
                }
                $('#belongMatter').multiselect('refresh');
            }
        });
    }
}

/**
 * 初始化隐藏参数值
 */
function initHiddenParams(){
    $("#userInfoLs").val(userInfoLs);
}

function init_areaInfo() {
    $("#Area_Ls").val(defaultAreaLs);
    $("#Area_Name").val(defaultAreaName);

}

function trClick() {
//为表格行绑定点击事件
    $('#example1 tbody').on('click', 'tr', function () {
        $("#example1 tr:even").css({
            "background": "#f9f9f9",
            "color": "#676a6c"
        });
        $("#example1 tr:odd").css({
            "background": "white",
            "color": "#676a6c"
        });
        $(this).css({
            "background" : "rgb(255, 128, 64)",
            "color": "white"
        });
        dataId = $(this).find("td:eq(0) input[type='checkbox']").val();
        var level = $(this).find("td:eq(3)").text();
        //点击其他行，先把当前文件控件内容清空
        //将当前信息设置到顶部显示belongMatter
        $("#belongDept>option[value="+$(this).find("td:eq(6)").text()+"]").prop("selected",true);
        $("#matterLevel>option[value="+level+"]").prop("selected",true);
        levelCssChange();
        if(level=='1'){
            getOneLevelMatterList($(this).find("td:eq(7)").text());
        }else if(level=='2'){
            getOneLevelMatterList($(this).find("td:eq(7)").text());
            getTwoLevelMatterList($(this).find("td:eq(8)").text());
        }
        $("#userMatterLs").val($(this).find("td:eq(0) input[type='checkbox']").val());
    });
}

function addClick() {
//为新增按钮绑定点击事件
    $(document).on('click', '#addItem', function () {
//效验表单数据
        if (!validateData(true)) {
            return;
        }
//获取表单数据，将表单以Ajax形式提交
        var params = $("#itemInfoForm").serialize();

        var rootPath = getWebRootPath();
        var url = rootPath + "/QueueUsermatter/insert";

        $.ajax({
            url: url,
            type: "post",
            datatype: "json",
            data: params,
            success: function (data) {
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
    });//新增事件处理完毕
}

function updateClick() {
//为修改绑定点击事件
    $(document).on('click', '#modifyItem', function () {
        if (dataId== '') {
            layer.alert("请选择要修改的数据！");
            return;
        }

//若效验通过，则获取表单数据，发送修改请求
        if (!validateData(false)) {
            return;
        }
        var rootPath = getWebRootPath();
        var url = rootPath + "/QueueUsermatter/update";
//获取表单数据
        var params = $("#itemInfoForm").serialize();
//若效验通过，则进行表单提交
        $.ajax({
            url: url,
            type: "post",
            datatype: "json",
            data: params,
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
//为删除绑定点击事件
    $(document).on('click', '#deleteItem', function () {
//点击删除，获取当前复选框选中的所有项
        var items = new Array();
        var cBox = $("[name=choice]:checked");
        if (cBox.length == 0) {
            layer.alert("请勾选您所要删除的数据！");
            return;
        }
        var title = "删除";
        layer.confirm("您确定要" + title + "这" + cBox.length + "条记录吗？", {
            btn: ['确定', '取消']
        }, function () {

//循环获取每个事项的流水号
            for (var i = 0; i < cBox.length; i++) {
                items.push(cBox.eq(i).val());
            }
            var rootPath = getWebRootPath();
            var url = rootPath + "/QueueUsermatter/delete";

            $.ajax({
                type: 'POST',
                url: url,
                cache: false,
                data: {"deleteLss": items.toString()},
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
                }
            });
        });
    });//删除处理完毕
}

function validateData(isAdd){
    var $trs = $("#example1 tr:gt(0)");
    var selectDept = $("#belongDept").val();
    var matterLevel = $("#matterLevel").val();
    var selectMatter = showValues("belongMatter");//$("#belongMatter").val();
    var selectUpMatter = showValues("upMatterLs");//$("#upMatterLs").val();
    var selectLs = $("#userMatterLs").val();
    var isExit = false;
    if(selectDept==null || selectDept=='' || selectDept=='0'){
        layer.alert("委办局不能为空！")
        return false;
    }
    if(matterLevel==null || matterLevel==''){
        layer.alert("事项层级不能为空！")
        return false;
    }
    if(matterLevel=='2'){
        if(selectMatter==null || selectMatter==''){
            layer.alert("二级事项不能为空！")
            return false;
        }
    }else if(matterLevel=='1'){
        if(selectUpMatter==null || selectUpMatter==''){
            layer.alert("一级事项不能为空！")
            return false;
        }
    }

    //循环列表判断是否已经存在
    $trs.each(function(index,element){
        if(matterLevel=='2'){
            if($(element).find("td:eq(6)").text() == selectDept
                &&(","+selectMatter).indexOf($(element).find("td:eq(8)").text())!=-1
                &&(","+matterLevel).indexOf($(element).find("td:eq(3)").text())!=-1){
                var userMatterLs = $(element).find("td:eq(0) input[type='checkbox']").val();
                if(isAdd){
                    isExit=true;
                    layer.alert("该信息已存在！");
                    return false;
                }else{
                    if(userMatterLs!=selectLs){
                        isExit=true;
                        layer.alert("该信息已存在！");
                        return false;
                    }
                }
            }
        }else if(matterLevel=='1'){
            //alert($(element).find("td:eq(6)").text()+"=="+selectDept +" "+((","+selectUpMatter).indexOf(","+$(element).find("td:eq(7)").text())!=-1)+
            //(","+matterLevel).indexOf(","+$(element).find("td:eq(3)").text())!=-1);
            if($(element).find("td:eq(6)").text() == selectDept
                &&(","+selectUpMatter).indexOf(","+$(element).find("td:eq(7)").text())!=-1
                &&(","+matterLevel).indexOf(","+$(element).find("td:eq(3)").text())!=-1){
                var userMatterLs = $(element).find("td:eq(0) input[type='checkbox']").val();
                if(isAdd){
                    isExit=true;
                    layer.alert("该信息已存在！");
                    return false;
                }else{
                    if(userMatterLs!=selectLs){
                        isExit=true;
                        layer.alert("该信息已存在！");
                        return false;
                    }
                }
            }
        }
    });
    if(isExit){
        return false;
    }
    return true;
}

function clearData() {

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