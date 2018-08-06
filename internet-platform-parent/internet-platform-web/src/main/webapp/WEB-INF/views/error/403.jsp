<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%response.setStatus(403);%>
<!DOCTYPE html>
<html>
<head>
	<title>403 - 操作权限不足.</title>
</head>

<body>
	<h2>403 - 操作权限不足.</h2>
	<p><a href="<c:url value="/"/>">返回首页</a></p>
</body>
</html>