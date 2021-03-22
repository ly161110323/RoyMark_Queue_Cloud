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
    <script type="text/javascript" src="./js/Queue_UserInfo.js">
    </script>

    <script type="text/javascript">
        var table;
        var dataId = "";
        var isSearch = "0";
        var  defaultAreaLs ="${sessionScope.DEFAULT_PROJECT.areaLs}";
        var defaultAreaName="${sessionScope.DEFAULT_PROJECT.areaName}";
        var userInfoLs;
        var userName;

        $(document).ready(function() {
            //加载表格
            loadTable();
            trClick();
            checkBoxStyle_Control();
            $(document).on('click','#txtUserInfoBirth',function(){
                WdatePicker({dateFmt:'yyyy-MM-dd'});
            });

        });

        function loadTable() {
            $(".i-checks").iCheck({checkboxClass: "icheckbox_square-green", radioClass: "iradio_square-green",});
            $("#yes").iCheck('check');
            $("#no").iCheck('uncheck');
            $("#partyYes").iCheck('uncheck');
            $("#partyNo").iCheck('check');
            $("#matterYes").iCheck('uncheck');
            $("#matterNo").iCheck('check');
            var h = document.documentElement.clientHeight || document.body.clientHeight;
            if(h>600){
                h = h-230;
            }else{
                h = h-230;
            }
            var tableUrl = "${ctx}/QueueUserinfo/list";
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
                        // "sScrollY" : h,
                        "displayLength": 10,
                        "sAjaxDataProp": "data",
                        "bServerSide": true,
                        "sAjaxSource":tableUrl,
                        "fnServerData": loadData,
                        "bLengthChange": false,
                        "aoColumns" : [
                            {'mData': 'userinfoId', 'sTitle': '<input type="checkbox" name="checklist" id="checkall" />', 'sName': 'userinfoId', 'sClass': 'center'},
                            {'mData': 'userinfoId', 'sTitle': '序号', 'sName': 'userinfoId', 'sClass': 'center'},
                            {'mData': 'deptName', 'sTitle': '所属委办局', 'sName': 'deptName', 'sClass': 'center'},
                            {'mData': 'userinfoName', 'sTitle': '人员名称', 'sName': 'userinfoName', 'sClass': 'center'},
                            {'mData': 'userinfoCode', 'sTitle': '工号', 'sName': 'userinfoCode', 'sClass': 'center'},
                            {'mData': 'userinfoSex', 'sTitle': '性别', 'sName': 'userinfoSex', 'sClass': 'center'},
                            {'mData': 'userinfoBirth', 'sTitle': '出生年月', 'sName': 'userinfoBirth', 'sClass': 'center'},
                            /* {'mData': 'userInfoAge', 'sTitle': '年龄', 'sName': 'userInfoAge', 'sClass': 'center'}, */
                            {'mData': 'positionName', 'sTitle': '职务', 'sName': 'positionName', 'sClass': 'center'},
                            {'mData': 'userinfoTel', 'sTitle': '手机号码', 'sName': 'userinfoTel', 'sClass': 'center'},
                            {'mData': 'userinfoMail', 'sTitle': '邮箱地址', 'sName': 'userinfoMail', 'sClass': 'center'},
                            {'mData': 'userinfoImagepath', 'sTitle': '人员头像', 'sName': 'userinfoImagepath', 'sClass': 'center'},
                            {'mData': 'userinfoId', 'sTitle': '人员编号', 'sName': 'userinfoId', 'sClass': 'center'},
                            {'mData': 'userinfoIstheparty', 'sTitle': '是否党员', 'sName': 'userinfoIstheparty', 'sClass': 'center'},
                            {'mData': 'userinfoIsmatter', 'sTitle': '是否关联叫号事项', 'sName': 'userinfoIsmatter', 'sClass': 'center'},
                            {'mData': 'userinfoImagepath', 'sTitle': '人员头像', 'sName': 'userinfoImagepath', 'sClass': 'hidden'}
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
                        "columnDefs": [
                            {targets: 0,data: "LS",title: "操作",
                                render: function (data, type, row, meta) {

                                    var html = "<input type='checkbox' value="+row.userinfoLs+" class='lsCheck' name='choice' />";
                                    html+="<input type='hidden' name='deptLs' value="+row.deptLs+"></input>";
                                    html+="<input type='hidden' name='userinfoId' value="+row.userinfoId+"></input>";
                                    html+="<input type='hidden' name='userInfoSex' value="+row.userinfoSex+"></input>";
                                    html+="<input type='hidden' name='userinfoImagepath' value="+row.userinfoImagepath+"></input>";
                                    html+="<input type='hidden' name='webAccountLs' value="+row.webAccountLs+"></input>";
                                    html+="<input type='hidden' name='userInfoPosition' value="+row.userinfoPosition+"></input>";
                                    html+="<input type='hidden' name='userInfoIsTheParty' value="+row.userinfoIstheparty+"></input>";
                                    html+="<input type='hidden' name='userInfoIsMatter' value="+row.userinfoIsmatter+"></input>";
                                    return html;
                                }
                            },
                            {targets: 5,data: "userinfoSex",title: "性别",
                                render: function (data, type, row, meta) {

                                    return row.userinfoSex=='1'?'男':'女';
                                }
                            },{targets: 10,data: "userinfoImagepath",title: "人员头像",
                                render: function (data, type, row, meta) {
                                    var userinfoImagepath=row.userinfoImagepath;
                                    var userInfoImageName='';
                                    if(userinfoImagepath!=null && userinfoImagepath!='' && typeof(userinfoImagepath)!='undefined')
                                    {
                                        userInfoImageName=userinfoImagepath.substring(userinfoImagepath.lastIndexOf("/")+1);
                                    }
                                    return userInfoImageName;
                                }
                            },{targets: 12,data: "userinfoIstheparty",title: "是否党员",
                                render: function (data, type, row, meta) {
                                    return row.userinfoIstheparty=='1'?'是':'否';
                                }
                            },{targets: 13,data: "userinfoIsmatter",title: "是否关联叫号事项",
                                render: function (data, type, row, meta) {
                                    return row.userinfoIsmatter=='1'?'是':'否';
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
            $(".lsCheck").prop("checked", false);
            $("#checkall").prop("checked", false);
            dataId = "";
            var dept = $("#selectCommitOffice");
            selectedDept = dept.val();
            var userInfoName=$("#selectItemName").val();
            var userInfoCode=$("#selectItemCode").val();
            var params = {"userinfoName":userInfoName};
            if(dept.val() != "0"){
                //如果选择了所属委办局,则将选中的委办局流水号一起传递
                //params = {"queueUserInfo.userInfoName":userInfoName,"queueUserInfo.deptLs":selectedDept};
                params["deptLs"]=selectedDept;
            }
            params["pageNo"] = pageNo;
            params["pageSize"] = pageSize;
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
                        <table  class="table_zd" align="center" width="100%">
                            <tr>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 40%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                class="must_field">*</span>所属大厅：</label>
                                        <div class="col-sm-8">
                                            <input type="text" style="width: 98%;" autocomplete="off"
                                                   class="form-control m-b input_btn_input table_content_zd"
                                                   readonly="readonly" name="Area_Name" id="Area_Name">
                                            <input type="hidden" name="Area_Ls" id="Area_Ls">
                                        </div>
                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width:40%;" class="col-sm-3 control-label input_lable_hm table_label_zd"><span style="color:red;">*</span>所属委办局：</label>
                                        <div class="col-sm-8" >
                                            <select class="form-control table_content_zd" name="queueUserInfo.deptLs"   id="belongDept" style="width:98%;">
                                            </select>
                                            <input type="hidden" name="queueUserInfo.userInfoLs" id="txtUserInfoLs" />
                                        </div>
                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label  style="width:40%" class="col-md-3 control-label input_lable_hm table_label_zd"><span style="color:red;">*</span>人员名称：</label>
                                        <div class="col-sm-8" >
                                            <input type="text"   autocomplete="off" spellcheck="false"  class="form-control input_btn_input"   id="txtUserInfoName" name="queueUserInfo.userInfoName" style="width:98%;">
                                        </div>
                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width:40%;" class="col-sm-3 control-label input_lable_hm table_label_zd"><span style="color:red;">*</span>人员编号：</label>
                                        <div class="col-sm-8" >
                                            <input type="text"   autocomplete="off" spellcheck="false"  class="form-control table_content_zd"  id="txtUserInfoId" name="queueUserInfo.userInfoId" style="width:98%;">
                                        </div>
                                    </div>
                                </td>
                                <%--<td style="width: 25%;">--%>
                                    <%--<div class="form-group" style="margin-left;0px;">--%>
                                        <%--<label style="width:40%;" class="col-md-3 control-label input_lable_hm table_label_zd"><span style="color:red;">*</span>所属项目：</label>--%>
                                        <%--<div class="col-sm-8" >--%>
                                            <%--<input type="text"   autocomplete="off" spellcheck="false"  class="form-control table_content_zd " disabled  id="areaName" name="queueUserInfo.areaName" style="width:98%;">--%>
                                            <%--<input type="text"  name="queueUserInfo.areaLs" id="areaLs" style="display:none;" >--%>

                                        <%--</div>--%>
                                    <%--</div>--%>
                                <%--</td>--%>
                            </tr>
                            <tr>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width:40%;" class="col-md-3 control-label input_lable_hm table_label_zd"><span style="color:red;">*</span>工号：</label>
                                        <div class="col-sm-8" >
                                            <input type="text"   autocomplete="off" spellcheck="false"  class="form-control input_btn_input"  id="txtUserInfoCode" name="queueUserInfo.userInfoCode" style="width:98%;">
                                        </div>
                                    </div>
                                </td>
                                <td style="width: 25%;padding-left: 0px;padding-top:0px;">
                                    <div class="form-group">
                                        <label style="width:40%;" class="col-sm-3 control-label input_lable_hm table_label_zd"><span style="color:red;">*</span>性别：</label>
                                        <div class="col-sm-8" style="margin-top: -4px;margin-left: -15px;" >
                                            <div class="radio i-checks radio_rm">
                                                <label>
                                                    <input type="radio" name="queueUserInfo.userInfoSex" value="1" id="yes">
                                                    <i></i> 男
                                                </label>
                                            </div>
                                            <div class="radio i-checks radio_rm">
                                                <label>
                                                    <input type="radio" name="queueUserInfo.userInfoSex" value=2" id="no">
                                                    <i></i> 女
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width:40%;" class="col-sm-3 control-label input_lable_hm table_label_zd">出生年月：</label>
                                        <div class="col-sm-8" >

                                            <input type="text" readonly="readonly" id="txtUserInfoBirth" spellcheck="false"  name="queueUserInfo.userInfoBirth" style="width:98%;" class="td-text Wdate form-control table_content_zd">
                                        </div>

                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group" style="margin-left;0px;">
                                        <label style="width:40%;" class="col-md-3 control-label">职务：</label>
                                        <div class="col-sm-8" >
                                            <select class="form-control m-b" name="queueUserInfo.userInfoPosition" placeholder="请选择职务"  id="userInfoPosition" style="width:98%;">
                                            </select>
                                        </div>

                                        <!-- <label style="width:40%;" class="col-md-3 control-label">年龄：</label>
                                        <div class="col-sm-8" >
                                            <input type="text"  disabled="disabled"  autocomplete="off" class="form-control input_btn_input " placeholder="年龄"   id="txtUserInfoAge" name="queueUserInfo.userInfoAge" style="width:98%;">

                                        </div> -->
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width:40%;" class="col-md-3 control-label input_lable_hm table_label_zd">手机号码：</label>
                                        <div class="col-sm-8" >
                                            <input type="text"   autocomplete="off"  spellcheck="false"  class="form-control table_content_zd "   id="txtUserInfoTel" name="queueUserInfo.userInfoTel" style="width:98%;">
                                        </div>
                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width:40%;" class="col-md-3 control-label input_lable_hm table_label_zd">邮箱地址：</label>
                                        <div class="col-sm-8" >
                                            <input type="text"   autocomplete="off" spellcheck="false"  class="form-control table_content_zd "   id="txtUserInfoMail" name="queueUserInfo.userInfoMail" style="width:98%;">
                                        </div>
                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width:40%;" class="col-md-3 control-label input_lable_hm table_label_zd">人员头像：</label>
                                        <div class="col-sm-8" >
                                            <input type="file" name="matterFile" id="workGuide" style="display: none;" />
                                            <input type="text" style="width: 65%;float:left;" autocomplete="off"  class="form-control table_content_zd" readonly="readonly" id="workGuideFileName" name="matterFileName">
                                            </input>
                                            <button type="button" class="btn btn-primary btn-sm input_btn_btn" id="btnChooseWorkGuide" style="float: left;margin-left: 5px;margin-top:2px;">选择</button>
                                    </div>
                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width:40%;" class="col-sm-3 control-label"><span style="color:red;">*</span>是否党员：</label>
                                        <div class="col-sm-8" style="margin-top: -4px;margin-left: -15px;" >
                                            <div class="radio i-checks radio_rm">
                                                <label>
                                                    <input type="radio" name="queueUserInfo.userInfoIsTheParty" value="1" id="partyYes">
                                                    <i></i> 是
                                                </label>
                                            </div>
                                            <div class="radio i-checks radio_rm">
                                                <label>
                                                    <input type="radio" name="queueUserInfo.userInfoIsTheParty" value="0" id="partyNo">
                                                    <i></i> 否
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width:40%;" class="col-sm-3 control-label"><span style="color:red;">*</span>关联叫号事项：</label>
                                        <div class="col-sm-8" style="margin-top: -4px;margin-left: -15px;" >
                                            <div class="radio i-checks radio_rm">
                                                <label>
                                                    <input type="radio" name="queueUserInfo.userInfoIsMatter" value="1" id="matterYes">
                                                    <i></i> 是
                                                </label>
                                            </div>
                                            <div class="radio i-checks radio_rm">
                                                <label>
                                                    <input type="radio" name="queueUserInfo.userInfoIsMatter" value="0" id="matterNo">
                                                    <i></i> 否
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr class="table_menu_tr_zd">
                                <td class="table_menu_tr_td_left_zd" colspan="2">
                                    <select class="form-control table_content_zd" name="selectCommitOffice" id="selectCommitOffice" style="width:35%;float:left;margin-right:10px;">
                                    </select>
                                    <input type="text"   autocomplete="off" placeholder="人员姓名"  spellcheck="false"  style="width:35%;" class="form-control input_btn_input table_content_zd"  name="selectItemName" id="selectItemName" >
                                    <button type="button" class="btn btn-sm input_btn_btn search_rm_button_index table_button_zd "  id="index_select">查询</button>
                                </td>


                                <td  class="table_menu_tr_td_right_zd" colspan="2">
                                    <button type="button" class="btn btn-primary btn-sm input_btn_btn list_btn  table_button_zd" id="userMatterConfig">叫号事项</button>
                                    <button type="button" class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd "    id="createAccount">生成账号</button>
                                    <button type="button" class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd "  id="clearData">清除</button>

                                    <button type="button" class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd "  id="deleteItem">删除</button>

                                    <button type="button" class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd "    id="modifyItem">修改</button>
                                    <button type="button" class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd "  id="addItem">新增</button>
                                </td>
                            </tr>

                        </table>

                    </form>

                    <table id="itemResultTable" class="table table-bordered"></table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

</html>


