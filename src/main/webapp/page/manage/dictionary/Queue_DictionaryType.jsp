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
    <style type="text/css">
        .table{width:98%!important;max-width:98%!important;margin:auto!important;}
    </style>
    <link rel="shortcut icon" href="${ctx}/resources/images/favicon.ico"
          type="image/x-icon"/>
    <script type="text/javascript" src="./js/Queue_DictionaryType.js">
    </script>

    <script type="text/javascript">


        var table;
        var dataId = "";
        var isSearch = "0";

        $(document).ready(function () {
            //加载表格
            loadTable();


        });

        function loadTable() {
            $(".i-checks").iCheck({checkboxClass: "icheckbox_square-green", radioClass: "iradio_square-green",});
            $("#yes").iCheck('check');
            $("#no").iCheck('uncheck');
            var tableUrl = "${ctx}/QueueDictionarytype/listall";
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
                        "sScrollY" : 225,
                        "displayLength": 10,
                        "sAjaxDataProp": "data",
                        "bServerSide": true,
                        "sAjaxSource": tableUrl,
                        "fnServerData": loadData,
                        "bLengthChange": false,
                        "aoColumns": [
                            {'mData': 'dictionarytypeLs', 'sTitle': '<input type="checkbox" name="checklist" id="checkall" />', 'sName': 'dictionarytypeLs', 'sClass': 'center'},
                            {'mData': 'dictionarytypeLs', 'sTitle': '序号', 'sName': 'dictionarytypeLs', 'sClass': 'center'},
                            {'mData': 'dictionarytypeName', 'sTitle': '字典类型名称', 'sName': 'dictionarytypeName', 'sClass': 'center'},
                            {'mData': 'dictionarytypeId', 'sTitle': '字典类型编号', 'sName': 'dictionarytypeId', 'sClass': 'center'},
                            {'mData': 'dictionarytypeIsshow', 'sTitle': '是否启用', 'sName': 'dictionarytypeIsshow', 'sClass': 'center'}
                        ],
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
                            $(".dataTables_scrollHead").css({overflow:"scroll","overflow-x":"hidden"});
                            $(".dataTables_scrollBody").attr('id','scrollBodyDiv');
                            $(".dataTables_scrollBody").css({"overflow-y":"auto","overflow-x":"hidden"});
                            var obj=document.getElementById("scrollBodyDiv");
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
                            {targets: 0,data: "dictionarytypeLs",title: "操作",
                                render: function (data, type, row, meta) {
                                    var html = "<input type='checkbox' value="+row.dictionarytypeLs+" class='lsCheck' name='choice' />";
                                    return html;
                                }
                            },
                            {targets: 4,data: "dictionarytypeIsshow",title: "是否启用",
                                render: function (data, type, row, meta) {
                                    return data == "1"?"是":"否";
                                }
                            }
                        ]
                    });

            trClick();
            checkBox_init();
        }

        function loadData(sSource, aoData, fnCallback) {
            var pageSize = aoData.iDisplayLength;
            var pageNo = aoData.iDisplayStart % aoData.iDisplayLength == 0 ? aoData.iDisplayStart / aoData.iDisplayLength + 1 : aoData.iDisplayStart / aoData.iDisplayLength;
            if (isSearch == "1") {
                pageNo = 0;
                pageSize = 10;
            }

            dataId = "";
            $.ajax({
                type: 'POST',
                url: sSource,
                cache: false,
                async: true,
                dataType: 'json',
                data: {
                    "dictionarytypeName":$("#terminal_name_query").val().trim(),
                    "pageSize": pageSize,
                    "pageNo": pageNo
                },
                success: function (resule) {
                    isSearch = "0";
                    var suData = resule;
                    var datainfo = suData.returnObject;
                    var obj = {};
                    obj['data'] = datainfo;
                    if(typeof(datainfo)!="undefined"&&datainfo.length>0){
                        obj.iTotalRecords = suData.total;
                        obj.iTotalDisplayRecords = suData.total;
                        fnCallback(obj);
                    }else if((typeof(datainfo)=="undefined")&&pageNo>1){
                        var oTable = $("#example1").dataTable();
                        oTable.fnPageChange(0);
                    }else{
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
                        <table class="table_zd"   align="center" width="98%">
                            <tr>
                                <td style="width: 33%;">
                                    <div class="form-group">
                                        <label style="width: 36%;" class="col-sm-3 control-label input_lable_hm table_label_zd"><span class="must_field">*</span>字典类型名称：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" placeholder="字典类型名称" class="form-control table_content_zd" id="dictionaries_type_name" name="dictionaries_type_name">
                                        </div>
                                    </div>
                                </td>
                                <td style="width: 33%;">
                                    <div class="form-group">
                                        <label style="width: 36%;" class="col-sm-3 control-label input_lable_hm table_label_zd"><span class="must_field">*</span>字典类型编号：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" placeholder="字典类型编号" class="form-control table_content_zd" id="dictionaries_type_id" name="dictionaries_type_id">
                                        </div>
                                    </div>
                                </td>
                                <td style="width: 33%;">
                                    <div class="form-group">
                                        <label style="width: 30%;" class="col-sm-3 control-label input_lable_hm table_label_zd"><span class="must_field">*</span>是否启用：</label>
                                        <div class="col-sm-8" style="margin-top: -4px;margin-left: -15px;">
                                            <div class="radio i-checks radio_rm">
                                                <label>
                                                    <input type="radio" name="dictionaryType_IsShow" value="1" id="yes">
                                                    <i></i> 是
                                                </label>
                                            </div>
                                            <div class="radio i-checks radio_rm">
                                                <label>
                                                    <input type="radio" name="dictionaryType_IsShow" value="0" id="no">
                                                    <i></i> 否
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr class="table_menu_tr_zd">
                                <td class="table_menu_tr_td_left_zd">
                                    <input type="text"  placeholder="字典类型名称" autocomplete="off" class="form-control input_btn_input table_content_zd" id="terminal_name_query" name="terminal_name_query" style="width:70%;">
                                    <button type="button" class="table_button_zd  btn btn-sm input_btn_btn search_rm_button_index" id="index_select">查询</button>
                                </td>
                                <td class="table_menu_tr_td_right_zd" colspan="3">
                                    <%--<button type="button" class="btn btn-sm input_btn_btn list_btn table_button_zd" id="deleteObj" disabled="disabled">删除</button>--%>
                                    <button type="button" class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd" id="clearData">清除</button>
                                    <button type="button" class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd" id="updateDataBtn">修改</button>
                                    <button type="button" class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd" id="addDataBtn">新增</button>
                                </td>
                            </tr>
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


