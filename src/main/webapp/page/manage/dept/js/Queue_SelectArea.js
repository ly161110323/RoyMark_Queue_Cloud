$(document).ready(function () {
    initHiddenParams();
    init_areaInfo();
    checkBox_init();
    addClick();
    updateClick();
    deleteClick();
});

/**
 * 初始化隐藏参数值
 */
function initHiddenParams() {
//$("#questionLs").val(questionLs);
//$("#questionNaireLs").val(questionNaireLs);
}

function init_areaInfo() {
    $("#Area_Ls").val(defaultAreaLs);
    $("#Area_Name").val(defaultAreaName);

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
        $("#selectAreaLs").val(dataId);
        $("#selectAreaName").val($(this).find("td:eq(2)").text());
        $("#selectAreaId").val($(this).find("td:eq(3)").text());
        $("#selectAreaPrint").val($(this).find("td:eq(4)").text());
        $("#selectAreaOrderNo").val($(this).find("td:eq(5)").text());
        $("#areaName").val($(this).find("td:eq(7)").text());
        $("#areaLs").val($(this).find("td:eq(6)").text());

    });
}

function addClick() {
//为新增按钮绑定点击事件
    $(document).on('click', '#addItem', function () {
//效验表单数据
        if (!validateForm(true)) {
            return;
        }
//获取表单数据，将表单以Ajax形式提交
        var params = $("#itemInfoForm").serialize();

        var rootPath = getWebRootPath();
        var url = rootPath + "/QueueSelectarea/insert";

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
        if (dataId == '') {
            layer.alert("请选择要修改的数据！");
            return;
        }

//若效验通过，则获取表单数据，发送修改请求
        if (!validateForm(false)) {
            return;
        }
        var rootPath = getWebRootPath();
        var url = rootPath + "/QueueSelectarea/update";
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
            var url = rootPath + "/QueueSelectarea/delete";

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

function validateForm(isAdd){
    //判断是否有重复的
    var $trs = $("#itemResultTable tr:gt(0)");
    var selectName = $("#selectAreaName").val();
    var selectLs = $("#selectAreaLs").val();
    var isExit = false;

    if($("#selectAreaName").val().trim()==""){
        layer.alert("区域名称不能为空！")
        return false;
    }

    if($("#selectAreaId").val().trim()==""){
        layer.alert("区域编号不能为空！")
        return false;
    }
    if($("#selectAreaOrderNo").val().trim()==""){
        layer.alert("顺序号不能为空！")
        return false;
    }
    //循环列表判断是否已经存在
    $trs.each(function(index,element){
        if($(element).find("td:eq(2)").text() == selectName){
            var objLs = $(element).find("td:eq(0)>input").val();
            if(isAdd){
                isExit=true;
                layer.alert("该名称已存在！");
                return false;
            }else{
                if(objLs!=selectLs){
                    isExit=true;
                    layer.alert("该名称已存在！");
                    return false;
                }
            }
        }
        var tempAreadId=$(element).find("td:eq(3)").text();//区域编号
        var inputAreadId=$("#selectAreaId").val();
        if(tempAreadId == inputAreadId){
            var objLs = $(element).find("td:eq(0)>input").val();
            if(isAdd){
                isExit=true;
                layer.alert("该区域编号已存在！");
                return false;
            }else{
                if(objLs!=selectLs){
                    isExit=true;
                    layer.alert("该区域编号已存在！");
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
function clearData() {
    $("#selectAreaLs").val("");
    $("#selectAreaName").val("");
    $("#selectAreaId").val("");
    $("#selectAreaPrint").val("");
    $("#selectAreaOrderNo").val("");
}
function checkBox_init()
{
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