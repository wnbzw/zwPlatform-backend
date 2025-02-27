package com.zw.zwplatform.aop;

import com.jd.platform.hotkey.client.callback.JdHotKeyStore;
import com.zw.zwplatform.annotation.HotKeyCacheable;
import com.zw.zwplatform.common.BaseResponse;
import com.zw.zwplatform.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashSet;

@Slf4j
@Aspect
@Component
public class HotKeyCacheAspect {

    //
    @Around("@annotation(com.zw.zwplatform.annotation.HotKeyCacheable)")
    public Object cacheHotKey(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 获取注解
        HotKeyCacheable hotKeyCacheable = method.getAnnotation(HotKeyCacheable.class);

        String keyPrefix = hotKeyCacheable.keyPrefix();

        //log.info("keyPrefix:{}", keyPrefix);
        // 构建缓存键
        String key = keyPrefix + joinPoint.getArgs()[0];

        log.info("key:{}", key);
        // 检查缓存中是否有数据
        if(JdHotKeyStore.isHotKey(key)){
            Object cachedValue = JdHotKeyStore.get(key);
            log.info("cachedValue:{}", cachedValue);
            if (cachedValue != null) {
                // 构建 BaseResponse 并返回
                BaseResponse response = new BaseResponse<>(ErrorCode.SUCCESS);
                response.setData(cachedValue);
                return response;
            }
        }

        // 执行方法并获取返回值
        BaseResponse result = (BaseResponse) joinPoint.proceed();
        log.info("result:{}", result);
        // 将结果存入缓存
        JdHotKeyStore.smartSet(key, result.getData());
        return result;
    }
}
