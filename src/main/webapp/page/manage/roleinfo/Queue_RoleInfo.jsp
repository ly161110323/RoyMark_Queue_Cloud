<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/page/common/includewithnewztreestyle.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <title>禾麦智能大厅管理系统</title>
    <link rel="shortcut icon" href="${ctx}/resources/images/favicon.ico"
          type="image/x-icon"/>
    <%--ztree下拉框树相关js开始--%>

    <script type="text/javascript" src="./js/tree-select.js"></script>
    <%--ztree下拉框树相关js结束--%>
    <script type="text/javascript" src="./js/roleinfo.js"></script>
    <script type="text/javascript">
        var table;
        var dataId = "";
        var mapData = {};
        var updateName = "";
        var updateNo = "";
        var isSearch = "0";

        $(document).ready(function () {
            //加载表格
            loadTable();
            //新增
            $(document).on('click', '#addDataBtn', function () {
                if (!validateData()) {
                    return;
                }
                $.ajax({
                    url: "${ctx}/QueueRoleinfo/insert",
                    type: "post",
                    dataType: "json",
                    data: {
                        "roleCode": $("#Role_Code").val(),
                        "roleName": $("#Role_Name").val(),
                        "roleIsshow": $("input[name='Role_IsShow']:checked").val(),
                        "roleText": $("#Role_Text").val(),
                        "areaLs": $("#Area_Ls").val()
                    },
                    success: function (data) {
                        if (data.result == "error") {
                            layer.alert("服务器错误！");
                            return;
                        }
                        if (data.result == "repeat") {
                            layer.alert("该角色已存在！");
                            return;
                        }
                        if (data.result == "ok") {
                            layer.alert("新增成功！");
                        } else if (data.result == "no") {
                            layer.alert("新增失败！");
                        }
                        clearData2();
                        table.draw(false);
                    }
                });
            });

            //修改
            $(document).on('click', '#updateDataBtn', function () {
                if (!validateData()) {
                    return;
                }
                if (dataId == "") {
                    layer.alert("请选择需要修改的数据！");
                    return;
                }
                $.ajax({
                    url: "${ctx}/QueueRoleinfo/update",
                    type: "post",
                    dataType: "json",
                    data: {
                        "updateName": updateName,
                        "updateNo": updateNo,
                        "roleLs": dataId,
                        "roleCode": $("#Role_Code").val(),
                        "roleName": $("#Role_Name").val(),
                        "roleIsshow": $("input[name='Role_IsShow']:checked").val(),
                        "roleText": $("#Role_Text").val(),
                        "areaLs": $("#Area_Ls").val(),
                    },
                    success: function (data) {
                        if (data.result == "error") {
                            layer.alert("服务器错误！");
                            return;
                        }
                        if (data.result == "repeat") {
                            layer.alert("该角色已存在！");
                            return;
                        }
                        if (data.result == "ok") {
                            layer.alert("修改成功！");
                        } else if (data.result == "no") {
                            layer.alert("修改失败！");
                        }
                        clearData2();
                        table.draw(false);
                    }
                });
            });

            //为删除按钮绑定点击事件
            $(document).on('click', '#deleteDataBtn', function () {
                var items = new Array();
                var title = "删除";
                var $cBox = $("[name=choice]:checked");
                if ($cBox.length == 0) {
                    layer.alert("请勾选您所要删除的数据！");
                    return;
                }
                layer.confirm("您确定要" + title + "这" + $cBox.length + "条记录吗？",
                    {
                        btn: ['确定', '取消']
                    },
                    function () {
                        for (var i = 0; i < $cBox.length; i++) {
                            items.push($cBox.eq(i).val());
                        }
                        var data = {"deleteLss": items.toString()};
                        var url = "${ctx}/HospitalqueueRoleinfo/delete";
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
                                clearData2();
                                table.draw(false);
                            },
                            error: function (data) {
                                layer.alert("错误！");
                            }
                        });
                    }, function () {

                    });
            });

            //为查询按钮绑定点击事件
            $(document).on('click', '#index_select', function () {
                isSearch = "1";
                table.draw(true);
            });

            //更多配置
            $(document).on('click', '#configWindow', function () {
                var btn = document.getElementById("configWindow");
                var btnV = btn.innerText;
                if (btnV == "显示配置按钮") {
                    $("#configTr").slideDown();
                    btn.innerHTML = "隐藏配置按钮";
                } else {
                    $("#configTr").slideUp();
                    btn.innerHTML = "显示配置按钮";
                }

            });

            //为项目权限按钮绑定事件
            $("#caseConfig").click(function () {
                var $cBox = $("[name=choice]:checked");
                if ($cBox.length == 0 || $cBox.length > 1) {
                    layer.alert("请先选中一条角色记录！");
                    return;
                }
                var roleLs = $cBox.eq(0).val();
                var roleName = $cBox.closest('tr').children().eq(2).text();
                var argTitle = "项目权限[" + roleName + "]";
                var params = "?roleLs=" + roleLs + "&roleName=" + roleName + "&permissionsType=case";
                //参数中有中文，需要转码
                params = encodeURI(encodeURI(params));
                showFrameDialog(argTitle, "${ctx}/page/manage/roleinfo/HospitalRoleCase.jsp" + params)
            });

            //为菜单权限按钮绑定事件
            $("#menuConfig").click(function () {
                var $cBox = $("[name=choice]:checked");
                if ($cBox.length == 0 || $cBox.length > 1) {
                    layer.alert("请先选中一条角色记录！");
                    return;
                }
                var roleLs = $cBox.eq(0).val();
                var roleName = $cBox.closest('tr').children().eq(2).text();
                var areaLs=$cBox.closest('tr').children().eq(7).text();
                console.log("areaLs:"+areaLs);
                var argTitle = "菜单权限[" + roleName + "]";
                var params = "?roleLs=" + roleLs + "&roleName=" + roleName + "&areaLs=" + areaLs +"&permissionsType=menu";
                //参数中有中文，需要转码
                params = encodeURI(encodeURI(params));
                showFrameDialog(argTitle, "${ctx}/page/manage/roleinfo/QueueRoleMenu.jsp" + params)
            });

            //确认按钮事件
            $(document).on('click', '#subtimBtn', function () {
                //调用弹窗中的editRolePermission()方法
                document.getElementById("role_ModelId").contentWindow.editRolePermission();
            });

            //全选按钮事件
            $(document).on('click', '#checktreelist', function () {
                if ($(this).is(":checked")) {
                    document.getElementById("role_ModelId").contentWindow.operatAll(true);
                } else {
                    document.getElementById("role_ModelId").contentWindow.operatAll(false);
                }
            });

        });

        function loadTable() {
            $(".i-checks").iCheck({
                checkboxClass: "icheckbox_square-green",
                radioClass: "iradio_square-green",
            });
            $("#Role_IsShow").iCheck('check');
            var tableUrl = "${ctx}/QueueRoleinfo/list";
            table = $('#example1')
                .DataTable(
                    {
                        "bPaginate": true, //开关，是否显示分页器
                        "paging": true,
                        "lengthChange": true,
                        "searching": false,
                        "ordering": false,
                        "info": true,
                        "autoWidth": false,
                        "displayLength": 10,
                        "sAjaxDataProp": "data",
                        "bServerSide": true,
                        "sAjaxSource": tableUrl,
                        "fnServerData": loadData,
                        "bLengthChange": false,
                        "aoColumns": [
                            {
                                'mData': 'roleLs',
                                'sTitle': '<input type="checkbox" name="checklist" id="checkall" />',
                                'sName': 'roleLs',
                                "width": "50px",
                                'sClass': 'center'
                            },
                            {
                                'mData': 'roleLs',
                                'sTitle': '序号',
                                'sName': 'roleLs',
                                "width": "50px",
                                'sClass': 'center'
                            },
                            {
                                'mData': 'roleName',
                                'sTitle': '角色名称',
                                'sName': 'roleName',
                                'sClass': 'center'
                            },
                            {
                                'mData': 'roleIsshow',
                                'sTitle': '是否启用',
                                'sName': 'roleIsshow',
                                'sClass': 'center'
                            },
                            {
                                'mData': 'roleText',
                                'sTitle': '备注',
                                'sName': 'roleText',
                                'sClass': 'center'
                            },
                            {
                                'mData': 'roleCode',
                                'sTitle': '角色编号',
                                'sName': 'roleCode',
                                'sClass': 'center'
                            },
                            {
                                'mData': 'areaName',
                                'sTitle': '所属大厅',
                                'sName': 'areaName',
                                'sClass': 'center'
                            },
                            {
                                'mData': 'areaLs',
                                'sTitle': 'areaLs',
                                'sName': 'areaLs',
                                'sClass': 'hidden areaLs'
                            }
                        ],
                        "fnRowCallback": function (nRow, aData,
                                                   iDisplayIndex) {
                            let api = this.api();
                            let startIndex = api.context[0]._iDisplayStart;//获取本页开始的条数
                            $("td:nth-child(2)", nRow).html(
                                iDisplayIndex + startIndex + 1);//设置序号位于第一列，并顺次加一
                            return nRow;
                        },
                        //对列的数据显示进行设置
                        "columnDefs": [{
                            targets: 0,
                            data: "roleLs",
                            title: "操作",
                            render: function (data, type, row, meta) {
                                var html = "<input type='checkbox' value=" + row.roleLs + " class='lsCheck' name='choice' />";
                                return html;
                            }
                        },
                            {
                                targets: 3,
                                data: "isDiversion",
                                title: "是否分诊",
                                render: function (data, type, row, meta) {
                                    return (data == "1") ? "是" : "否";
                                }
                            }]
                    });

            //为表格行绑定点击事件
            $('#example1 tbody').on('click', 'tr', function () {
                dataId = $(this).find("td:eq(0) input[type='checkbox']").val();
                //为修改提供验证
                updateName = $(this).find("td:eq(2)").text();
                updateNo = $(this).find("td:eq(5)").text();

                $("#example1 tr:even").css({
                    "background": "#f9f9f9",
                    "color": "#676a6c"
                });
                $("#example1 tr:odd").css({
                    "background": "white",
                    "color": "#676a6c"
                });
                $(this).css({
                    "background": "rgb(255, 128, 64)",
                    "color": "white"
                });

                $("#Role_Name").val($(this).find("td:eq(2)").text());                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             //  }
                $("#Role_Text").val($(this).find("td:eq(4)").text());
                $("#Role_Code").val($(this).find("td:eq(5)").text());

                $("#Area_Name").val($(this).find("td:eq(6)").text());
                $("#Area_Ls").val($(this).find("td:eq(7)").text());

                if ($(this).find("td:eq(3)").text() == "是") {
                    $("#Role_IsShow").iCheck('check');
                    $("#Role_Not_IsShow").iCheck('uncheck');
                } else {
                    $("#Role_IsShow").iCheck('uncheck');
                    $("#Role_Not_IsShow").iCheck('check');
                }
                if (ts) {
                    // console.log("调用树控件的方法!");
                    ts.changeInput();
                }
            });

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
                } else if (checkedNum < allCheckNum) {
                    $("#checkall").prop("checked", false);
                }
            });

        }

        function loadData(sSource, aoData, fnCallback) {
            var roleName = $("#Role_Name2").val();
            if (roleName == null || roleName == "") {
                roleName = "";
            }
            var pageSize = aoData.iDisplayLength;
            var pageNo = aoData.iDisplayStart % aoData.iDisplayLength == 0 ? aoData.iDisplayStart / aoData.iDisplayLength + 1 : aoData.iDisplayStart / aoData.iDisplayLength;
            if (isSearch == "1") {
                pageNo = 0;
                pageSize = 10;
            }
            $(".lsCheck").prop("checked", false);
            $("#checkall").prop("checked", false);
            dataId = "";
            $.ajax({
                type: 'POST',
                url: sSource,
                cache: false,
                async: true,
                dataType: 'json',
                data: {
                    "roleName": roleName,
                    "pageSize": pageSize,
                    "pageNo": pageNo
                },
                success: function (resule) {
                    isSearch = "0";
                    var suData = resule.returnObject[0];
                    var datainfo = suData.records
                    var obj = {};
                    obj['data'] = datainfo;
                    if (typeof(datainfo) != "undefined" && datainfo.length > 0) {
                        obj.iTotalRecords = suData.total;
                        obj.iTotalDisplayRecords = suData.total;
                        fnCallback(obj);
                    } else if ((typeof(datainfo) == "undefined") && pageNo > 1) {
                        var oTable = $("#example1").dataTable();
                        oTable.fnPageChange(0);
                    } else {
                        obj['data'] = [];
                        obj.iTotalRecords = 0;
                        obj.iTotalDisplayRecords = 0;
                        fnCallback(obj);
                    }
                }
            });
        }

        //页面数据合法性验证
        function validateData() {
            if ($("#Role_Name").val().trim() == "") {
                layer.alert("角色名称不能为空！");
                return;
            }
            if ($(".Role_IsShow").val() == "") {
                layer.alert("是否启用不能为空！");
                return;
            }
            if ($("#Role_Code").val().trim() == "") {
                layer.alert("角色编号不能为空！");
                return;
            }
            if ($("#Area_Name").val().trim() == "") {
                layer.alert("所属大厅不能为空！");
                return;
            }
            var areaName = $("#Area_Name").val();
            if(areaName==null||areaName==""){
            	layer.alert("所属大厅不能为空！");
            	return false;
            }
            return true;
        }

        //清除数据
        function clearData() {
            dataId = "";
            $("#Role_Code").val('');
            $("#Role_Name").val('');
            $("#Role_Name2").val('');
            $("#Role_Text").val('');
            $("#Role_IsShow").iCheck('check');
            $("#Role_Not_IsShow").iCheck('uncheck');
            $("#Area_Name").val('');
            $("#Area_Ls").val('');
        }

        function clearData2() {
            dataId = "";
            $("#Role_Code").val('');
            $("#Role_Name").val('');
            $("#Role_Text").val('');
            $("#Role_IsShow").iCheck('check');
            $("#Role_Not_IsShow").iCheck('uncheck');
            $("#Area_Name").val('');
            $("#Area_Ls").val('');
        }

        //弹窗方法
        function showFrameDialog(argTitle, argUrl) {
            $("#role_title").html(argTitle);
            $("#role_ModelId").attr("src", argUrl);
            $("#myModa_role").modal('show');
        }

        function closeFramDialog(msg) {
            layer.alert(msg);
            $("#myModa_role").modal('hide');
        }
    </script>

