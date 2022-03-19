package org.javaboyer.vhr.controller.system.basic;

import org.javaboyer.vhr.mapper.DepartmentMapper;
import org.javaboyer.vhr.model.Department;
import org.javaboyer.vhr.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhangfu.huang
 * @date 2022年03月18日 20:32
 */
@RestController
@RequestMapping("/system/basic/department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @GetMapping("/")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

}
