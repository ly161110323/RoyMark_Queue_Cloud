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
        //获取项目流水号
        var caseLs = "";
        //获取项目名称
        var caseName = "";
        
        $(document).ready(function() {
            //加载表格
            loadTable();

            //为查询按钮绑定点击事件
            $(document).on('click', '#index_select', function() {
                table.draw(false);
            });

            //为选择按钮绑定点击事件
            $(document).on('click', '#selectBtn', function() {
                if(dataId == ""){
                	layer.alert("请勾选一条项目信息！");
                	return;
                }
                var $cBox = $("[name=choice]:checked");
                if($cBox.length>1){
                	layer.alert("只能选择一条项目信息！");
                	return;
                }
                //返回值给父页面
                parent.$('#Case_Name').val(caseName);
                parent.$('#Case_Ls').val(caseLs);
                var index = parent.layer.getFrameIndex(window.name);  
                parent.layer.close(index);//关闭当前页  
            });
            
        });

        function loadTable() {
            $(".i-checks").iCheck({
                checkboxClass : "icheckbox_square-green",
                radioClass : "iradio_square-green",
            });
            var tableUrl = "${ctx}/HospitalqueueCase/list";
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
                    	        "sScrollY" : "400px",
                    	        "displayLength": 10,       
                    	        "sAjaxDataProp": "data",
                    	        "bServerSide": true,
                    	        "sAjaxSource":tableUrl,
                    	        "fnServerData": loadData,
                    	        "bLengthChange": false,
                                "aoColumns" : [
                                                    {
                                                        'mData' : 'caseLs',
                                                        'sTitle' : '<input type="checkbox" name="checklist" id="checkall" />',
                                                        'sName' : 'caseLs',
                                                        "width" : "50px",
                                                        'sClass' : 'center'
                                                    },
                                                    {
                                                        'mData' : 'caseLs',
                                                        'sTitle' : '序号',
                                                        'sName' : 'caseLs',
                                                        "width" : "50px",
                                                        'sClass' : 'center'
                                                    },
                                                        {
                                                            'mData' : 'caseId',
                                                            'sTitle' : '项目编号',
                                                            'sName' : 'caseId',
                                                            'sClass' : 'center'
                                                        },
                                                        {
                                                            'mData' : 'caseName',
                                                            'sTitle' : '项目名称',
                                                            'sName' : 'caseName',
                                                            'sClass' : 'center'
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
                                "initComplete": function( settings, json ) {
                                    $(".dataTables_scrollHead").css({overflow:"scroll","overflow-x":"hidden"});
                                    $(".dataTables_scrollHeadInner").css({width:"100%"});
                                    $(".dataTables_scrollHeadInner table").css({width:"100%"});
                                    $(".dataTables_scrollBody").attr('id','scrollBodyDiv');
                                    $(".dataTables_scrollBody").css({"overflow-y":"auto","overflow-x":"hidden"});
                                    var obj=document.getElementById("scrollBodyDiv");
                                    //如果数据DIV有滚动条，则标题头也需要增加滚动条，以保持一致
                                    if(obj.scrollHeight>obj.clientHeight||obj.offsetHeight>obj.clientHeight){
                                        $(".dataTables_scrollHead").css({overflow:"scroll","overflow-x":"hidden"});
                                    }else{
                                        $(".dataTables_scrollHead").css({overflow:"auto","overflow-x":"hidden"});
                                    }

                                },
                                "drawCallback": function( settings ) {
                                    $(".dataTables_scrollHead").css({overflow:"scroll","overflow-x":"hidden"});
                                    $(".dataTables_scrollHeadInner").css({width:"100%"});
                                    $(".dataTables_scrollHeadInner table").css({width:"100%"});
                                    $(".dataTables_scrollBody").attr('id','scrollBodyDiv');
                                    $(".dataTables_scrollBody").css({"overflow-y":"auto","overflow-x":"hidden"});
                                    var obj=document.getElementById("scrollBodyDiv");
                                    //如果数据DIV有滚动条，则标题头也需要增加滚动条，以保持一致
                                    if(obj.scrollHeight>obj.clientHeight||obj.offsetHeight>obj.clientHeight){
                                        $(".dataTables_scrollHead").css({overflow:"scroll","overflow-x":"hidden"});
                                    }else{
                                        $(".dataTables_scrollHead").css({overflow:"auto","overflow-x":"hidden"});
                                    }

                                },
                                //对列的数据显示进行设置
                                "columnDefs" : [
                                                    {
                                                        targets : 0,
                                                        data : "caseLs",
                                                        title : "操作",
                                                        render : function(data, type, row, meta) {
                                                            var html = "<input type='checkbox' value="+row.caseLs+" class='lsCheck' name='choice' />";
                                                            return html;
                                                        }
                                                    }
                                    ]
                            });
            
            //为表格行绑定点击事件
            $('#example1 tbody').on('click', 'tr', function() {
                dataId = $(this).find("td:eq(0) input[type='checkbox']").val();
                caseLs = $(this).find("td:eq(0) input[type='checkbox']").val();
                caseName = $(this).find("td:eq(3)").text();

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
        	var caseName = $("#Case_Name").val();
            if(caseName == null || caseName == ""){
            	caseName = "";
            }
            var pageSize = aoData.iDisplayLength;
            var pageNo = aoData.iDisplayStart % aoData.iDisplayLength == 0 ? aoData.iDisplayStart / aoData.iDisplayLength + 1 : aoData.iDisplayStart / aoData.iDisplayLength;
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
                	"caseName":caseName,
                    "pageSize":pageSize,
                    "pageNo":pageNo
                },
                success : function(resule) {
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
            return true;
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
							<table class="table_zd" align="center" width="100%" style="margin-bottom:-12px;">
								<tbody>
									<tr class="table_menu_tr_zd">
										<td class="table_menu_tr_td_left_zd"><label
											style="width: 10%;"
											class="col-sm-3 control-label input_lable_hm table_label_zd">项目名称：</label>
											<input type="text" autocomplete="off"
											class="form-control input_btn_input table_content_zd"
											id="Case_Name" style="width: 25%;">
											<button type="button"
												class="table_button_zd btn btn-sm search_rm_button_index"
												id="index_select">查询</button></td>

										<td class="table_menu_tr_td_right_zd" colspan="2">
											<button type="button"
												class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
												id="selectBtn">选择</button>
										</td>
									</tr>
								</tbody>
							</table>
						</form>
						<table id="example1" class="table table-bordered"
							style="table-layout: fixed"></table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

</html>


