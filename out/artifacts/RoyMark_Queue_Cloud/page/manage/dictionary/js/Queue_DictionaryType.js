$(document).ready(function () {

    clearClick();
    addClick();
    updateClick();
    searchClick();
});

function validateData(isAdd) {
    if($("#dictionaries_type_name").val().trim() == ""){
        layer.alert("字典类型名称不能为空！");
        return;
    }
    if($("#dictionaries_type_id").val()==""){
        layer.alert("字典类型编号不能为空！");
        return;
    }
    var $trs = $("#example1 tr:gt(0)");
    var chooseName = $("#dictionaries_type_name").val();
    var chooseId = $("#dictionaries_type_id").val();
    var isExit = false;

    //循环列表判断是否已经存在
    $trs.each(function(index,element){
        var objLs = $(element).find("td:eq(0)>input").val();
        if($(element).find("td:eq(2)").text() == chooseName){
            if(isAdd){
                isExit=true;
                layer.alert("该字典类型名称已存在！");
                return false;
            }else{
                if(objLs!=dataId){
                    isExit=true;
                    layer.alert("该字典类型名称已存在！");
                    return false;
                }
            }
        }
        if($(element).find("td:eq(3)").text() == chooseId){
            if(isAdd){
                isExit=true;
                layer.alert("该字典类型编号已存在！");
                return false;
            }else{
                if(objLs!=dataId){
                    isExit=true;
                    layer.alert("该字典类型编号已存在！");
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

function trClick()
{
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
        $("[name=dictionaries_type_name]").val( $(this).find("td:eq(2)").text());
        $("[name=dictionaries_type_id]").val( $(this).find("td:eq(3)").text());
        var radio=$(this).find("td:eq(4)").text();
        if(radio=='是'){
            $("#yes").iCheck('check');
            $("#no").iCheck('uncheck');
        }else{
            $("#yes").iCheck('uncheck');
            $("#no").iCheck('check');
        }
    });
}

//清除数据
function clearData(){
    $("[name=dictionaries_type_name]").val("");
    $("[name=dictionaries_type_id]").val("");
    dataId='';
}
function checkBox_init()
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

function addClick() {
    $(document).on('click', '#addDataBtn', function () {
        if (!validateData(true)) {
            return;
        }
        var params = {"dictionarytypeId" : $("#dictionaries_type_id").val(),
            "dictionarytypeName" : $("#dictionaries_type_name").val(),
            "dictionarytypeIsshow" : $("#yes").parent().hasClass("checked")?"1" :"0"};
        var rootPath = getWebRootPath();
        var url = rootPath + "/QueueDictionarytype/insert";
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

        var params = {"dictionarytypeId" : $("#dictionaries_type_id").val(),
            "dictionarytypeName" : $("#dictionaries_type_name").val(),
            "dictionarytypeLs" : dataId,
            "dictionarytypeIsshow" : $("#yes").parent().hasClass("checked")?"1" :"0"};
        var rootPath = getWebRootPath();
        var url = rootPath + "/QueueDictionarytype/update";

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

