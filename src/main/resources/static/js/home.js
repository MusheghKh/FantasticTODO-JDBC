$(document).ready(function () {

    var todos = [];

    $.get(baseUrl + "/api/todos", {}, function (data) {
        todos = data;
        drawTodos(todos);
    });

    $('.add-items').submit(function(event)
    {
        event.preventDefault();

        var title = $('#todo-list-item').val();

        if(title && title !== "")
        {
            $.post(baseUrl + "/api/todos", $("#todoForm").serialize(), function (data) {
                todos.push(data);
                drawTodos(todos);
                $('#todo-list-item').val("");
            });
        }

    });

    $(document).on('change', '.checkbox', function()
    {
        var header = $("meta[name='_csrf_header']").attr("content");
        var token = $("meta[name='_csrf']").attr("content");

        var $t = $(this);
        var index = $t.parent().attr('data-index');
        var completed = !$t.attr('checked');

        var todo = todos[index];
        todo.completed = completed;
        delete todo.createdAt;
        delete todo.user;

        console.log(todo);

        $.ajax({
            url: baseUrl + "/api/todos/" + todo.id,
            method: "PUT",
            data: todo,
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (data) {
                console.log(data);
                todos[index] = data;
                drawTodos(todos);
            }
        });
    });

    $(document).on('click', '.remove', function()
    {
        var header = $("meta[name='_csrf_header']").attr("content");
        var token = $("meta[name='_csrf']").attr("content");

        var index = $(this).parent().attr('data-index');
        $.ajax({
            url: baseUrl + "/api/todos/" + todos[index].id,
            type: "DELETE",
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function () {
                todos.splice(index, 1);

                drawTodos(todos);
            }
        });

    });

});

function drawTodos(data) {
    var part1Completed = "<li ";
    var part2Index = " data-index='";
    var part3Checked = "'><input class='checkbox' type='checkbox' ";
    var part4Title = "/>";
    var part5End = "<a class='remove'>x</a><hr></li>";

    $('#list-items').html("");

    $.each(data, function (index, todo) {
        var part1;
        var part2 = index;
        var part3;
        var part4 = todo.title;
        if(todo.completed === true){
            part1 = "class='completed'";
            part3 = "checked='checked'";
        }else {
            part1 = "";
            part3 = "";
        }
        var html = part1Completed + part1
            + part2Index + part2
            + part3Checked + part3
            + part4Title + part4
            + part5End;
        $('#list-items').append(html);
    });
}