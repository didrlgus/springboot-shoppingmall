package com.shoppingmall.job;

import com.shoppingmall.domain.product.Product;
import com.shoppingmall.domain.product.ProductRepository;
import com.shoppingmall.domain.productPurchaseCount.ProductPurchaseCount;
import com.shoppingmall.domain.productPurchaseCount.ProductPurchaseCountRepository;
import com.shoppingmall.domain.productPurchaseMergeCount.ProductPurchaseMergeCount;
import com.shoppingmall.jobParameter.ProductPurchaseCountJobParameter;
import com.shoppingmall.listener.ProductPurchaseCountJobListener;
import com.shoppingmall.tasklet.DeleteProductPurchaseCountTasklet;
import com.shoppingmall.util.ProductCount;
import com.shoppingmall.util.QueryProvider;
import com.shoppingmall.util.StepShareMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.shoppingmall.util.QueryUtils.MERGE_PRODUCT_PURCHASE_COUNT_QUERY;
import static com.shoppingmall.util.QueryUtils.SUM_PRODUCT_PURCHASE_COUNT_QUERY;
import static java.util.Objects.isNull;

/**
 상품 구매 카운트 배치 Job
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class ProductPurchaseCountJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DeleteProductPurchaseCountTasklet deleteProductPurchaseCountTasklet;
    private final ProductRepository productRepository;
    private final StepShareMap<LocalDateTime> stepShareMap;
    private final ProductPurchaseCountJobParameter jobParameter;
    private final ProductPurchaseCountRepository productPurchaseCountRepository;
    private final EntityManagerFactory entityManagerFactory;

    private static final Long ZERO_COUNT = 0L;
    private static final Long TEN_MINUTE = 10L;

    private int chunkSize;
    @Value("${chunkSize}")
    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    private int pageSize;
    @Value("${pageSize}")
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    private int poolSize;
    @Value("${poolSize}")
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    @Bean(name = "productPurchaseCountJobTaskPool")
    public TaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(poolSize);
        executor.setThreadNamePrefix("multi-thread-");
        executor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
        executor.initialize();
        return executor;
    }

    @JobScope
    @Bean
    public ProductPurchaseCountJobParameter jobParameter(@Value("#{jobParameters[requestDateTime]}") String requestDateTime) {
        return new ProductPurchaseCountJobParameter(requestDateTime);
    }

    @Bean
    public Job productPurchaseCountJob(ProductPurchaseCountJobListener productPurchaseCountJobListener) throws Exception {
        return jobBuilderFactory.get("productPurchaseCountJob")
                .listener(productPurchaseCountJobListener)
                .start(sumProductPurchaseCountStep())
                .next(mergeProductPurchaseCountStep())
                .next(deleteProductPurchaseCountStep())
                .build();
    }

    @Bean
    public Step sumProductPurchaseCountStep() throws Exception {
        return stepBuilderFactory.get("sumProductPurchaseCountStep")
                .<ProductPurchaseMergeCount, ProductCount>chunk(chunkSize)
                .reader(sumProductPurchaseCountReader())
                .processor(sumProductPurchaseCountProcessor())
                .writer(sumProductPurchaseCountWriter())
                .taskExecutor(executor())
                .build();
    }

    @Bean
    public Step mergeProductPurchaseCountStep() throws Exception {
        return stepBuilderFactory.get("mergeProductPurchaseCountStep")
                .<ProductPurchaseCount, ProductPurchaseMergeCount>chunk(chunkSize)
                .reader(mergeProductPurchaseCountReader())
                .processor(mergeProductPurchaseProcessor())
                .writer(mergeProductPurchaseWriter())
                .taskExecutor(executor())
                .build();
    }

    @Bean
    public Step deleteProductPurchaseCountStep() {
        return stepBuilderFactory.get("deleteProductPurchaseCountStep")
                .tasklet(deleteProductPurchaseCountTasklet).build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<ProductPurchaseMergeCount> sumProductPurchaseCountReader() throws Exception {

        LocalDateTime standardTime = stepShareMap.get("standardTime");
        LocalDateTime jobParameterDateTime = jobParameter.getDateTime();

        if (isNull(standardTime)) {
            long count = getProductPurchaseCount();

            if (count == ZERO_COUNT) {
                standardTime = getOldAndNewStandardTimeFromJobParameter(jobParameterDateTime);
            } else {
                ProductPurchaseCount firstProductPurchaseCount = productPurchaseCountRepository.findFirstByOrderByDateTimeAsc();
                standardTime = firstProductPurchaseCount.getDateTime();
            }

            stepShareMap.put("standardTime", standardTime);
        }

        QueryProvider<ProductPurchaseMergeCount> queryProvider = new QueryProvider<>();
        JpaNativeQueryProvider<ProductPurchaseMergeCount> jpaNativeQueryProvider
                = queryProvider.getJpaNativeQueryProvider(SUM_PRODUCT_PURCHASE_COUNT_QUERY, ProductPurchaseMergeCount.class);

        Map<String, Object> paramMap = getStartToEndDateTimeParamMap(standardTime, jobParameterDateTime);

        JpaPagingItemReader<ProductPurchaseMergeCount> jpaPagingItemReader = new JpaPagingItemReader<>();
        setJpaPagingItemReader(jpaPagingItemReader, paramMap, jpaNativeQueryProvider);

        return jpaPagingItemReader;
    }

    private ItemProcessor<ProductPurchaseMergeCount, ProductCount> sumProductPurchaseCountProcessor() {
        return item -> ProductCount.builder()
                .productId(item.getProductId())
                .count(item.getCount())
                .build();
    }

    private JpaItemWriter<ProductCount> sumProductPurchaseCountWriter() {
        return new JpaItemWriter<ProductCount>() {
            @Override
            public void write(List<? extends ProductCount> items) {
                log.info("##### items size : {}", items.size());
                List<Long> productIdList = new ArrayList<>();
                Map<Long, Integer> productCountMap = new HashMap<>();

                items.forEach((productCount) -> {
                    productIdList.add(productCount.getProductId());
                    productCountMap.put(productCount.getProductId(), productCount.getCount());
                });

                List<Product> productList = productRepository.findAllById(productIdList);

                productList.forEach((product) -> {
                    Integer updateCount = productCountMap.get(product.getId());
                    product.setPurchaseCount(updateCount);
                });
            }
        };
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<ProductPurchaseCount> mergeProductPurchaseCountReader() throws Exception {

        LocalDateTime standardTime = stepShareMap.get("standardTime");
        LocalDateTime jobParameterDateTime = jobParameter.getDateTime();

        QueryProvider<ProductPurchaseCount> queryProvider = new QueryProvider<>();

        JpaNativeQueryProvider<ProductPurchaseCount> jpaNativeQueryProvider
                = queryProvider.getJpaNativeQueryProvider(MERGE_PRODUCT_PURCHASE_COUNT_QUERY, ProductPurchaseCount.class);

        Map<String, Object> paramMap = getStartToEndDateTimeParamMap(standardTime, jobParameterDateTime);

        JpaPagingItemReader<ProductPurchaseCount> jpaPagingItemReader = new JpaPagingItemReader<>();
        setJpaPagingItemReader(jpaPagingItemReader, paramMap, jpaNativeQueryProvider);

        return jpaPagingItemReader;
    }

    private ItemProcessor<ProductPurchaseCount, ProductPurchaseMergeCount> mergeProductPurchaseProcessor() {
        return item -> {
            LocalDateTime jobParameterDateTime = jobParameter.getDateTime();

            return ProductPurchaseMergeCount.builder()
                    .dateTime(jobParameterDateTime)
                    .productId(item.getProductId())
                    .count(item.getCount())
                    .build();
        };
    }

    private JpaItemWriter<ProductPurchaseMergeCount> mergeProductPurchaseWriter() {
        JpaItemWriter<ProductPurchaseMergeCount> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);

        return jpaItemWriter;
    }

    private long getProductPurchaseCount() {
        return productPurchaseCountRepository.count();
    }

    private LocalDateTime getOldAndNewStandardTimeFromJobParameter(LocalDateTime jobParameterDateTime) {
        return jobParameterDateTime.minusMinutes(TEN_MINUTE);
    }

    private Map<String, Object> getStartToEndDateTimeParamMap(LocalDateTime standardTime, LocalDateTime jobParameterDateTime) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("standardTime", standardTime);
        paramMap.put("jobParameterDateTime", jobParameterDateTime);

        return paramMap;
    }

    private void setJpaPagingItemReader(JpaPagingItemReader<?> jpaPagingItemReader,
                                        Map<String, Object> paramMap, JpaNativeQueryProvider<?> jpaNativeQueryProvider) {
        jpaPagingItemReader.setParameterValues(paramMap);
        jpaPagingItemReader.setEntityManagerFactory(entityManagerFactory);
        jpaPagingItemReader.setQueryProvider(jpaNativeQueryProvider);
        jpaPagingItemReader.setPageSize(pageSize);
    }

}
