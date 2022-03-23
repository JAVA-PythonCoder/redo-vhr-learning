package org.javaboyer.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboyer.vhr.model.Hr;
import org.javaboyer.vhr.model.Role;

import java.util.List;

public interface HrMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Hr record);

    int insertSelective(Hr record);

    Hr selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Hr record);

    int updateByPrimaryKey(Hr record);

    Hr loadUserByUsername(String username);

    List<Role> getHrRolesById(Integer id);

    List<Hr> getAllHrs(@Param("id") Integer id, @Param("keywords") String keywords);

    List<Role> getHrRoles(Integer hrId);
}