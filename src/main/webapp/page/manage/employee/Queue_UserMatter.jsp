<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/page/common/includewithnewztreestyle.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <title>禾麦智能大厅管理系统</title>
    <link rel="shortcut icon" href="${ctx}/resources/images/favicon.ico"
          type="image/x-icon" />
    <style type="text/css">
        .table {
            width: 98% !important;
            max-width: 98% !important;
            margin: auto !important;
        }
    </style>

    <link rel="stylesheet" type="text/css"
          href="${ctx}/resources/multiselect/jquery.multiselect.css" />
    <link rel="stylesheet" type="text/css"
          href="${ctx}/resources/multiselect/jquery-ui.css" />
    <script type="text/javascript"
            src="${ctx}/resources/multiselect/jquery-ui.min.js"></script>
    <script type="text/javascript"
            src="${ctx}/resources/multiselect/jquery.multiselect.js"></script>

    <script type="text/javascript" src="./js/Queue_UserMatter.js">
    </script>
    <script type="text/javascript">
        var table;
        var dataId = "";
        var  defaultAreaLs ="${sessionScope.DEFAULT_PROJECT.areaLs}";
        var defaultAreaName="${sessionScope.DEFAULT_PROJECT.areaName}";
        var userInfoLs ="${param.userinfoLs}";


        $(document).ready(function() {
            //加载表格
            loadTable();
            trClick();
            checkBoxStyle_Control();

        });

        function loadTable() {

            var tableUrl = "${ctx}/QueueUsermatter/listall";
            table = $('#example1')
                .DataTable(
                    {
                        "bPaginate": false, //开关，是否显示分页器
                        "paging": false,
                        "lengthChange": true,
                        "searching": false,
                        "ordering": false,
                        "info": false,
                        "autoWidth": false,
                        "sScrollY" : "120px",
                        "displayLength": 10,
                        "sAjaxDataProp": "data",
                        "bServerSide": true,
                        "sAjaxSource":tableUrl,
                        "fnServerData": loadData,
                        "bLengthChange": false,
                        "aoColumns" : [
                            {'mData': 'usermatterLs', 'sTitle': '<input type="checkbox" name="checklist" id="checkall" />', 'sName': 'usermatterLs', 'sClass': 'center'},
                            {'mData': 'usermatterLs', 'sTitle': '序号', 'sName': 'usermatterLs', 'sClass': 'center'},
                            {'mData': 'deptName', 'sTitle': '委办局', 'sName': 'deptName', 'sClass': 'center'},
                            {'mData': 'matterLevel', 'sTitle': '事项等级', 'sName': 'matterLevel', 'sClass': 'center'},
                            {'mData': 'upMatterName', 'sTitle': '一级事项', 'sName': 'upMatterName', 'sClass': 'center'},
                            {'mData': 'matterName', 'sTitle': '二级事项', 'sName': 'matterName', 'sClass': 'center'},
                            {'mData': 'deptLs', 'sTitle': 'deptLs', 'sName': 'deptLs', 'sClass': 'hidden deptLs'},
                            {'mData': 'upMatterLs', 'sTitle': 'upMatterLs', 'sName': 'upMatterLs', 'sClass': 'hidden upMatterLs'},
                            {'mData': 'matterLs', 'sTitle': 'matterLs', 'sName': 'matterLs', 'sClass': 'hidden matterLs'}
                        ],
                        "fnRowCallback" : function(nRow, aData,
                                                   iDisplayIndex) {
                            let api = this.api();
                            let startIndex = api.context[0]._iDisplayStart;//获取本页开始的条数
                            $("td:nth-child(2)", nRow).html(
                                iDisplayIndex + startIndex + 1);//设置序号位于第一列，并顺次加一
                            return nRow;
                        },
                        "initComplete": function( settings, json ) {
                            $(".dataTables_scrollHead").css({overflow:"scroll","overflow-x":"hidden"});
                            $(".dataTables_scrollHeadInner").css({width:"100%"});
                            $(".dataTables_scrollHeadInner table").css({width:"100%"});
                            $(".dataTables_scrollBody").attr('id','scrollBodyDiv');
                            $(".dataTables_scrollBody").css({"overflow-y":"auto","overflow-x":"hidden"});
                            var obj=document.getElementById("scrollBodyDiv");
                            //如果数据DIV有滚动条，则标题头也需要增加滚动条，以保持一致
                            if(obj)
                            {
                                if(obj.scrollHeight>obj.clientHeight||obj.offsetHeight>obj.clientHeight){
                                    $(".dataTables_scrollHead").css({overflow:"scroll","overflow-x":"hidden"});
                                }else{
                                    $(".dataTables_scrollHead").css({overflow:"auto","overflow-x":"hidden"});
                                }
                            }

                        },
                        "drawCallback": function( settings ) {
                            $(".dataTables_scrollHead").css({overflow:"scroll","overflow-x":"hidden"});
                            $(".dataTables_scrollHeadInner").css({width:"100%"});
                            $(".dataTables_scrollHeadInner table").css({width:"100%"});
                            $(".dataTables_scrollBody").attr('id','scrollBodyDiv');
                            $(".dataTables_scrollBody").css({"overflow-y":"auto","overflow-x":"hidden"});
                            var obj=document.getElementById("scrollBodyDiv");
                            //如果数据DIV有滚动条，则标题头也需要增加滚动条，以保持一致
                            if(obj) {
                                if (obj.scrollHeight > obj.clientHeight || obj.offsetHeight > obj.clientHeight) {
                                    $(".dataTables_scrollHead").css({
                                        overflow: "scroll",
                                        "overflow-x": "hidden"
                                    });
                                } else {
                                    $(".dataTables_scrollHead").css({overflow: "auto", "overflow-x": "hidden"});
                                }
                            }

                        },
                        //对列的数据显示进行设置
                        "columnDefs" : [
                            {targets: 0,data: "objLs",title: "操作",
                                render: function (data, type, row, meta) {
                                    var html = "<input type='checkbox' value="+row.usermatterLs+" class='lsCheck' name='choice' />";
                                    return html;
                                }
                            }
                        ]
                    });
        }

        function loadData(sSource, aoData, fnCallback) {
            dataId = "";
            var pageSize = aoData.iDisplayLength;
            var pageNo = aoData.iDisplayStart % aoData.iDisplayLength == 0 ? aoData.iDisplayStart / aoData.iDisplayLength + 1 : aoData.iDisplayStart / aoData.iDisplayLength;
            var params = {
                "userinfoLs":userInfoLs,
                "pageSize":pageSize,
                "pageNo":pageNo
            };

            $.ajax({
                type: 'POST',
                url: sSource,
                cache: false,
                async: false,
                dataType: 'json',
                data: params,
                success: function (resule) {
                    var suData = resule;
                    var datainfo = suData.returnObject;
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

    </script>

<body class="gray-bg">
<div id="content" style="padding-top: 0px;">
    <div class="div-inherit">
        <form class="form-horizontal" role="form" action="#" method="post" id="itemInfoForm">
            <table class="table_zd" align="center" width="98%">
                <tr>
                    <td style="width: 33%;">
                        <div class="form-group">
                            <label style="width: 40%;"
                                   class="col-sm-3 control-label input_lable_hm table_label_zd " ><span
                                    class="must_field">*</span>委办局：</label>
                            <div class="col-sm-8">
                                <select class="form-control m-b table_content_zd"
                                        name="deptLs" id="belongDept"
                                        onchange="changeDeptGetUpMatter();">
                                </select>
                            </div>
                        </div> <input type="hidden" name="userinfoLs" id="userInfoLs" />
                        <input type="hidden" name="usermatterLs" id="userMatterLs" />
                    </td>
                    <td style="width: 33%;">
                        <div class="form-group">
                            <label style="width: 40%;"
                                   class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                    class="must_field">*</span>事项等级：</label>
                            <div class="col-sm-8">
                                <select class="form-control m-b table_content_zd"
                                        name="matterLevel" id="matterLevel"
                                        onchange="changeLevelGetUpMatter();">
                                    <option value="1">1级</option>
                                    <option value="2">2级</option>
                                </select>
                            </div>
                        </div>
                    </td>
                    <td style="width: 33%;">
                        <div class="form-group">
                            <label style="width: 40%;"
                                   class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                    class="must_field">*</span>一级事项：</label>
                            <div class="col-sm-8">
                                <select class="td-select table_content_zd"
                                        name="upMatterLs" id="upMatterLs"
                                        style="width: 100%" multiple="multiple"
                                        onchange="getTwoLevelMatterList('');">
                                </select>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr >
                    <td style="width: 33%;">
                        <div class="form-group">
                            <label style="width: 40%;"
                                   class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                    class="must_field">*</span>二级事项：</label>
                            <div class="col-sm-8" style="padding-top:5px;">
                                <select class="td-select table_content_zd"
                                        name="matterLs" id="belongMatter"
                                        style="width: 100%;" multiple="multiple">
                                </select>
                            </div>
                        </div>
                    </td>
                    <td style="width: 33%;">
                        <div class="form-group"></div>
                    </td>
                    <td style="width: 33%;">
                        <div class="form-group"></div>
                    </td>
                </tr>
                <tr class="table_menu_tr_zd">
                    <td colspan="3" class="table_menu_tr_td_right_zd">
                        <button type="button"
                                style="margin-top: 2.5px; margin-bottom: 2px;"
                                class="btn btn-primary btn-sm input_btn_btn list_btn"
                                id="deleteItem">删除</button>
                        <button type="button"
                                style="margin-top: 2.5px; margin-bottom: 2px;"
                                class="btn btn-primary btn-sm input_btn_btn list_btn"
                                id="modifyItem">修改</button>
                        <button type="button"
                                style="margin-top: 2.5px; margin-bottom: 2px;"
                                class="btn btn-primary btn-sm input_btn_btn list_btn"
                                id="addItem">新增</button>
                    </td>
                </tr>
            </table>
            <table id="example1" class="table table-bordered" style="table-layout:fixed">

            </table>
        </form>
    </div>
</div>
</body>

</html>


