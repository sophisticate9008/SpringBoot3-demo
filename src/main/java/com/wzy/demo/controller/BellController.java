package com.wzy.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wzy.demo.annotation.InjectUser;
import com.wzy.demo.common.DataGridView;
import com.wzy.demo.common.ResultObj;
import com.wzy.demo.entity.User;
import com.wzy.demo.service.BellService;
import com.wzy.demo.vo.MultiVo;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wzy
 * @since 2024-09-08
 */
@InjectUser
@RestController
@RequestMapping("/bell")
public class BellController {
    private User activeUser;
    @Autowired
    private BellService bellService;
    @GetMapping("getAll")
    public DataGridView getAll(){
        return new DataGridView(bellService.getByUserId(activeUser.getId()));
    }

    @PostMapping("remove")
    public ResultObj remove(@RequestBody MultiVo multiVo){
        if(bellService.removeByIds(multiVo.getIds())) {
            return ResultObj.DELETE_SUCCESS;
        }else {
            return ResultObj.DELETE_ERROR;
        }
        
    }
}
