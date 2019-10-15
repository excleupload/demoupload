package com.example.tapp.common.list.helper;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class Page {

    public static final String PAGE = "page";
    public static final String OFFSET = "offset";
    public static final String LIMIT = "limit";

    private int offset = 0;
    private int limit = 10;

    private Page() {
    }

    private Page(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public int offset() {
        return offset;
    }

    public Page offset(int offset) {
        this.offset = offset;
        return this;
    }

    public int limit() {
        return limit;
    }

    public Page limit(int limit) {
        this.limit = limit;
        return this;
    }

    public static Page of(int offset, int limit) {
        return new Page(offset, limit);
    }

    public static Page option(int offset, int limit) {
        Page page = new Page(offset, limit);
        page.offset((page.offset() - 1) * page.limit());
        return page;
    }

    public static Page getDefault() {
        return new Page();
    }

    public static void setPageOptions(HashMap<String, Object> options, HttpServletRequest request) {
        if ((request.getParameter(Page.OFFSET) != null && !request.getParameter(Page.OFFSET).isEmpty())
                && (request.getParameter(Page.LIMIT) != null && !request.getParameter(Page.LIMIT).isEmpty())) {
            Page page = Page.of(Integer.parseInt(request.getParameter(Page.OFFSET)),
                    Integer.parseInt(request.getParameter(Page.LIMIT)));
            page.offset((page.offset() - 1) * page.limit());
            options.put(PAGE, page);
        } else {
            options.put(PAGE, Page.getDefault());
        }
    }

    @Override
    public String toString() {
        return "{ limit = " + limit + " , offset = " + offset + "}";
    }
}