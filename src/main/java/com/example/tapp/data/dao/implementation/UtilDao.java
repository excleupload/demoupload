package com.example.tapp.data.dao.implementation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.example.tapp.common.list.helper.Order;
import com.example.tapp.common.list.helper.Page;
import com.example.tapp.common.list.helper.Sort;

public abstract class UtilDao {
	   
    protected void applySorting(CriteriaBuilder builder, CriteriaQuery<?> criteriaQuery, Root<?> root, Sort... sort) {
        if (sort == null || sort.length == 0)
            return;

        if (sort != null && sort.length > 0) {
            List<javax.persistence.criteria.Order> orders = new ArrayList<>();
        
            for (int i = 0; i < sort.length; i++) {
                Sort _sort = sort[i];
                if (_sort == null || _sort.columns().isEmpty())
                    continue;

                if (_sort.order().equals(Order.DESC)) {
                    _sort.columns().stream().forEach((col) -> orders.add(builder.asc(root.get(col))));
                } else {
                    _sort.columns().stream().forEach((col) -> orders.add(builder.desc(root.get(col))));
                }
            }
            criteriaQuery.orderBy(orders);
        }
    }
    protected void applyPage(TypedQuery<?> query, Page page) {
        Page _page = page == null ? Page.getDefault() : page;
        query.setFirstResult(_page.offset());
        query.setMaxResults(_page.limit());
    }

    protected CriteriaQuery<Long> countQuery(Class<?> domainClass, CriteriaBuilder builder) {
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        criteriaQuery.select(builder.count(criteriaQuery.from(domainClass)));
        return criteriaQuery;
    }



}
