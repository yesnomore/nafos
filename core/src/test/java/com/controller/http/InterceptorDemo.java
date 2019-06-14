package com.controller.http;

import io.netty.channel.ChannelHandlerContext;
import nafos.bootStrap.handle.http.NsRequest;
import nafos.core.entry.ResultStatus;
import nafos.core.entry.error.BizException;
import nafos.core.mode.interceptor.AbstractHttpInterceptor;
import org.springframework.stereotype.Component;

@Component
public class InterceptorDemo extends AbstractHttpInterceptor {
    @Override
    public ResultStatus interptor(ChannelHandlerContext ctx, NsRequest req) {
        if("hello".equals(req.stringQueryParam("hello"))){
            return new ResultStatus(true);
        }else{
            return new ResultStatus(false, BizException.LOGIN_SESSION_TIME_OUT);
        }
    }
}
