package com.wzy.demo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "MultiGetVo", description = "多次查询用")
public class MultiGetVo {
    private Integer[] ids;
}
