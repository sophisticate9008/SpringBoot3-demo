package com.wzy.demo.aspect;

import java.lang.reflect.Field;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;
import org.aspectj.lang.ProceedingJoinPoint;
import com.wzy.demo.common.WebUtils;
import com.wzy.demo.entity.User;

@Aspect
@Component
public class InjectUserAspect {
    @Around("within(@com.wzy.demo.annotation.InjectUser *)")
    public Object injectUser(ProceedingJoinPoint joinPoint) throws Throwable {
        User user = (User) WebUtils.getSession().getAttribute("user");
        Object target = joinPoint.getTarget();

        // 使用反射设置目标对象的字段值
        try {
            Field field = target.getClass().getDeclaredField("activeUser");
            field.setAccessible(true);
            field.set(target, user);
        } catch (NoSuchFieldException | IllegalAccessException e) {

        }

        return joinPoint.proceed();

    }

}
