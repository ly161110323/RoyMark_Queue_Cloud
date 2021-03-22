<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/page/common/includewithnewztreestyle.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

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
	<script type="text/javascript" src="./js/Queue_Dictionary.js">
	</script>
<script type="text/javascript">
        var table;
        var dataId = "";

        var updateName = "";
    	var updateNo = "";
    	var isSearch = "0";
        var  defaultAreaLs ="${sessionScope.DEFAULT_PROJECT.areaLs}";
        var defaultAreaName="${sessionScope.DEFAULT_PROJECT.areaName}";

        $(document).ready(function() {
            //加载表格
            loadTable();
           });

        function loadTable() {
            // $(".i-checks").iCheck({
            //     checkboxClass : "icheckbox_square-green",
            //     radioClass : "iradio_square-green",
            // });
            // $("#Dictionary_IsShow").iCheck('check');
            $(".i-checks").iCheck({checkboxClass: "icheckbox_square-green", radioClass: "iradio_square-green",});
            $("#yes").iCheck('check');
            $("#no").iCheck('uncheck');

            var tableUrl = "${ctx}/QueueDictionary/list";
            table = $('#example1')
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
                                    {'mData': 'dictionaryLs', 'sTitle': '<input type="checkbox" name="checklist" id="checkall" />', 'sName': 'dictionaryLs', 'sClass': 'center'},
                                    {'mData': 'dictionaryLs', 'sTitle': '序号', 'sName': 'dictionaryLs', 'sClass': 'center'},
                                    {'mData': 'dictionarytypeName', 'sTitle': '字典类型', 'sName': 'dictionarytypeName', 'sClass': 'center'},
                                    {'mData': 'dictionaryName', 'sTitle': '字典名称', 'dictionaryName': 'name', 'sClass': 'center'},
                                    {'mData': 'dictionaryIsshow', 'sTitle': '是否启用', 'sName': 'dictionaryIsshow', 'sClass': 'center'},
                                    {'mData': 'dictionaryParameter1', 'sTitle': '字典参数1', 'sName': 'dictionaryParameter1', 'sClass': 'center'},
                                    {'mData': 'dictionaryParameter2', 'sTitle': '字典参数2', 'sName': 'dictionaryParameter2', 'sClass': 'center'},
                                    {'mData': 'dictionaryParameter3', 'sTitle': '字典参数3', 'sName': 'dictionaryParameter3', 'sClass': 'center'},
                                    {'mData': 'dictionaryParameter4', 'sTitle': '字典参数4', 'sName': 'dictionaryParameter4', 'sClass': 'center'},
                                    {'mData': 'dictionaryParameter5', 'sTitle': '字典参数5', 'sName': 'dictionaryParameter5', 'sClass': 'center'},
                                    {'mData': 'dictionaryParameter6', 'sTitle': '字典参数6', 'sName': 'dictionaryParameter6', 'sClass': 'center'},
                                    {'mData': 'dictionaryParameter7', 'sTitle': '字典参数7', 'sName': 'dictionaryParameter7', 'sClass': 'center'},
                                    {'mData': 'dictionaryId', 'sTitle': '字典编号', 'sName': 'dictionaryId', 'sClass': 'center'},
                                    {'mData': 'areaLs', 'sTitle': 'areaLs', 'sName': 'areaLs', 'sClass': 'hidden areaLs'},
                                    {'mData': 'dictionarytypeLs', 'sTitle': 'typeLs', 'sName': 'dictionarytypeLs', 'sClass': 'hidden dictionarytypeLs'},
                                    {'mData': 'dictionaryIsshow', 'sTitle': 'isShow', 'sName': 'dictionaryIsshow', 'sClass': 'hidden dictionaryIsshow'}
                                ],
                                "fnRowCallback" : function(nRow, aData, iDisplayIndex){
                                    let api = this.api();
                                    let startIndex = api.context[0]._iDisplayStart;//获取本页开始的条数
                                    $("td:nth-child(2)", nRow).html(iDisplayIndex+startIndex+1);//设置序号位于第一列，并顺次加一
                                    return nRow;
                                },
                                "initComplete": function( settings, json ) {
                                    $(".dataTables_scrollHead").css({overflow:"scroll","overflow-x":"hidden"});
                                    $(".dataTables_scrollHeadInner").css({width:"100%"});
                                    $(".dataTables_scrollHeadInner table").css({width:"100%"});
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
                                "drawCallback": function( settings ) {
                                    $(".dataTables_scrollHead").css({overflow:"scroll","overflow-x":"hidden"});
                                    $(".dataTables_scrollHeadInner").css({width:"100%"});
                                    $(".dataTables_scrollHeadInner table").css({width:"100%"});
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
                                "columnDefs": [
                                    {targets: 0,data: "dictionaryLs",title: "操作",
                                        render: function (data, type, row, meta) {
                                            var html = "<input type='checkbox' value="+row.dictionaryLs+" class='lsCheck' name='choice' />";
                                            return html;
                                        }
                                    },
                                    {targets: 4,data: "dictionaryIsshow",title: "是否启用",
                                        render: function (data, type, row, meta) {
                                            var isdefault = "否";
                                            if(data=="1"){
                                                isdefault = "是";
                                            }
                                            return isdefault;
                                        }
                                    }
                                ]
                            });

            trClick();

            checkBoxStyle_Control();
        }

        function loadData(sSource, aoData, fnCallback) {
            var dictionarytypeLs = $("#dictionaries_type_query").val();
            var dictionaryName = $("#selectName").val();
            var pageSize = aoData.iDisplayLength;
            var pageNo = aoData.iDisplayStart % aoData.iDisplayLength == 0 ? aoData.iDisplayStart / aoData.iDisplayLength + 1 : aoData.iDisplayStart / aoData.iDisplayLength;
            $(".lsCheck").prop("checked", false);
            $("#checkall").prop("checked", false);
            dataId = "";
            if(isSearch=="1"){
            	pageNo = 0;
            	pageSize = 10;
            }
            $.ajax({
                type : 'POST',
                url : sSource,
                cache:false,
                async:true,
                dataType : 'json',
                data : {
                	"dictionaryName":dictionaryName,
                    "dictionarytypeLs":dictionarytypeLs,
                    "pageSize":pageSize,
                    "pageNo":pageNo
                },
                success : function(resule) {
                	isSearch = "0";
                    var suData = resule.returnObject[0];
                    var datainfo = suData.records;
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
						<form class="form-horizontal" role="form" action="#" method="post"
							id="itemInfoForm">
							<table class="table_zd" align="center" width="100%">
								<tr>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width:35%;"
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
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 35%;" class="col-sm-3 control-label input_lable_hm table_label_zd"><span class="must_field">*</span>字典名称：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"  class="form-control table_content_zd" id="dictionaries_name" name="dictionaries_name">
											</div>
										</div>
									</td>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 35%;" class="col-sm-3 control-label input_lable_hm table_label_zd"><span class="must_field">*</span>字典类型：</label>
											<div class="col-sm-8">
												<select class="form-control table_content_zd" id="dictionaries_type" name="dictionaries_type">
													<option value="">请选择字典类型</option>
												</select>
											</div>
										</div>
									</td>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 35%;" class="col-sm-3 control-label input_lable_hm table_label_zd"><span class="must_field">*</span>字典编号：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"  class="form-control table_content_zd" id="dictionaries_id" name="dictionaries_id">
											</div>
										</div>
									</td>

								</tr>
								<tr>
									<td>
										<div class="form-group">
											<label style="width: 35%;" class="col-sm-3 control-label input_lable_hm table_label_zd"><span class="must_field">*</span>是否启用：</label>
											<div class="col-sm-8" style="margin-top: -4px;margin-left: -15px;">
												<div class="radio i-checks radio_rm">
													<label>
														<input type="radio" name="isShow" value="1" id="yes">
														<i></i> 是
													</label>
												</div>
												<div class="radio i-checks radio_rm">
													<label>
														<input type="radio" name="isShow" value="0" id="no">
														<i></i> 否
													</label>
												</div>
											</div>
										</div>
									</td>
									<td>
										<div class="form-group">
											<label style="width: 35%;" class="col-sm-3 control-label input_lable_hm table_label_zd">字典参数1：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"  class="form-control table_content_zd" id="dictionaries_parameter_one" name="dictionaries_parameter_one">
											</div>
										</div>
									</td>
									<td>
										<div class="form-group">
											<label style="width: 35%;" class="col-sm-3 control-label input_lable_hm table_label_zd">字典参数2：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"  class="form-control table_content_zd" id="dictionaries_parameter_two" name="dictionaries_parameter_two">
											</div>
										</div>
									</td>
									<td>
										<div class="form-group">
											<label style="width: 35%;" class="col-sm-3 control-label input_lable_hm table_label_zd">字典参数3：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"   class="form-control table_content_zd" id="dictionaries_parameter_three" name="dictionaries_parameter_three">
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="form-group">
											<label style="width: 35%;" class="col-sm-3 control-label input_lable_hm table_label_zd">字典参数4：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"  class="form-control table_content_zd" id="dictionaries_parameter_four" name="dictionaries_parameter_four">
											</div>
										</div>
									</td>
									<td>
										<div class="form-group">
											<label style="width: 35%;" class="col-sm-3 control-label input_lable_hm table_label_zd">字典参数5：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"  class="form-control table_content_zd" id="dictionaries_parameter_five" name="dictionaries_parameter_five">
											</div>
										</div>
									</td>
									<td>
										<div class="form-group">
											<label style="width: 35%;" class="col-sm-3 control-label input_lable_hm table_label_zd">字典参数6：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"  class="form-control table_content_zd" id="dictionaries_parameter_six" name="dictionaries_parameter_six">
											</div>
										</div>
									</td>
									<td>
										<div class="form-group">
											<label style="width: 35%;" class="col-sm-3 control-label input_lable_hm table_label_zd">字典参数7：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"  class="form-control table_content_zd" id="dictionaries_parameter_seven" name="dictionaries_parameter_seven">
											</div>
										</div>
									</td>
								</tr>

							</table>
							<table class="table_zd" align="center" width="100%"
								style="margin-bottom: -12px;">
								<tbody>
								<tr class="table_menu_tr_zd">
									<td class="table_menu_tr_td_left_zd" colspan="2">
										<select class="form-control table_content_zd  " id="dictionaries_type_query" name="dictionaries_type_query" style="width:35%;float:left;">
											<option value="">请选择字典类型</option>
										</select>
										&nbsp;&nbsp;
										<input type="text" placeholder="字典名称" autocomplete="off"
											   spellcheck="false"
											   class="form-control input_btn_input table_content_zd"
											   id="selectName" style="width: 30%;">
										<button type="button" class="btn btn-sm input_btn_btn search_rm_button_index" id="index_select">查询</button>

									</td>
									<td class="table_menu_tr_td_right_zd" colspan="2">
										<button type="button" class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd" id="configWindow">显示配置按钮</button>
										<button type="button" class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd " id="clearData">清除</button>
										<button type="button" class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd " id="updateDataBtn">修改</button>
										<button type="button" class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd " id="addDataBtn">新增</button>
									</td>
								</tr>
								</tbody>
							</table>
						</form>
						<div id="configTr" class="table_menu_tr_zd"
							style="height: 35px; display: none; margin-top: 0px; border: 1px solid white;">
							<div class="optionRow-right" style="padding-right: 10px">
								<button type="button"
									class="btn btn-primary btn-sm input_btn_btn list_btn  table_button_zd"
									id="dictionaryTypeConfig">字典类型</button>

							</div>
						</div>
						<table id="example1" class="table table-bordered"></table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

</html>


