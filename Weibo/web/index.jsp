<%--
  Created by IntelliJ IDEA.
  User: Yoga3
  Date: 2018/6/7
  Time: 16:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>用户登录</title>
  </head>
  <body>
  <form action="/showPage" method="post">
    <p align="center"><font size="8">欢迎登录微博Weibo</font></p>
    <p align="center">用户名：<input type="text" name="username"></p>
    <p align="center">密&nbsp;&nbsp;&nbsp;码：<input type="password" name="password"></p>
    <p align="center"><input type="submit" value="登录"></p>
    <p align="right"><a href="/region.jsp">没有账号？去注册</a></p>
  </form>

  </body>
</html>
