package com.rxh.anew.tools;

import com.rxh.exception.PayException;
import com.rxh.utils.SystemConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/9/30
 * Time: 10:24
 * Project: Management
 * Package: com.rxh.exception
 */
public class PayExceptionResolver implements HandlerExceptionResolver {

    @Autowired
    private MessageSource messageSource;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 获取接口类型（内嵌或跳转）
        Short interfaceType = (Short) request.getSession().getAttribute("interface");
        // 输出异常信息
        logger.error(ex.getMessage(), ex);
        //ex = new PayException();
        PayException payException;
        if (ex instanceof PayException) {
            payException = (PayException) ex;
        } else {
            payException = new PayException("系统内部错误！", 6000);
            payException.setStackTrace(ex.getStackTrace());
        }
        ModelAndView modelAndView = new ModelAndView();
        if (interfaceType != null) {
            if (interfaceType == SystemConstant.INTERFACE_INNER) {
                response.setStatus(HttpStatus.OK.value());
                response.setContentType(MediaType.TEXT_HTML_VALUE);
                response.setCharacterEncoding("UTF-8");
                try {
                    response.getWriter().write("tradeNo=;billNo=;succeed=error;result=" + messageSource.getMessage("error." + payException.getCode(), null, request.getLocale()) + ";md5Info=;remark=error." + payException.getCode() + ";currency=;amount=");
                } catch (IOException e) {
                    logger.error("与客户端通讯异常：" + e.getMessage(), e);
                }
            } else {
                // 跳转捕获异常返回页面，内嵌捕获异常返回字符串
                modelAndView.addObject("code", payException.getCode());
                modelAndView.setViewName("pay/error");
            }
        } else {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.TEXT_HTML_VALUE);
            response.setCharacterEncoding("UTF-8");
            try {
                response.getWriter().write(payException.getMessage());
            } catch (IOException e) {
                logger.error("与客户端通讯异常：" + e.getMessage(), e);
            }
        }
        return modelAndView;
    }
}