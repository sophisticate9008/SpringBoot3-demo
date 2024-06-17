package com.wzy.demo.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(hidden = true)
public class DataGridView {

    private Integer code=0;
    private String msg="";
    //返回的记录总条数
    private Long count=0L;
    //返回的记录
    private Object data;

    public DataGridView(Long count, Object data) {
        this.count = count;
        this.data = data;
    }

    public DataGridView(Object data) {
        this.data = data;
    }

    public DataGridView(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
