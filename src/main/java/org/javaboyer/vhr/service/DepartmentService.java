package org.javaboyer.vhr.service;

import org.javaboyer.vhr.mapper.DepartmentMapper;
import org.javaboyer.vhr.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangfu.huang
 * @date 2022年03月18日 20:34
 */
@Service
public class DepartmentService {

    @Autowired
    DepartmentMapper departmentMapper;

    public List<Department> getAllDepartments() {

        return departmentMapper.getAllDepartmentsByPid(-1);

    }
}
