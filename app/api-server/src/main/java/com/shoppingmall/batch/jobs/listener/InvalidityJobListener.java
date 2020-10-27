package com.shoppingmall.batch.jobs.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InvalidityJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Before Batch Job");
        log.info("Job 시작시간 : {}", jobExecution.getStartTime());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("After Batch Job");
        log.info("Job 종료시간 : {}", jobExecution.getEndTime());
    }
}
