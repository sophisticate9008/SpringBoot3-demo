package com.wzy.demo.vo;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "MultiVo", description = "多次查询用")
public class MultiVo {
    private List<Integer> ids;
}
