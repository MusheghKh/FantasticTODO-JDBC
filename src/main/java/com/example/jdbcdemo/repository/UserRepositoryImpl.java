package com.example.jdbcdemo.repository;

import com.example.jdbcdemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository<Long> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User save(User entity) {
        if (entity.getId() == null) {
            jdbcTemplate.update(
                    "INSERT INTO users (username=?, password=?, role_id=?)",
                    entity.getUsername(), entity.getPassword(), entity.getRole_id());
        } else {
            jdbcTemplate.update(
                    "UPDATE users SET username=?, password=? role_id=?",
                    entity.getUsername(), entity.getPassword(), entity.getRole_id()
            );
        }
        return findByUsername(entity.getUsername());
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users",
                UserRowMapper.newInstance()
        );
    }

    @Override
    public User findOne(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id=?",
                new Object[]{id},
                UserRowMapper.newInstance()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(
                "DELETE FROM users WHERE id=?",
                id
        );
    }

    @Override
    public User findByUsername(String username) {
        try{
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM users WHERE username=?",
                    new Object[]{username},
                    UserRowMapper.newInstance()
            );
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    private static class UserRowMapper implements RowMapper<User> {

        static UserRowMapper newInstance() {
            return new UserRowMapper();
        }

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setPassword(rs.getString("password"));
            user.setUsername(rs.getString("username"));
            user.setRole_id(rs.getLong("role_id"));
            return user;
        }
    }
}
