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
    <script type="text/javascript" src="./js/Queue_SelectArea.js">
    </script>
    <script type="text/javascript">
        var table;
        var dataId = "";
        var  defaultAreaLs ="${sessionScope.DEFAULT_PROJECT.areaLs}";
        var defaultAreaName="${sessionScope.DEFAULT_PROJECT.areaName}";



        $(document).ready(function() {
            //加载表格
            loadTable();
            trClick();


        });

        function loadTable() {

            var tableUrl = "${ctx}/QueueSelectarea/listall";
            table = $('#itemResultTable')
                .DataTable(
                    {
                        "bPaginate": false, //开关，是否显示分页器
                        "paging": false,
                        "lengthChange": true,
                        "searching": false,
                        "ordering": false,
                        "info": false,
                        "autoWidth": false,
                        "sScrollY" : 265,
                        "displayLength": 10,
                        "sAjaxDataProp": "data",
                        "bServerSide": true,
                        "sAjaxSource":tableUrl,
                        "fnServerData": loadData,
                        "bLengthChange": false,
                        "aoColumns" : [
                            {'mData': 'selectareaLs', 'sTitle': '<input type="checkbox" name="checklist" id="checkall" />', 'sName': 'selectareaLs', 'sClass': 'center'},
                            {'mData': 'selectareaLs', 'sTitle': '序号', 'sName': 'selectareaLs', 'sClass': 'center'},
                            {'mData': 'selectareaName', 'sTitle': '区域名称', 'sName': 'selectareaName', 'sClass': 'center'},
                            {'mData': 'selectareaId', 'sTitle': '区域编号', 'sName': 'selectareaId', 'sClass': 'center'},
                            {'mData': 'selectareaPrint', 'sTitle': '打印前缀', 'sName': 'selectareaPrint', 'sClass': 'center'},
                            {'mData': 'selectareaOrderno', 'sTitle': '顺序号', 'sName': 'selectareaOrderno', 'sClass': 'center'},
                            {'mData': 'areaLs', 'sTitle': 'areaLs', 'sName': 'areaLs', 'sClass': 'hidden areaLs'},
                            {'mData': 'areaName', 'sTitle': 'areaName', 'sName': 'areaName', 'sClass': 'hidden areaName'},

                        ],
                        "fnRowCallback" : function(nRow, aData, iDisplayIndex){
                            let api = this.api();
                            let startIndex = api.context[0]._iDisplayStart;//获取本页开始的条数
                            $("td:nth-child(2)", nRow).html(iDisplayIndex+startIndex+1);//设置序号位于第一列，并顺次加一
                            return nRow;
                        },
                        "initComplete": function( settings, json ) {
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
                        "fnRowCallback" : function(nRow, aData, iDisplayIndex){
                            let api = this.api();
                            let startIndex = api.context[0]._iDisplayStart;//获取本页开始的条数
                            $("td:nth-child(2)", nRow).html(iDisplayIndex+startIndex+1);//设置序号位于第一列，并顺次加一
                            return nRow;
                        },
                        "columnDefs": [
                            {targets: 0,data: "LS",title: "操作",
                                render: function (data, type, row, meta) {
                                    var html = "<input type='checkbox' value="+row.selectareaLs+" class='lsCheck' name='choice' />";
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
            var params = {};   	//"info.select_info":$("#select_info").val().trim(),
            // "info.pageSize":pageSize,
            // "info.pageNo":pageNo
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
<div id="content" style="padding-top: 0px;">
    <div class="div-inherit">
        <form class="form-horizontal" role="form" action="#" method="post" id="itemInfoForm">
            <table class="table_zd" align="center" width="98%" >
                <tr>
                    <td style="width: 33%;">
                        <div class="form-group">
                            <label style="width: 30%;"
                                   class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                    style="color: red;">*</span>区域名称：</label>
                            <div class="col-sm-8">
                                <input type="text" autocomplete="off" placeholder="" spellcheck="false"
                                       class="form-control table_content_zd"
                                       name="selectareaName" id="selectAreaName">
                            </div>
                            <input type="hidden" name="selectareaLs"
                                   id="selectAreaLs" />
                        </div>
                    </td>
                    <td style="width: 33%;">
                        <div class="form-group">
                            <label style="width: 33%;"
                                   class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                    style="color: red;">*</span>区域编号：</label>
                            <div class="col-sm-8">
                                <input type="text" autocomplete="off" placeholder="" spellcheck="false"
                                       class="form-control table_content_zd"
                                       name="selectareaId" id="selectAreaId">
                            </div>

                        </div>
                    </td>

                    <td style="width: 33%;">
                        <div class="form-group">
                            <label style="width:40%;"
                                   class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                    class="must_field">*</span>所属大厅：</label>
                            <div class="col-sm-8">
                                <input type="text" style="width: 98%;" autocomplete="off"
                                       class="form-control m-b input_btn_input table_content_zd"
                                       readonly="readonly" name="Area_Name" id="Area_Name">
                                <input type="hidden" name="areaLs" id="Area_Ls">
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 33%;">
                        <div class="form-group">
                            <label style="width: 30%;"
                                   class="col-sm-3 control-label input_lable_hm table_label_zd">打印前缀：</label>
                            <div class="col-sm-8">
                                <input type="text" autocomplete="off" placeholder="" spellcheck="false"
                                       class="form-control table_content_zd"
                                       name="selectareaPrint" id="selectAreaPrint">
                            </div>
                            <input type="hidden" />
                        </div>
                    </td>
                    <td style="width: 33%;">
                        <div class="form-group">
                            <label style="width: 33%;"
                                   class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                    style="color: red;">*</span>顺序号：</label>
                            <div class="col-sm-8">
                                <input type="text" autocomplete="off" placeholder="" spellcheck="false"
                                       class="form-control table_content_zd"
                                       name="selectareaOrderno"
                                       id="selectAreaOrderNo">
                            </div>

                        </div>
                    </td>
                    <td style="width: 33%;">
                        <div class="form-group">


                        </div>
                    </td>
                </tr>

                <tr style="background-color: aliceblue;border-top: 7px solid #FFFFFF;">
                    <td style="border: 1px solid aliceblue;padding-right:10px;" colspan="4">
                        <button type="button"
                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                style="margin-top:2.5px;margin-bottom:2px;"
                                id="deleteItem">删除</button>
                        <button type="button"
                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                style="margin-top:2.5px;margin-bottom:2px;"
                                id="modifyItem">修改</button>
                        <button type="button"
                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                style="margin-top:2.5px;margin-bottom:2px;"
                                id="addItem">新增</button>
                    </td>
                </tr>
            </table>
            <table id="itemResultTable" class="table table-bordered" style="table-layout:fixed">

            </table>
        </form>
    </div>
</div>
</body>

</html>


