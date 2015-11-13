<%-------@ page import="com.ami.entity.Employee" --%>
<html>

<head>
    <title>My JSP 'indexnewpage.jsp' starting page</title>
</head>

<body>
<%-----
    Employee employee = (Employee) request.getAttribute("employee");
--%>

<form action="/admin/employee" method="get" >
    <p><small><i>Employee Name</i></small> : <h3><%-- employee.getFirstName(); %> <% employee.getLastName(); --%></h3></p>
    <input type="text" size=10 name="id" value="<%-- employee.getId(); --%>"/>
    <button type="submit">Get Employee</button>
</form>
</body>

</html>