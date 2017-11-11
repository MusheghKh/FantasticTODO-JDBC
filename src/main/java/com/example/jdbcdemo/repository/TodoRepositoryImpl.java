package com.example.jdbcdemo.repository;

import com.example.jdbcdemo.model.Todo;
import com.example.jdbcdemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TodoRepositoryImpl implements TodoRepository<Long> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TodoRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Todo> findAllByUser(User user) {
        return jdbcTemplate.query(
                "SELECT * FROM todos WHERE udser_id=?",
                new Object[]{user.getId()},
                TodoRowMapper.newInstance()
        );
    }

    @Override
    public Todo save(Todo entity) {
        if (entity.getId() == null) {
            jdbcTemplate.update(
                    "INSERT INTO todos(title, completed, createdAt, user_id) VALUES(?,?,?,?)",
                    entity.getTitle(), entity.getCompleted(), entity.getCreatedAt(), entity.getUser_id()
            );
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM todos WHERE user_id=? ORDER BY DESC LIMIT 1",
                    new Object[]{entity.getUser_id()},
                    TodoRowMapper.newInstance()
            );
        }
        jdbcTemplate.update(
                "UPDATE todos SET title=?, completed=? WHERE id=?",
                entity.getTitle(), entity.getCompleted(), entity.getId()
        );
        return findOne(entity.getId());
    }

    @Override
    public List<Todo> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM todos",
                TodoRowMapper.newInstance()
        );
    }

    @Override
    public Todo findOne(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM todos WHERE id=?",
                new Object[]{id},
                TodoRowMapper.newInstance()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(
                "DELETE FROM todos WHERE id=?",
                id
        );
    }

    private static class TodoRowMapper implements RowMapper<Todo> {

        static TodoRowMapper newInstance() {
            return new TodoRowMapper();
        }

        @Override
        public Todo mapRow(ResultSet resultSet, int i) throws SQLException {
            Todo todo = new Todo();
            todo.setId(resultSet.getLong("id"));
            todo.setCompleted(resultSet.getBoolean("completed"));
            todo.setTitle(resultSet.getString("title"));
            todo.setCreatedAt(resultSet.getDate("createdAt"));
            todo.setUser_id(resultSet.getLong("user_id"));
            return todo;
        }
    }
}
