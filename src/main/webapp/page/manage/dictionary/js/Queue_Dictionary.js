$(document).ready(function () {
    getDictionaryType();
    addClick();
    updateClick();
    searchClick();
    clearClick();
    configClick();
    dicionaryTypeClick();
    init_areaInfo();
});

function init_areaInfo() {
    $("#Area_Ls").val(defaultAreaLs);
    $("#Area_Name").val(defaultAreaName);

}
function getDictionaryType()
{
    var rootPath = getWebRootPath();
    var url=rootPath+"/QueueDictionarytype/listall"
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
            var str = "<option value=''>请选择字典类型</option>";
            for (var i = 0; i < list.length; i++) {
                str += "<option value='" + list[i].dictionarytypeLs + "'>" + list[i].dictionarytypeName + "</option>";
            }
            //将节点插入
            $("#dictionaries_type").empty();
            $("#dictionaries_type").append(str);

            $("#dictionaries_type_query").empty();
            $("#dictionaries_type_query").append(str);
        }
    });
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
        var areaLs = $(this).find("td:eq(13)").text();
        dataId = $(this).find("td:eq(0) input[type='checkbox']").val();
        $("#dictionaries_name").val($(this).find("td:eq(3)").text());
        $("#dictionaries_type").val($(this).find("td:eq(14)").text());
        $("#dictionaries_id").val($(this).find("td:eq(12)").text());
        if($(this).find("td:eq(4)").text()=="是"){
            $("#yes").iCheck('check');
            $("#no").iCheck('uncheck');
        }else{
            $("#yes").iCheck('uncheck');
            $("#no").iCheck('check');
        }
        $("#dictionaries_parameter_one").val($(this).find("td:eq(5)").text());
        $("#dictionaries_parameter_two").val($(this).find("td:eq(6)").text());
        $("#dictionaries_parameter_three").val($(this).find("td:eq(7)").text());
        $("#dictionaries_parameter_four").val($(this).find("td:eq(8)").text());
        $("#dictionaries_parameter_five").val($(this).find("td:eq(9)").text());
        $("#dictionaries_parameter_six").val($(this).find("td:eq(10)").text());
        $("#dictionaries_parameter_seven").val($(this).find("td:eq(11)").text());

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
    if($("#dictionaries_name").val().trim() == ""){
        layer.alert("字典名称不能为空！");
        return;
    }
    if($("#dictionaries_type").val().trim() == ""){
        layer.alert("字典类型不能为空！");
        return;
    }

    if($("#dictionaries_id").val().trim() == ""){
        layer.alert("字典编号不能为空！");
        return;
    }

    if($("#regionId").val()==""){
        layer.alert("请先设置默认项目！");
        return;
    }
    var $trs = $("#example1 tr:gt(0)");
    var chooseName = $("#dictionaries_name").val();
    var chooseId = $("#dictionaries_id").val();
    var isExit = false;

    //循环列表判断是否已经存在
    $trs.each(function(index,element){
        var objLs = $(element).find("td:eq(0)>input").val();
        if($(element).find("td:eq(3)").text() == chooseName){
            if(isAdd){
                isExit=true;
                layer.alert("该字典名称已存在！");
                return false;
            }else{
                if(objLs!=dataId){
                    isExit=true;
                    layer.alert("该字典名称已存在！");
                    return false;
                }
            }
        }
        if($(element).find("td:eq(12)").text() == chooseId){
            if(isAdd){
                isExit=true;
                layer.alert("该字典编号已存在！");
                return false;
            }else{
                if(objLs!=dataId){
                    isExit=true;
                    layer.alert("该字典编号已存在！");
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

    var dictionaries_name = document.getElementById("dictionaries_name");
    var dictionaries_type = document.getElementById("dictionaries_type");
    var dictionaries_id = document.getElementById("dictionaries_id");
    var value='';
    var dictionaries_parameter_one = document.getElementById("dictionaries_parameter_one");
    var dictionaries_parameter_two = document.getElementById("dictionaries_parameter_two");
    var dictionaries_parameter_three = document.getElementById("dictionaries_parameter_three");
    var dictionaries_parameter_four = document.getElementById("dictionaries_parameter_four");
    var dictionaries_parameter_five = document.getElementById("dictionaries_parameter_five");
    var dictionaries_parameter_six = document.getElementById("dictionaries_parameter_six");
    var dictionaries_parameter_seven = document.getElementById("dictionaries_parameter_seven");
    var inputs = new Array(
        dictionaries_name,
        value,
        dictionaries_parameter_one,
        dictionaries_parameter_two,
        dictionaries_parameter_three,
        dictionaries_parameter_four,
        dictionaries_parameter_five,
        dictionaries_parameter_six,
        dictionaries_parameter_seven,
        dictionaries_id);
    for(var i = 3;i<=12;i++){
        if(i-3==1){
            inputs[i-3]=$(this)
                .find("td:eq("+i+")")
                .text();
        }else{
            inputs[i-3].value = "";
        }
    }

    dictionaries_type.value = "";
    $("#yes").iCheck('check');
    $("#no").iCheck('uncheck');
}

function addClick() {
    $(document).on('click','#addDataBtn',function(){
        if (!validateData(true)) {
            return;
        }
        var params = {
            "areaLs":$("#Area_Ls").val(),
            "dictionaryName": $("#dictionaries_name").val(),
            "dictionarytypeLs": $("#dictionaries_type").val(),
            "dictionaryIsshow": $('input[name="isShow"]:checked').val(),
            "dictionaryId": $("#dictionaries_id").val(),
            "dictionaryParameter1": $("#dictionaries_parameter_one").val(),
            "dictionaryParameter2": $("#dictionaries_parameter_two").val(),
            "dictionaryParameter3": $("#dictionaries_parameter_three").val(),
            "dictionaryParameter4": $("#dictionaries_parameter_four").val(),
            "dictionaryParameter5": $("#dictionaries_parameter_five").val(),
            "dictionaryParameter6": $("#dictionaries_parameter_six").val(),
            "dictionaryParameter7": $("#dictionaries_parameter_seven").val()
        }
            // "dictionaryLs" : dataId};
        var rootPath = getWebRootPath();
        var url=rootPath+"/QueueDictionary/insert";
        $.ajax({
            type: 'POST',
            url:  url,
            cache: false,
            data: params,
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
    $(document).on('click', '#updateDataBtn', function () {
        if (dataId=='') {
            layer.alert("请选择要修改的数据！");
            return;
        }

        //若效验通过，则获取表单数据，发送修改请求
        if (!validateData(false)) {
            return;
        }
        var tempIsshow= $('input[name="isShow"]:checked').val();
        console.log("tempIsshow:"+tempIsshow);
        var params = {
            "areaLs":$("#Area_Ls").val(),
            "dictionaryName": $("#dictionaries_name").val(),
            "dictionarytypeLs": $("#dictionaries_type").val(),
            "dictionaryIsshow":tempIsshow,
            "dictionaryId": $("#dictionaries_id").val(),
            "dictionaryParameter1": $("#dictionaries_parameter_one").val(),
            "dictionaryParameter2": $("#dictionaries_parameter_two").val(),
            "dictionaryParameter3": $("#dictionaries_parameter_three").val(),
            "dictionaryParameter4": $("#dictionaries_parameter_four").val(),
            "dictionaryParameter5": $("#dictionaries_parameter_five").val(),
            "dictionaryParameter6": $("#dictionaries_parameter_six").val(),
            "dictionaryParameter7": $("#dictionaries_parameter_seven").val(),
             "dictionaryLs" : dataId};

        var rootPath = getWebRootPath();
        var url = rootPath + "/QueueDictionary/update";

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

}
function searchClick()
{
    //为查询按钮绑定点击事件
    $(document).on('click','#index_select',function(){
        isSearch="1";
        table.draw();

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
function dicionaryTypeClick()
{   var rootPath = getWebRootPath();
    //字典类型管理按钮
    $(document).on('click','#dictionaryTypeConfig',function(){
        var targetUrl = rootPath + "/page/manage/dictionary/Queue_DictionaryType.jsp";
        var argTitle="字典类型管理";
        openwindowNoRefresh(targetUrl,argTitle,1020,480);
    });
}