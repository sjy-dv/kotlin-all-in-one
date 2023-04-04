package all.inner.one

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.runApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.context.annotation.Import
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing


@SpringBootApplication
@EnableBatchProcessing
@EnableJpaAuditing
@Import(CorsConfig::class)
@PropertySource("classpath:/application.properties")
class OneApplication

fun main(args: Array<String>) {
	runApplication<OneApplication>(*args)
}
