package com.wzy.demo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(name = "HomeVo", description = "分页查询过滤用")
public class PageVo {
    @Schema(description = "页码")
    private Integer page;

    @Schema(description = "每页数量")
    private Integer limit;
}
