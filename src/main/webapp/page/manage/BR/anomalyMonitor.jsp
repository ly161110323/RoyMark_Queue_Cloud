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
    <script type="text/javascript" src="./js/anomalyMonitor.js">
    </script>
    <script type="text/javascript" src="${ctx}/resources/js/layDate-v5.3.0/laydate/laydate.js">
    </script>
<%--    <link href="${ctx}/resources/inputSelect/bootstrap/bootstrap.min.css" rel="stylesheet" />--%>
    <script src="${ctx}/resources/inputSelect/bootstrap/bootstrap.js"></script>
    <link href="${ctx}/resources/inputSelect/jquery.editable-select/jquery.editable-select.min.css" rel="stylesheet" />
    <script src="${ctx}/resources/inputSelect/jquery.editable-select/jquery.editable-select.min.js"></script>

    <script type="text/javascript">
        var table;
        var dataId = "";
        var selectInfo = {};
        var isSearch = "0";
        var searchData = {};
        var defaultAreaLs = "${sessionScope.DEFAULT_PROJECT.areaLs}";
        var defaultAreaName = "${sessionScope.DEFAULT_PROJECT.areaName}";
        var userInfos = [];
        var ctx = "${pageContext.request.contextPath}";
        $(document).ready(function () {
            //加载表格
            loadTable();
            trClick();
            checkBoxStyle_Control();

        });

        function loadTable() {

            var tableUrl = "${ctx}/anomaly/queryData";
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
                        "displayLength": 15,
                        "sAjaxDataProp": "data",
                        "bServerSide": true,
                        "sAjaxSource": tableUrl,
                        "fnServerData": loadData,
                        "bLengthChange": false,
                        "aoColumns": [
                            {'mData': 'anomalyHiddenId', 'sTitle': '<input type="checkbox" name="checklist" id="checkall" />', 'sName': 'userHiddenId', 'sClass': 'center'},
                            {'mData': 'anomalyHiddenId', 'sTitle': '序号', 'sName': 'anomalyHiddenId', 'sClass': 'center'},
                            {'mData': 'windowId', 'sTitle': '窗口ID', 'sName': 'windowId', 'sClass': 'center'},
                            {'mData': 'windowName', 'sTitle': '窗口名称', 'sName': 'windowName', 'sClass': 'center'},
                            {'mData': 'userNames', 'sTitle': '人员姓名', 'sName': 'userNames', 'sClass': 'center'},
                            {'mData': 'anomalyEvent', 'sTitle': '异常事件', 'sName': 'anomalyEvent', 'sClass': 'center'},
                            {'mData': 'anomalyStartDate', 'sTitle': '开始时间', 'sName': 'anomalyStartDate', 'sClass': 'center'},
                            {'mData': 'anomalyEndDate', 'sTitle': '结束时间', 'sName': 'anomalyEndDate', 'sClass': 'center'},
                            {'mData': 'anomalyConfidence', 'sTitle': '异常置信度', 'sName': 'anomalyConfidence', 'sClass': 'center'},
                            {'mData': 'faceConfs', 'sTitle': '身份置信度', 'sName': 'faceConfs', 'sClass': 'center'},
                            {'mData': 'windowHiddenId', 'sTitle': 'windowHiddenId', 'sName': 'windowHiddenId', 'sClass': 'hidden'},
                            {'mData': 'userHiddenIds', 'sTitle': 'userHiddenIds', 'sName': 'userHiddenIds', 'sClass': 'hidden'},
                            {'mData': 'anomalyVideoPath', 'sTitle': 'anomalyVideoPath', 'sName': 'anomalyVideoPath', 'sClass': 'hidden'},
                            {'mData': 'anomalyImagePath', 'sTitle': 'anomalyImagePath', 'sName': 'anomalyImagePath', 'sClass': 'hidden'},
                            {'mData': 'anomalyStatus', 'sTitle': '确认结果', 'sName': 'anomalyStatus', 'sClass': 'center'},
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
                                targets: 0, data: "anomalyHiddenId", title: "操作",
                                render: function (data, type, row, meta) {
                                    var html = "<input type='checkbox' value=" + row.anomalyHiddenId + " class='lsCheck' name='choice' />";
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
            var that = this;
            var pageSize = aoData.iDisplayLength;
            var pageNo = aoData.iDisplayStart % aoData.iDisplayLength == 0 ? aoData.iDisplayStart / aoData.iDisplayLength + 1 : aoData.iDisplayStart / aoData.iDisplayLength;



            // console.log(showPending);
            var params;
            params = {

                "pageSize":pageSize,
                "pageNo":pageNo,
            };

            if(isSearch=="1"){
                params = {
                    "pageSize":pageSize,
                    "pageNo":1,
                };
            }
            $.extend(params,searchData);
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
                    var status = {'pending':'待处理','valid':"有效","invalid":"无效"};

                    obj['data'].forEach(function (item) {
                        item['anomalyStatus'] =status[item.anomalyStatus];
                        item['anomalyConfidence'] = parseFloat(item.anomalyConfidence).toFixed(2).toString();
                        let faceConfs = [];
                        let userNames = [];
                        let userIds = [];

                        item.userShortInfos.forEach(function (x){
                            faceConfs.push(parseFloat(x['faceConf']).toFixed(2));
                            userNames.push(x['userName']);
                            userIds.push(x['userId'])

                        });

                        item['faceConfs'] = faceConfs.toString();
                        item['userNames'] = userNames.toString()
                        item['userHiddenIds'] = userIds.toString()
                        // if(faceConfs.length==0){ // 保留2位小数
                        //     item['faceConfs'] = '';
                        // }else {
                        //     let faceConfsStr = item.faceConfs.toString();
                        //     let re = []
                        //     faceConfsStr.split(',').forEach(function (i){
                        //         re.push(parseFloat(i).toFixed(2))
                        //     });
                        //     item['faceConfs'] = re.toString()
                        // }



                    });

                    if (typeof (datainfos) != "undefined" && datainfos.length > 0) {
                        obj.iTotalRecords = pageList.total;
                        obj.iTotalDisplayRecords = pageList.total;
                        that.api().page(pageList.current-1);
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
                                               class="col-sm-3 control-label input_lable_hm table_label_zd">窗口ID：</label>
                                        <div class="col-sm-8">
                                            <select class="form-control m-b table_content_zd" disabled="disabled"
                                                    id="windowId" name="windowId">
                                                <option value="">请选择窗口ID</option>

                                            </select>
                                        </div>

                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd">窗口名称：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false" disabled="disabled"
                                                   class="form-control table_content_zd"
                                                   name="windowName" id="windowName">
                                        </div>
                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd">人员姓名：</label>
                                        <div class="col-sm-8" style="width: 60%;">
                                            <select class="form-control m-b table_content_zd es-input" autocomplete="off"
                                                    id="userName" name="userName" >
                                                <option value="">请选择新增人员姓名</option>
                                            </select>
                                        </div>
                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd">异常事件：</label>
                                        <div class="col-sm-8">
                                            <select class="form-control m-b table_content_zd" disabled="disabled"
                                                    id="anomalyEvent" name="anomalyEvent">
                                                <option value="">请选择异常事件</option>
                                                <option value="离岗">离岗</option>
                                                <option value="睡觉">睡觉</option>
                                                <option value="玩手机">玩手机</option>
                                                <option value="聚众聊天">聚众聊天</option>
                                            </select>
                                        </div>

                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd">开始时间：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false" disabled="disabled"
                                                   class="form-control table_content_zd"
                                                   name="anomalyStartTime" id="anomalyStartTime">
                                        </div>
                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd">结束时间：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false" disabled="disabled"
                                                   placeholder="" class="form-control table_content_zd"
                                                   name="anomalyEndTime" id="anomalyEndTime">
                                        </div>

                                    </div>
                                </td>
                                <td >
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd">确认结果：</label>
                                        <div class="col-sm-8" >
                                            <select class="form-control m-b "
                                                    id="anomalyStatus" name="anomalyStatus">
                                                <option value="">请选择确认结果</option>
                                                <option value="pending">待处理</option>
                                                <option value="valid">有效</option>
                                                <option value="invalid">无效</option>
                                            </select>

                                        </div>

                                    </div>

                                </td>




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
                                            placeholder="请输入人员姓名"
                                            autocomplete="off"
                                            spellcheck="false"
                                            placeholder=""
                                            style="width: 25%;"
                                            class="form-control input_btn_input table_content_zd"
                                            name="inputCommitUserName"
                                            id="inputCommitUserName">
                                    <select
                                            class="form-control table_content_zd" name="selectCommitAnomalyEvent"
                                            id="selectCommitAnomalyEvent"
                                            style="width: 25%; float: left; margin-right: 10px">
                                        <option value="">请选择异常事件</option>
                                        <option value="离岗">离岗</option>
                                        <option value="睡觉">睡觉</option>
                                        <option value="玩手机">玩手机</option>
                                        <option value="聚众聊天">聚众聊天</option>
                                    </select>

                                    <input
                                            type="text"
                                            placeholder="请选择查询时间"
                                            autocomplete="off"
                                            spellcheck="false"
                                            placeholder=""
                                            style="width: 25%;"
                                            class="form-control input_btn_input table_content_zd"
                                            name="selectCommitDate"
                                            id="selectCommitDate">

                                    <input type="checkbox" name="filter" id="showPending"  value="pending" checked="" >显示待处理
                                    <input type="checkbox" name="filter"  id="showValid" value="valid" checked="">显示有效
                                    <input type="checkbox" name="filter"  id="showInvalid" value="invalid" checked="">显示无效

                                    <button type="button"
                                            class="btn btn-sm input_btn_btn search_rm_button_index table_button_zd"
                                            style="margin-top: 2.5px; margin-bottom: 2px;"
                                            id="queCommit">
                                        查询
                                    </button>
                                </td>

                                <td class="table_menu_tr_td_right_zd" colspan="2">
                                    <div style="float: right;">
<%--                                        <button type="button"--%>
<%--                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"--%>
<%--                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"--%>
<%--                                                id="addCommit">新增--%>
<%--                                        </button>--%>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="modifyCommit">确认结果
                                        </button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="clearUser">清除人员
                                        </button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="addUser">新增人员
                                        </button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="deleteCommit">删除
                                        </button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="showPhoto">查看截图
                                        </button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="showVideo">查看视频
                                        </button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="contactManagerButton">报警联系人管理
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

