<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/page/common/include.jsp"%>
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

<script type="text/javascript">
	var table;
	var dataId = "";
	var mapData = {};
	var upDepName = new Array();
	var upDepLs = new Array();
	var updateDepName = "";
	var updateDepNo = "";
	var isSearch = "0";
	
	$(document).ready(function() {
		//查询上级科室	
		$.ajax({
	        url: "${pageContext.request.contextPath }/HospitalqueueDepartment/selectUpDepartment",
	        type: "post",
	        dataType: "json",
	        success: function (data) {
	        	if(data == "error"){
	        		layer.alert("服务器错误！");
	        	}else{
	        		var result=data;
	                for (var i = 0; i < result.length; i++) {
	                    upDepName[i] = result[i]["departmentName"];
	                    upDepLs[i] = result[i]["departmentLs"];
	                }
	                //加载表格
	        		loadTable();
	        	}
	        }
	    });
		
		//查询科室类别	
		$.ajax({
	        url: "${pageContext.request.contextPath }/HospitalqueueDictionary/selectdictionarybytypeid",
	        type: "post",
	        dataType: "json",
	        data:{
	        	"dictionarytypeId":"Department_Type"
	        },
	        success: function (data) {
	        	if(data == "error"){
	        		layer.alert("服务器错误！");
	        	}else{
	        		var result=data;
	        		for (var i = 0; i < result.length; i++) {
	        		     $("#department_Type").append("<option value=" + result[i]["dictionaryLs"] + ">" + result[i]["dictionaryName"] + "</option>");
	        		}
	        	}
	        }
	    });
		
		//查询科室类别	
		$.ajax({
	        url: "${pageContext.request.contextPath }/HospitalqueueDictionary/selectdictionarybytypeid",
	        type: "post",
	        dataType: "json",
	        data:{
	        	"dictionarytypeId":"Department_Type"
	        },
	        success: function (data) {
	        	if(data == "error"){
	        		layer.alert("服务器错误！");
	        	}else{
	        		var result=data;
	        		for (var i = 0; i < result.length; i++) {
	        		     $("#department_Type2").append("<option value=" + result[i]["dictionaryLs"] + ">" + result[i]["dictionaryName"] + "</option>");
	        		}
	        	}
	        }
	    });
		
		//科室等级改变事件
		$("#department_Level").change(function () {
	        var department_Level = $("#department_Level").val();
	        if(department_Level == "2"){
	        	$.ajax({
	                url: "${pageContext.request.contextPath }/HospitalqueueDepartment/selectUpDepartment",
	                type: "post",
	                dataType: "json",
	                success: function (data) {
	                	if(data == "error"){
	                		layer.alert("服务器错误！");
	                	}else{
	                		var result=data;
	                        for (var i = 0; i < result.length; i++) {
	                            $("#up_Department").append("<option value=" + result[i]["departmentLs"] + ">" + result[i]["departmentName"] + "</option>");
	                        }
	                	}
	                }
	            });
	        }else{
	        	$("#up_Department").empty();
	        	$("#up_Department").append("<option value='0'>请选择</option>");
	        }
	    });
		
		//新增 
		$(document).on('click','#addDataBtn',function() {
			if (!validateData()) {
				return;
			}
			if($("#department_Level").val()=="2"&&$("#up_Department").val()=="0"){
				layer.alert("上级科室不能为空！");
				return;
			}
			$.ajax({
				url : "${pageContext.request.contextPath }/HospitalqueueDepartment/insert",
				type : "post",
				dataType : "json",
				data : {
					"departmentName" : $("#department_Name").val(),
					"departmentLevel" : $("#department_Level").val(),
					"departmentId" : $("#department_No").val(),
					"departmentPrint" : $("#department_Print").val(),
					"upDepartment" : $("#up_Department").val(),
					"departmentType" : $("#department_Type").val(),
					"isDiversion" : $('input[name="isDiversion"]:checked').val(),
					"departmentShowno" : $("#department_Shownum").val()
				},
				success : function(data) {
					if (data.result == "error") {
						layer.alert("服务器错误！");
						return;
					}
					if (data.result == "repeat") {
						layer.alert("该科室已存在！");
						return;
					}
					if (data.result == "ok") {
						layer.alert("新增成功！");
						$.ajax({
					        url: "${pageContext.request.contextPath }/HospitalqueueDepartment/selectUpDepartment",
					        type: "post",
					        dataType: "json",
					        success: function (data) {
					        		var result=data;
					        		upDepName.length=0;
					        		upDepLs.length=0;
					                for (var i = 0; i < result.length; i++) {
					                    upDepName[i] = result[i]["departmentName"];
					                    upDepLs[i] = result[i]["departmentLs"];
					                }
					        }
					    });
					} else if (data.result == "no") {
						layer.alert("新增失败！");
					}
					clearData2();
					table.draw(false);
				}
			});
	     });

		//修改
		$(document).on('click', '#updateDataBtn', function() {
			if (!validateData()) {
				return;
			}
			if(dataId==""){
        		layer.alert("请选择需要修改的数据！");
        		return;
        	}
			if($("#department_Level").val()=="2"&&$("#up_Department").val()=="0"){
				layer.alert("上级科室不能为空！");
				return;
			}
			$.ajax({
				url : "${pageContext.request.contextPath }/HospitalqueueDepartment/update",
				type : "post",
				dataType : "json",
				data : {
					"updateDepName" : updateDepName,
					"updateDepNo" : updateDepNo,
					"departmentLs" : dataId,
					"departmentName" : $("#department_Name").val(),
					"departmentLevel" : $("#department_Level").val(),
					"upDepartment" : $("#up_Department").val(),
					"departmentPrint" : $("#department_Print").val(),
					"departmentId" : $("#department_No").val(),
					"departmentType" : $("#department_Type").val(),
					"isDiversion" : $('input[name="isDiversion"]:checked').val(),
					"departmentShowno" : $("#department_Shownum").val()
				},
				success : function(data) {
					if (data.result == "error") {
						layer.alert("服务器错误！");
						return;
					}
					if (data.result == "repeat") {
						layer.alert("该科室已存在！");
						return;
					}
					if (data.result == "ok") {
						layer.alert("修改成功！");
					} else if (data.result == "no") {
						layer.alert("修改失败！");
					}
					    clearData2();
						table.draw(false);
					}
			});
		});

		//为删除按钮绑定点击事件
		$(document).on('click','#deleteDataBtn',function() {
					var items = new Array();
					var title = "删除";
					var $cBox = $("[name=choice]:checked");
					if ($cBox.length == 0) {
						layer.alert("请勾选您所要删除的数据！");
						return;
					}
					layer.confirm("您确定要" + title + "这" + $cBox.length+ "条记录吗？",
						{
							btn : [ '确定', '取消' ]
						},
					function() {
						for (var i = 0; i < $cBox.length; i++) {
							items.push($cBox.eq(i).val());
						}
						var data = {"deleteLss" : items.toString()};
					    var url = "${pageContext.request.contextPath }/HospitalqueueDepartment/delete";
						$.ajax({
							    type : 'POST',
								url : url,
								data : data,
								success : function(data) {
									if (data.result == "error") {
									    layer.alert("服务器错误！删除失败");
									    return;
								    }
									if (data.result == "ok") {
										layer.alert("删除成功！");
									}
									clearData2();
									table.draw(false);
								},
								error : function(data){
									layer.alert("错误！");
								}
						});
					}, function() {
						
				});
		});
		
		//为查询按钮绑定点击事件
		$(document).on('click', '#index_select', function() {
			isSearch = "1";
			table.draw(true);
		});
	});

	function loadTable() {
		$(".i-checks").iCheck({
			checkboxClass : "icheckbox_square-green",
			radioClass : "iradio_square-green",
		});
		$("#is_Not_Diversion").iCheck('check');
		var tableUrl = "${pageContext.request.contextPath}/HospitalqueueDepartment/list";
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
									{
										'mData' : 'departmentLs',
										'sTitle' : '<input type="checkbox" name="checklist" id="checkall" />',
										'sName' : 'section_Ls',
										"width" : "50px",
										'sClass' : 'center'
									}, {
										'mData' : 'departmentLs',
										'sTitle' : '序号',
										'sName' : 'departmentLs',
										"width" : "50px",
										'sClass' : 'center'
									}, {
										'mData' : 'departmentName',
										'sTitle' : '科室名称',
										'sName' : 'departmentName',
										'sClass' : 'center'
									}, {
										'mData' : 'departmentLevel',
										'sTitle' : '科室等级',
										'sName' : 'departmentLevel',
										'sClass' : 'center'
									}, {
										'mData' : 'upDepartment',
										'sTitle' : '上级科室',
										'sName' : 'upDepartment',
										'sClass' : 'center'
									}, {
										'mData' : 'dictionaryName',
										'sTitle' : '科室类别',
										'sName' : 'dictionaryName',
										'sClass' : 'center'
									}, {
										'mData' : 'departmentPrint',
										'sTitle' : '号单打印前缀',
										'sName' : 'departmentPrint',
										'sClass' : 'center'
									}, {
										'mData' : 'isDiversion',
										'sTitle' : '是否分诊',
										'sName' : 'isDiversion',
										'sClass' : 'center'
									}, {
										'mData' : 'departmentShowno',
										'sTitle' : '显示顺序',
										'sName' : 'departmentShowno',
										'sClass' : 'center'
									}, {
										'mData' : 'departmentId',
										'sTitle' : '科室编号',
										'sName' : 'departmentId',
										'sClass' : 'center'
									},
                                    {
										'mData': 'upDepartment', 
										'sTitle': 'upDepartment', 
										'sName': 'upDepartment', 
										'sClass': 'hidden upDepartment'
									}, //隐藏列
									{
										'mData' : 'dictionaryLs',
										'sTitle' : '科室类别编号',
										'sName' : 'dictionaryLs',
										'sClass' : 'hidden'
									}
									],
							"fnRowCallback" : function(nRow, aData,
									iDisplayIndex) {
								let api = this.api();
								let startIndex = api.context[0]._iDisplayStart;//获取本页开始的条数
								$("td:nth-child(2)", nRow).html(
										iDisplayIndex + startIndex + 1);//设置序号位于第一列，并顺次加一
								return nRow;
							},
							//对列的数据显示进行设置
							"columnDefs" : [
									{
										targets : 0,
										data : "departmentLs",
										title : "操作",
										render : function(data, type, row, meta) {
											var html = "<input type='checkbox' value="+row.departmentLs+" class='lsCheck' name='choice' />";
											return html;
										}
									},
									{
										targets : 4,
										data : "upDepartment",
										title : "上级科室",
										render : function(data, type, row, meta) {	
											if(data == "0"){
												return "无";
											}else{
												for(var i=0;i<upDepName.length;i++){
													if(data==upDepLs[i]){
														return upDepName[i];
													}
												}
											}
										}
									},
									{
										targets : 7,
										data : "isDiversion",
										title : "是否分诊",
										render : function(data, type, row, meta) {
											return (data == "1") ? "是" : "否";
										}
									},
									{
										targets : 3,
										data : "departmentLevel",
										title : "科室级别",
										render : function(data, type, row, meta) {
											return (data == "1") ? "一级科室"
													: "二级科室";
										}
									} ]
						});
		
		//为表格行绑定点击事件    	
	    $('#example1 tbody').on('click', 'tr', function() {	
	    	//获取点击的科室名称和科室编号，为修改提供验证
	    	updateDepName = $(this).find("td:eq(2)").text();
	    	updateDepNo = $(this).find("td:eq(9)").text();
	    	
	    	dataId = $(this).find("td:eq(0) input[type='checkbox']").val();
	    	$("#up_Department").empty();

	    	$("#example1 tr:even").css({
				"background" : "#f9f9f9",
				"color" : "#676a6c"
			});
			$("#example1 tr:odd").css({
				"background" : "white",
				"color" : "#676a6c"
			});
			$(this).css({
				"background" : "rgb(255, 128, 64)",
				"color" : "white"
			});	
			
			//表格选中，下拉框改变事件
			$("#department_Name").val($(this).find("td:eq(2)").text());
			if ($(this).find("td:eq(3)").text() == "一级科室") {
				$("#department_Level option").eq(0).prop(
						"selected", 'selected');
				$("#up_Department").empty();
	        	$("#up_Department").append("<option value='0'>请选择</option>");
	        	$("#up_Department option").eq(0).prop(
						"selected", 'selected');
			}
			if ($(this).find("td:eq(3)").text() == "二级科室") {
				$("#department_Level option").eq(1).prop(
						"selected", 'selected');
              var parentLs= $(this).find("td:eq(10)").text();
              // console.log("parentLs:"+parentLs);
				$.ajax({
	                url: "${pageContext.request.contextPath }/HospitalqueueDepartment/selectUpDepartment",
	                type: "post",
	                dataType: "json",
	                success: function (data) {
	                	if(data == "error"){
	                		layer.alert("服务器错误！");
	                	}else{
	                		var result=data;
	                		$("#up_Department").append("<option value='0'>请选择</option>");
	                        for (var i = 0; i < result.length; i++) {
	                            $("#up_Department").append("<option value=" + result[i]["departmentLs"] + ">" + result[i]["departmentName"] + "</option>");
	                        }
                            $("#up_Department").val(parentLs);

	                	}
	                }
	            });
			}
			var departmentType= $(this).find("td:eq(11)").text();
			$("#department_Type").val(departmentType);
			$("#department_Print").val(
					$(this).find("td:eq(6)").text());
			if($(this).find("td:eq(7)").text() == "是"){
				$("#is_Diversion").iCheck('check');
				$("#is_Not_Diversion").iCheck('uncheck');
			}else{
				$("#is_Diversion").iCheck('uncheck');
				$("#is_Not_Diversion").iCheck('check');
			}
			$("#department_Shownum").val(
					$(this).find("td:eq(8)").text());
			$("#department_No").val(
					$(this).find("td:eq(9)").text());		
	  	});

		//表格头部复选框点击事件
		$("#checkall").unbind("#checkall").bind("click", function() {
			if ($(this).is(":checked")) {
				$(".lsCheck").prop("checked", true);
			} else {
				$(".lsCheck").prop("checked", false);
			}
		});
		
		//表格主体复选框点击事件
		$(".lsCheck").unbind(".lsCheck").bind("click", function() {
			var allCheckNum = $(".lsCheck").length;
			var checkedNum = $(".lsCheck:checked").length;
			if (allCheckNum == checkedNum) {
				$("#checkall").prop("checked", true);
			} else if (checkedNum < allCheckNum) {
				$("#checkall").prop("checked", false);
			}
		});
	}

	function loadData(sSource, aoData, fnCallback) {
		var departmentName = $("#department_Name2").val();
		var departmentLevel = $("#department_Level2").val();
		var departmentType = $("#department_Type2").val();
		if(departmentName == null || departmentName == ""){
			departmentName = "";
		}
		var pageSize = aoData.iDisplayLength;
    	var pageNo = aoData.iDisplayStart % aoData.iDisplayLength == 0 ? aoData.iDisplayStart / aoData.iDisplayLength + 1 : aoData.iDisplayStart / aoData.iDisplayLength;
    	if(isSearch=="1"){
        	pageNo = 0;
        	pageSize = 10;
        }
		$(".lsCheck").prop("checked", false);
		$("#checkall").prop("checked", false);
		dataId = "";
		$.ajax({
			type : 'POST',
			url : sSource,
			cache:false,
			async:false,
			dataType : 'json',
			data : {
				"departmentType":departmentType,
				"departmentName":departmentName,
				"departmentLevel":departmentLevel,
				"pageSize":pageSize,
				"pageNo":pageNo
			},
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

	//页面数据合法性验证
	function validateData() {
		if ($("#department_Name").val().trim() == "") {
			layer.alert("科室名称不能为空！");
			return;
		}
		if ($("#department_Level").val() == '') {
			layer.alert("科室等级不能为空！");
			return;
		}
		if ($("#department_No").val().trim() == "") {
			layer.alert("科室编号不能为空！");
			return;
		}
		if ($("#department_Type").val() == '') {
			layer.alert("科室类别不能为空！");
			return;
		}
		if ($("#department_Print").val() == '') {
			layer.alert("号单打印前缀不能为空！");
			return;
		}
		if ($("#department_Print").val().length >6) {
			layer.alert("号单打印前缀仅限6位！请重新输入");
			return;
		}
		if ($(".isDiversion").val() == '') {
			layer.alert("自动分诊不能为空！");
			return;
		}
		if ($("#department_Shownum").val() == '') {
			layer.alert("显示顺序不能为空！");
			return;
		}
		return true;
	}

	//清除数据
	function clearData() {
		dataId = "";
		$("#department_Name2").val('');
		$("#department_Level2 option").eq(0).prop(
				"selected", 'selected');
		$("#department_Type2 option").eq(0).prop(
				"selected", 'selected');
		$("#department_Name").val('');
		$("#department_Level option").eq(0).prop(
				"selected", 'selected');
		$("#department_No").val('');
		$("#is_Diversion").iCheck('uncheck');
		$("#is_Not_Diversion").iCheck('check');
		$("#department_Type").val('');
		$("#department_Print").val('');
		$("#department_Shownum").val('');
		$("#up_Department").empty();
    	$("#up_Department").append("<option value='0'>请选择</option>");
	}
	
	function clearData2() {
		dataId = "";
		$("#department_Name").val('');
		$("#department_Level option").eq(0).prop(
				"selected", 'selected');
		$("#department_No").val('');
		$("#is_Diversion").iCheck('uncheck');
		$("#is_Not_Diversion").iCheck('check');
		$("#department_Type").val('');
		$("#department_Print").val('');
		$("#department_Shownum").val('');
		$("#up_Department").empty();
    	$("#up_Department").append("<option value='0'>请选择</option>");
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
											<label style="width: 40%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd"><span
												class="must_field">*</span>科室名称：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"
													class="form-control table_content_zd"
													name="department_Name" id="department_Name">
											</div>
										</div>
									</td>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 40%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd"><span
												class="must_field">*</span>科室等级：</label>
											<div class="col-sm-8">
												<select autocomplete="off" spellcheck="false"
													class="form-control table_content_zd"
													name="department_Level" id="department_Level">
													<option value="1">一级</option>
													<option value="2">二级</option>
												</select>
											</div>
										</div>
									</td>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 40%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd">上级科室：</label>
											<div class="col-sm-8">
												<select autocomplete="off" spellcheck="false"
													class="form-control table_content_zd" name="up_Department"
													id="up_Department">
													<option value="0">请选择</option>
												</select>
											</div>
										</div>
									</td>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 40%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd"><span
												class="must_field">*</span>科室编号：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"
													class="form-control table_content_zd" id="department_No"
													name="department_No">
											</div>
										</div>
									</td>
								</tr>

								<tr>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 40%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd"><span
												class="must_field">*</span>科室类别：</label>
											<div class="col-sm-8">
											    <select autocomplete="off" spellcheck="false"
													class="form-control table_content_zd" name="department_Type"
													id="department_Type">
													<option value="0">请选择</option>
												</select>
											</div>
										</div>
									</td>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 40%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd"><span
												class="must_field">*</span>号单打印前缀：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"
													class="form-control table_content_zd"
													name="department_Print" id="department_Print">
											</div>
										</div>
									</td>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 40%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd"><span
												class="must_field">*</span>是否自动分诊：</label>
											<div class="col-sm-8 rm_from_check"
												style="margin-top: -5px; padding-top: 2px; padding-bottom: 3px;">
												<div class="radio i-checks radio_rm">
													<label> <input type="radio" name="isDiversion"
														value="1" id="is_Diversion"><i></i> 是
													</label>
												</div>
												<div class="radio i-checks radio_rm">
													<label> <input type="radio" name="isDiversion"
														value="0" id="is_Not_Diversion"> <i></i> 否
													</label>
												</div>
											</div>
										</div>
									</td>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 40%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd"><span
												class="must_field">*</span>显示顺序：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"
													class="form-control table_content_zd"
													id="department_Shownum" name="department_Shownum">
											</div>
										</div>
									</td>
								</tr>
							</table>
							<table class="table_zd" align="center" width="100%" style="margin-bottom:-12px;">
								<tbody>
									<tr class="table_menu_tr_zd">
										<td class="table_menu_tr_td_left_zd">
										<select
											class="form-control m-b input_btn_input table_content_zd"
											id="department_Type2" name="department_Type2"
											style="width: 30%;">
												<option value="0">科室类别</option>
										</select> 
											<select
											class="form-control m-b input_btn_input table_content_zd"
											id="department_Level2" name="department_Level2"
											style="width: 30%;">
												<option value="0">科室等级</option>
												<option value="1">一级</option>
												<option value="2">二级</option>
										</select> 
											<input type="text" autocomplete="off" placeholder="科室名称"
											class="form-control input_btn_input table_content_zd"
											id="department_Name2" style="width: 30%;">
											<button type="button"
												class="table_button_zd btn btn-sm search_rm_button_index"
												id="index_select">查询</button></td>

										<td class="table_menu_tr_td_right_zd" colspan="2">
											<button type="button"
												class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
												id="deleteDataBtn">删除</button>
											<button type="button"
												class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
												id="clearDataBtn" onClick="clearData()">清除</button>
											<button type="button"
												class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
												id="updateDataBtn">修改</button>
											<button type="button"
												class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
												id="addDataBtn">新增</button>
										</td>
									</tr>
								</tbody>
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
