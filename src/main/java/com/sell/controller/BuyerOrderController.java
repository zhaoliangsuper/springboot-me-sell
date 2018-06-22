package com.sell.controller;

import com.sell.VO.ResultVO;
import com.sell.converter.OrderForm2OrderDTOConverter;
import com.sell.dto.OrderDTO;
import com.sell.enums.ResultEnum;
import com.sell.exception.SellException;
import com.sell.form.OrderForm;
import com.sell.service.BuyerService;
import com.sell.service.OrderService;
import com.sell.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 廖师兄
 * 2017-06-18 23:27
 */
@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController
{

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;



    //创建订单
    @PostMapping("/create")
    public ResultVO<Map<String, String>> create(@Valid OrderForm orderForm,
                                                BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            log.error("【创建订单】参数不正确, orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList()))
        {
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }

        OrderDTO createResult = orderService.create(orderDTO);

        Map<String, String> map = new HashMap<>();
        map.put("orderId", createResult.getOrderId());

        return ResultVOUtil.success(map);
    }

    //订单列表  http://127.0.0.1:8080/sell/buyer/order/list?openid=110110
    @GetMapping("/list")
    //@Cacheable(cacheNames = "product", key = "productList")//缓存到redis中，如果返回值和order下面的list是同一个对象可以这么用
    @CacheEvict(cacheNames = "product", key = "#openid")//清除缓存，调用这个方法之后会做清除操作
    public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid,
                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size)
    {
        if (StringUtils.isEmpty(openid))
        {
            log.error("【查询订单列表】openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        PageRequest request = new PageRequest(page, size);
        Page<OrderDTO> orderDTOPage = orderService.findList(openid, request);

        return ResultVOUtil.success(orderDTOPage.getContent());
    }


    //订单详情
    @GetMapping("/detail")
    public ResultVO<OrderDTO> detail(@RequestParam("openid") String openid,
                                     @RequestParam("orderId") String orderId)
    {
        OrderDTO orderDTO = buyerService.findOrderOne(openid, orderId);
        return ResultVOUtil.success(orderDTO);
    }

    //取消订单
    @PostMapping("/cancel")
    public ResultVO cancel(@RequestParam("openid") String openid,
                           @RequestParam("orderId") String orderId)
    {
        buyerService.cancelOrder(openid, orderId);
        return ResultVOUtil.success();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultVO<Map<String, String>> Login(String username, String password)
    {
        return ResultVOUtil.success("adfsdfds");
    }
}
