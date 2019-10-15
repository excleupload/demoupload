package com.example.tapp.common.list.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class Sort {

    public static final String SORT = "sort";

    private Order order = Order.ASC;
    private List<String> columns = new ArrayList<>();

    private Sort(Order order, List<String> columns) {
        this.order = order;
        this.columns = columns;
    }

    public Order order() {
        return order;
    }

    public List<String> columns() {
        return columns;
    }

    public static Sort of(Order order, String... columns) {
        if (columns != null && columns.length > 0) {
            List<String> cols = new ArrayList<>();
            for (int i = 0; i < columns.length; i++) {
                cols.add(columns[i]);
            }
            return new Sort(order, cols);
        }
        return null;
    }

    public static void setSortOptions(HashMap<String, Object> options, HttpServletRequest request) {

        if (request.getParameter(Order.ASC.name().toLowerCase()) != null
                && !request.getParameter(Order.ASC.name().toLowerCase()).isEmpty()) {
            Sort sort = Sort.of(Order.ASC, request.getParameter(Order.ASC.name().toLowerCase()).split(","));
            options.put(Order.ASC.name(), sort);
        }

        if (request.getParameter(Order.DESC.name()) != null && !request.getParameter(Order.DESC.name()).isEmpty()) {
            Sort sort = Sort.of(Order.DESC, request.getParameter(Order.DESC.name()).split(","));
            options.put(Order.DESC.name(), sort);
        }

    }

    @Override
    public String toString() {
        return "{ order = " + order.name() + ", columns = " + columns.toString() + " }";
    }


}

