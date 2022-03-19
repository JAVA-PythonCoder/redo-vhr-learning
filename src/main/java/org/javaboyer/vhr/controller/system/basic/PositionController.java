package org.javaboyer.vhr.controller.system.basic;

import org.javaboyer.vhr.model.Position;
import org.javaboyer.vhr.model.RespBean;
import org.javaboyer.vhr.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 接口设计中对资源的增删改查要利用post、delete、put、get请求格式。
 * JSON不会对整型、浮点型、布尔型进行序列化
 *
 * @author zhangfu.huang
 * @date 2022年03月15日 14:34
 */
@RestController
@RequestMapping("/system/basic/pos")
public class PositionController {

    @Autowired
    PositionService positionService;

    @GetMapping("/")
    public List<Position> getAllPositions() {
        return positionService.getAllPositions();
    }

    @PostMapping("/")
    public RespBean addPosition(@RequestBody Position position) {
        if (positionService.addPosition(position) == 1) {
            return RespBean.ok("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @PutMapping("/")
    public RespBean updatePositionById(@RequestBody Position position) {
        if (positionService.updatePositionById(position) == 1) {
            return RespBean.ok("更新成功");
        }
        return RespBean.error("更新失败");
    }

    /**
     * -@PathVariable表示从url中获取值，url必须为/value1/value2形式
     *
     * @author zhangfu.huang
     * @date 2022/3/17 9:02
     * @param id
     * @return org.javaboyer.vhr.model.RespBean
     */
    @DeleteMapping("/{id}")
    public RespBean deletePositionById(@PathVariable Integer id) {
        if (positionService.deletePositionById(id) == 1) {
            return RespBean.ok("删除成功");
        }
        return RespBean.error("删除失败");
    }

    /**
     * 前端向后端传值，一般是JSON数据。
     * 也可用key/value形式，即url形如/?key1=value1&key2=value2...
     *
     * @author zhangfu.huang
     * @date 2022/3/17 9:03
     * @param ids
     * @return org.javaboyer.vhr.model.RespBean
     */
    @DeleteMapping("/")
    public RespBean deletePositionsByIds(Integer[] ids) {
        if (positionService.deletePositionsByIds(ids) == ids.length) {
            return RespBean.ok("批量删除成功");
        }
        return RespBean.error("批量删除失败");
    }
}
