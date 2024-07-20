package com.wzy.demo.service.impl;

import com.wzy.demo.common.AppFileUtils;
import com.wzy.demo.entity.Commission;
import com.wzy.demo.entity.Reply;
import com.wzy.demo.mapper.CommissionMapper;
import com.wzy.demo.mapper.ReplyMapper;
import com.wzy.demo.service.CommissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wzy
 * @since 2024-06-11
 */
@Service
public class CommissionServiceImpl extends ServiceImpl<CommissionMapper, Commission> implements CommissionService {


    @Autowired
    private ReplyMapper replyMapper;

    @Override
    public boolean locked(Integer commissionId) {
        return this.getById(commissionId).getCurrentNum() > 0;
    }

    @Override
    public Integer getAndSetCommissionNum(Integer id) {
        QueryWrapper<Reply> replyQueryWrapper = new QueryWrapper<>();
        replyQueryWrapper.eq("commission_id", id);
        replyQueryWrapper.ne("state", 2);
        replyQueryWrapper.ne("state", -2);
        long count = replyMapper.selectCount(replyQueryWrapper);
        Commission commission = this.getById(id).setCurrentNum((int) count);
        if(commission.getState() == 0 || commission.getState() == 2) {
            if(commission.getCurrentNum() >= commission.getNum()) {
                commission.setState(2);
            }else {
                commission.setState(0);
            }
        }
        this.updateById(commission);
        return (int) count;
    }

    @Override
    public String htmlStringHandle(String htmlString) {

        String regex = "path=([^\\s\"']*?\\.jpg_temp)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(htmlString);
        while (matcher.find()) {
            // 提取匹配的部分
            AppFileUtils.renameFile(matcher.group(1));
        }
        return htmlString.replace("_temp", "");
    }

    @Override
    public String htmlStringHandle(String oldStr, String newStr) {
        oldStr = this.htmlStringHandle(oldStr);
        newStr = this.htmlStringHandle(newStr);
        List<String> oldPaths = extractPaths(oldStr);
        List<String> newPaths = extractPaths(newStr);
        
        Set<String> newPathSet = new HashSet<>(newPaths);
        List<String> missingPaths = new ArrayList<>();
        
        for (String path : oldPaths) {
            if (!newPathSet.contains(path)) {
                missingPaths.add(path);
            }
        }
        for (String path : missingPaths) {
            AppFileUtils.removeFileByPath(path);
        }
        return newStr;
    }
    private List<String> extractPaths(String html) {
        List<String> paths = new ArrayList<>();
        String regex = "path=([^\"\\s]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        
        while (matcher.find()) {
            paths.add(matcher.group(1));
        }
        
        return paths;
    }

    
}
