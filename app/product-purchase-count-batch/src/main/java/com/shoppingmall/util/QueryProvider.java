package com.shoppingmall.util;

import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;

public class QueryProvider<E> {

    public JpaNativeQueryProvider<E> getJpaNativeQueryProvider(String query, Class<E> entityClazz) throws Exception {
        JpaNativeQueryProvider<E> queryProvider = new JpaNativeQueryProvider<>();
        queryProvider.setSqlQuery(query);
        queryProvider.setEntityClass(entityClazz);
        queryProvider.afterPropertiesSet();
        return queryProvider;
    }

}
