package com.example.demo.log;

import com.example.demo.annotation.MediaStatAnnotation;
import com.example.demo.annotation.StatAnnotation;
import com.example.demo.log.property.BaseStatProperty;
import com.example.demo.model.db.Goods;
import com.example.demo.util.SpringContextUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.List;

@Aspect
@Configuration
public class UserLog {

    //test synchronization
    private static BaseStatProperty userStatProperty;

    private static final Logger log = LoggerFactory.getLogger("statlog");

    @Bean(name = "userInfoProperty")
    public BaseStatProperty userInfoProperty(){
        System.out.println("create BaseStatProperty Bean,bean name= 'userInfoProperty'");
        return new BaseStatProperty();
    }


    @PostConstruct
    public void init(){
        System.out.println("UserLog postConstruct init()");
        userStatProperty = new BaseStatProperty();
    }

    @Pointcut("@annotation(com.example.demo.annotation.StatAnnotation)")
    public void statAnnotationPointCut(){
    }

    @Around("statAnnotationPointCut()")
    public Object normalStatAround(ProceedingJoinPoint joinPoint) throws Throwable{

        Object target = joinPoint.getTarget();
        String targetName = target.getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();

        //get annotation value
        StatAnnotation statAnnotation = method.getAnnotation(StatAnnotation.class);
        int interval = statAnnotation.interval();
        String name = statAnnotation.name();
        String className = statAnnotation.className();
        String beanName = statAnnotation.beanName();
        Class statProperty = statAnnotation.getClass();

        System.out.println("userInfoAround: annotation interval=" + interval +
                " name=" + name + " className=" + className + " statProperty=" + statProperty.getName());

        System.out.println("userInfoAround: " + targetName + "." + methodName);

        //get spring bean by className
        BaseStatProperty baseStatProperty = null;
        try {
            baseStatProperty = (BaseStatProperty) SpringContextUtil.getBean(beanName);
        }catch (Exception e){
            log.info("Exception={}",e);
        }
        if(null == baseStatProperty){
            log.error("baseStatProperty is null!!!");
            return joinPoint.proceed();
        }

        baseStatProperty.addRequestNum();

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        baseStatProperty.addRspTimeMs(endTime - startTime);
        baseStatProperty.addSuccessNum();

        System.out.println("|" + name + "|req=" + baseStatProperty.getRequestNum() + "succNum=" + baseStatProperty.getSuccessNum());
        return result;
    }

    @Pointcut("execution(* com.example.demo.dao.UserDao.getGoodsById(..)) &&" + "@annotation(mediaStatAnnotation)" )
    public void goodsPointCut(MediaStatAnnotation mediaStatAnnotation){
    }

    @Around("goodsPointCut(mediaStatAnnotation)")
    public Object goodsAround(ProceedingJoinPoint joinPoint, MediaStatAnnotation mediaStatAnnotation) throws Throwable {
        Object result = null;
        Signature signature = joinPoint.getSignature();

        try{
            result = joinPoint.proceed();
            String beanName = mediaStatAnnotation.beanName();
            if(beanName.equals("userInfoProperty")){
                if(null != result){
                    List<Goods> goodsList = (List<Goods>) result;
                    BaseStatProperty baseStatProperty = (BaseStatProperty) SpringContextUtil.getBean(beanName);
                    baseStatProperty.addGoodsNum(goodsList.size());
                }
            }else {
                log.error("no this bean.beanName{}", beanName);
            }

        }catch (Throwable e){
            System.out.println("Exception: " + e);
        }

        return result;
    }


    //@Scheduled(initialDelay = 2 * 1000, fixedRate = 12 * 1000)
    public void printLog(){
        BaseStatProperty baseStatProperty = (BaseStatProperty) SpringContextUtil.getBean("userInfoProperty") ;
        log.info("|user|req={}|succ={}|avg_cost_ms={}|goods_num={}|",
                baseStatProperty.getRequestNum(), baseStatProperty.getSuccessNum(), baseStatProperty.getRspTimeMs(), baseStatProperty.getGoodsNum());
        baseStatProperty.reset();
    }


////////////////////////////////////////////////////////////////////////////////
    @Pointcut("execution(* com.example.demo.controller.UserController.getUser(..)) &&" + "args(id)")
    public void controllerLog(int id){

    }

    @Around("controllerLog(id)")
    public Object around(ProceedingJoinPoint joinPoint, int id) throws Throwable {
        userStatProperty.addRequestNum();

        System.out.println("getUser around");
        //Object[] args = joinPoint.getArgs();
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        userStatProperty.addRspTimeMs(endTime - startTime);
        userStatProperty.addSuccessNum();
        System.out.println("rspTime=" + (endTime -startTime));

        return result;
    }

//    @Scheduled(initialDelay = 2 * 1000, fixedRate = 12 * 1000)
//    public void printLog(){
//        log.info("|user|req={}|succ={}|avg_cost_ms={}|",
//                userStatProperty.getRequestNum(), userStatProperty.getSuccessNum(), userStatProperty.getRspTimeMs());
//        userStatProperty.reset();
//    }

    @Before("controllerLog(id)")
    public void before(JoinPoint joinPoint, int id){
        //log.info(joinPoint.getSignature().getDeclaringType() + ",method: " + joinPoint.getSignature().getName()
        //+ ",params: " + Arrays.asList(joinPoint.getArgs()));
        System.out.println("getUser before");
    }

    @AfterReturning(pointcut = "controllerLog(id)", returning = "retVal")
    public void after(JoinPoint joinPoint, int id, Object retVal){
        System.out.println("getUser after");
        System.out.println("result: "+ retVal);
    }

}
