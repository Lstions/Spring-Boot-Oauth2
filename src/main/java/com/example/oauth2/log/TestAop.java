package com.example.oauth2.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class TestAop {

    /*
     * 定义一个切入点
     */
    // @Pointcut("execution (* findById*(..))")
    @Pointcut("execution(* com.example.oauth2.rbac..*.*(..))")
    public void excudeService(){}
    /*
     * 通过连接点切入
     */
//    @Before("execution(* findById*(..)) &&" + "args(id,..)")
//    public void twiceAsOld1(Long id){
//        System.err.println ("切面before执行了。。。。id==" + id);
//
//    }

    @Around("excudeService()")
    public Object twiceAsOld(ProceedingJoinPoint thisJoinPoint){
        System.err.println ("切面执行了。。。。");
        try {
            Object o= thisJoinPoint.proceed();
            return o;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

}
