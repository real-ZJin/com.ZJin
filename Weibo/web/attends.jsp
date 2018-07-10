<%--
  Created by IntelliJ IDEA.
  User: Yoga3
  Date: 2018/6/11
  Time: 18:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>我的关注</title>
</head>
<body>
    <p align="center">我关注的人</p>
    <c:forEach items="${list}" var="att">
        <p align="center">${att}&nbsp;&nbsp;&nbsp;<a href="/notattent?uid=${uid}&att_id=${att}">取消关注</a> </p>
    </c:forEach>
</body>
</html>
