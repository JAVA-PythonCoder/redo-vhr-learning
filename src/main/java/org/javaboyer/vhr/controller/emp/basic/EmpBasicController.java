package org.javaboyer.vhr.controller.emp.basic;

import org.javaboyer.vhr.model.Employee;
import org.javaboyer.vhr.model.RespBean;
import org.javaboyer.vhr.model.RespPageBean;
import org.javaboyer.vhr.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * post、delete、put、get请求数据时，四个在http请求定义中都可携带body内容，但在有的实现体（如postman等中不允许get有body）中get不能携带body，其他三个无论何种情况都可以携带body内容。
 * body内容可以是表单，也可是JSON数据。
 * get如果可携带body，则其会将body解析拼接到get的url中?key=value&key=value&...。
 * post、delete、put、get都可以从Url?key=value&key=value&...中读取key=value。
 * 所以post、delete、put即可从url中，也可从body中传递获取数据，不同的是适用场景限制。
 * 如果从url中读取数据，数据会自适应，不需要特别指定，如：?username=admin&password=123 中key是username的值是admin 不需要写成"admin"
 *
 * @author zhangfu.huang
 * @date 2022年03月22日 17:19
 */
@RestController
@RequestMapping("/emp/basic")
public class EmpBasicController {

    @Autowired
    EmployeeService employeeService;

    /**
     * getEmployeeByPage分页查询员工信息
     *
     * @param page：接收前端分页查询的页数
     * @param size：接收前端某页查询的记录数
     * @return org.javaboyer.vhr.model.RespPageBean
     * @author zhangfu.huang
     * @date 2022/3/22 17:37
     */
    @GetMapping("/")
    public RespPageBean getEmployeeByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, String keywords) {
        // 校验前端传过来的字段是否合法
        if (page == null || size == null || page < 0 || size < 0) {
            return null;
        }
        return employeeService.getEmployeeByPage(page, size, keywords);
    }

    @PostMapping("/")
    public RespBean addEmployee(@RequestBody Employee employee) {
        if (employeeService.addEmployee(employee) == 1) {
            return RespBean.ok("员工添加成功");
        }
        return RespBean.error("员工添加失败");
    }
}
