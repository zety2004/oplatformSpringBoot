package com.hklk.oplatform.dao.inter;

import com.hklk.oplatform.entity.table.Role;
import com.hklk.oplatform.entity.table.User;
import com.hklk.oplatform.entity.table.UserRoleKey;

import java.util.List;
public interface UserRoleMapper {
    int deleteByPrimaryKey(UserRoleKey key);

    int insert(UserRoleKey record);

    int insertSelective(UserRoleKey record);

    List<User> selectUserByRoleId(Integer roleId);

    List<Role> selectRoleByUserId(Integer userId);
}