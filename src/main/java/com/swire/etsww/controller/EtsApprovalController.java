package com.swire.etsww.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 招标审批控制类
 *
 * @author Neal on 2019/6/20
 */
@RestController
@RequestMapping("approval")
public class EtsApprovalController {

    @RequestMapping(value = "/oauth2", method = RequestMethod.GET)
    public void oauth2(HttpServletRequest request, HttpServletResponse response) {
//        String code = HttpUtil.get("",wxCpOAuth2Service.buildAuthorizationUrl("http://172.18.8.43:8080/ets-ww/approval/index",null,"snsapi_base"));
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpServletRequest request, HttpServletResponse response) {
        return "/approval/index";
    }
}
