
package com.filecloud.uiservice.exception.handler;

import com.filecloud.uiservice.constant.UiConst;
import com.filecloud.uiservice.dto.mvcmodel.LoginModel;
import com.filecloud.uiservice.exception.InvalidAccessException;
import com.filecloud.uiservice.exception.RecordNotFoundException;
import com.filecloud.uiservice.exception.ResponseException;
import com.filecloud.uiservice.exception.SessionExpiredException;
import feign.FeignException.BadRequest;
import feign.FeignException.Forbidden;
import feign.FeignException.NotFound;
import feign.FeignException.Unauthorized;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class UiServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({InvalidAccessException.class, Unauthorized.class, Forbidden.class})
    ModelAndView handleUnAuthorizeException(HttpServletRequest req, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("403");
        return modelAndView;
    }

    @ExceptionHandler({RecordNotFoundException.class, NotFound.class})
    ModelAndView handleNotFoundException(HttpServletRequest req, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404");
        return modelAndView;
    }

    @ExceptionHandler({SessionExpiredException.class, BadRequest.class})
    ModelAndView handleBadCredentialsException(HttpServletRequest req, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(UiConst.KEY_LOGIN_MODEL, new LoginModel());
        modelAndView.addObject(UiConst.KEY_ERROR, UiConst.MESSAGE_LOGIN);
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @ExceptionHandler({ResponseException.class, Exception.class})
    ModelAndView handleInternalServerException(HttpServletRequest req, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        return modelAndView;
    }

}
