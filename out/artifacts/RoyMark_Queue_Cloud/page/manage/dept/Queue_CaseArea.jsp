<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/page/common/includewithnewztreestyle.jsp"%>
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
    <script type="text/javascript" src="./js/Queue_CaseArea.js">
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

            var tableUrl = "${ctx}/QueueCasearea/listall";
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
                        "sScrollY" : "240",
                        "displayLength": 10,
                        "sAjaxDataProp": "data",
                        "bServerSide": true,
                        "sAjaxSource":tableUrl,
                        "fnServerData": loadData,
                        "bLengthChange": false,
                        "aoColumns" : [
                            {'mData': 'caseareaLs', 'sTitle': '<input type="checkbox" name="checklist" id="checkall" />', 'sName': 'caseareaLs', 'sClass': 'center'},
                            {'mData': 'caseareaLs', 'sTitle': '序号', 'sName': 'caseareaLs', 'sClass': 'center'},
                            {'mData': 'caseareaName', 'sTitle': '区域名称', 'sName': 'caseareaName', 'sClass': 'center'},
                            {'mData': 'caseareaId', 'sTitle': '区域编号', 'sName': 'caseareaId', 'sClass': 'center'},
                            {'mData': 'caseareaPrint', 'sTitle': '打印前缀', 'sName': 'caseareaPrint', 'sClass': 'center'},
                            {'mData': 'caseareaOrderno', 'sTitle': '顺序号', 'sName': 'caseareaOrderno', 'sClass': 'center'},
                            {'mData': 'areaLs', 'sTitle': 'areaLs', 'sName': 'areaLs', 'sClass': 'hidden areaLs'},
                            {'mData': 'areaName', 'sTitle': 'areaName', 'sName': 'areaName', 'sClass': 'hidden areaName'},
                            {'mData': 'hallareaLs', 'sTitle': 'hallareaLs', 'sName': 'hallareaLs', 'sClass': 'hidden hallareaLs'},
                            {'mData': 'caseareaIsurl', 'sTitle': 'caseareaIsurl', 'sName': 'caseareaIsurl', 'sClass': 'hidden caseareaIsurl'},
                            {'mData': 'caseareaUrlstr', 'sTitle': 'caseareaUrlstr', 'sName': 'caseareaUrlstr', 'sClass': 'hidden caseareaUrlstr'},
                            // {'mData': 'hallareaName', 'sTitle': '所属政务中心', 'hallareaName': 'hallareaName', 'sClass': 'center'},
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
                            if(obj)
                            {
                            //如果数据DIV有滚动条，则标题头也需要增加滚动条，以保持一致
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
                            {targets: 0,data: "caseareaLs",title: "操作",
                                render: function (data, type, row, meta) {
                                    var html = "<input type='checkbox' value="+row.caseareaLs+" class='lsCheck' name='choice' />";
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

<body>
<div id="content" style="padding-top: 0px;">
    <div class="div-inherit">
        <form class="form-horizontal" role="form" action="#" method="post" id="itemInfoForm">
                        <table class="table_zd" align="center" width="98%" style="margin-bottom:-12px;">
                            <tr>
                                <td style="width: 33%;">
                                    <div class="form-group">
                                        <label style="width: 30%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                class="must_field">*</span>所属大厅：</label>
                                        <div class="col-sm-8">
                                            <input type="text" style="width: 100%;" autocomplete="off"
                                                   class="form-control m-b input_btn_input table_content_zd"
                                                   readonly="readonly" name="Area_Name" id="Area_Name">
                                            <input type="hidden" name="areaLs" id="Area_Ls">
                                        </div>
                                    </div>
                                </td>

                                <td style="width: 33%;">
                                    <div class="form-group">
                                        <label style="width: 30%;" class="col-sm-3 control-label input_lable_hm table_label_zd"><span style="color: red;">*</span>区域名称：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"  placeholder="" class="form-control table_content_zd" name="queueCaseArea.caseAreaName" id="caseAreaName">
                                        </div>
                                        <input type="hidden" name="queueCaseArea.caseAreaLs" id="caseAreaLs" />
                                    </div>
                                </td>
                                <td style="width: 33%;">
                                    <div class="form-group">
                                        <label style="width: 30%;" class="col-sm-3 control-label input_lable_hm table_label_zd"><span style="color: red;">*</span>区域编号：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off"  spellcheck="false"  placeholder="" class="form-control table_content_zd" name="queueCaseArea.caseAreaId" id="caseAreaId">
                                        </div>

                                    </div>
                                </td>


                            </tr>
                            <tr>
                                <td style="width: 33%;">
                                    <div class="form-group">
                                        <label style="width: 30%;" class="col-sm-3 control-label input_lable_hm table_label_zd">打印前缀：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off"  spellcheck="false"  placeholder=""  class="form-control table_content_zd" name="queueCaseArea.caseAreaPrint" id="caseAreaPrint">
                                        </div>
                                        <input type="hidden"  />
                                    </div>
                                </td>
                                <td style="width: 33%;">
                                    <div class="form-group">
                                        <label style="width: 30%;" class="col-sm-3 control-label input_lable_hm table_label_zd"><span style="color: red;">*</span>顺序号：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"  placeholder="" class="form-control table_content_zd" name="queueCaseArea.caseAreaOrderNo" id="caseAreaOrderNo">
                                        </div>

                                    </div>
                                </td>
                                <td style="width: 33%;">
                                    <div class="form-group">
                                        <label style="width: 30%;" class="col-sm-3 control-label input_lable_hm table_label_zd">是否是链接：</label>
                                        <div class="col-sm-8">
                                            <div class="radio i-checks radio_rm">
                                                <label> <input type="radio"
                                                               name="caseareaIsurl" value="1" id="allYes">
                                                    <i></i> 是
                                                </label>      &nbsp;&nbsp; &nbsp;&nbsp;
                                            </div>

                                            <div class="radio i-checks radio_rm">
                                                <label> <input type="radio"
                                                               name="caseareaIsurl" value="0" id="allNo">
                                                    <i></i> 否
                                                </label>
                                            </div>
                                        </div>

                                    </div>
                                </td>
                            </tr>
                            <!--新增列-->
                            <tr>
                                <td style="width: 33%;">
                                    <div class="form-group">
                                        <label style="width: 30%;" class="col-sm-3 control-label input_lable_hm table_label_zd">链接地址：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"  placeholder="" class="form-control table_content_zd"
                                                   name="queueCaseArea.caseareaUrlstr" id="caseareaUrlstr">
                                        </div>

                                    </div>
                                </td>
                                <td style="width: 33%;">
                                    <div class="form-group">


                                    </div>
                                </td>
                                <td style="width: 33%;">
                                    <div class="form-group">


                                    </div>
                                </td>
                            </tr>
                            <!--新增列结束-->

                            <tr style="background-color: aliceblue;border-top: 7px solid #FFFFFF;">
                                <td style="border: 1px solid aliceblue;padding-right:10px;" colspan="4">
                                    <button type="button"
                                            class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                            id="deleteItem">删除</button>
                                    <button type="button"
                                            class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                            id="modifyItem">修改</button>
                                    <button type="button"
                                            class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                            id="addItem">新增</button>
                                </td>
                            </tr>
                        </table>
                    </form>

                    <table id="itemResultTable" class="table table-bordered"></table>
    </div>
</div>
</body>

</html>


