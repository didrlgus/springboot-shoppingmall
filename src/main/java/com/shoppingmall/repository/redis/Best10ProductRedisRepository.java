package com.shoppingmall.repository.redis;

import com.shoppingmall.domain.redis.Best10Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Best10ProductRedisRepository extends CrudRepository<Best10Product, Long> {
    List<Best10Product> findAll();
}
