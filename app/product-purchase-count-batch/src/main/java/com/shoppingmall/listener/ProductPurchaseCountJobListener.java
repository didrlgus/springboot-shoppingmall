package com.shoppingmall.listener;

import com.shoppingmall.domain.productPurchaseCountStandardTime.ProductPurchaseCountStandardTime;
import com.shoppingmall.domain.productPurchaseCountStandardTime.ProductPurchaseCountStandardTimeRepository;
import com.shoppingmall.util.StepShareMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.util.Objects.nonNull;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductPurchaseCountJobListener implements JobExecutionListener {

    private final ProductPurchaseCountStandardTimeRepository productPurchaseCountStandardTimeRepository;
    private final StepShareMap<LocalDateTime> stepShareMap;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("##### productPurchaseCountJob 시작 시간 : {}", jobExecution.getStartTime());

        ProductPurchaseCountStandardTime productPurchaseCountStandardTime
                = productPurchaseCountStandardTimeRepository.findFirstByOrderByStandardTimeDesc();

        if(nonNull(productPurchaseCountStandardTime)) {
            stepShareMap.put("standardTime", productPurchaseCountStandardTime.getStandardTime());
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("##### productPurchaseCountJob 종료 시간 : {}", jobExecution.getEndTime());
    }
}
