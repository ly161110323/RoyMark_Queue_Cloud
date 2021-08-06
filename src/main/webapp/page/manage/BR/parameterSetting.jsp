<%--
  Created by IntelliJ IDEA.
  User: gulante
  Date: 2021/4/12
  Time: 22:56
  To change this template use File | Settings | File Templates.
--%>
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
    <script type="text/javascript" src="./js/parameterSetting.js">
    </script>

    <script type="text/javascript">
        var table;
        var dataId = "";
        var isSearch = "0";


        var searchData = {};
        $(document).ready(function() {
            //加载表格
            loadTable();
            trClick();
            checkBoxStyle_Control();

        });

        function loadTable() {

            var tableUrl = "${ctx}/param/queryData";
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
                        "sAjaxSource":tableUrl,
                        "fnServerData": loadData,
                        "bLengthChange": false,
                        "aoColumns" : [
                            {'mData': 'paramHiddenId', 'sTitle': '<input type="checkbox" name="checklist" id="checkall" />', 'sName': 'paramHiddenId', 'sClass': 'center'},
                            {'mData': 'paramHiddenId', 'sTitle': '序号', 'sName': 'paramHiddenId', 'sClass': 'center'},
                            {'mData': 'paramName', 'sTitle': '参数名称', 'sName': 'paramName', 'sClass': 'center'},
                            {'mData': 'paramValue', 'sTitle': '参数值', 'sName': 'paramValue', 'sClass': 'center'},
                            {'mData': 'paramDefault', 'sTitle': '默认值', 'sName': 'paramDefault', 'sClass': 'center'},
                            {'mData': 'paramRemark', 'sTitle': '备注', 'sName': 'paramRemark', 'sClass': 'center'},

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
                        "columnDefs": [
                            {targets: 0,data: "paramHiddenId",title: "操作",
                                render: function (data, type, row, meta) {
                                    var html = "<input type='checkbox' value="+row.paramHiddenId+" class='lsCheck' name='choice' />";
                                    html+="<input type='hidden' name='deptImagepath' value="+row.deptImagepath+"></input>";
                                    return html;
                                }
                            },
                        ]
                    });
        }

        function loadData(sSource, aoData, fnCallback) {
            // console.log(sSource)
            // console.log(aoData)

            var pageSize = aoData.iDisplayLength;
            var pageNo = aoData.iDisplayStart % aoData.iDisplayLength == 0 ? aoData.iDisplayStart / aoData.iDisplayLength+1  : aoData.iDisplayStart / aoData.iDisplayLength;
            var that =this;

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
                type : 'POST',
                url : sSource,
                cache:false,
                async:true,
                dataType : 'json',
                data : params,
                success : function(result) {
                    isSearch = "0";

                    var pagelist = result.pageList;
                    var datainfos = pagelist.records
                    var obj = {};
                    obj['data'] = datainfos;
                    console.log(obj)
                    if(typeof(datainfos)!="undefined"&&datainfos.length>0){
                        obj.iTotalRecords = pagelist.total;
                        obj.iTotalDisplayRecords = pagelist.total;
                        that.api().page(pageList.current-1);
                        fnCallback(obj);
                    }else if((typeof(datainfos)=="undefined")&&pageNo>1){
                        var oTable = $("#itemResultTable").dataTable();
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


                        <table class="table_zd" align="center" width="100%">
                            <tr>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                style="color: red;">*</span>参数名称：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   class="form-control table_content_zd"
                                                   name="paramName" id="paramName">
                                        </div>
                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                style="color: red;">*</span>参数值</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   placeholder="" class="form-control table_content_zd"
                                                   name="paramValue" id="paramValue">
                                        </div>

                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd">默认值</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   placeholder="" class="form-control table_content_zd"
                                                   name="paramDefault" id="paramDefault">
                                        </div>

                                    </div>
                                </td>

                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd">备注：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   placeholder="" class="form-control table_content_zd"
                                                   name="paramRemark" id="paramRemark">
                                        </div>

                                    </div>
                                </td>

                            </tr>
                        </table>
                        <table class="table_zd" align="center" width="100%" style="margin-bottom:-12px;">
                            <tbody>
                            <tr class="table_menu_tr_zd" >
                                <td class="table_menu_tr_td_left_zd" colspan="2">
                                    <input type="text" placeholder="参数名称" autocomplete="off"
                                           spellcheck="false" placeholder="" style="width: 35%;"
                                           class="form-control input_btn_input table_content_zd"
                                           name="inputCommitParamName" id="inputCommitParamName"
                                    >

                                    <button type="button"
                                            class="btn btn-sm input_btn_btn search_rm_button_index table_button_zd"
                                            style="margin-top: 2.5px; margin-bottom: 2px;"
                                            id="queCommit">查询</button>
                                </td>

                                <td class="table_menu_tr_td_right_zd" colspan="2">
                                    <div style="float: right;">
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="addCommit">新增</button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="modifyCommit">修改</button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="clearData">清除</button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="deleteCommit">删除</button>
<%--                                        <button type="button"--%>
<%--                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"--%>
<%--                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"--%>
<%--                                                id="configWindow">显示配置按钮</button>--%>
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
                                    id="caseArea_administration">办事区域管理</button>
                            <button type="button"
                                    class="btn btn-primary btn-sm input_btn_btn list_btn"
                                    style="float: left; margin-bottom: 2px;"
                                    id="selectArea_administration">行政区域管理</button>
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