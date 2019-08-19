/*
package com.shoppingmall.batch;

import com.shoppingmall.repository.CartRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvalidityCartJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private CartRepository cartRepository;

    @Test
    public void 유효기간이_지난_장바구니_수정_테스트() throws Exception {
        Date nowDate = new Date();

        JobExecution jobExecution
                = jobLauncherTestUtils.launchJob(new JobParametersBuilder().addDate("nowDate", nowDate).toJobParameters());

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        assertEquals(0, cartRepository.findByCreatedDateBeforeAndUseYnEquals(LocalDateTime.now().minusDays(7), 'Y').size());
    }
}
*/
