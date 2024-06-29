package com.wzy.demo.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.SessionListenerAdapter;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import com.wzy.demo.common.Constast;
import com.wzy.demo.filter.JwtFilter;
import com.wzy.demo.realm.JwtRealm;
import com.wzy.demo.realm.UserRealm;

import jakarta.servlet.Filter;
import lombok.Data;
@Configuration
@ConfigurationProperties(prefix = "shiro")
@Data
public class ShiroAutoConfig {

    private static final String SHIRO_FILTER = "shiroFilter";
    // 加密方式
    private String hashAlgorithmName = Constast.AlgorithmName;
    // 散列次数
    private int hashIterations = Constast.HASHITERATIONS;
    private String loginUrl = "/index.html";
    private final Logger logger = LoggerFactory.getLogger(MySessionListener.class);
    private String[] anonUrls;
    private String logOutUrl;
    private String[] authcUrls;
    @Bean("sessionManager")
    public DefaultWebSessionManager sessionManager() {
        
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 设置会话超时时间，单位毫秒
        sessionManager.setGlobalSessionTimeout(18000000); // 300分钟
        // 是否删除过期的会话
        sessionManager.setDeleteInvalidSessions(true);
        // 设置定时检查失效的会话
        sessionManager.setSessionValidationSchedulerEnabled(true);
        // 配置会话监听器
        Collection<SessionListener> sessionListeners = new ArrayList<>();
        sessionListeners.add(new MySessionListener()); // 自定义会话监听器
        sessionManager.setSessionListeners(sessionListeners);
        return sessionManager;
    }

    public class MySessionListener extends SessionListenerAdapter {

        @Override
        public void onStop(org.apache.shiro.session.Session session) {
            // 在会话停止时执行注销后台内容的操作，可以根据需要调用相应的服务或方法
            
            // 调用后台注销方法，例如清除缓存、关闭连接等
        }
    }
    
    /**
     * 声明凭证匹配器
     */
    @Bean("credentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName(hashAlgorithmName);
        credentialsMatcher.setHashIterations(hashIterations);
        return credentialsMatcher;
    }
    /**
     * 声明userRealm
     */
    @Bean("userRealm")
    public UserRealm userRealm(CredentialsMatcher credentialsMatcher) {
        UserRealm userRealm = new UserRealm();
        // 注入凭证匹配器
        userRealm.setCredentialsMatcher(credentialsMatcher);
        return userRealm;
    }
    @Bean("jwtRealm")
    public JwtRealm jwtRealm() {
        JwtRealm jwtRealm = new JwtRealm();
        // 注入凭证匹配器
        jwtRealm.setCredentialsMatcher(new AllowAllCredentialsMatcher());
        return jwtRealm;
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(UserRealm userRealm,JwtRealm jwtRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置 Realm，这里配置了两个 Realm，密码登录 Realm 和 JWT Realm
        securityManager.setRealms(List.of(userRealm, jwtRealm));
        // 设置 SessionManager
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    @Bean(SHIRO_FILTER)
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        // 设置安全管理器
        factoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", new JwtFilter());
        factoryBean.setFilters(filters);

        // 设置未登陆的时要跳转的页面
        factoryBean.setLoginUrl(loginUrl);
        Map<String, String> filterChainDefinitionMap = new HashMap<>();
        // 设置放行的路径
        if (anonUrls != null && anonUrls.length > 0) {
            for (String anon : anonUrls) {
                filterChainDefinitionMap.put(anon, "anon");
            }
        }
        // 设置登出的路径
        if (null != logOutUrl) {
            filterChainDefinitionMap.put(logOutUrl, "logout");
        }
        // 设置拦截的路径
        if (authcUrls != null && authcUrls.length > 0) {
            for (String authc : authcUrls) {
                filterChainDefinitionMap.put(authc, "jwt");
            }
        }
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return factoryBean;
    }
    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> delegatingFilterProxy() {
        FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean = new FilterRegistrationBean<DelegatingFilterProxy>();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName(SHIRO_FILTER);
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }

    /* 加入注解的使用，不加入这个注解不生效--开始 */
    /**
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }




}