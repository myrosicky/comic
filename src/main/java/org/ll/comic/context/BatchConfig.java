package org.ll.comic.context;

import java.util.List;

import javax.sql.DataSource;

import org.ll.comic.model.Comic;
import org.ll.comic.service.impl.ComicCollectionTask;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    @Autowired private ComicCollectionTask comicCollectionTask;

    // tag::readerwriterprocessor[]
//    @Bean
//    public FlatFileItemReader<String> reader() {
//        return new FlatFileItemReaderBuilder<String>()
//            .name("personItemReader")
//            .resource(new ClassPathResource("sample-data.csv"))
//            .delimited()
//            .names(new String[]{"firstName", "lastName"})
//            .fieldSetMapper(new BeanWrapperFieldSetMapper<String>() {{
//                setTargetType(String.class);
//            }})
//            .build();
//    }

//    @Bean
//    public PersonItemProcessor processor() {
//        return new PersonItemProcessor();
//    }

//    @Bean
//    public JdbcBatchItemWriter<List<Comic>> writer(DataSource dataSource) {
//        return new JdbcBatchItemWriterBuilder<List<Comic>>()
//            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
//            .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
//            .dataSource(dataSource)
//            .build();
//    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob(JobExecutionListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step1)
            .end()
            .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
            .<String, List<Comic>> chunk(10)
            .reader(comicCollectionTask)
            .processor(comicCollectionTask)
            .writer(comicCollectionTask)
            .build();
    }
    // end::jobstep[]
}
