package com.sell.controller;

import com.sell.config.ProjectUrlConfig;
import com.sell.enums.ResultEnum;
import com.sell.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;

/**
 * Created by 廖师兄
 * 2017-07-03 01:20
 */
@Controller//不能是RestController，因为他是返回json格式的
@RequestMapping("/wechat")
@Slf4j
public class WechatController
{

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpService wxOpenService;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;


    /**
     * 构建获取openID的url地址
     * 会生成：https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx62d9089b9e644178&redirect_uri=http://f6vm97.natappfreecc&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect
     *
     * @author：赵亮
     * @date：2018-06-07 18:02
     * 调用：http://f6vm97.natappfreecc/sell/wechat/authorize?returnUrl=f6vm97.natappfreecc
     */
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl)
    {
        //1. 配置 用WechatOpenConfig这个类配置的。
        //2. 调用方法
        String url = projectUrlConfig.getWechatOpenAuthorize() + "/sell/wechat/userInfo";
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_BASE, URLEncoder.encode(returnUrl));
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl)
    {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try
        {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        }
        catch (WxErrorException e)
        {
            log.error("【微信网页授权】{}", e);
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getError().getErrorMsg());
        }

        String openId = wxMpOAuth2AccessToken.getOpenId();

        return "redirect:" + returnUrl + "?openid=" + openId;
    }

    @GetMapping("/qrAuthorize")
    public String qrAuthorize(@RequestParam("returnUrl") String returnUrl)
    {
        String url = projectUrlConfig.getWechatOpenAuthorize() + "/sell/wechat/qrUserInfo";
        String redirectUrl = wxOpenService.buildQrConnectUrl(url, WxConsts.QRCONNECT_SCOPE_SNSAPI_LOGIN, URLEncoder.encode(returnUrl));
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/qrUserInfo")
    public String qrUserInfo(@RequestParam("code") String code,
                             @RequestParam("state") String returnUrl)
    {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try
        {
            wxMpOAuth2AccessToken = wxOpenService.oauth2getAccessToken(code);
        }
        catch (WxErrorException e)
        {
            log.error("【微信网页授权】{}", e);
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getError().getErrorMsg());
        }
        log.info("wxMpOAuth2AccessToken={}", wxMpOAuth2AccessToken);
        String openId = wxMpOAuth2AccessToken.getOpenId();

        return "redirect:" + returnUrl + "?openid=" + openId;
    }
}
