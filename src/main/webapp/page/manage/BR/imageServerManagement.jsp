<%--
  Created by IntelliJ IDEA.
  User: liucl
  Date: 2021/3/24
  Time: 8:08 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.24/css/jquery.dataTables.css">
    <script src="http://code.jquery.com/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.js"></script>
</head>
<body>
<table id="table_id" class="display">
    <thead>
    <tr>
        <th>服务器ID</th>
        <th>服务器IP</th>
        <th>端口</th>
        <th>服务器状态</th>
        <th>程序状态</th>
        <th>操作</th>


    </tr>
    </thead>
<%--    <tbody>--%>
<%--    <tr>--%>
<%--        <td>Row 1 Data 1</td>--%>
<%--        <td>Row 1 Data 2</td>--%>
<%--    </tr>--%>
<%--    <tr>--%>
<%--        <td>Row 2 Data 1</td>--%>
<%--        <td>Row 2 Data 2</td>--%>
<%--    </tr>--%>
<%--    </tbody>--%>
</table>
<script>

    $(document).ready( function () {
        $('#table_id').DataTable({
            // "scrollY": 400,
            // select: true,
            "ajax" : {
                "url" : "${ctx}/server/getAll",
                "type" : "get",

                "dataSrc" : "servers"
            },
            "columns": [
                {"data": "id"},
                {"data": "ip"},
                {"data": "port"},
                {"data": "name"},

            ]


        });
    } );
</script>
</body>
</html>
