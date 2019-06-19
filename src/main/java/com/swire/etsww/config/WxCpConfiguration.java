package com.swire.etsww.config;

import com.google.common.collect.Maps;
import lombok.val;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.WxCpInMemoryConfigStorage;
import me.chanjar.weixin.cp.message.WxCpMessageRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Configuration
@EnableConfigurationProperties(WxCpProperties.class)
public class WxCpConfiguration {

    private WxCpProperties properties;

    private static Map<Integer, WxCpMessageRouter> routers = Maps.newHashMap();
    private static Map<Integer, WxCpService> cpServices = Maps.newHashMap();

    @Autowired
    public WxCpConfiguration(WxCpProperties properties) {
        this.properties = properties;
    }


    public static Map<Integer, WxCpMessageRouter> getRouters() {
        return routers;
    }

    public static WxCpService getCpService(Integer agentId) {
        return cpServices.get(agentId);
    }

    @PostConstruct
    public void initServices() {
        cpServices = this.properties.getAppConfigs().stream().map(a -> {
            val configStorage = new WxCpInMemoryConfigStorage();
            configStorage.setCorpId(this.properties.getCorpId());
            configStorage.setAgentId(a.getAgentId());
            configStorage.setCorpSecret(a.getSecret());
            configStorage.setToken(a.getToken());
            configStorage.setAesKey(a.getAesKey());
            val service = new WxCpServiceImpl();
            service.setWxCpConfigStorage(configStorage);
            routers.put(a.getAgentId(), this.newRouter(service));
            return service;
        }).collect(Collectors.toMap(service -> service.getWxCpConfigStorage().getAgentId(), a -> a));
    }

    private WxCpMessageRouter newRouter(WxCpService wxCpService) {
        final val newRouter = new WxCpMessageRouter(wxCpService);
        return newRouter;
    }
}
