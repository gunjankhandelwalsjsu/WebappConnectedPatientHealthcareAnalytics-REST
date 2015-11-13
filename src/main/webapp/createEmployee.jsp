
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Employee Create Form</title>
</head>
<body id="main_body">

<script type="text/javascript">
    function fnCreateEmployee() {
        document.forms["empoyee_detail"].submit();
    }

</script>

<h2>Employee Create Form</h2>

<form name="empoyee_detail" action="/admin/employee" method="post">

    First Name: <input type="text" name="firstName"><br>
    Last Name: <input type="text" name="lastName"><br>
    Id : <input type="text" name="id"><br>
    <td>
        <li class="buttons">
            <input class="button_text" type="submit" name="Submit" onclick="fnCreateEmployee()"/>
        </li>
    </td>

</form>
<h2>Success</h2>
</body>
</html>