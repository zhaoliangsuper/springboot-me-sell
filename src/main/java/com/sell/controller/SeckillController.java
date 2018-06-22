package com.sell.controller;

import com.sell.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 赵亮
 * @date 2018-06-12 17:37
 * 商品秒杀
 */
@RestController
@RequestMapping("/skill")
@Slf4j
public class SeckillController
{
    @Autowired
    private SeckillService seckillService;

    /**
     *查询秒杀活动特价商品信息
     *@author：赵亮
     *@date：2018-06-12 17:40
     * http://127.0.0.1:8080/sell/skill/query/123456
    */
    @GetMapping("/query/{productId}")
    public String query(@PathVariable String productId) throws Exception
    {
        return seckillService.querySeckillProductInfo(productId);
    }
    
    /**
     *秒杀，没有抢到获得“哎呦呦，xxxx”，抢到了会返回剩余库存数量
     * http://127.0.0.1:8080/sell/skill/order/123456
     *@author：赵亮
     *@date：2018-06-12 17:43
    */
    @GetMapping("/order/{productId}")
    public String skill(@PathVariable String productId) throws Exception
    {
        log.info("@skill request, productId"+productId);
        seckillService.orderProductMockDiffUser(productId);
        return seckillService.querySeckillProductInfo(productId);
    }
}
