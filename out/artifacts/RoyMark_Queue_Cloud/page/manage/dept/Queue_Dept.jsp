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
    <script type="text/javascript" src="./js/Queue_Dept.js">
    </script>

    <script type="text/javascript">
        var table;
        var dataId = "";
        var isSearch = "0";
        var  defaultAreaLs ="${sessionScope.DEFAULT_PROJECT.areaLs}";
        var defaultAreaName="${sessionScope.DEFAULT_PROJECT.areaName}";

        $(document).ready(function() {
            //加载表格
            loadTable();
            trClick();
            checkBoxStyle_Control();

        });

        function loadTable() {

            var tableUrl = "${ctx}/QueueDept/list";
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
                        "sAjaxSource":tableUrl,
                        "fnServerData": loadData,
                        "bLengthChange": false,
                        "aoColumns" : [
                            {'mData': 'deptLs', 'sTitle': '<input type="checkbox" name="checklist" id="checkall" />', 'sName': 'deptLs', 'sClass': 'center'},
                            {'mData': 'deptLs', 'sTitle': '序号', 'sName': 'deptLs', 'sClass': 'center'},
                            {'mData': 'deptName', 'sTitle': '委办局名称', 'sName': 'deptName', 'sClass': 'center'},
                            {'mData': 'deptOrderno', 'sTitle': '显示顺序', 'sName': 'deptOrderno', 'sClass': 'center'},
                            {'mData': 'deptPrint', 'sTitle': '打印前缀', 'sName': 'deptPrint', 'sClass': 'center'},
                            {'mData': 'deptMaxtakeno', 'sTitle': '现场取号上限', 'sName': 'deptMaxtakeno', 'sClass': 'center'},
                            {'mData': 'deptMaxappointment', 'sTitle': '现场预约上限', 'sName': 'controlTabletIp', 'sClass': 'center'},
                            {'mData': 'deptImagepath', 'sTitle': '委办局图标', 'sName': 'deptImagepath', 'sClass': 'center'},
                            {'mData': 'deptId', 'sTitle': '委办局编号', 'sName': 'deptId', 'sClass': 'center'},
                            {'mData': 'caseareaLs', 'sTitle': 'caseareaLs', 'sName': 'caseareaLs', 'sClass': 'hidden caseareaLs'},
                            {'mData': 'isHaveMatter', 'sTitle': 'isHaveMatter', 'sName': 'typeLs', 'sClass': 'hidden isHaveMatter'},
                            {'mData': 'isHaveUser', 'sTitle': 'isHaveUser', 'sName': 'isHaveUser', 'sClass': 'hidden isHaveUser'},
                            {'mData': 'selectareaLs', 'sTitle': 'selectareaLs', 'sName': 'selectareaLs', 'sClass': 'hidden selectareaLs'},
                            {'mData': 'otherId', 'sTitle': '第三方编号', 'sName': 'otherId', 'sClass': 'center'}
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
                            {targets: 0,data: "deptLs",title: "操作",
                                render: function (data, type, row, meta) {
                                    var html = "<input type='checkbox' value="+row.deptLs+" class='lsCheck' name='choice' />";
                                    html+="<input type='hidden' name='deptImagepath' value="+row.deptImagepath+"></input>";
                                    return html;
                                }
                            },{
                                targets: 7,data: "deptImagepath",title: "委办局图标名称",
                                render: function (data, type, row, meta) {
                                    var deptImagepath=row.deptImagepath;
                                    var deptImageName='';
                                    if(deptImagepath!=null && deptImagepath!='' && typeof(deptImagepath)!='undefined')
                                    {
                                     deptImageName=deptImagepath.substring(deptImagepath.lastIndexOf("/")+1);
                                    }
                                    return deptImageName;
                                }
                            }
                        ]
                    });
        }

        function loadData(sSource, aoData, fnCallback) {

            var pageSize = aoData.iDisplayLength;
            var pageNo = aoData.iDisplayStart % aoData.iDisplayLength == 0 ? aoData.iDisplayStart / aoData.iDisplayLength + 1 : aoData.iDisplayStart / aoData.iDisplayLength;
            if(isSearch=="1"){
                pageNo = 0;
                pageSize = 10;
            }
            dataId = "";
            var params;
            //设置参数
            var caseAreaObj = $("[name=selectCaseLs]>option:selected");
            var selectedArea = caseAreaObj.val();
            var deptName = $("#selectCommitOfficeName").val();

            if(caseAreaObj.val() != ""){
                params = {
                    "deptName":deptName,
                    "caseareaLs":caseAreaObj.val(),
                    "pageSize":pageSize,
                    "pageNo":pageNo,
                };
            }else{
                params = {
                    "deptName":deptName,
                    "pageSize":pageSize,
                    "pageNo":pageNo
                };
            }

            $.ajax({
                type : 'POST',
                url : sSource,
                cache:false,
                async:true,
                dataType : 'json',
                data : params,
                success : function(resule) {
                    isSearch = "0";
                    var suData = resule.returnObject[0];
                    var datainfo = suData.records
                    var obj = {};
                    obj['data'] = datainfo;
                    if(typeof(datainfo)!="undefined"&&datainfo.length>0){
                        obj.iTotalRecords = suData.total;
                        obj.iTotalDisplayRecords = suData.total;
                        fnCallback(obj);
                    }else if((typeof(datainfo)=="undefined")&&pageNo>1){
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
                                                        class="must_field">*</span>所属大厅：</label>
                                                <div class="col-sm-8">
                                                    <input type="text" style="width: 100%;" autocomplete="off"
                                                           class="form-control m-b input_btn_input table_content_zd"
                                                           readonly="readonly" name="Area_Name" id="Area_Name">
                                                    <input type="hidden" name="areaLs" id="Area_Ls">
                                                </div>
                                            </div>
                                        </td>

                                        <td style="width: 25%;">
                                            <div class="form-group">
                                                <label style="width: 38%;"
                                                       class="col-sm-3 control-label input_lable_hm table_label_zd">部门图标：</label>
                                                <div class="col-sm-8" style="width: 60%;">
                                                    <input type="file" name="departmentIcon" id="departmentIcon"
                                                           style="display: none;"> <input type="text"
                                                                                          style="width: 65%;" autocomplete="off"
                                                                                          placeholder="768*365"
                                                                                          class="form-control m-b input_btn_input"
                                                                                          readonly="readonly" name="departmentIconFileName"
                                                                                          id="departmentIconFileName">
                                                    <button type="button"
                                                            class="btn btn-primary btn-sm input_btn_btn"
                                                            id="btnChooseDepartmentIcon"
                                                            style="float: left; margin-left: 5px;">选择</button>


                                                </div>

                                            </div>
                                        </td>

                                        <td style="width: 25%;">
                                            <div class="form-group">
                                                <label style="width: 38%;"
                                                       class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                        style="color: red;">*</span>委办局编号：</label>
                                                <div class="col-sm-8">
                                                    <input type="text" autocomplete="off" spellcheck="false"
                                                           placeholder="" class="form-control table_content_zd"
                                                           name="queueDept.deptId" id="txtDeptId">
                                                </div>
                                                <input type="hidden" name="queueDept.deptLs" id="txtDeptLs" />
                                            </div>
                                        </td>

                                        <td style="width: 25%;">
                                            <div class="form-group">
                                                <label style="width: 38%;"
                                                       class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                        style="color: red;">*</span>委办局名称：</label>
                                                <div class="col-sm-8">
                                                    <input type="text" autocomplete="off" spellcheck="false"
                                                           class="form-control table_content_zd"
                                                           name="queueDept.deptName" id="txtDeptName">
                                                </div>
                                            </div>
                                        </td>

                                    </tr>
                                    <tr>
                                        <td style="width: 25%;">
                                            <div class="form-group">
                                                <label style="width: 38%;"
                                                       class="col-sm-3 control-label input_lable_hm table_label_zd">打印前缀：</label>
                                                <div class="col-sm-8">
                                                    <input type="text" autocomplete="off" spellcheck="false"
                                                           placeholder="" class="form-control table_content_zd"
                                                           name="queueDept.deptPrint" id="txtDeptPrint">
                                                </div>

                                            </div>
                                        </td>

                                        <td style="width: 25%;">
                                            <div class="form-group">
                                                <label style="width: 38%;"
                                                       class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                        style="color: red;">*</span>现场取号上限：</label>
                                                <div class="col-sm-8">
                                                    <input type="text" autocomplete="off" spellcheck="false"
                                                           placeholder="" class="form-control table_content_zd"
                                                           name="queueDept.deptMaxtakeno" id="txtDeptMaxtakeno">
                                                </div>

                                            </div>
                                        </td>

                                        <td style="width: 25%;">
                                            <div class="form-group">
                                                <label style="width: 38%;"
                                                       class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                        style="color: red;">*</span>现场预约上限：</label>
                                                <div class="col-sm-8">
                                                    <input type="text" autocomplete="off" spellcheck="false"
                                                           placeholder="" class="form-control table_content_zd"
                                                           name="queueDept.deptMaxappointment"
                                                           id="txtDeptMaxappointment">
                                                </div>

                                            </div>
                                        </td>

                                        <td style="width: 25%;">
                                            <div class="form-group">
                                                <label style="width: 38%;"
                                                       class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                        style="color: red;">*</span>显示顺序：</label>
                                                <div class="col-sm-8">
                                                    <input type="text" autocomplete="off" spellcheck="false"
                                                           placeholder="" class="form-control table_content_zd"
                                                           name="queueDept.deptOrderno" id="txtDeptOrderno">
                                                </div>

                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width: 25%;">
                                            <div class="form-group">
                                                <label style="width: 38%;"
                                                       class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                        style="color: red;">*</span>办事区域：</label>
                                                <div class="col-sm-8">
                                                    <select class="form-control m-b table_content_zd"
                                                            id="formCaseAreaLs" name="queueDept.caseAreaLs">
                                                        <option value="0">请选择区域</option>
                                                    </select>
                                                    <!-- <button type="button" style="float: left;margin-left: 5px;" class="btn btn-primary btn-sm input_btn_btn list_btn" id="caseArea_administration">管理</button>	 -->
                                                </div>

                                            </div>
                                        </td>
                                        <td style="width: 25%;">
                                            <div class="form-group">
                                                <label style="width: 38%;"
                                                       class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                        style="color: red;">*</span>行政区域：</label>
                                                <div class="col-sm-8">
                                                    <select class="form-control m-b table_content_zd"
                                                            id="adjSelectArea" >
                                                        <option value="0">请选择区域</option>
                                                    </select>
                                                </div>

                                            </div>
                                        </td>

                                        <td style="width: 25%;">
                                            <div class="form-group">
                                                <label style="width: 38%;"
                                                       class="col-sm-3 control-label input_lable_hm table_label_zd"></span>第三方编号：</label>
                                                <div class="col-sm-8">
                                                    <input type="text" autocomplete="off" spellcheck="false"
                                                           placeholder="" class="form-control table_content_zd"
                                                           name="queueDept.otherId" id="otherId">
                                                </div>

                                            </div>
                                        </td>
                                    </tr>
                                </table>
                        <table class="table_zd" align="center" width="100%" style="margin-bottom:-12px;">
                            <tbody>
                            <tr class="table_menu_tr_zd" >
                                <td class="table_menu_tr_td_left_zd" colspan="2">
                                    <select
                                            class="form-control table_content_zd" name="selectCaseLs"
                                            id="selectCaseLs"
                                            style="width: 35%; float: left; margin-right: 10px">
                                        <option value="">请选择区域</option>
                                    </select> <input type="text" placeholder="委办局名称" autocomplete="off"
                                                     spellcheck="false" placeholder="" style="width: 35%;"
                                                     class="form-control input_btn_input table_content_zd"
                                                     name="selectCommitOfficeName" id="selectCommitOfficeName">

                                    <button type="button"
                                            class="btn btn-sm input_btn_btn search_rm_button_index table_button_zd"
                                            style="margin-top: 2.5px; margin-bottom: 2px;"
                                            id="btnCommitOfficeSelect">查询</button>
                                </td>

                                <td class="table_menu_tr_td_right_zd" colspan="2">
                                    <div style="float: right;">
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="addCommitOffice">新增</button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="modifyCommitOffice">修改</button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="clearData">清除</button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="deleteCommitOffice">删除</button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="configWindow">显示配置按钮</button>
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


