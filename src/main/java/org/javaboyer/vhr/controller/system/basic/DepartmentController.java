package org.javaboyer.vhr.controller.system.basic;

import org.javaboyer.vhr.mapper.DepartmentMapper;
import org.javaboyer.vhr.model.Department;
import org.javaboyer.vhr.model.RespBean;
import org.javaboyer.vhr.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/")
    public RespBean addDept(@RequestBody Department dept) {
        if (departmentService.addDept(dept) == 1) {
            // 返回从前端接收的dept，方便更新树节点，因为树节点必须由label和children
            return RespBean.ok("部门添加成功", dept);
        }
        return RespBean.error("部门添加失败");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteDept(@PathVariable Integer id) {
        Department dept = new Department();
        dept.setId(id);
        departmentService.deleteDept(dept);
        Integer result = dept.getResult();
        if (result == 1) {
            return RespBean.ok("部门删除成功", dept);
        } else if (result == -2) {
            return RespBean.error("部门下还有子部门，无法删除");
        } else if (result == -1) {
            return RespBean.ok("部门下还有员工，无法删除");
        }
        return RespBean.error("部门删除失败");
    }
}
