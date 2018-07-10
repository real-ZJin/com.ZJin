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
    <title>我的粉丝</title>
</head>
<body>
    <p align="center">我的粉丝</p>
    <c:forEach items="${fans}" var="fan">
        <p align="center">${fan}</p>
    </c:forEach>
</body>
</html>
