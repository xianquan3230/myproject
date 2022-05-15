package com.lcoil.commons.aop;


import com.lcoil.commons.annotation.Log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 日志切面处理类
 *
 * @author lcoil
 * @create 2020-05-24
 */
@Aspect
@Component
@Slf4j
public class LogAspect {


    /**
     * 日志切入点
     */
    @Pointcut("@annotation(com.lcoil.commons.annotation.Log)")
    public void logPointCut(){}

    /**
     * 前置通知
     * @param joinPoint
     */
    @Before(value ="logPointCut()")
    public void Before(JoinPoint joinPoint) {
         /**
         * 解析Log注解
         */
        String methodName = joinPoint.getSignature().getName();
        Method method = currentMethod(joinPoint,methodName);
        Log log = method.getAnnotation(Log.class);
    }
    
     /**
     * 环绕通知
     * @param proceedingJoinPoint
     * @return
     */
    @Around(value ="logPointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {
        log.info("环绕通知开始");
        try {
            log.info("执行方法:" + proceedingJoinPoint.getSignature().getName());
            MethodSignature signature =(MethodSignature) proceedingJoinPoint.getSignature();
//            Action action = (Action) signature.getMethod().getAnnotation(Action.class);
//            System.out.println("菜单="+action.description());
            
            Object object =  proceedingJoinPoint.proceed();
            log.info("环绕通知结束，方法返回:" + object);
            return object;
        } catch (Throwable e) {
            log.error("执行方法异常:" + e.getClass().getName());
            return null;
        }
    }

    /**
     * 后置通知
     * @param joinPoint
     */
    @After(value ="logPointCut()")
    public void After(JoinPoint joinPoint) {
        log.info("执行方法之后");
    }
    
    /**
     * 后置通知，带返回值
     * @param obj
     */
    @AfterReturning(pointcut = "logPointCut()",returning = "obj")
    public void doAfter(Object obj){
        log.info("执行方法之后获取返回值："+obj);
    }
    
    /**
     * 后置通知，异常时执行
     * @param e
     */
    @AfterThrowing(throwing = "e",pointcut = "logPointCut()")
    public void doAfterThrowing(Exception e) {
        log.info("执行方法异常："+e.getClass().getName());
    }
    
    /**
     * 获取当前执行的方法
     *
     * @param joinPoint  连接点
     * @param methodName 方法名称
     * @return 方法
     */
    private Method currentMethod(JoinPoint joinPoint, String methodName) {
        /**
         * 获取目标类的所有方法，找到当前要执行的方法
         */
        Method[] methods = joinPoint.getTarget().getClass().getMethods();
        Method resultMethod = null;
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                resultMethod = method;
                break;
            }
        }
        return resultMethod;
    }

}