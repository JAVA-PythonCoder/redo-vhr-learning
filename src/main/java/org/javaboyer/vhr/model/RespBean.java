package org.javaboyer.vhr.model;

/**
 * 封装后端响应信息，并将其返回给前端
 * http响应相关的类。status响应状态码，msg响应信息，obj响应对象。
 * 构造方法全是私有的，且ok、error方法静态，导致加载类就会生成相应的类。
 * 利用ok、error方法构造了一个封装了响应状态码、响应信息、响应实体的类。
 *
 *
 * obj响应具体对象
 *
 * @author zhangfu.huang
 * @date 2022年02月21日 23:33
 */
public class RespBean {
    private Integer status;
    private String msg;
    private Object obj;

    private RespBean() {
    }

    private RespBean(Integer status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    public static RespBean ok(String msg) {
        return new RespBean(200, msg, null);
    }
    public static RespBean ok(String msg, Object obj) {
        return new RespBean(200, msg, obj);
    }
    public static RespBean error(String msg) {
        return new RespBean(500, msg, null);
    }
    public static RespBean error(String msg, Object obj) {
        return new RespBean(500, msg, obj);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
