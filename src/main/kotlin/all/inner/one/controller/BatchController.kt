package all.inner.one.controller

import all.inner.one.models.Person
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.support.ListItemReader
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BatchController(
    private val jobLauncher: JobLauncher,
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val jobExplorer: JobExplorer
) {

    @GetMapping("/start")
    fun startBatchJob(): ResponseEntity<Any> {
        val jobParameters = JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters()
        val jobExecution = jobLauncher.run(job(), jobParameters)
        return ResponseEntity.ok("Job ID: ${jobExecution.id}")
    }

    private fun job(): Job {
        return jobBuilderFactory.get("personJob")
            .incrementer(RunIdIncrementer())
            .start(step())
            .build()
    }

    private fun step(): Step {
        return stepBuilderFactory.get("personStep")
            .chunk<Person, Person>(2)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build()
    }

    private fun reader(): ItemReader<Person> {
        val people = listOf(
            Person("John", "Doe"),
            Person("Jane", "Doe"),
            Person("Mike", "Smith"),
            Person("Sara", "Johnson"),
            Person("David", "Lee")
        )
        return ListItemReader(people)
    }

    private fun processor(): ItemProcessor<Person, Person> {
        return ItemProcessor { person ->
            person.firstName = person.firstName.toUpperCase()
            person.lastName = person.lastName.toUpperCase()
            person
        }
    }

    private fun writer(): ItemWriter<Person> {
        return ItemWriter { people ->
            for (person in people) {
                println("Writing person: $person")
            }
        }
    }
}
