package org.javaboyer.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboyer.vhr.model.HrRole;

import java.util.List;

public interface HrRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrRole record);

    int insertSelective(HrRole record);

    HrRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HrRole record);

    int updateByPrimaryKey(HrRole record);

    void deleteByHrId(Integer hrId);

    Integer insertHrWithRoles(@Param("hrId") Integer hrId, @Param("roleIds") Integer[] roleIds);
}