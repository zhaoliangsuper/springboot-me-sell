package com.sell.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author 赵亮
 * @date 2018-06-12 18:15
 * redis锁
 */
@Component
@Slf4j
public class RedisLock
{
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 加锁
     *
     * @param key
     * @param value 当前时间+超时时间（就是一个时间戳）
     * @author：赵亮
     * @date：2018-06-12 18:16
     */
    public boolean lock(String key, String value)
    {

        //SETNX key value命令，http://www.redis.cn/commands/setnx.html  文档
        if (redisTemplate.opsForValue().setIfAbsent(key, value))//可以设置的话返回就是true
        {
            return true;
        }
        //currentValue = A，这两个现成的value都是B，key都是productID，但是只会是一个线程拿到锁
        String currentValue = redisTemplate.opsForValue().get(key);
        //如果锁过期，存在并且小于当前时间
        if(!StringUtils.isEmpty(currentValue)
                && Long.parseLong(currentValue)<System.currentTimeMillis())
        {
            //获取上一个锁的时间，到了这里肯定是某一个线程先执行这段代码，执行完后oldValue就是B了
            String oldValue = redisTemplate.opsForValue().getAndSet(key,value);
            //第三个线程来了之后获取的oldValue就是B了，currentValue就是A，所以不相等，要等待
            if(!StringUtils.isEmpty(oldValue)&&oldValue.equals(currentValue))
            {
                return true;
            }
        }
        return false;
    }

    /**
     *解锁
     *@author：赵亮
     *@date：2018-06-13 11:32
    */
    public void unlock(String key,String value)
    {
        try
        {
            String currentValue = redisTemplate.opsForValue().get(key);
            if(!StringUtils.isEmpty(currentValue)&& currentValue.equals(value))
            {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        }
        catch(Exception ex)
        {
            log.error("【redis分布式锁】解锁异常");
        }

    }
}
