package com.wzy.demo.service;

import com.wzy.demo.entity.Commission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wzy
 * @since 2024-06-11
 */
public interface CommissionService extends IService<Commission> {
    boolean locked(Integer commissionId);
    Integer getAndSetCommissionNum(Integer commissionId);
    String htmlStringHandle(String htmlString);
    String htmlStringHandle(String oldStr, String newStr);

    void setCommissionExpiredJob(Commission commission);
    void setCommissionBeginJob(Commission commission);
}
