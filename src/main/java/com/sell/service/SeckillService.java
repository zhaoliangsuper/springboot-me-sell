package com.sell.service;

import com.sell.exception.SellException;
import com.sell.utils.KeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 赵亮
 * @date 2018-06-12 17:38
 */
@Component
public class SeckillService
{
    private static final int TIMEOUT = 10 * 1000;//10s
    /**
     * 国庆活动，皮蛋粥特价，限量100000份
     *
     * @author：赵亮
     * @date：2018-06-12 17:44
     */
    static Map<String, Integer> products;
    static Map<String, Integer> stock;
    static Map<String, String> orders;

    static
    {
        /**
         *模拟多个表，商品信息表，库存表，秒杀成功订单
         *@author：赵亮
         *@date：2018-06-12 17:45
         */
        products = new HashMap<>();
        stock = new HashMap<>();
        orders = new HashMap<>();
        products.put("123456", 100000);
        stock.put("123456", 100000);
    }

    private String queryMap(String productId)
    {
        return "国庆活动，皮蛋粥特价，限量份"
                + products.get(productId)
                + "还剩" + stock.get(productId) + "份"
                + "该商品成功下单用户数目："
                + orders.size() + "人";
    }

    @Autowired
    private RedisLock redisLock;

    public String querySeckillProductInfo(String productId)
    {
        return this.queryMap(productId);
    }

    public void orderProductMockDiffUser(String productId)
    {
        //加锁
        long time = System.currentTimeMillis() + TIMEOUT;
        if (!redisLock.lock(productId, String.valueOf(time)))
        {
            throw new SellException(101, "哎呦喂，人也太多了，换个姿势再试试");
        }
        //1、查询该商品库存，为0则活动结束。
        int stockNum = stock.get(productId);
        if (stockNum == 0)
        {
            throw new SellException(100, "活动结束");
        }
        else
        {
            //2、下单（模拟不同用户openid，不同）
            orders.put(KeyUtil.genUniqueKey(), productId);
            //3、减库存
            stockNum = stockNum - 1;
            //为了模拟真实环境，减库存的时候有可能业务比较多，所以100毫秒休眠
            try
            {
                Thread.sleep(100);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            stock.put(productId, stockNum);
        }
        //解锁
        redisLock.unlock(productId, String.valueOf(time));
    }
}
