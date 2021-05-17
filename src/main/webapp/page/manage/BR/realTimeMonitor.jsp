<%--
  Created by IntelliJ IDEA.
  User: gulante
  Date: 2021/4/12
  Time: 22:56
  To change this template use File | Settings | File Templates.
--%>
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
        <%--.gray-bg{--%>
        <%--  background: url("${ctx}/resources/images/xw/maptest.jpg") no-repeat;--%>
        <%--}--%>
    </style>
    <link rel="shortcut icon" href="${ctx}/resources/images/favicon.ico"
          type="image/x-icon"/>

    <script src="${ctx}/resources/jquery/jquery-ui-1.12.1/jquery-ui.js"></script>
    <script type="text/javascript" charset="UTF-8" src="${ctx}/static/resources/js/websocket.js"></script>
    <script type="text/javascript" src="./js/realTimeMonitor.js"></script>


<body class="gray-bg" id="bodyBg">
<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-content" >
                    <form class="form-horizontal" role="form" action="#" method="post" id="itemInfoForm">


                        <table class="table_zd" align="center" width="100%" style="margin-bottom:-12px;">
                            <tbody>
                            <tr class="table_menu_tr_zd">
                                <td class="table_menu_tr_td_left_zd" colspan="2" style="width: 35%">
                                    <label type="text"  style="width: 10%; float: left; margin-right: 10px"
                                           class="control-label input_lable_hm table_label_zd ">地图ID</label>
                                    <select
                                            class="form-control table_content_zd" name="selectCommitWindowId"
                                            id="selectCommitFloorId"
                                            style="width: 20%; float: left; margin-right: 10px">
                                        <option value="">请选择地图ID</option>
                                    </select>
                                    <label type="text"  style="width: auto; float: left; margin-right: 10px"
                                           class="control-label input_lable_hm table_label_zd ">地图名称</label>
                                    <select
                                            class="form-control table_content_zd" name="selectCommitServerId"
                                            id="selectCommitFloorName"
                                            style="width: 20%; float: left; margin-right: 10px">
                                        <option value="">请选择地图名称</option>
                                    </select>


                                    <button type="button"
                                            class="btn btn-sm input_btn_btn search_rm_button_index table_button_zd"
                                            style="margin-top: 2.5px; margin-bottom: 2px;"
                                            id="changeCommit">切换地图
                                    </button>



                                </td>
                                <td class="table_menu_tr_td_left_zd" colspan="2" style="width: 35%">
                                    <label type="text"  style="width: 15%; float: left; margin-right: 10px"
                                           class="control-label input_lable_hm table_label_zd ">添加摄像头</label>
                                    <select
                                            class="form-control table_content_zd" name="selectCommitServerId"
                                            id="selectCameraAdd"
                                            style="width: 25%; float: left; margin-right: 10px">
                                        <option value=""></option>
                                    </select>
                                    <button type="button"
                                            class="btn btn-sm input_btn_btn search_rm_button_index table_button_zd"
                                            style="margin-top: 2.5px; margin-bottom: 2px;"
                                            id="addCameraCommit">添加
                                    </button>
                                    <button type="button"
                                            class="btn btn-sm input_btn_btn search_rm_button_index table_button_zd"
                                            style="margin-top: 2.5px; margin-bottom: 2px;"
                                            id="saveCameraCommit">保存摄像头编辑
                                    </button>
                                </td>
                                <td class="table_menu_tr_td_right_zd" colspan="2">
                                    <div style="float: right;">
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="addCommit">新增
                                        </button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="modifyCommit">修改
                                        </button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="clearData">清除
                                        </button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="deleteCommit">删除
                                        </button>
                                        <button type="button"
                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"
                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"
                                                id="cameraEdit">摄像头编辑关闭
                                        </button>
                                    </div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </form>
                    <%--                    main 的positon必须设置为absolute--%>



                </div>
            </div>

        </div>
    </div>
    <div style="width: 100%;height: 100%;display: block">
        <%--<video width="1024" height="576" controls>
            <source src="http://localhost:8080/crowdManagement/getVideo?video_address=" +${video_address}
                    type="video/mp4">
        </video>--%>
        <img id="show_video" style="width: auto;height: 100%;object-fit:cover" src="">
    </div>
</div>






</body>




</html>