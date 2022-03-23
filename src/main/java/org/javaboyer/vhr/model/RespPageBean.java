package org.javaboyer.vhr.model;

import java.util.List;

/**
 * RespPageBean封装分页查询的结果，将每次查询的记录放在data中，不过total则存放SQL无limit约束查询的记录总数
 *
 * @author zhangfu.huang
 * @date 2022年03月22日 17:23
 */
public class RespPageBean {
    private Long total;
    private List<?> data;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
