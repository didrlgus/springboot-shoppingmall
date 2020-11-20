package com.shoppingmall.util;

public class QueryUtils {

    public static final String SUM_PRODUCT_PURCHASE_COUNT_QUERY
            = "SELECT A.id, A.date_time, A.product_id, sum(A.count) as count " +
              "FROM (SELECT id, date_time, product_id, count " +
              "FROM product_purchase_count " +
              "WHERE date_time >= :standardTime " +
              "AND date_time < :jobParameterDateTime " +
              "UNION ALL " +
              "SELECT id, date_time, product_id, count " +
              "FROM product_purchase_merge_count " +
              "WHERE date_time < :standardTime) AS A " +
              "GROUP BY A.product_id " +
              "ORDER BY A.date_time ASC";

    public static final String MERGE_PRODUCT_PURCHASE_COUNT_QUERY
            = "SELECT id, date_time, client_id, product_id, sum(count) count " +
              "FROM product_purchase_count " +
              "WHERE date_time >= :standardTime " +
              "AND date_time < :jobParameterDateTime " +
              "GROUP BY product_id " +
              "ORDER BY product_id ASC";

}
