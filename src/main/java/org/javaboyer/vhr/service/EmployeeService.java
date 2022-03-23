package org.javaboyer.vhr.service;

import org.javaboyer.vhr.mapper.EmployeeMapper;
import org.javaboyer.vhr.model.Employee;
import org.javaboyer.vhr.model.RespPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangfu.huang
 * @date 2022年03月22日 17:36
 */
@Service
public class EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    /**
     * @author zhangfu.huang
     * @date 2022/3/22 17:48
     * @param page
     * @param size
     * @param keywords
     * @return org.javaboyer.vhr.model.RespPageBean
     */
    public RespPageBean getEmployeeByPage(Integer page, Integer size, String keywords) {
        page = (page - 1) * size;
        List<Employee> data = employeeMapper.getEmployeeByPage(page, size, keywords);
        // 表中记录总数
        Long total = employeeMapper.getTotal(keywords);

        RespPageBean bean = new RespPageBean();
        bean.setTotal(total);
        bean.setData(data);
        return bean;
    }

    public Integer addEmployee(Employee employee) {

        return employeeMapper.insertSelective(employee);

    }
}
