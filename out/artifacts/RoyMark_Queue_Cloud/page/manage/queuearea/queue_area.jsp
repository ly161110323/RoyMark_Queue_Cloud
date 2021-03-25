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

    <script type="text/javascript" src="./js/queue_area.js"></script>
    <script type="text/javascript">

        var table;
        var dataId = "";
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
                    url: "${ctx}/QueueArea/insert",
                    type: "post",
                    dataType: "json",
                    data: {
                        "areaName": $("#Area_Name").val(),
                        "areaId": $("#Area_Id").val(),
                        "areaOrderno": $("#Area_OrderNo").val(),
                        "parentAreaLs": $("#parent_area_ls").val(),
                        "dbconnect": $("#dbconnect").val()
                    },
                    success: function (data) {
                        if (data.result == "error") {
                            layer.alert("服务器错误！");
                            return;
                        }
                        if (data.result == "repeat") {
                            layer.alert("该办事大厅已存在！");
                            return;
                        }
                        if (data.result == "ok") {
                            layer.alert("新增成功！");
                            //刷新树状文本框

                            var rootPath=getWebRootPath();

                            var serverUrl=rootPath+"/QueueArea/gettreenodes";
                            getData(serverUrl);
                        } else if (data.result == "no") {
                            layer.alert("新增失败！");
                        }
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
                    url: "${ctx}/QueueArea/update",
                    type: "post",
                    dataType: "json",
                    data: {
                        "areaLs": dataId,
                        "areaName": $("#Area_Name").val(),
                        "areaId": $("#Area_Id").val(),
                        "areaOrderno": $("#Area_OrderNo").val(),
                        "parentAreaLs": $("#parent_area_ls").val(),
                        "dbconnect": $("#dbconnect").val()
                    },
                    success: function (data) {

                        if (data.result == "error") {
                            layer.alert("服务器错误！");
                            return;
                        }
                        if (data.result == "repeat") {
                            layer.alert("该办事大厅已存在！");
                            return;
                        }
                        if (data.result == "nochoose") {
                            layer.alert("请选择需要修改的数据！");
                            return;
                        }
                        if (data.result == "ok") {
                            layer.alert("修改成功！");
                            var rootPath=getWebRootPath();
                            var serverUrl=rootPath+"/QueueArea/gettreenodes";
                            getData(serverUrl);
                        } else if (data.result == "no") {
                            layer.alert("修改失败！");
                        }
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
                        var url = "${ctx}/QueueArea/delete";
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
                table.draw(false);
            });


            //为表格行绑定点击事件
            $('#example1 tbody').on('click', 'tr', function () {
                dataId = $(this).find("td:eq(0) input[type='checkbox']").val();
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
                $("#Area_Name").val($(this).find("td:eq(2)").text());

                $("#Area_Id").val($(this).find("td:eq(3)").text());
                $("#Area_OrderNo").val($(this).find("td:eq(4)").text());

                $("#parent_area_ls").val($(this).find("td:eq(5)").text());
                $("#parent_area_name").val($(this).find("td:eq(6)").text());
                if(ts)
                {
                    // console.log("调用树控件的方法!");
                    ts.changeInput();
                }
                $("#dbconnect").val($(this).find("td:eq(7)").text());
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
        });

        function loadTable() {
            $(".i-checks").iCheck({
                checkboxClass: "icheckbox_square-green",
                radioClass: "iradio_square-green",
            });

            var tableUrl = "${ctx}/QueueArea/list";
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
                                'mData': 'areaLs',
                                'sTitle': '<input type="checkbox" name="checklist" id="checkall" />',
                                'sName': 'areaLs',
                                'sClass': 'center'
                            },
                            {'mData': 'areaLs', 'sTitle': '序号', 'sName': 'areaLs', 'sClass': 'center'},


                            {
                                'mData': 'areaName',
                                'sTitle': '办事大厅名称',
                                'sName': 'areaName',
                                'sClass': 'center'
                            },
                            {
                                'mData': 'areaId',
                                'sTitle': '办事大厅编号',
                                'sName': 'areaId',
                                'sClass': 'center'
                            },
                            {
                                'mData': 'areaOrderno',
                                'sTitle': '顺序号',
                                'sName': 'areaOrderno',
                                'sClass': 'center'
                            },
                            {
                                'mData': 'parentAreaLs',
                                'sTitle': '上级办事大厅流水号',
                                'sName': 'parentAreaLs',
                                'sClass': 'hidden'
                            },
                            {
                                'mData': 'parentAreaName',
                                'sTitle': '上级办事大厅',
                                'sName': 'parentAreaName',
                                'sClass': 'center'
                            },
                            {
                                'mData': 'dbconnect',
                                'sTitle': '数据库连接字符串',
                                'sName': 'dbconnect',
                                'sClass': 'center'
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
                        "columnDefs": [
                            {
                                targets: 0,
                                data: "areaLs",
                                title: "操作",
                                render: function (data, type, row, meta) {
                                    var html = "<input type='checkbox' value=" + row.areaLs + " class='lsCheck' name='choice' />";
                                    return html;
                                }
                            },

                        ]
                    });
        }

        function loadData(sSource, aoData, fnCallback) {
            var searchAraName = $("#search_AreaName").val();
            if (searchAraName == null || searchAraName == "") {
                searchAraName = "";
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
                    "areaName": searchAraName,
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
            if ($("#Area_Name").val().trim() == "") {
                layer.alert("办事大厅名称不能为空！");
                return;
            }
            if ($("#Area_Id").val().trim() == "") {
                layer.alert("办事大厅编号不能为空！");
                return;
            }
            if ($("#Area_OrderNo").val().trim() == "") {
                layer.alert("办事大厅顺序号不能为空！");
                return;
            }
            if ($("#parent_area_name").val().trim() == "") {
                layer.alert("上级办事大厅不能为空！");
                return;
            }
            if ($("#parent_area_ls").val().trim() == "") {
                layer.alert("上级办事大厅不能为空！");
                return;
            }

            return true;
        }

        //清除数据
        function clearData() {
            $("#Area_Name").val('');
            $("#Area_Id").val('');
            $("#Area_OrderNo").val('');
            $("#parent_area_ls").val('');
            $("#dbconnect").val('');
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
                                                class="must_field">*</span>办事大厅名称：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   class="form-control table_content_zd"
                                                   name="Area_Name" id="Area_Name">
                                        </div>
                                    </div>
                                </td>

                                <td style="width: 25%;">
                                <div class="form-group">
                                    <label style="width: 40%;"
                                           class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                            class="must_field">*</span>办事大厅编号：</label>
                                    <div class="col-sm-8">
                                        <input type="text" autocomplete="off" spellcheck="false"
                                               class="form-control table_content_zd"
                                               name="Area_Id" id="Area_Id">
                                    </div>
                                </div>
                            </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 40%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                class="must_field">*</span>办事大厅顺序号：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   class="form-control table_content_zd"
                                                   name="Area_OrderNo" id="Area_OrderNo">
                                        </div>
                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 40%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                class="must_field">*</span>上级办事大厅：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   class="form-control table_content_zd"
                                                   placeholder="单击选择"
                                                   name="parent_area_name" id="parent_area_name"
                                            readonly="readonly"
                                            >
                                            <input type="hidden"
                                                   name="parent_area_ls" id="parent_area_ls">
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>

                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 40%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd">数据库连接字符串：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   class="form-control table_content_zd"
                                                   name="dbconnect" id="dbconnect">
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </table>


                        <table class="table_zd" align="center" width="100%">
                            <tbody>
                            <tr class="table_menu_tr_zd">
                                <td class="table_menu_tr_td_left_zd">
                                    <input type="text" placeholder="办事大厅名称"
                                           autocomplete="off" spellcheck="false"
                                           class="form-control input_btn_input table_content_zd"
                                           id="search_AreaName" style="width:35%;">
                                    <button type="button"
                                            class="table_button_zd btn btn-sm search_rm_button_index"
                                            id="index_select">查询
                                    </button>
                                </td>

                                <td class="table_menu_tr_td_right_zd" colspan="3">

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

                    <table id="example1" class="table table-bordered"></table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

</html>


