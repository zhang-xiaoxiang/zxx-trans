package com.github.aop;


import com.github.uitl.TransUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class AutoTransAspect {

    @AfterReturning(pointcut = "@annotation(com.github.annotation.AutoTrans)", returning = "methodResult")
    public Object afterReturning(JoinPoint joinPoint, Object methodResult) {
        TransUtil.trans(methodResult);
        return methodResult;
    }

}
