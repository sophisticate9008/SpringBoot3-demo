package com.wzy.demo.filter;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.regex.Pattern;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;

import com.wzy.demo.common.JwtToken;

@Component
public class JwtFilter extends AuthenticatingFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        // 从请求头或请求参数中获取 JWT Token
        String token = getJwtToken(request);
        if (token != null) {
            return new JwtToken(token);
        }
        return null; // 没有找到 JWT Token，返回 null，交给 onAccessDenied 处理
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        String[] annoUrls = (String[])((String[])mappedValue);
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        if (isAnnoUrl(requestURI, annoUrls)) {
            return true;
        }        
        String token = getJwtToken(request);
        if(token != null) {
            try {
                executeLogin(request, response);
                return true;
            }catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        return false;
        
    }
    private boolean isAnnoUrl(String requestURI, String[] annoUrls) {
        for (String urlPattern : annoUrls) {
            String regex = convertWildcardToRegex(urlPattern);
            if (Pattern.matches(regex, requestURI)) {
                return true;
            }
        }
        return false;
    }
    private String convertWildcardToRegex(String wildcard) {
        String regex = wildcard
            .replace("**", ".*")  // 匹配任意字符
            .replace("*", "[^/]*");  // 匹配单层路径中的任意字符
        return "^" + regex + "$";
    }
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 执行 JWT 认证逻辑
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        // 设置返回的状态码和消息内容
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 未授权
        httpResponse.setContentType("application/json; charset=UTF-8");
        httpResponse.getWriter().write("{\"message\": \"需要登录\"}");
        return false;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
            ServletResponse response) {
        // 认证失败处理逻辑，例如返回 HTTP 401 错误
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
            ServletRequest request, ServletResponse response) throws Exception {
        return true;
    }

    private String getJwtToken(ServletRequest request) {
        // 从请求头中获取 JWT Token
        String token = WebUtils.toHttp(request).getHeader("Authorization");
        if(token != null) {
            token.replace("Bearer ", "");
        }
        return token;
        
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        // 获取 JWT Token
        String token = getJwtToken(request);
        // 创建 JWT Token 实例
        JwtToken jwtToken = new JwtToken(token);
        // 提交给 Realm 进行认证
        Subject subject = getSubject(request, response);
        try {
            subject.login(jwtToken);
            return true;
        } catch (AuthenticationException e) {
            return false;
        }
    }
}
