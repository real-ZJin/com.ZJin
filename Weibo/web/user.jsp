<%--
  Created by IntelliJ IDEA.
  User: Yoga3
  Date: 2018/6/11
  Time: 19:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>用户搜索</title>
</head>
<body>
<p align="center">用户id：${uid}</p>
<table align="center" border="0">
    <tr align="center">
        <td width="900" colspan="3" align="center"><font size="8">Ta的主页</font></td>
    </tr>
    <tr align="center" aria-rowspan="3">
        <td width="300" align="right"><a href="/attends?uid=${uid}"><font size="5">关注</font></a></td>
        <td width="300"></td>
        <td width="300" align="left"><a href="/fans?uid=${uid}"><font size="5">粉丝</font></a></td>
    </tr>
    <tr align="center">
        <td width="900" colspan="3" align="left"><font size="5">Ta的动态：</font></td>
    </tr>
    <c:forEach items="${contents}" var="content">
        <tr align="center">
            <td width="450" align="center">${content.content}</td>
            <td width="450" align="center">${content.date}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
