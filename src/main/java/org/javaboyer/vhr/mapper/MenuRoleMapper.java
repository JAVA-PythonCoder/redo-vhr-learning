package org.javaboyer.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboyer.vhr.model.MenuRole;

import java.util.List;

public interface MenuRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MenuRole record);

    int insertSelective(MenuRole record);

    MenuRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MenuRole record);

    int updateByPrimaryKey(MenuRole record);

    List<Integer> selectMidsByRid(Integer rid);

    void deleteByRid(Integer rid);

    Integer insertRecords(@Param("rid") Integer rid, @Param("mids") Integer[] mids);
}