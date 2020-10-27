package com.quartzscheduler.common;

import com.shoppingmall.dto.ProductResponseDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class CustomRowMapper implements RowMapper<ProductResponseDto.MainProductResponseDto> {

    @Override
    public ProductResponseDto.MainProductResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {

        return ProductResponseDto.MainProductResponseDto.builder()
                .id(rs.getLong("id"))
                .productNm(rs.getString("product_nm"))
                .titleImg(rs.getString("title_img"))
                .price(rs.getInt("price"))
                .rateAvg(rs.getInt("rate_avg"))
                .disPrice(rs.getInt("dis_prc"))
                .salePrice(rs.getInt("sale_prc"))
                .build();
    }
}
