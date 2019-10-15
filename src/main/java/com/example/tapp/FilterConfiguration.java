package com.example.tapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.tapp.filter.AdminAuthFilter;
import com.example.tapp.filter.RequestFilter;
import com.example.tapp.filter.UserAuthFilter;

@Configuration
public class FilterConfiguration {

    @Autowired
    private AdminAuthFilter adminAuthFilter;

    @Autowired
    private UserAuthFilter userAuthFilter;

    @Bean(name = "requestInfoFilter")
    public FilterRegistrationBean<RequestFilter> filterRegistrationBean() {
        FilterRegistrationBean<RequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestFilter());
        registration.addUrlPatterns("/*");
        registration.setName("requestFilter");
        return registration;
    }

    @Bean(name = "adminAuthFilterRegistrationBean")
    public FilterRegistrationBean<?> adminAuthFilterRegistrationBean() {
        FilterRegistrationBean<AdminAuthFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(adminAuthFilter);
        filterRegistrationBean.addUrlPatterns("/admin/*");
        return filterRegistrationBean;
    }

    @Bean(name = "userAuthFilterRegistrationBean")
    public FilterRegistrationBean<?> userAuthFilterRegistrationBean() {
        FilterRegistrationBean<UserAuthFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(userAuthFilter);
        filterRegistrationBean.addUrlPatterns("/auth/*");
        return filterRegistrationBean;
    }
}