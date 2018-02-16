<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html>
<head>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
</head>
<body>
<p><a href="${pageContext.servletContext.contextPath}/creategroup">Создать группу</a></p>
<p><a href="${pageContext.servletContext.contextPath}/createuser">Создать юзера</a></p>
<input id="group_name" size="12" placeholder="group" type="text" name="first_name">
<input id="first_name" size="12" placeholder="first name" type="text" name="first_name">
<input id="last_name" size="12" placeholder="last name" type="text" name="last_name">
<input id="search_button" type="button" align="center" value="Поиск"/>
<input id="export_button" type="button" align="center" value="Экспорт"/>
<a id="import_button" href="xml"> Импорт</a>
<div id="data">
    <dl id="search">
        <c:forEach var="group" items="${groups}">
            <dt>${group.id}: ${group.name} :
                <a href="editgroup?id=${group.id}">Изменить</a>
                <a href="deletegroup?id=${group.id}">Удалить</a>
            </dt>
            <c:forEach var="user" items="${group.users}">
                <dd>
                    ${user.id}:
                    ${user.firstName} :
                    ${user.lastName} :
                        <a href="edituser?id=${user.id}">Изменить</a>
                        <a href="deleteuser?id=${user.id}"> Удалить</a>
                </dd>
            </c:forEach>
        </c:forEach>
    </dl>
</div>
------------------------------------------------
<div id="xml">
    <dl id="export">
        <c:forEach var="group" items="${backup}">
            <dt>${group.id}: ${group.name}
            </dt>
            <c:forEach var="user" items="${group.users}">
                <dd>
                        ${user.id}:
                        ${user.firstName} :
                        ${user.lastName}
                </dd>
            </c:forEach>
        </c:forEach>
    </dl>
</div>
<script>
    $(document).ready(function() {
        $("#search_button").click(function(){
            var data = 'first_name='+$("#first_name").val()+'&last_name='+$("#last_name").val()+'&group_name='+$("#group_name").val();
            $.ajax({
                url: "search",
                type: 'POST',
                data: data,

                success: function (data) {
                    $("#search").remove();

                    var $dl = $("<dl id='search'>").appendTo($("#data"));
                    var $dt, $dd;
                    $.each(data, function(index, group) {
                        $dt = $("<dt>").appendTo($dl).text(group.id + ": " + group.name + " : ");
                        $("<a>").appendTo($dt).attr("href", "editgroup?id="+group.id).text("Изменить");
                        $("<a>").appendTo($dt).attr("href", "deletegroup?id="+group.id).text(" Удалить");
                        $.each(group.users, function(i, user) {
                            $dd = $("<dd>").appendTo($dt).text(user.id + ": " +
                                user.firstName + " : " +
                                user.lastName + " : ");
                            $("<a>").appendTo($dd).attr("href", "edituser?id="+user.id).text("Изменить");
                            $("<a>").appendTo($dd).attr("href", "deleteuser?id="+user.id).text(" Удалить");
                        });
                    });
                },
                error:function(data,status,er) {
                    alert("error: "+data+" status: "+status+" er:"+er);
                }
            });
        });
        $("#export_button").click(function(){
            var data = 'first_name='+$("#first_name").val()+'&last_name='+$("#last_name").val()+'&group_name='+$("#group_name").val();
            $.ajax({
                url: "xml",
                type: 'POST',
                data: data,

                success: function (data) {
                    $("#export").remove();

                    var $dl = $("<dl id='export'>").appendTo($("#xml"));
                    var $dt, $dd;
                    $.each(data, function(index, group) {
                        $dt = $("<dt>").appendTo($dl).text(group.id + ": " + group.name);
                        $.each(group.users, function(i, user) {
                            $dd = $("<dd>").appendTo($dt).text(user.id + ": " +
                                user.firstName + " : " +
                                user.lastName);
                        });
                    });
                },
                error:function(data,status,er) {
                    alert("error: "+data+" status: "+status+" er:"+er);
                }
            });
        });
    });
</script>
</body>
</html>
