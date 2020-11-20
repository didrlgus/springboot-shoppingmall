package com.shoppingmall.tasklet;

import com.shoppingmall.domain.productPurchaseCount.ProductPurchaseCountRepository;
import com.shoppingmall.domain.productPurchaseCountStandardTime.ProductPurchaseCountStandardTime;
import com.shoppingmall.domain.productPurchaseCountStandardTime.ProductPurchaseCountStandardTimeRepository;
import com.shoppingmall.util.StepShareMap;
import com.shoppingmall.jobParameter.ProductPurchaseCountJobParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Configuration
public class DeleteProductPurchaseCountTasklet implements Tasklet {

    private final ProductPurchaseCountRepository productPurchaseCountRepository;
    private final ProductPurchaseCountJobParameter jobParameter;
    private final ProductPurchaseCountStandardTimeRepository productPurchaseCountStandardTimeRepository;
    private final StepShareMap<LocalDateTime> stepShareMap;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LocalDateTime standardTime = stepShareMap.get("standardTime");
        productPurchaseCountRepository.deleteAllByDateTimeBefore(standardTime);

        LocalDateTime nextStandardTime = jobParameter.getDateTime();
        productPurchaseCountStandardTimeRepository
                .save(ProductPurchaseCountStandardTime.builder().standardTime(nextStandardTime).build());

        return RepeatStatus.FINISHED;
    }
}
