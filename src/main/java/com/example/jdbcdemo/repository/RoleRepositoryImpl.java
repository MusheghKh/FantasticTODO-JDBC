package com.example.jdbcdemo.repository;

import com.example.jdbcdemo.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository<Long> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoleRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Role findByName(String roleName) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM roles WHERE name=?",
                new Object[]{roleName},
                RoleRowMapper.newInstance()
        );
    }

    @Override
    public Role save(Role entity) {
        if (entity.getId() == null){
            jdbcTemplate.update(
                    "INSERT INTO roles(name) VALUES(?)",
                    entity.getName());
        }else {
            jdbcTemplate.update(
                    "UPDATE roles SET name=? WHERE id=?",
                    entity.getName(), entity.getId());
        }
        return findByName(entity.getName());
    }

    @Override
    public List<Role> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM roles",
                RoleRowMapper.newInstance()
        );
    }

    @Override
    public Role findOne(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM roles WHERE id=?",
                new Object[]{id},
                RoleRowMapper.newInstance()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(
                "DELETE FROM roles WHERE id=?",
                id);
    }

    private static class RoleRowMapper implements RowMapper<Role>{

        private static RoleRowMapper newInstance(){
            return new RoleRowMapper();
        }

        @Override
        public Role mapRow(ResultSet resultSet, int i) throws SQLException {
            Role role = new Role();
            role.setId(resultSet.getLong("id"));
            role.setName(resultSet.getString("name"));
            return role;
        }
    }
}
