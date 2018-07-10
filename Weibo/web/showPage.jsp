<%--
  Created by IntelliJ IDEA.
  User: Yoga3
  Date: 2018/6/7
  Time: 18:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>微博主页</title>
</head>
<body>
    欢迎<font color="red" size="5">${username}</font>
    <table align="center" border="0">
        <tr align="center">
            <td width="900" colspan="3" align="center"><font size="8">我的主页</font></td>
        </tr>
        <tr align="center" aria-rowspan="3">
            <td width="300" align="right"><a href="/attends?uid=${uid}"><font size="5">关注</font></a></td>
            <td width="300"></td>
            <td width="300" align="left"><a href="/fans?uid=${uid}"><font size="5">粉丝</font></a></td>
        </tr>
        <tr align="center">
            <td width="900" colspan="3" align="center">
                <form action="/user" method="get">
                    <input type="text" size="100" name="uid"><input type="submit" value="搜索">
                </form>
            </td>
        </tr>
        <tr align="center">
            <td width="900" colspan="3" align="center">
                <form action="/send" method="get">
                    <textarea rows="5" cols="100" name="msg"></textarea>
                    <input type="text" name="uid" hidden value="${uid}">
                    <input type="text" name="username" hidden value="${username}">
                    <input type="text" name="password" hidden value="${password}"><input type="submit" value="发表">
                </form>
            </td>
        </tr>
        <tr align="center">
            <td width="900" colspan="3" align="left"><font size="5">我关注的动态：</font></td>
        </tr>
        <c:forEach items="${contents}" var="content">
            <tr align="center">
                <td width="300" align="center">${content.username}</td>
                <td width="300" align="center">${content.content}</td>
                <td width="300" align="center">${content.date}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
