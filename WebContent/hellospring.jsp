<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C/DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="org.springframework.context.ApplicationContext" %>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@page import="domain.HelloSpring" %>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>

<%
ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
HelloSpring helloSpring = context.getBean(HelloSpring.class);
out.println(helloSpring.sayHello("Root Context"));
%>
</body>
</html>