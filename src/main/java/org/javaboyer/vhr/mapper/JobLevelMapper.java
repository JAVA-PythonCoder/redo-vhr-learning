package org.javaboyer.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboyer.vhr.model.JobLevel;

import java.util.List;

public interface JobLevelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JobLevel record);

    int insertSelective(JobLevel record);

    JobLevel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JobLevel record);

    int updateByPrimaryKey(JobLevel record);

    List<JobLevel> selectAllJobLevels();

    Integer deleteJobLevelsByIds(@Param("ids") Integer[] ids);
}