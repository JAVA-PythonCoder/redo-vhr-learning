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

    public Integer addDept(Department dept) {
        // 对于增加的部门默认设置启用
        dept.setEnabled(true);
        departmentMapper.addDept(dept);
        return dept.getResult();

    }

    public void deleteDept(Department dept) {
        departmentMapper.deleteDept(dept);
    }
}
