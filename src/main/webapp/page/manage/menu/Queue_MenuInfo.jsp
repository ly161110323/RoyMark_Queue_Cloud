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
<script type="text/javascript" src="./js/menuInfo.js"></script>
<%--ztree下拉框树相关js开始--%>
<script type="text/javascript" src="./js/tree-select.js"></script>
<%--ztree下拉框树相关js结束--%>

<script type="text/javascript">
        var table;
        var dataId = "";

        var isSearch = "0";
        var updateName = "";
        var updateCode = "";
        
        $(document).ready(function() {

        $("#isQueueItem").iCheck('check');
        $("#isNotQueueItem").iCheck('uncheck');
        $("#isNewItem").iCheck('uncheck');
        $("#isNotNewItem").iCheck('check');

        
        //加载表格
        loadTable();
        $("#Menu_Level").val("2");
        searchUpMenuList("1"); //默认2级菜单选中，上级菜单是1级要去除

        //新增
        $(document).on('click','#addDataBtn',function() {
        if (!validateData(true)) {
        return;
        }
        var formData = new FormData();
        formData.append("uploadinfo", $('#iconFile')[0].files[0]);
        formData.append("menuCode",$("#Menu_Code").val());
        formData.append("menuName",$("#Menu_Name").val());
        formData.append("menuLevel",$("#Menu_Level").val());
        formData.append("menuUp", $("#Menu_Up").val());
        formData.append("menuIsshow", $('input[name="Menu_IsShow"]:checked').val());
        formData.append("menuIsnewwindow", $('input[name="Menu_IsNewWindow"]:checked').val());
        formData.append("menuUrl", $("#Menu_Url").val());
        formData.append("menuNo", $("#Menu_ShowNo").val());
        formData.append("menuImagepath", $("#Menu_ImagePath").val());
        formData.append("areaLs", $("#Area_Ls").val());

        $.ajax({
        url : "${ctx}/QueueMenuinfo/insert",
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
        if (data.result == "repeat") {
        layer.alert("该菜单已存在！");
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
        formData.append("uploadinfo", $('#iconFile')[0].files[0]);
        formData.append("menuLs",dataId);
        formData.append("menuCode",$("#Menu_Code").val());
        formData.append("menuName",$("#Menu_Name").val());
        formData.append("menuLevel",$("#Menu_Level").val());
        formData.append("menuUp", $("#Menu_Up").val());
        formData.append("menuIsshow", $('input[name="Menu_IsShow"]:checked').val());
        formData.append("menuIsnewwindow", $('input[name="Menu_IsNewWindow"]:checked').val());
        formData.append("menuUrl", $("#Menu_Url").val());
        formData.append("menuShowno", $("#Menu_ShowNo").val());
        formData.append("menuImagepath", $("#Menu_ImagePath").val());
        formData.append("areaLs", $("#Area_Ls").val());
        formData.append("updateName", updateName);
        formData.append("updateCode", updateCode);

        $.ajax({
        url : "${ctx}/QueueMenuinfo/update",
        type : "post",
        dataType : "json",
        processData : false, // 使数据不做处理
        contentType : false, // 不要设置Content-Type请求头
        data :formData,
        success : function(data) {
        if (data.result == "error") {
        layer.alert("服务器错误！");
        return;
        }
        if (data.result == "repeat") {
        layer.alert("该菜单已存在！");
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
        var url = "${ctx}/QueueMenuinfo/delete";
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

        //为表格行绑定点击事件
        $('#example1 tbody').on('click', 'tr', function() {

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
        dataId = $(this).find("td:eq(0) input[type='checkbox']").val();
    
            var menuUp=$(this).find("td:eq(11)").text();
            if(menuUp==null || menuUp==""){
            menuUp="0";
            }
            var menuLevel = $(this).find("td:eq(3)").text();
            if(menuLevel==null || menuLevel==""){
            menuLevel=1;
            }
            $("#Menu_Level>option[value="+$(this).find("td:eq(3)").text()+"]").prop("selected",true);
            searchUpMenuList(menuLevel-1);
            //设置上级菜单
            $("#Menu_Up>option[value="+menuUp+"]").prop("selected",true);

            $("#txtMenuLs").val($(this).find("td:eq(0) input[type='checkbox']").val());
            $("#Menu_Name").val($(this).find("td:eq(2)").text());
            $("#Menu_Code").val($(this).find("td:eq(9)").text());
            $("#Menu_Url").val($(this).find("td:eq(8)").text());
            $("#Menu_ShowNo").val($(this).find("td:eq(6)").text());
            $("#Area_Ls").val($(this).find("td:eq(13)").text());
            $("#Area_Name").val($(this).find("td:eq(10)").text());
            updateName = $("#Menu_Name").val();
            updateCode = $("#Menu_Code").val();
            
            if(ts)
            {
                // console.log("调用树控件的方法!");
                ts.changeInput();
            }
            if($(this).find("td:eq(5)").text()=="是"){
            $("#isQueueItem").iCheck('check');
            $("#isNotQueueItem").iCheck('uncheck');
            }else{
            $("#isQueueItem").iCheck('uncheck');
            $("#isNotQueueItem").iCheck('check');
            }
            if($(this).find("td:eq(7)").text()=="是"){
            $("#isNewItem").iCheck('check');
            $("#isNotNewItem").iCheck('uncheck');
            }else{
            $("#isNewItem").iCheck('uncheck');
            $("#isNotNewItem").iCheck('check');
            }
         $("#Menu_ImagePath").val($(this).find("td:eq(12)").text()); //菜单图标
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
        });

        function loadTable() {
        $(".i-checks").iCheck({
        checkboxClass : "icheckbox_square-green",
        radioClass : "iradio_square-green",
        });
        $("#is_Not_Diversion").iCheck('check');
        var tableUrl = "${ctx}/QueueMenuinfo/list";
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
        "displayLength":10,
        "sAjaxDataProp": "data",
        "bServerSide": true,
        "sAjaxSource":tableUrl,
        "fnServerData": loadData,
        "bLengthChange": false,
        "aoColumns" : [
        {
        'mData' : 'menuLs',
        'sTitle' : '<input type="checkbox" name="checklist" id="checkall" />',
        'sName' : 'menuLs',
        "width" : "50px",
        'sClass' : 'center'
        },
        {
        	'mData': 'menuLs', 
        	'sTitle': '序号', 
        	'sName': 'menuLs', 
        	"width" : "50px",
        	'sClass': 'center'
        },
        {
        'mData' : 'menuName',
        'sTitle' : '菜单名称',
        'sName' : 'menuName',
        'sClass' : 'center'
        },
        {
        'mData' : 'menuLevel',
        'sTitle' : '菜单层级',
        'sName' : 'menuLevel',
        'sClass' : 'center'
        },
        {
        'mData' : 'upName',
        'sTitle' : '上级菜单名称',
        'sName' : 'upName',
        'sClass' : 'center'
        },
        {
        'mData' : 'menuIsshow',
        'sTitle' : '是否显示',
        'sName' : 'menuIsshow',
        'sClass' : 'center'
        },
        {
        'mData' : 'menuNo',
        'sTitle' : '显示顺序',
        'sName' : 'menuNo',
        'sClass' : 'center'
        },
        {
        'mData' : 'menuIsnewwindow',
        'sTitle' : '新窗口打开',
        'sName' : 'menuIsnewwindow',
        'sClass' : 'center'
        },
        {
        'mData' : 'menuUrl',
        'sTitle' : '菜单Url',
        'sName' : 'menuUrl',
        'sClass' : 'center'
        },
        {
        'mData' : 'menuCode',
        'sTitle' : '菜单编号',
        'sName' : 'menuCode',
        'sClass' : 'center'
        },
        {
            'mData' : 'areaName',
            'sTitle' : '所属大厅',
            'sName' : 'areaName',
            'sClass' : 'center'
         },
        {
        	'mData': 'menuUp', 
        	'sTitle': 'menuUp', 
        	'sName': 'menuUp',
        	'sClass': 'hidden menuUp'
        },
        {
        'mData' : 'menuImagepath',
        'sTitle' : '菜单图标存放路径',
        'sName' : 'menuImagepath',
        'sClass' : 'hidden menuImagepath'
        },
         {
        	 'mData': 'areaLs',
        	 'sTitle': 'areaLs', 
        	 'sName': 'areaLs',
        	 'sClass': 'hidden areaLs'
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
        data : "menuLs",
        title : "操作",
        render : function(data, type, row, meta) {
        var html = "<input type='checkbox' value="+row.menuLs+" class='lsCheck' name='choice' />";
        return html;
        }
        },
        {
        targets : 5,
        data : "menuIsshow",
        title : "是否显示",
        render : function(data, type, row, meta) {
        return (data == "1") ? "是" : "否";
        }
        },
        {
        targets : 7,
        data : "menuIsnewwindow",
        title : "新窗口打开",
        render : function(data, type, row, meta) {
        return (data == "1") ? "是" : "否";
        }
        }
        ]
        });
        }

        function loadData(sSource, aoData, fnCallback) {
        var selectName = $("#selectName").val();
        var selectLevel = $("#selectLevel").val();
        if(selectName == null || selectName == ""){
            selectName = "";
        }
        if(selectLevel == null || selectLevel == ""){
            selectLevel = "";
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
        "menuLevel":selectLevel,
        "menuName":selectName,
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
        function validateData(isAdd) {
        var areaName = $("#Area_Name").val();
        var menuLevel = $("#Menu_Level").val();
        var menuUp = $("#Menu_Up").val();
        var selectMenuLs = $("#txtMenuLs").val();
        if(menuLevel==null || menuLevel==''){
        layer.alert("菜单层级不能为空！")
        return false;
        }else{
        if(menuLevel=='1' && menuUp!="" && menuUp!="0"){
        layer.alert("一级菜单不能设置上级菜单！")
        $("#Menu_Up ").val("");
        return false;
        }
        }
        if(!isAdd){
        if(selectMenuLs==menuUp){
        layer.alert("菜单不能设置上级菜单为本身！")
        $("#Menu_Up ").val("");
        return false;
        }
        }
        if($("#Menu_Name").val().trim()==""){
        layer.alert("菜单名称不能为空！")
        return false;
        }
        if($("#Menu_Code").val().trim()==""){
        layer.alert("菜单编号不能为空！")
        return false;
        }
        if(menuLevel != "1"){
        	if($("#Menu_Url").val().trim()==""){
                layer.alert("菜单Url不能为空！")
                return false;
                }
        }else{
        	
        }
        if($("#Menu_ShowNo").val().trim()==""){
        layer.alert("显示顺序不能为空！")
        return false;
        }
        if(isNaN($("#Menu_ShowNo").val())){
        layer.alert("显示顺序必须为数字！");
        return false;
        }
        if(areaName==null||areaName==""){
        	layer.alert("所属大厅不能为空！");
        	return false;
        }
        return true;
   }

        //清除数据
        function clearData() {
        	$("#selectLevel option").eq(0).prop("selected", 'selected');
        	$("#selectName").val("");
            $("#txtMenuLs").val("");
            $("#Menu_Level").val("");
            $("#Menu_Up").val("");
            $("#Menu_Name").val("");
            $("#Menu_Code").val("");
            $("#Menu_Url").val("");
            $("#Menu_ShowNo").val("");
            $("#Area_Name").val("");
            $("#Area_Ls").val("");
            var $file = $("#iconFile");
            $file.after($file.clone().val(""));
            $file.remove();
            $("#Menu_ImagePath").val("");
            $("#isQueueItem").iCheck('check');
            $("#isNotQueueItem").iCheck('check');
            $("#isNotNewItem").iCheck('check');
            $("#isNewItem").iCheck('check');
            updateName = "";
            updateCode = "";
        }
        
        function clearData2() {
            $("#txtMenuLs").val("");
            $("#Menu_Level").val("");
            $("#Menu_Up").val("");
            $("#Menu_Name").val("");
            $("#Menu_Code").val("");
            $("#Menu_Url").val("");
            $("#Menu_ShowNo").val("");
            $("#Area_Name").val("");
            $("#Area_Ls").val("");
            var $file = $("#iconFile");
            $file.after($file.clone().val(""));
            $file.remove();
            $("#Menu_ImagePath").val("");
            $("#isQueueItem").iCheck('check');
            $("#isNotQueueItem").iCheck('check');
            $("#isNotNewItem").iCheck('check');
            $("#isNewItem").iCheck('check');
            updateName = "";
            updateCode = "";
        }

        function changeMenuLevel(){
        var menuLevel = $("#Menu_Level").val();
        if(menuLevel == "1"){
        	$("#Menu_Url").attr("readonly","readonly");
        	$("#Menu_Url").addClass("input-disabled");
        }else{
        	$("#Menu_Url").removeAttr("readonly");
        	$("#Menu_Url").removeClass("input-disabled");
        }
        if(menuLevel !=null && menuLevel !=""){
        searchUpMenuList(menuLevel-1);
        }else{
        searchUpMenuList("0");
        }
        }

        function searchUpMenuList(menuLevel){
        //获取上级菜单
        $.ajax({
        type:'POST',
        url:'${ctx}/QueueMenuinfo/searchupmenulist?menuLevel='+menuLevel,
        cache:false,
        async:true,
        dataType: 'json',
        success: function(data){
        //调用成功时对返回的值进行解析，就是将新的区域列表值更新表格
        queryUpMenuList(data);
        }
        });
        }

        //返回上级菜单
        function queryUpMenuList(data){
        // console.log("data:"+JSON.stringify(data));

        var program = data.returnObject;
        if(data.errorCode!="00"){
        layer.alert("获取上级菜单失败！");
        return;
        }

        var selectObj = document.getElementById("Menu_Up");
        //清除表格中原有的信息
        $("#Menu_Up").empty();
        var content = "<option value='0'></option>";
        for(var i=0;i<program.length;i++) {
        content = content + "<option value='"+program[i].menuLs+"'>"+program[i].menuName+"</option> ";
        }
        selectObj.innerHTML = content;
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
								<input type="hidden" name="txtMenuLs" id="txtMenuLs" />

								<tr>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 40%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd"><span
												class="must_field">*</span>菜单名称&nbsp;&nbsp;：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"
													class="form-control table_content_zd" name="Menu_Name"
													id="Menu_Name">
											</div>
										</div>
									</td>

									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 33%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd"><span
												class="must_field">*</span>菜单层级：</label>
											<div class="col-sm-8">
												<select class="form-control table_content_zd"
													name="Menu_Level" id="Menu_Level"
													style="width: 100%; float: left;"
													onchange="changeMenuLevel();">
													<option value=""></option>
													<option value="1">1</option>
													<option value="2">2</option>
													<option value="3">3</option>
												</select>
											</div>

										</div>
									</td>

									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 33%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd">上级菜单：</label>
											<div class="col-sm-8">
												<select class="form-control table_content_zd" name="Menu_Up"
													id="Menu_Up" style="width: 100%; float: left;">
												</select>
											</div>
										</div>
									</td>

									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 40%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd"><span
												class="must_field">*</span>菜单编号：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"
													class="form-control table_content_zd" name="Menu_Code"
													id="Menu_Code">
											</div>
										</div>
									</td>
								</tr>

								<tr>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 40%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd"><span
												class="must_field">*</span>是否显示&nbsp;&nbsp;：</label>
											<div class="col-sm-8"
												style="margin-top: -4px; margin-left: -15px;">
												<div class="radio i-checks radio_rm">
													<label> <input type="radio" name="Menu_IsShow"
														value="1" id="isQueueItem"> <i></i> 是
													</label>
												</div>
												<div class="radio i-checks radio_rm">
													<label> <input type="radio" name="Menu_IsShow"
														value="0" id="isNotQueueItem"> <i></i> 否
													</label>
												</div>
											</div>
										</div>
									</td>

									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 33%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd"><span
												class="must_field">*</span>菜单Url：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"
													class="form-control table_content_zd" name="Menu_Url"
													id="Menu_Url">
											</div>
										</div>
									</td>

									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 33%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd"><span
												class="must_field">*</span>显示顺序：</label>
											<div class="col-sm-8">
												<input type="text" autocomplete="off" spellcheck="false"
													class="form-control table_content_zd" name="Menu_ShowNo"
													id="Menu_ShowNo">
											</div>
										</div>
									</td>

									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 40%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd"><span
												class="must_field">*</span>新窗口打开：</label>
											<div class="col-sm-8"
												style="margin-top: -4px; margin-left: -15px;">
												<div class="radio i-checks radio_rm">
													<label> <input type="radio" name="Menu_IsNewWindow"
														value="1" id="isNewItem"> <i></i> 是
													</label>
												</div>
												<div class="radio i-checks radio_rm">
													<label> <input type="radio" name="Menu_IsNewWindow"
														value="0" id="isNotNewItem"> <i></i> 否
													</label>
												</div>
											</div>
										</div>
									</td>
								</tr>

								<tr>
									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 40%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd">菜单图标&nbsp;&nbsp;：</label>
											<div class="col-sm-8">
												<input type="file" name="iconFile" id="iconFile"
													style="display: none;" /> <input type="text"
													style="width: 66%;" autocomplete="off"
													class="form-control m-b input_btn_input table_content_zd"
													readonly="readonly" name="Menu_ImagePath"
													id="Menu_ImagePath">
												<button type="button"
													class="btn btn-primary btn-sm input_btn_btn"
													id="btnChooseIcon" name="btnChooseIcon"
													style="float: left; margin-left: 5px; margin-top: 2px;">选择</button>
											</div>
										</div>
									</td>

									<td style="width: 25%;">
										<div class="form-group">
											<label style="width: 33%;"
												class="col-sm-3 control-label input_lable_hm table_label_zd"><span
												class="must_field">*</span>所属大厅：</label>
											<div class="col-sm-8">
												<input type="text" style="width: 100%;" autocomplete="off"
													class="form-control m-b input_btn_input table_content_zd"
													readonly="readonly" name="Area_Name" id="Area_Name">
												<input type="hidden" name="Area_Ls" id="Area_Ls">
											</div>
										</div>
									</td>


								</tr>


							</table>


							<table class="table_zd" align="center" width="100%"
								style="margin-bottom: -12px;">
								<tbody>
									<tr class="table_menu_tr_zd">
										<td class="table_menu_tr_td_left_zd"><select
											class="form-control table_content_zd" name="selectLevel"
											id="selectLevel" style="width: 30%; float: left;">
												<option value="">菜单层级</option>
												<option value="1">1</option>
												<option value="2">2</option>
												<option value="3">3</option>
										</select> <input type="text" placeholder="菜单名称" autocomplete="off"
											spellcheck="false"
											class="form-control input_btn_input table_content_zd"
											id="selectName" style="width: 30%;">
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