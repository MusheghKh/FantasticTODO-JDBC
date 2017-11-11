package com.example.jdbcdemo.repository;

import com.example.jdbcdemo.model.Role;

public interface RoleRepository<ID> extends BaseRepository<Role, ID>{

    Role findByName(String roleName);
}
