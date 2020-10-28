package com.shoppingmall.batch.jobs;

import com.shoppingmall.batch.jobs.listener.InvalidityJobListener;
import com.shoppingmall.domain.cart.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class InvalidityCartJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final InvalidityJobListener invalidityJobListener;

    private int chunkSize = 10;

    @Bean
    public Job jpaPagingItemReaderJob() {
        return jobBuilderFactory.get("invalidityCartJob")
                .preventRestart()
                .listener(invalidityJobListener)
                .start(jpaPagingItemReaderStep())
                .build();
    }

    @Bean
    public Step jpaPagingItemReaderStep() {
        return stepBuilderFactory.get("jpaPagingItemReaderStep")
                .<Cart, Cart>chunk(chunkSize)
                .reader(jpaPagingItemReader())
                .processor(invalidityCartProcessor())
                .writer(invalidityCartWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Cart> jpaPagingItemReader() {

        JpaPagingItemReader<Cart> jpaPagingItemReader = new JpaPagingItemReader() {
            @Override
            public int getPage() {
                return 0;
            }
        };

        // jpaPagingItemReader를 사용하려면 쿼리를 직접 짜야함
        jpaPagingItemReader.setQueryString("select c from Cart as c where c.createdDate < :createdDate and c.useYn = :useYn");

        Map<String, Object> map = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        map.put("createdDate", now.minusDays(7));
        map.put("useYn", 'Y');

        jpaPagingItemReader.setParameterValues(map);
        jpaPagingItemReader.setEntityManagerFactory(entityManagerFactory);
        jpaPagingItemReader.setPageSize(chunkSize);       // 한번에 10개씩 읽어옴

        return jpaPagingItemReader;
    }


    public ItemProcessor<Cart, Cart> invalidityCartProcessor() {
        return Cart::setInvalidity;
        /*return new ItemProcessor<Cart, Cart>() {
        @Override
        public Cart process(Cart item) throws Exception {
            return item.setInvalidity();
        }*/
    }

    private JpaItemWriter<Cart> invalidityCartWriter() {
        JpaItemWriter<Cart> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);

        return jpaItemWriter;
    }
}
