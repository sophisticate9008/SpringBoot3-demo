package com.wzy.demo.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wzy.demo.annotation.InjectUser;
import com.wzy.demo.common.DataGridView;
import com.wzy.demo.entity.User;
import com.wzy.demo.service.BillService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wzy
 * @since 2024-09-08
 */
@RestController
@RequestMapping("/bill")
@InjectUser
public class BillController {
    User activeUser;

    @Autowired
    BillService billService;

    @GetMapping("getAll")
    public DataGridView getAll() {
        return new DataGridView(billService.getByUserId(activeUser.getId()));
    }


}
