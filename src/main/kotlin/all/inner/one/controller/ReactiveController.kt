package all.inner.one.controller

import org.springframework.web.bind.annotation.*
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.LocalTime
import org.springframework.stereotype.Controller;
import kotlinx.coroutines.delay

@RestController
class ReactiveController {

    @GetMapping("/delay")
    suspend fun suspendlock(): String {
        delay(10000)
        return "un-lock"
    }

    @GetMapping("/stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun stream(): Flux<ServerSentEvent<String>> {
        return Flux.interval(Duration.ofSeconds(1))
            .map {
                val time = LocalTime.now()
                ServerSentEvent.builder("tick $it")
                    .id(it.toString())
                    .event("tick")
                    .comment("current time: $time")
                    .build()
            }
            .take(10)
    }

    @GetMapping("/mono")
    fun mono(): Mono<String> {
        return Mono.just("Hello, Mono!")
    }
}