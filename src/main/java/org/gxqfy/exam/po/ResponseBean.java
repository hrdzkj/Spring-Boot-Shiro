package org.gxqfy.exam.po;

// 既然想要实现restful，那我们要保证每次返回的格式都是相同的，因此建立了一个ResponseBean来统一返回的格式。
public class ResponseBean {

    // http 状态码
    private int code;

    // 返回信息
    private String msg;

    // 返回的数据
    private Object data;

    private ResponseBean() {
      super();	
    } 
    
    public ResponseBean(int code, String msg, Object data) {
    	super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
