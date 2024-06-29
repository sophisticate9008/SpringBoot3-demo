package com.wzy.demo.common;

import java.security.DrbgParameters.Reseed;
import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Hidden
public class ResultObj implements Cloneable{
    
    private Integer code;
    private String msg;
    private Map<String, String> args;
    public static final ResultObj LOGIN_SUCCESS= new ResultObj(Constast.SUCCESS,"登陆成功", null);
    public static final ResultObj LOGIN_ERROR= new ResultObj(Constast.ERROR,"登陆失败",null);
    public static final ResultObj LOGIN_ERROR_PASS= new ResultObj(Constast.ERROR,"用户名或密码错误",null);
    public static final ResultObj LOGIN_ERROR_CODE= new ResultObj(Constast.ERROR,"验证码错误",null);
    public static final ResultObj REGISTER_REPEAT = new ResultObj(Constast.ERROR, "用户名已被注册",null);
    public static final ResultObj REGISTER_SUCCESS = new ResultObj(Constast.SUCCESS, "注册成功",null);
    public static final ResultObj REGISTER_ERROR = new ResultObj(Constast.ERROR, "注册失败",null);
    public static final ResultObj ADD_SUCCESS = new ResultObj(Constast.SUCCESS, "添加成功",null);
    public static final ResultObj ADD_ERROR = new ResultObj(Constast.ERROR, "添加失败",null);
    public static final ResultObj UPDATE_SUCCESS = new ResultObj(Constast.SUCCESS, "修改成功",null);
    public static final ResultObj UPDATE_ERROR = new ResultObj(Constast.ERROR, "修改失败",null);
    public static final ResultObj DELETE_SUCCESS = new ResultObj(Constast.SUCCESS, "删除成功",null);
    public static final ResultObj DELETE_ERROR = new ResultObj(Constast.ERROR, "删除失败",null);
    public static final ResultObj Permission_Exceed = new ResultObj(Constast.ERROR, "越权操作",null);
    public static final ResultObj LOCK_SUCCESS = new ResultObj(Constast.SUCCESS, "锁定成功",null);
    public static final ResultObj LOCK_ERROR = new ResultObj(Constast.ERROR, "锁定失败,委托关闭或未开始或人数已满或失去锁定权",null);
    public static final ResultObj UNLOCK_SUCCESS = new ResultObj(Constast.SUCCESS, "解锁成功",null);
    public static final ResultObj UNLOCK_ERROR = new ResultObj(Constast.ERROR, "解锁失败",null);
    public static final ResultObj OPERATION_SUCCESS = new ResultObj(Constast.SUCCESS, "操作成功",null);
    public static final ResultObj OPERATION_ERROR = new ResultObj(Constast.ERROR, "操作失败",null);
    
    public ResultObj addOther(String val) {
        try {
            ResultObj newInstance = (ResultObj) this.clone();
            newInstance.msg += val;
            return newInstance;            
        } 
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return this;
        }

    }
    public ResultObj putArgs(String key, String val) {
        try {
            ResultObj newInstance = (ResultObj) this.clone();
            if(newInstance.args == null) {
                newInstance.args = new HashMap<String, String>();
                
            }
            newInstance.args.put(key, val);
            return newInstance;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return this;
        }
        
    }
}
