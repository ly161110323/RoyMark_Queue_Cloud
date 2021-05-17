<%--
  Created by IntelliJ IDEA.
  User: liucl
  Date: 2021/3/28
  Time: 9:42 上午
  To change this template use File | Settings | File Templates.
--%>
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
    <script type="text/javascript" src="./js/windowManagement.js">
    </script>

    <script type="text/javascript">
        var table;
        var dataId = "";
        var isSearch = "0";
        var selectInfo ={};


        $(document).ready(function () {
            //加载表格
            loadTable();
            trClick();
            checkBoxStyle_Control();

        });

        function loadTable() {

            var tableUrl = "${ctx}/window/queryData";
            table = $('#itemResultTable')
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
                            {'mData': 'windowHiddenId', 'sTitle': '<input type="checkbox" name="checklist" id="checkall" />', 'sName': 'windowHiddenId', 'sClass': 'center'},
                            {'mData': 'windowHiddenId', 'sTitle': '序号', 'sName': 'windowHiddenId', 'sClass': 'center'},
                            {'mData': 'windowId', 'sTitle': '窗口ID', 'sName': 'windowId', 'sClass': 'center'},
                            {'mData': 'windowName', 'sTitle': '名称', 'sName': 'windowName', 'sClass': 'center'},
                            {'mData': 'camId', 'sTitle': '摄像头ID', 'sName': 'camId', 'sClass': 'center'},
                            {'mData': 'windowDepartment', 'sTitle': '部门', 'sName': 'windowDepartment', 'sClass': 'center'},
                            {'mData': 'windowEvent', 'sTitle': '事项', 'sName': 'windowEvent', 'sClass': 'center'},

                            {'mData': 'windowNinePalaces', 'sTitle': '九宫格监控', 'sName': 'windowNinePalaces', 'sClass': 'center'},
                            {'mData': 'windowActionAnalysis', 'sTitle': '行为分析', 'sName': 'windowActionAnalysis', 'sClass': 'center'},
                            {'mData': 'windowCoordinates', 'sTitle': '区域坐标', 'sName': 'windowCoordinates', 'sClass': 'center'},
                            {'mData': 'camHiddenId', 'sTitle': 'camHiddenId', 'sName': 'camHiddenId', 'sClass': 'hidden'},



                        ],
                        "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                            let api = this.api();
                            let startIndex = api.context[0]._iDisplayStart;//获取本页开始的条数
                            $("td:nth-child(2)", nRow).html(iDisplayIndex + startIndex + 1);//设置序号位于第一列，并顺次加一
                            return nRow;
                        },
                        "initComplete": function (settings, json) {
                            $(".dataTables_scrollHeadInner").css({width: "100%"});
                            $(".dataTables_scrollHeadInner table").css({width: "100%"});
                            $(".dataTables_scrollBody").attr('id', 'scrollBodyDiv');
                            $(".dataTables_scrollBody").css({"overflow-y": "auto", "overflow-x": "hidden"});
                            var obj = document.getElementById("scrollBodyDiv");
                            //如果数据DIV有滚动条，则标题头也需要增加滚动条，以保持一致
                            if (obj) {
                                if (obj.scrollHeight > obj.clientHeight || obj.offsetHeight > obj.clientHeight) {
                                    $(".dataTables_scrollHead").css({overflow: "scroll", "overflow-x": "hidden"});
                                } else {
                                    $(".dataTables_scrollHead").css({overflow: "auto", "overflow-x": "hidden"});
                                }
                            }
                        },
                        "drawCallback": function (settings) {
                            $(".dataTables_scrollHeadInner").css({width: "100%"});
                            $(".dataTables_scrollHeadInner table").css({width: "100%"});
                            $(".dataTables_scrollBody").attr('id', 'scrollBodyDiv');
                            $(".dataTables_scrollBody").css({"overflow-y": "auto", "overflow-x": "hidden"});
                            var obj = document.getElementById("scrollBodyDiv");
                            //如果数据DIV有滚动条，则标题头也需要增加滚动条，以保持一致
                            if (obj) {
                                if (obj.scrollHeight > obj.clientHeight || obj.offsetHeight > obj.clientHeight) {
                                    $(".dataTables_scrollHead").css({overflow: "scroll", "overflow-x": "hidden"});
                                } else {
                                    $(".dataTables_scrollHead").css({overflow: "auto", "overflow-x": "hidden"});
                                }
                            }
                        },
                        "columnDefs": [
                            {
                                targets: 0, data: "windowHiddenId", title: "操作",
                                render: function (data, type, row, meta) {
                                    var html = "<input type='checkbox' value=" + row.windowHiddenId + " class='lsCheck' name='choice' />";
                                    html += "<input type='hidden' name='deptImagepath' value=" + row.deptImagepath + "></input>";
                                    return html;
                                }
                            },
                            // {
                            //     targets: 7,data: "deptImagepath",title: "委办局图标名称",
                            //     render: function (data, type, row, meta) {
                            //         var deptImagepath=row.deptImagepath;
                            //         var deptImageName='';
                            //         if(deptImagepath!=null && deptImagepath!='' && typeof(deptImagepath)!='undefined')
                            //         {
                            //             deptImageName=deptImagepath.substring(deptImagepath.lastIndexOf("/")+1);
                            //         }
                            //         return deptImageName;
                            //     }
                            // }
                        ]
                    });
        }

        function loadData(sSource, aoData, fnCallback) {
            console.log(sSource)
            console.log(aoData)

            var pageSize = aoData.iDisplayLength;
            var pageNo = aoData.iDisplayStart % aoData.iDisplayLength == 0 ? aoData.iDisplayStart / aoData.iDisplayLength + 1 : aoData.iDisplayStart / aoData.iDisplayLength;
            var windowId = $("#inputCommitWindowId").val();
            var windowName = $("#inputCommitWindowName").val();
            var windowDepartment = $("#inputCommitWindowDepartment").val();
            var params;
            params = {

                "pageSize": pageSize,
                "pageNo": pageNo,
            };
            if (isSearch == "1") {
                if (windowId != "") {
                    params["windowId"] = windowId;

                }
                if (windowName != "") {
                    params["windowName"] = windowName;

                }
                if (windowDepartment != "") {
                    params["windowDepartment"] = windowDepartment;

                }

            }
            dataId = "";

            $.ajax({
                type: 'POST',
                url: sSource,
                cache: false,
                async: true,
                dataType: 'json',
                data: params,
                success: function (result) {
                    isSearch = "0";
                    var pageList = result.pageList;
                    var datainfos = pageList.records
                    var obj = {};
                    obj['data'] = datainfos;
                    obj['data'].forEach(function (item) {
                        item['windowActionAnalysis'] = item["windowActionAnalysis"]?"开":"关";
                        item['windowNinePalaces'] = item["windowNinePalaces"]?"开":"关";
                    })
                    if (typeof (datainfos) != "undefined" && datainfos.length > 0) {
                        obj.iTotalRecords = pageList.total;
                        obj.iTotalDisplayRecords = pageList.total;
                        fnCallback(obj);
                    } else if ((typeof (datainfos) == "undefined") && pageNo > 1) {
                        var oTable = $("#itemResultTable").dataTable();
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
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                style="color: red;">*</span>窗口ID：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   placeholder="" class="form-control table_content_zd"
                                                   name="windowId" id="windowId">
                                        </div>

                                    </div>
                                </td>

                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                style="color: red;">*</span>名称：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   placeholder="" class="form-control table_content_zd"
                                                   name="windowName" id="windowName">
                                        </div>

                                    </div>
                                </td>

                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                style="color: red;">*</span>部门：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   placeholder="" class="form-control table_content_zd"
                                                   name="windowDepartment" id="windowDepartment">
                                        </div>
                                        <%--                                                                        <input type="hidden" name="queueDept.deptLs" id="txtDeptLs" />--%>
                                    </div>
                                </td>

                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                style="color: red;">*</span>事项：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   class="form-control table_content_zd"
                                                   name="windowEvent" id="windowEvent">
                                        </div>
                                    </div>
                                </td>

                            </tr>
                            <tr>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                style="color: red;">*</span>区域坐标：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   class="form-control table_content_zd"
                                                   disabled="disabled"
                                                   name="windowEvent" id="windowCoordinates">
                                        </div>
                                    </div>
                                </td>

                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                style="color: red;">*</span>九宫格监控：</label>
                                        <div class="col-sm-8">
                                            <select class="form-control m-b table_content_zd" id="windowNinePalaces">
                                                <option value="">请选择开启或关闭</option>
                                                <option value="true">开</option>
                                                <option value="false">关</option>
                                            </select>
                                        </div>

                                    </div>
                                </td>

                                <%--                                <td style="width: 25%;">--%>
                                <%--                                    <div class="form-group">--%>
                                <%--                                        <label style="width: 38%;"--%>
                                <%--                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span--%>
                                <%--                                                style="color: red;">*</span>现场预约上限：</label>--%>
                                <%--                                        <div class="col-sm-8">--%>
                                <%--                                            <input type="text" autocomplete="off" spellcheck="false"--%>
                                <%--                                                   placeholder="" class="form-control table_content_zd"--%>
                                <%--                                                   name="queueDept.deptMaxappointment"--%>
                                <%--                                                   id="txtDeptMaxappointment">--%>
                                <%--                                        </div>--%>

                                <%--                                    </div>--%>
                                <%--                                </td>--%>


                                <%--                            </tr>--%>
                                <%--                                                        <tr>--%>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                style="color: red;">*</span>行为分析：</label>
                                        <div class="col-sm-8">
                                            <select
                                                    class="form-control m-b table_content_zd"
                                                    id="windowActionAnalysis">
                                                <option value="">请选择开启或关闭</option>
                                                <option value="true">开</option>
                                                <option value="false">关</option>
                                            </select>
                                        </div>

                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                style="color: red;">*</span>绑定摄像头ID：</label>
                                        <div class="col-sm-8">
                                            <select
                                                    class="form-control m-b table_content_zd"
                                                    id="camId">
                                                <option value="">请选择绑定摄像头ID</option>
                                            </select>
                                        </div>

                                    </div>
                                </td>

                                <%--                                                            <td style="width: 25%;">--%>
                                <%--                                                                <div class="form-group">--%>
                                <%--                                                                    <label style="width: 38%;"--%>
                                <%--                                                                           class="col-sm-3 control-label input_lable_hm table_label_zd"></span>第三方编号：</label>--%>
                                <%--                                                                    <div class="col-sm-8">--%>
                                <%--                                                                        <input type="text" autocomplete="off" spellcheck="false"--%>
                                <%--                                                                               placeholder="" class="form-control table_content_zd"--%>
                                <%--                                                                               name="queueDept.otherId" id="otherId">--%>
                                <%--                                                                    </div>--%>

                                <%--                                                                </div>--%>
                                <%--                                                            </td>--%>
                            </tr>

                        </table>
                        <table class="table_zd" align="center" width="100%" style="margin-bottom:-12px;">
                            <tbody>
                            <tr class="table_menu_tr_zd">
                                <td class="table_menu_tr_td_left_zd"
                                    colspan="2">
                                    <input
                                            type="text"
                                            placeholder="请输入窗口ID"
                                            autocomplete="off"
                                            spellcheck="false"
                                            placeholder=""
                                            style="width: 25%;"
                                            class="form-control input_btn_input table_content_zd"
                                            name="inputCommitWindowId"
                                            id="inputCommitWindowId">
                                    <input
                                            type="text"
                                            placeholder="请输入窗口名称"
                                            autocomplete="off"
                                            spellcheck="false"
                                            placeholder=""
                                            style="width: 25%;"
                                            class="form-control input_btn_input table_content_zd"
                                            name="inputCommitWindowName"
                                            id="inputCommitWindowName">
                                    <input
                                            type="text"
                                            placeholder="请输入窗口所属部门"
                                            autocomplete="off"
                                            spellcheck="false"
                                            placeholder=""
                                            style="width: 25%;"
                                            class="form-control input_btn_input table_content_zd"
                                            name="inputCommitWindowDepartment"
                                            id="inputCommitWindowDepartment">

                                    <button type="button"
                                            class="btn btn-sm input_btn_btn search_rm_button_index table_button_zd"
                                            style="margin-top: 2.5px; margin-bottom: 2px;"
                                            id="queCommit">
                                        查询
                                    </button>
                                </td>

                                <td class="table_menu_tr_td_right_zd" colspan="2">
                                    <div style="float: right;">
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="addCommit">新增
                                        </button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="modifyCommit">修改
                                        </button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="clearData">清除
                                        </button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="deleteCommit">删除
                                        </button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="drawWindow">绘制窗口区域
                                        </button>
                                    </div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </form>
                    <div id="configTr" class="table_menu_tr_zd"
                         style="height: 35px; display: none; margin-top: 0px; border: 1px solid white;">
                        <span style="padding-left: 45px;">&nbsp;&nbsp;</span>
                        <div class="optionRow-right" style="margin-right: 10px">
                            <button type="button"
                                    class="btn btn-primary btn-sm input_btn_btn list_btn"
                                    style="float: left; margin-bottom: 2px;"
                                    id="caseArea_administration">办事区域管理
                            </button>
                            <button type="button"
                                    class="btn btn-primary btn-sm input_btn_btn list_btn"
                                    style="float: left; margin-bottom: 2px;"
                                    id="selectArea_administration">行政区域管理
                            </button>
                        </div>
                    </div>
                    <table id="itemResultTable" class="table table-bordered"></table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

</html>

<%--<table id="table_id" class="display">--%>
<%--    <thead>--%>
<%--    <tr>--%>
<%--        <th>服务器ID</th>--%>
<%--        <th>服务器IP</th>--%>
<%--        <th>端口</th>--%>
<%--        <th>服务器状态</th>--%>
<%--        <th>程序状态</th>--%>
<%--        <th>操作</th>--%>


<%--    </tr>--%>
<%--    </thead>--%>
<%--    <tbody>--%>
<%--    <tr>--%>
<%--        <td>Row 1 Data 1</td>--%>
<%--        <td>Row 1 Data 2</td>--%>
<%--    </tr>--%>
<%--    <tr>--%>
<%--        <td>Row 2 Data 1</td>--%>
<%--        <td>Row 2 Data 2</td>--%>
<%--    </tr>--%>
<%--    </tbody>--%>
<%--</table>--%>
<%--<script>--%>
<%--    var table;--%>
<%--    var dataId = "";--%>
<%--    var isSearch = "0";--%>
<%--    var  defaultAreaLs ="${sessionScope.DEFAULT_PROJECT.areaLs}";--%>
<%--    var defaultAreaName="${sessionScope.DEFAULT_PROJECT.areaName}";--%>
<%--    $(document).ready( function () {--%>
<%--        $('#table_id').DataTable({--%>
<%--            // "scrollY": 400,--%>
<%--            // select: true,--%>
<%--            ajax : {--%>
<%--                "url" : "${ctx}/server/getAll",--%>
<%--                "type" : "get",--%>

<%--                "dataSrc" : "servers"--%>
<%--            },--%>
<%--            columns: [--%>
<%--                {"data": "id"},--%>
<%--                {"data": "ip"},--%>
<%--                {"data": "port"},--%>
<%--                {"data": "name"},--%>

<%--            ],--%>
<%--            processing: true,--%>
<%--            language: {--%>
<%--                "url": "${ctx}/page/manage/BR/js/datatables_language.json"--%>
<%--            }--%>


<%--        });--%>
<%--    } );--%>
<%--</script>--%>
