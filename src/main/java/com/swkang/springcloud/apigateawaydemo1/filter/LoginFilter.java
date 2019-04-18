package com.swkang.springcloud.apigateawaydemo1.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 登录过滤器
 */
@Component
public class LoginFilter extends ZuulFilter {

    /**
     * 过滤器类型
     * @return
     */
    @Override
    public String filterType() {
        return PRE_TYPE;  //过滤器类型为前置
    }

    /**
     * 过滤器顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return 4;    //越小，越先执行
    }

    /**
     * 是否过滤
     * @return
     */
    @Override
    public boolean shouldFilter() {

        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String requestURI = request.getRequestURI();
        StringBuffer requestURL = request.getRequestURL();
        System.out.println("requestURI: " + requestURI);  ///apigateaway/order/api/v1/order/save
        System.out.println("requestURL: " + requestURL);  //http://localhost:9000/apigateaway/order/api/v1/order/save

        if("/apigateaway/order/api/v1/order/save".equalsIgnoreCase(requestURI)){  //过滤特定的访问
            return true;
        }
        return false;
    }

    /**
     * shouldFilter为true，进行相关的业务校验
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {

        System.out.println("启用拦截器");

        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String token = request.getHeader("token");
        if(StringUtils.isBlank(token)){
            token = request.getParameter("token");
        }

        //登录校验逻辑，可根据情况自定义JWT
        if(StringUtils.isBlank(token)){
            currentContext.setSendZuulResponse(false);  //不允许访问
            currentContext.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);  //设置返回码
        }
        return null;  //允许访问

    }

}
