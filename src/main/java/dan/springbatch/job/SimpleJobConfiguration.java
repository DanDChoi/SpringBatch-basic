package dan.springbatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
public class SimpleJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    //@Bean
    //@JobScope
    public Job simpleJob() {
        return jobBuilderFactory.get("simpleJob")
                .start(simpleStep1())
                .next(simpleStep2(null))
                .build();
    }

    private final SimpleJobTasklet tasklet1;

//    @Bean
//    @JobScope
//    public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate) {
    public Step simpleStep1() {
        return stepBuilderFactory.get("simpleStep1")
                .tasklet(tasklet1);
//                .tasklet((contribution, chunkContext) -> {
////                    throw new IllegalArgumentException("step1에서 실패합니다."); //실패시키기용
//                    log.info(">>>>> This is Step1");
//                    log.info(">>>>> requestDate = {}", requestDate);
//                    return RepeatStatus.FINISHED;
//                })
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep2(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("simpleStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step2");
                    log.info(">>>>> requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}