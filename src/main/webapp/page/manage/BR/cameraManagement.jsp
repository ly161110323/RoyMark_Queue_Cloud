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
    <script type="text/javascript" src="./js/cameraManagement.js">
    </script>

    <script type="text/javascript">
        var table;
        var dataId = "";
        var windowHieednId = "";
        var serverHiddenId = "";
        var isSearch = "0";
        var defaultAreaLs = "${sessionScope.DEFAULT_PROJECT.areaLs}";
        var defaultAreaName = "${sessionScope.DEFAULT_PROJECT.areaName}";

        $(document).ready(function () {
            //加载表格
            loadTable();
            trClick();
            checkBoxStyle_Control();

        });

        function loadTable() {

            var tableUrl = "${ctx}/camera/queryData";
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
                            {
                                'mData': 'camHiddenId',
                                'sTitle': '<input type="checkbox" name="checklist" id="checkall" />',
                                'sName': 'camHiddenId',
                                'sClass': 'center'
                            },
                            {'mData': 'camHiddenId', 'sTitle': '序号', 'sName': 'camHiddenId', 'sClass': 'center'},
                            {'mData': 'camId', 'sTitle': '摄像头ID', 'sName': 'camId', 'sClass': 'center'},
                            {'mData': 'camIp', 'sTitle': '摄像头IP', 'sName': 'camIp', 'sClass': 'center'},
                            {'mData': 'windowId', 'sTitle': '窗口ID', 'sName': 'windowId', 'sClass': 'center'},
                            {'mData': 'serverId', 'sTitle': '服务器ID', 'sName': 'serverId', 'sClass': 'center'},
                            {'mData': 'camStatus', 'sTitle': '设备状态', 'sName': 'camStatus', 'sClass': 'center'},
                            {'mData': 'camVideoAddr', 'sTitle': '视频流地址', 'sName': 'camVideoAddr', 'sClass': 'center'},
                            {'mData': 'camMacAddr', 'sTitle': 'mac地址', 'sName': 'camMacAddr', 'sClass': 'center'},
                            {'mData': 'camBrand', 'sTitle': '厂商', 'sName': 'camBrand', 'sClass': 'center'},
                            {'mData': 'camType', 'sTitle': '型号', 'sName': 'camType', 'sClass': 'center'},
                            {'mData': 'camBirth', 'sTitle': '出厂日期', 'sName': 'camBirth', 'sClass': 'center'},
                            {
                                'mData': 'serverHiddenId',
                                'sTitle': 'serverHiddenId',
                                'sName': 'serverHiddenId',
                                'sClass': 'hidden'
                            },
                            {
                                'mData': 'windowHiddenId',
                                'sTitle': 'windowHiddenId',
                                'sName': 'windowHiddenId',
                                'sClass': 'hidden'
                            },
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
                                targets: 0, data: "camHiddenId", title: "操作",
                                render: function (data, type, row, meta) {
                                    var html = "<input type='checkbox' value=" + row.camHiddenId + " class='lsCheck' name='choice' />";
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

            var inputCamId = $("#inputCommitCamId").val();
            var selectServerId = $("#selectCommitServerId").find("option:selected").text();
            var selectWindowId = $("#selectCommitWindowId").find("option:selected").text();
            var params;
            params = {

                "pageSize":pageSize,
                "pageNo":pageNo,
            };
            if(isSearch=="1"){
                if(selectServerId != "请选择绑定服务器ID"){
                    params["serverId"] = selectServerId;

                }
                if(selectWindowId !="请选择绑定窗口ID"){
                    params["windowId"] = selectWindowId;

                }
                if(inputCamId !=""){
                    params["camId"] = inputCamId;
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
                    // console.log(obj['data'])
                    obj['data'].forEach(function (item) {
                        item['camBirth'] = formatDate(new Date(item["camBirth"]["time"]), "yyyy-MM-dd")
                    })

                    if (typeof (datainfos) != "undefined" && datainfos.length > 0) {
                        obj.iTotalRecords = datainfos.length;
                        obj.iTotalDisplayRecords = datainfos.length;
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
                                                style="color: red;">*</span>摄像头ID：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   placeholder="" class="form-control table_content_zd"
                                                   name="camId" id="camId">
                                        </div>

                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                style="color: red;">*</span>摄像头IP：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   class="form-control table_content_zd"
                                                   name="camIp" id="camIp">
                                        </div>
                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                style="color: red;">*</span>窗口ID：</label>
                                        <div class="col-sm-8">
                                            <select class="form-control m-b table_content_zd"
                                                    id="windowId" name="windowId">
                                                <option value="0">请选择绑定窗口ID</option>
                                            </select>
                                        </div>

                                    </div>
                                </td>


                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                style="color: red;">*</span>mac地址：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   class="form-control table_content_zd"
                                                   name="camMacAddr" id="camMacAddr">
                                        </div>
                                    </div>
                                </td>

                            </tr>
                            <tr>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                style="color: red;">*</span>服务器ID：</label>
                                        <div class="col-sm-8">
                                            <select class="form-control m-b table_content_zd"
                                                    id="serverId" name="serverId">
                                                <option value="0">请选择绑定服务器ID</option>
                                            </select>
                                        </div>

                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd">厂商：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   placeholder="" class="form-control table_content_zd"
                                                   name="camBrand" id="camBrand">
                                        </div>

                                    </div>
                                    <%--                                </td>--%>

                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                style="color: red;">*</span>型号：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   placeholder="" class="form-control table_content_zd"
                                                   name="camType" id="camType">
                                        </div>

                                    </div>
                                </td>


                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                style="color: red;">*</span>生产日期：</label>
                                        <div class="col-sm-8">
                                            <input type="date" autocomplete="off" spellcheck="false"
                                                   placeholder="" class="form-control table_content_zd"
                                                   name="camBirth" id="camBirth">
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
                            <tr>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                style="color: red;">*</span>视频流地址：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   placeholder="" class="form-control table_content_zd"
                                                   name="camVideoAddr" id="camVideoAddr">
                                        </div>
                                        <%--                                                                        <input type="hidden" name="queueDept.deptLs" id="txtDeptLs" />--%>
                                    </div>
                                </td>
                            </tr>
                        </table>
                        <table class="table_zd" align="center" width="100%" style="margin-bottom:-12px;">
                            <tbody>
                            <tr class="table_menu_tr_zd">
                                <td class="table_menu_tr_td_left_zd" colspan="2">
                                    <input type="text" placeholder="摄像头ID" autocomplete="off"
                                           spellcheck="false" placeholder="" style="width: 25%;"
                                           class="form-control input_btn_input table_content_zd"
                                           name="inputCommitCamId" id="inputCommitCamId">
                                    <select
                                            class="form-control table_content_zd" name="selectCommitWindowId"
                                            id="selectCommitWindowId"
                                            style="width: 25%; float: left; margin-right: 10px">
                                        <option value="">请选择绑定窗口ID</option>
                                    </select>
                                    <select
                                            class="form-control table_content_zd" name="selectCommitServerId"
                                            id="selectCommitServerId"
                                            style="width: 25%; float: left; margin-right: 10px">
                                        <option value="">请选择绑定服务器ID</option>
                                    </select>



                                    <button type="button"
                                            class="btn btn-sm input_btn_btn search_rm_button_index table_button_zd"
                                            style="margin-top: 2.5px; margin-bottom: 2px;"
                                            id="queCommit">查询
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
                                                id="configWindow">显示配置按钮
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
