<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/page/common/include.jsp"%>
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

<script type="text/javascript">
        var table;
        var dataId = "";
        var mapData = {};
        var updateName = "";
    	var updateNo = "";
    	var isSearch = "0";
        
        $(document).ready(function() {
            //加载表格
            loadTable();
            //新增
            $(document).on('click','#addDataBtn',function() {
                if (!validateData()) {
                    return;
                }
                //封装数据
                var formData = new FormData();
                formData.append("uploadinfo", $('#workGuide')[0].files[0]);
                formData.append("webaccountCode",$("#WebAccount_Code").val());
                formData.append("webaccountName",$("#WebAccount_Name").val());
                formData.append("webaccountLogin",$("#WebAccount_LogIn").val());
                formData.append("webaccountPassword", $("#WebAccount_Password").val());
                formData.append("employeeLs", $("#Employee_Ls").val());
                formData.append("caseLs", $("#Case_Ls").val());
                formData.append("webaccountImage", $("#WebAccount_Image").val());
                formData.append("webaccountText", $("#WebAccount_Text").val());
                //
                $.ajax({
                    url : "${ctx}/webaccount/insert",
                    type : "post",
                    dataType : "json",
                    processData : false, // 使数据不做处理
                    contentType : false, // 不要设置Content-Type请求头
                    data : formData,
                    success : function(data) {
                        if (data.result == "error") {
                            layer.alert("服务器错误！");
                            return;
                        }
                        if (data.result == "Loginrepeat") {
                            layer.alert("该登录账号已存在！");
                            return;
                        }
                        if (data.result == "Coderepeat") {
                            layer.alert("该账号编号已存在！");
                            return;
                        }
                        if (data.result == "ok") {
                            layer.alert("新增成功！");
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
                //封装数据
                var formData = new FormData();
                formData.append("uploadinfo", $('#workGuide')[0].files[0]);
                formData.append("webaccountLs", dataId);
                formData.append("webaccountCode",$("#WebAccount_Code").val());
                formData.append("webaccountName",$("#WebAccount_Name").val());
                formData.append("webaccountLogin",$("#WebAccount_LogIn").val());
                formData.append("webaccountPassword", $("#WebAccount_Password").val());
                formData.append("employeeLs", $("#Employee_Ls").val());
                formData.append("caseLs", $("#Case_Ls").val());
                formData.append("webaccountImage", $("#WebAccount_Image").val());
                formData.append("webaccountText", $("#WebAccount_Text").val());
                formData.append("updateName", updateName);
                formData.append("updateNo", updateNo);
                //
                $.ajax({
                    url : "${ctx}/webaccount/update",
                    type : "post",
                    dataType : "json",
                    processData : false, // 使数据不做处理
                    contentType : false, // 不要设置Content-Type请求头
                    data : formData,
                    success : function(data) {
                        if (data.result == "error") {
                            layer.alert("服务器错误！");
                            return;
                        }
                        if (data.result == "Loginrepeat") {
                            layer.alert("该登录账号已存在！");
                            return;
                        }
                        if (data.result == "Coderepeat") {
                            layer.alert("该账号编号已存在！");
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
                            var url = "${ctx}/webaccount/delete";
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
            
            //为按钮《关联配置》绑定事件
			$("#accountConfig").click(function(){
				var $cBox = $("[name=choice]:checked");
				if($cBox.length == 0 || $cBox.length >1){
					layer.alert("请先选中一条账号记录！");
					return;
				}	
				var webAccountLs = $cBox.eq(0).val();
				var webAccountName = $cBox.closest('tr').children().eq(2).text();
				var params = "?webAccountLs="+webAccountLs+"&webAccountName="+webAccountName;
				var argTitle = "账号权限["+webAccountName+"]";
				//参数中有中文，需要转码
				params = encodeURI (encodeURI(params));
				showFrameDialog(argTitle,"${ctx}/page/manage/webaccount/HospitalWebAccountRole.jsp"+params);
			});
            
			//确认按钮事件
			$(document).on('click', '#subtimBtn', function() {
				//调用弹窗中的editRolePermission()方法
				document.getElementById("role_ModelId").contentWindow.editRolePermission();
			});
			
			//全选按钮事件
			$(document).on('click', '#checktreelist', function() {
				if($(this).is(":checked")){
					document.getElementById("role_ModelId").contentWindow.operatAll(true);
				}else{
					document.getElementById("role_ModelId").contentWindow.operatAll(false);
				}
			});
            
            //为查询按钮绑定点击事件
            $(document).on('click', '#index_select', function() {
            	isSearch = "1";
                table.draw(true);
            });
            
            //更多配置
    		$(document).on('click','#configWindow',function(){
    			var btn=document.getElementById("configWindow");
    			var btnV = btn.innerHTML;
    			if(btnV=="显示配置维护"){
    				$("#configTr").slideDown(); 
    				btn.innerHTML="隐藏配置维护";
    			}else{
    				$("#configTr").slideUp(); 
    				btn.innerHTML="显示配置维护";
    			}
    			
    		});
            
    		//为选择按钮绑定点击事件
    		$(document).on('click', '#btnChooseIcon', function() {
    			$("#workGuide").click();	
    		});
            
    		//为文件组合框绑定值改变事件
    		$(document).on('change', '#workGuide', function() {
    			var arrs = $(this).val().split('\\');
    			var filename = arrs[arrs.length - 1];
    			$("#WebAccount_Image").val(filename);
    		});
    		

            //所属项目选择界面
            $(document).on('click','#btnChooseCase',function(){
            	var targetUrl="${ctx}/page/manage/webaccount/HospitalQueue_AreaSelect.jsp";
            	var argTitle="所属项目选择界面";
            	openwindowNoRefresh(targetUrl,argTitle,1020,650);
            });
            
            //所属员工选择界面
            $(document).on('click','#btnChooseUser',function(){
            	var targetUrl="${ctx}/page/manage/webaccount/HospitalQueue_EmployeeSelect.jsp";
            	var argTitle="所属人员选择界面";
            	openwindowNoRefresh(targetUrl,argTitle,1020,650);
            });
        });

        function loadTable() {
            $(".i-checks").iCheck({
                checkboxClass : "icheckbox_square-green",
                radioClass : "iradio_square-green",
            });
            var tableUrl = "${ctx}/webaccount/list";
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
                                                        'mData' : 'webaccountLs',
                                                        'sTitle' : '<input type="checkbox" name="checklist" id="checkall" />',
                                                        'sName' : 'webaccountLs',
                                                        "width" : "50px",
                                                        'sClass' : 'center'
                                                    },
                                                    {
                                                        'mData' : 'webaccountLs',
                                                        'sTitle' : '序号',
                                                        'sName' : 'webaccountLs',
                                                        "width" : "50px",
                                                        'sClass' : 'center'
                                                    },
                                                        {
                                                            'mData' : 'webaccountName',
                                                            'sTitle' : '账号名称',
                                                            'sName' : 'webaccountName',
                                                            'sClass' : 'center'
                                                        },
                                                        {
                                                            'mData' : 'webaccountLogin',
                                                            'sTitle' : '登录账号',
                                                            'sName' : 'webaccountLogin',
                                                            'sClass' : 'center'
                                                        },
                                                        {
                                                            'mData' : 'webaccountPassword',
                                                            'sTitle' : '登录密码',
                                                            'sName' : 'webaccountPassword',
                                                            'sClass' : 'center'
                                                        },
                                                        {
                                                            'mData' : 'caseName',
                                                            'sTitle' : '所属项目',
                                                            'sName' : 'caseName',
                                                            'sClass' : 'center'
                                                        },
                                                        {
                                                            'mData' : 'employeeName',
                                                            'sTitle' : '所属人员',
                                                            'sName' : 'employeeName',
                                                            'sClass' : 'center'
                                                        },
                                                        {
                                                            'mData' : 'webaccountImage',
                                                            'sTitle' : '账号头像',
                                                            'sName' : 'webaccountImage',
                                                            'sClass' : 'center'
                                                        },
                                                        {
                                                            'mData' : 'webaccountCode',
                                                            'sTitle' : '账号编号',
                                                            'sName' : 'webaccountCode',
                                                            'sClass' : 'center'
                                                        },
                                                        {
                                                            'mData' : 'webaccountText',
                                                            'sTitle' : '备注',
                                                            'sName' : 'webaccountText',
                                                            'sClass' : 'hidden'
                                                        },
                                                        {
                                                            'mData' : 'caseLs',
                                                            'sTitle' : '项目编号',
                                                            'sName' : 'caseLs',
                                                            'sClass' : 'hidden'
                                                        },{
                                                            'mData' : 'employeeLs',
                                                            'sTitle' : '人员编号',
                                                            'sName' : 'employeeLs',
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
                                        data : "webaccountLs",
                                        title : "操作",
                                        render : function(data, type, row, meta) {
                                            var html = "<input type='checkbox' value="+row.webaccountLs+" class='lsCheck' name='choice' />";
                                            return html;
                                        }
                                    },
                                    {
                                        targets: 4,
                                        data: "webAccountPassword",
                                        title: "登录密码",
                                       	render: function (data, type, row, meta) {
                                       	    var typeName="*********";	
                                    	    return typeName;
                                       	}
                                    },
                                    {
                                    	targets: 7,
                                    	data: "webAccountImage",
                                    	title: "头像",
                                    	render: function (data, type, row, meta) {
                                    	    var webAccountImage=data;
                                    	    if(webAccountImage!=null && webAccountImage != ""){
                                    	       var imageName=webAccountImage.substring(webAccountImage.lastIndexOf("/")+1);
                                    		   return imageName;
                                    	    }else{
                                    	       return "";
                                    	    }
                                    	 }
                                    }]
                            });
            
            //为表格行绑定点击事件
            $('#example1 tbody').on('click', 'tr', function() {
                dataId = $(this).find("td:eq(0) input[type='checkbox']").val();
                //为修改提供验证
    	    	updateName = $(this).find("td:eq(3)").text();
    	    	updateNo = $(this).find("td:eq(8)").text();

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

                $("#WebAccount_Name").val($(this).find("td:eq(2)").text());                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  //  }
                $("#WebAccount_LogIn").val($(this).find("td:eq(3)").text());
                $("#WebAccount_Password").val($(this).find("td:eq(4)").text());
                $("#WebAccount_Code").val($(this).find("td:eq(8)").text());
                $("#Case_Name").val($(this).find("td:eq(5)").text());
                $("#Employee_Name").val($(this).find("td:eq(6)").text());
                $("#WebAccount_Image").val($(this).find("td:eq(7)").text());
                $("#WebAccount_Text").val($(this).find("td:eq(9)").text());
                $("#Case_Ls").val($(this).find("td:eq(10)").text());
                $("#Employee_Ls").val($(this).find("td:eq(11)").text());
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
            var webaccountName = $("#WebAccount_Name2").val();
            if(webaccountName == null || webaccountName == ""){
            	webaccountName = "";
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
                async:true,
                dataType : 'json',
                data : {
                    "webaccountName":webaccountName,
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
                if ($("#WebAccount_Name").val().trim() == "") {
                    layer.alert("账号名称不能为空！");
                    return;
                }
                if ($("#WebAccount_LogIn").val().trim() == "") {
                    layer.alert("登录账号不能为空！");
                    return;
                }
                if ($("#WebAccount_Password").val().trim() == "") {
                    layer.alert("登录密码不能为空！");
                    return;
                }
                var passWord=$("#WebAccount_Password").val();
                if(passWord!=null && passWord.trim()!="")
                {
                    if(!ispassWord(passWord))
                    {
                        layer.alert("密码格式只能为英文、字母和数字的组合！");
                        return
                    }

                }
                if ($("#WebAccount_Code").val().trim() == "") {
                    layer.alert("账号编号不能为空！");
                    return;
                }
            return true;
        }

        //清除数据
        function clearData() {
        	dataId = "";
        	$("#WebAccount_Name").val("");   
        	$("#WebAccount_Name2").val("");                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               //  }
            $("#WebAccount_LogIn").val("");  
            $("#WebAccount_Password").val("");  
            $("#WebAccount_Code").val("");  
            $("#Case_Name").val("");
            $("#Case_Ls").val("");
            $("#Employee_Name").val("");  
            $("#Employee_Ls").val("");
            $("#WebAccount_Image").val("");  
            $("#WebAccount_Text").val("");  
            $("#workGuide").val("");
        }
        
        function clearData2() {
        	dataId = "";
        	$("#WebAccount_Name").val(""); 
            $("#WebAccount_LogIn").val("");  
            $("#WebAccount_Password").val("");  
            $("#WebAccount_Code").val("");  
            $("#Case_Name").val("");
            $("#Case_Ls").val("");
            $("#Employee_Name").val("");  
            $("#Employee_Ls").val("");
            $("#WebAccount_Image").val("");  
            $("#WebAccount_Text").val("");  
            $("#workGuide").val("");
        }
        
        //弹窗方法
        function showFrameDialog(argTitle,argUrl){
    		$("#role_title").html(argTitle);
    		$("#role_ModelId").attr("src",argUrl);
    		$("#myModa_role").modal('show');
    	}

        function closeFramDialog(msg){
    		layer.alert(msg);
    		$("#myModa_role").modal('hide');
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
											<label style="width: 40%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd"><span
												class="must_field">*</span>账号名称：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"
													class="form-control table_content_zd"
													name="WebAccount_Name" id="WebAccount_Name">
											</div>
										</div> 
									</td>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 40%;"
												class="col-sm-4 control-label input_lable_hm table_label_zd"><span
												class="must_field">*</span>登录账号：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"
													class="form-control table_content_zd"
													name="WebAccount_LogIn" id="WebAccount_LogIn">
											</div>
										</div>
									</td>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 40%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd"><span
												class="must_field">*</span>登录密码：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"
													class="form-control table_content_zd"
													name="WebAccount_Password"
													id="WebAccount_Password">
											</div>
										</div>
									</td>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 39%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd"><span
												class="must_field">*</span>账号编号：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"
													class="form-control table_content_zd"
													name="WebAccount_Code" id="WebAccount_Code">
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 40%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd">所属项目：</label>
											<div class="col-sm-8">
												<input type="text" name="Case_Ls" id="Case_Ls"
													style="display: none;" /> <input type="text"
													style="width: 66%;" autocomplete="off"
													class="form-control m-b input_btn_input table_content_zd"
													readonly="readonly" name="Case_Name" id="Case_Name">
												<button type="button"
													class="btn btn-primary btn-sm input_btn_btn"
													id="btnChooseCase" style="float: left; margin-left: 5px;">选择</button>
											</div>
										</div>
									</td>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 40%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd">所属人员：</label>
											<div class="col-sm-8">
												<input type="text" name="Employee_Ls" id="Employee_Ls"
													style="display: none;" /> <input type="text"
													style="width: 66%;" autocomplete="off"
													class="form-control m-b input_btn_input table_content_zd"
													readonly="readonly" name="Employee_Name" id="Employee_Name">
												<button type="button"
													class="btn btn-primary btn-sm input_btn_btn"
													id="btnChooseUser" style="float: left; margin-left: 5px;">选择</button>
											</div>
										</div>
									</td>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 40%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd">账号头像：</label>
											<div class="col-sm-8">
												<input type="file" name="matterFile" id="workGuide"
													style="display: none;" /> <input type="text"
													style="width: 66%;" autocomplete="off"
													class="form-control m-b input_btn_input table_content_zd"
													readonly="readonly" name="WebAccount_Image"
													id="WebAccount_Image">
												<button type="button"
													class="btn btn-primary btn-sm input_btn_btn"
													id="btnChooseIcon" name="btnChooseIcon"
													style="float: left; margin-left: 5px;">选择</button>
											</div>
										</div>
									</td>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 39%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd">备注：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"
													class="form-control table_content_zd"
													name="WebAccount_Text" id="WebAccount_Text">
											</div>
										</div>
									</td>
								</tr>
							</table>
							<table class="table_zd" align="center" width="100%"
								style="margin-bottom: -12px;">
								<tbody>
									<tr class="table_menu_tr_zd">
										<td class="table_menu_tr_td_left_zd">
											<input type="text" placeholder="账号名称" autocomplete="off"
											class="form-control input_btn_input table_content_zd"
											id="WebAccount_Name2" style="width: 40%;">
											<button type="button"
												class="table_button_zd btn btn-sm search_rm_button_index"
												id="index_select">查询</button></td>
												
										<!-------------------弹出层 start----------------------->
										<div class="modal inmodal fade" id="myModa_role" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static">
											<div class="modal-dialog modal-lg" style="width: 400px;height: 400px;">
										         <div class="modal-content">
										             <div class="modal-header" style="padding: 10px;text-align: left;background-color: #E5E5E5;">
										                 <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
										                 </button>	
										                 <div class="rm_modal_title" id="role_title"></div> 
										             </div>
										             <div class="modal-body">
										             	 <iframe id='role_ModelId' style='border:0px;margin:0px;width:380px;height:370px;'></iframe>
										             </div>
										             <div class="modal-footer">
										      			<input type="checkbox" name="checktreelist" id="checktreelist" style="float: left;margin-top: 7px;"/>
		                								<span style="float: left;margin-left: 5px;margin-top: 5px;">全选</span>
										             	<button type="button" class="btn btn-sm input_btn_btn search_rm_button_index" data-dismiss="modal" id="closeBtn" style="float:right;">关闭</button>	
										             	<button type="button" class="btn btn-primary btn-sm" id="subtimBtn">确认</button>								             	                										 
										             </div>
										         </div>
										     </div>
										</div>

										<td class="table_menu_tr_td_right_zd" colspan="2">
											<button type="button"
												class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
												id="configWindow">显示配置维护</button>
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
						<div id="configTr" class="table_menu_tr_zd"
							style="display: none; border: 1px solid white; height: 34px;">
							<span style="padding-left: 45px;">&nbsp;&nbsp;</span>
							<div class="optionRow-right" style="margin-right: 10px;">
								<button type="button"
									class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
									id="accountConfig"
									style="margin-top: 2.5px; margin-bottom: 2px;">账号权限</button>
								<button type="button"
									class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
									id="resetPassword"
									style="margin-top: 2.5px; margin-bottom: 2px; display: none;">密码重置</button>
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


