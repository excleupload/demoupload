package com.example.tapp.filter;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestFilter.class);

    private static final String LOG_PETTERN = "REQUEST_ID: [{}], TIME: [{}], METHOD: [{}], HOST: [{}], URI: [{}], ENDPOINT: [{}], AGENT: [{}] ,REMOTE_ADDRESS: [{}]";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        UUID id = UUID.randomUUID();
        log.info(LOG_PETTERN, id, new Date(), request.getMethod(), request.getHeader("HOST"), request.getRequestURL(),
                request.getRequestURI(), request.getHeader("User-Agent"), request.getRemoteAddr());
        chain.doFilter(req, res);
    }
}
