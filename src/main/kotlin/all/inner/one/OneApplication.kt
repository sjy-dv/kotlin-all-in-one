package all.inner.one

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.runApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:/application.properties")
class OneApplication

fun main(args: Array<String>) {
	runApplication<OneApplication>(*args)
}