<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-content">
                    <form class="form-horizontal" role="form" action="#" method="post" id="itemInfoForm">
                        <table class="table_zd" align="center" width="100%">
                            <tr>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 40%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                class="must_field">*</span>角色名称：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   class="form-control table_content_zd"
                                                   name="Role_Name" id="Role_Name">
                                        </div>
                                    </div>
                                </td>

                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 40%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                class="must_field">*</span>是否启用：</label>
                                        <div class="col-sm-8 rm_from_check"
                                             style="margin-top: -5px; padding-top: 2px; padding-bottom: 3px;">
                                            <div class="radio i-checks radio_rm">
                                                <label> <input type="radio"
                                                               name="Role_IsShow" value="1" id="Role_IsShow"><i></i>
                                                    是
                                                </label>
                                            </div>
                                            <div class="radio i-checks radio_rm">
                                                <label> <input type="radio"
                                                               name="Role_IsShow" value="0"
                                                               id="Role_Not_IsShow"> <i></i> 否
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </td>

                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 40%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd">备注：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   class="form-control table_content_zd"
                                                   name="Role_Text" id="Role_Text">
                                        </div>
                                    </div>
                                </td>

                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 40%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                class="must_field">*</span>角色编号：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   class="form-control table_content_zd"
                                                   name="Role_Code" id="Role_Code">
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td style="width: 25%;">

                                    <div class="form-group">
                                        <label style="width: 40%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                class="must_field">*</span>所属大厅：</label>
                                        <div class="col-sm-8">
                                            <input type="text" style="width: 100%;" autocomplete="off"
                                                   class="form-control m-b input_btn_input table_content_zd"
                                                   readonly="readonly" name="Area_Name" id="Area_Name">
                                            <input type="hidden" name="Area_Ls" id="Area_Ls">
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </table>

                        <table class="table_zd" align="center" width="100%" style="margin-bottom:-12px;">
                            <tbody>
                            <tr class="table_menu_tr_zd">
                                <td class="table_menu_tr_td_left_zd">
                                    <input type="text" autocomplete="off" placeholder="角色名称"
                                           class="form-control input_btn_input table_content_zd"
                                           id="Role_Name2" style="width: 40%;">
                                    <button type="button"
                                            class="table_button_zd btn btn-sm search_rm_button_index"
                                            id="index_select">查询
                                    </button>

                                    <!-------------------弹出层 start----------------------->
                                    <div class="modal inmodal fade" id="myModa_role" tabindex="-1" role="dialog"
                                         aria-hidden="true" data-backdrop="static">
                                        <div class="modal-dialog modal-lg" style="width: 400px;height: 400px;">
                                            <div class="modal-content">
                                                <div class="modal-header"
                                                     style="padding: 10px;text-align: left;background-color: #E5E5E5;">
                                                    <button type="button" class="close" data-dismiss="modal"><span
                                                            aria-hidden="true">&times;</span><span
                                                            class="sr-only">Close</span>
                                                    </button>
                                                    <div class="rm_modal_title" id="role_title"></div>
                                                </div>
                                                <div class="modal-body">
                                                    <iframe id='role_ModelId'
                                                            style='border:0px;margin:0px;width:380px;height:370px;'></iframe>
                                                </div>
                                                <div class="modal-footer">
                                                    <input type="checkbox" name="checktreelist" id="checktreelist"
                                                           style="float: left;margin-top: 7px;"/>
                                                    <span style="float: left;margin-left: 5px;margin-top: 5px;">全选</span>
                                                    <button type="button"
                                                            class="btn btn-sm input_btn_btn search_rm_button_index"
                                                            data-dismiss="modal" id="closeBtn" style="float:right;">关闭
                                                    </button>
                                                    <button type="button" class="btn btn-primary btn-sm" id="subtimBtn">
                                                        确认
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </td>

                                <td class="table_menu_tr_td_right_zd" colspan="2">
                                    <button type="button"
                                            class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                            id="configWindow">显示配置按钮
                                    </button>
                                    <button type="button"
                                            class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                            id="deleteDataBtn">删除
                                    </button>
                                    <button type="button"
                                            class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                            id="clearDataBtn" onClick="clearData()">清除
                                    </button>
                                    <button type="button"
                                            class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                            id="updateDataBtn">修改
                                    </button>
                                    <button type="button"
                                            class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                            id="addDataBtn">新增
                                    </button>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </form>
                    <div id="configTr" class="table_menu_tr_zd"
                         style="height: 35px; display: none; margin-top: 0px; border: 1px solid white;">
                        <div class="optionRow-right" style="padding-right: 10px">
                            <button type="button" style="display:none;"
                                    class="btn btn-primary btn-sm input_btn_btn list_btn  table_button_zd"
                                    id="caseConfig">项目权限
                            </button>
                            <button type="button"
                                    class="btn btn-primary btn-sm input_btn_btn list_btn  table_button_zd"
                                    id="menuConfig">菜单权限
                            </button>
                        </div>
                    </div>
                    <table id="example1" class="table table-bordered"></table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

</html>


