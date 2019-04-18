package com.swkang.springcloud.apigateawaydemo1.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 通过guava做令牌桶的限流
 */
@Component
public class OrderRateLimiterFilter extends ZuulFilter {

    //每秒钟创建1000个令牌
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(1000);

    @Override
    public String filterType() {
        return PRE_TYPE;  //过滤器类型为前置
    }

    @Override
    public int filterOrder() {
        return -4;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        //对订单接口做限流
        if("/apigateaway/order/api/v1/order/save".equalsIgnoreCase(request.getRequestURI())){
            return true;
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        if(!RATE_LIMITER.tryAcquire()){
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(429); //请求太多
        }
        return null;
    }

}
