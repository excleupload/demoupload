package com.example.tapp.filter;
import static com.example.tapp.common.response.ResponseUtils.errorMessageWithStatus;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.example.tapp.business.service.AdminUserService;
import com.example.tapp.common.utils.AppUtils;
import com.example.tapp.common.utils.KeyUtils;

@Component
public class AdminAuthFilter extends GenericFilterBean {

    @Autowired
    private AdminUserService adminUserService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest _request = (HttpServletRequest) request;
        if (!_request.getRequestURI().contains("login")) {
            String authHeader = _request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith(KeyUtils.TOKEN_PREFIX)) {
                String authToken = authHeader.replace(KeyUtils.TOKEN_PREFIX, "");
                try {
                    if (!adminUserService.checkToken(authToken)) {
                        send((HttpServletResponse) response);
                        return;
                    }
                } catch (Exception e) {
                    send((HttpServletResponse) response);
                    return;
                }
            } else {
                send((HttpServletResponse) response);
                return;
            }
        }
        chain.doFilter(_request, response);
    }

    private void send(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(KeyUtils.RESPONSE_JSON);
        response.getWriter().println(AppUtils
                .jsonStringify(errorMessageWithStatus.apply("Authentication is required.", HttpStatus.UNAUTHORIZED)));
    }

}
