package com.example.tapp.filter;

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

import com.example.tapp.common.utils.AppUtils;
import com.example.tapp.common.utils.KeyUtils;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.business.service.UserService;
import static com.example.tapp.common.response.ResponseUtils.errorMessageWithStatus;

@Component
public class UserAuthFilter extends GenericFilterBean {

    @Autowired
    private UserService userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest _request = (HttpServletRequest) request;
       
        String authHeader = _request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(KeyUtils.TOKEN_PREFIX)) {
            String authToken = authHeader.replace(KeyUtils.TOKEN_PREFIX, "");
            try {
                if (userService.checkToken(authToken)) {
                    chain.doFilter(_request, response);
                } else {
                    send((HttpServletResponse) response, "");
                    return;
                }
            } catch (GeneralException | RecordNotFoundException e) {
                send((HttpServletResponse) response, e.getMessage());
                return;
            }
        } else {
            send((HttpServletResponse) response, "");
            return;
        }


    }

    private void send(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(KeyUtils.RESPONSE_JSON);
        response.getWriter().println(AppUtils.jsonStringify(errorMessageWithStatus
                .apply(message.isEmpty() ? "Authentication is required." : message, HttpStatus.UNAUTHORIZED)));
    }
}