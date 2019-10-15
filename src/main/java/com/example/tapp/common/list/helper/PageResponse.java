package com.example.tapp.common.list.helper;

import java.util.List;

public class PageResponse<T> {

    private long total = 0;
    private List<T> list;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setTotal(long count, Page page) {
        if (count > 0) {
            int totalPages = (int) Math.ceil((float) count / page.limit());
            this.total = totalPages;
        } else {
            this.total = count;
        }
    }

}