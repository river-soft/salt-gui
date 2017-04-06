package org.riversoft.salt.gui.config

import org.springframework.security.core.context.SecurityContext
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RequestInterceptor extends HandlerInterceptorAdapter {

    @Override
    void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if (!modelAndView) {
            return
        }

        def security = (request.session.getAttribute("SPRING_SECURITY_CONTEXT") as SecurityContext)?.authentication?.principal

        if (security) {
            modelAndView.addObject("security", security)
        }

        super.postHandle(request, response, handler, modelAndView)
    }
}
