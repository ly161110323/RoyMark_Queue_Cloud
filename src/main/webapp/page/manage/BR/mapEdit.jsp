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
    <script type="text/javascript" src="./js/mapEdit.js"></script>
    <style>
        #page{
            position: fixed;
            bottom: 0;
            left:0;
            height: 40px;
        }
        #page ul li{
            float: left;
            display: inherit;
            padding: 0.3125rem 0.25rem 0.25rem 0.25rem;
            border-radius: 0.25rem;
            border: 0.0625rem solid black;
            width: 1.5625rem;
            height: 1.5625rem;
            text-align: center;
            margin: 0.25rem;
        }
        #page ul li:hover{
            background-color: #00aa00;
            color: aliceblue;
        }
        #page ul li a{
            text-decoration: none;
        }
        #page ul{
        }
        .active{
            background-color: #00aaff;
        }
    </style>

<body class="gray-bg" id="bodyBg">
<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-content" >
                    <form class="form-horizontal" role="form" action="#" method="post" id="itemInfoForm">

                        <table class="table_zd" align="center" width="100%">
                            <tr>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                style="color: red;">*</span>地图ID：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   placeholder="" class="form-control table_content_zd"
                                                   name="mapId" id="mapId">
                                        </div>
                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd"><span
                                                style="color: red;">*</span>地图名称：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   placeholder="" class="form-control table_content_zd"
                                                   name="mapName" id="mapName">
                                        </div>
                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd">上传图片：</label>
                                        <div class="col-sm-8" style="width: 60%;">
                                            <input type="file" name="mapImage" id="mapImage" accept="image/*"
                                                   style="display: none;"> <input type="text"
                                                                                  style="width: 65%;" autocomplete="off"
                                        <%--                                                                                  placeholder="768*365"--%>
                                                                                  class="form-control m-b input_btn_input"
                                                                                  readonly="readonly" name="mapImageFileName"
                                                                                  id="mapImageFileName">
                                            <button type="button"
                                                    class="btn btn-primary btn-sm input_btn_btn"
                                                    id="btnChooseMapImage"
                                                    style="float: left; margin-left: 5px;">选择</button>
                                        </div>

                                    </div>
                                </td>
                                <td style="width: 25%;">
                                    <div class="form-group">
                                        <label style="width: 38%;"
                                               class="col-sm-3 control-label input_lable_hm table_label_zd">已绑定摄像头数量：</label>
                                        <div class="col-sm-8">
                                            <input type="text" autocomplete="off" spellcheck="false"
                                                   placeholder="" class="form-control table_content_zd"
                                                   name="cameraNum" id="cameraNum" readonly="readonly">
                                        </div>
                                    </div>
                                </td>

                            </tr>


                        </table>
                        <table class="table_zd" align="center" width="100%" style="margin-bottom:-12px;">
                            <tbody>
                            <tr class="table_menu_tr_zd">
                                <td class="table_menu_tr_td_left_zd" colspan="2" style="width: 35%">
                                    <label type="text"  style="width: 10%; float: left; margin-right: 10px"
                                           class="control-label input_lable_hm table_label_zd ">地图ID</label>
                                    <select
                                            class="form-control table_content_zd" name="selectCommitWindowId"
                                            id="selectCommitMapId"
                                            style="width: 20%; float: left; margin-right: 10px">
                                        <option value="">请选择地图ID</option>
                                    </select>
                                    <label type="text"  style="width: auto; float: left; margin-right: 10px"
                                           class="control-label input_lable_hm table_label_zd ">地图名称</label>
                                    <select
                                            class="form-control table_content_zd" name="selectCommitServerId"
                                            id="selectCommitMapName"
                                            style="width: 20%; float: left; margin-right: 10px">
                                        <option value="">请选择地图名称</option>
                                    </select>


                                    <button type="button"
                                            class="btn btn-sm input_btn_btn search_rm_button_index table_button_zd"
                                            style="margin-top: 2.5px; margin-bottom: 2px;"
                                            id="changeCommit">切换地图
                                    </button>



                                </td>
                                <td class="table_menu_tr_td_left_zd" colspan="2" style="width: 38%">
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
<%--                                        <button type="button"--%>
<%--                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"--%>
<%--                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"--%>
<%--                                                id="cameraEdit">关闭摄像头编辑--%>
<%--                                        </button>--%>
<%--                                        <button type="button"--%>
<%--                                                class="btn btn-primary btn-sm input_btn_btn list_btn table_button_zd"--%>
<%--                                                style="float: left; margin-top: 2.5px; margin-bottom: 2px;"--%>
<%--                                                id="mapEdit">地图编辑编辑--%>
<%--                                        </button>--%>
                                    </div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </form>
                    <%--                    main 的positon必须设置为absolute--%>
                    <div style="height: 80%">
                        <div id="main" style="height:500px;position: absolute; left:0; right:0;margin: auto;float: top">
                            <%--                        <canvas id="myCanvas" width="1280" height="720" style="">--%>
                            <%--                            Your browser does not support the HTML5 canvas tag.--%>
                            <%--                        </canvas>--%>
                        </div>

                    </div>


                </div>
            </div>

        </div>
    </div>

</div>



<div>
    <ul id="rightMenu" style="width: 100px; position: absolute;">
<%--        <li>--%>
<%--            <button id="realtimeMinitor" onclick="realtimeMinitor()" value="实时监控">实时监控</button>--%>
<%--        </li>--%>
<%--        <li>--%>
<%--            <button id="reviewVideo" onclick="reviewVideo()" value="录像回放">录像回放</button>--%>
<%--        </li>--%>
<%--        <li>--%>
<%--            <button id="anomalyConfirm" onclick="anomalyConfirm()" value="异常确认">异常确认</button>--%>
<%--        </li>--%>
        <li>
            <button id="deleteCamera" onclick="deleteCamera()" type="button"  value="删除">删除</button>
        </li>
    </ul>
</div>

</body>




</html>