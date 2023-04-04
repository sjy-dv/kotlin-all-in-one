package all.inner.one.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.*;

@Service
class Response {

    fun SuccessReply(): ResponseEntity<Any> {
        val map = mapOf("result" to "success")
        return ResponseEntity(map, HttpStatus.OK)
    }

    fun FailReply(): ResponseEntity<Any> {
        val map = mapOf("result" to "failed")
        return ResponseEntity(map,HttpStatus.OK)
    }

    fun Reply(obj:Any): ResponseEntity<Any> {
        return ResponseEntity(obj, HttpStatus.OK)
    }

    fun Notice(msg: String): ResponseEntity<Any> {
        val map = mapOf("msg" to msg)
        return ResponseEntity(map, HttpStatus.OK)
    }

    @ExceptionHandler(Exception::class)
    fun Ereply(e: Exception): ResponseEntity<Any> {
        val message = e.message
        if (message != null && message.contains("JWT expired")) {
            val map = mapOf("expired" to true)
            return ResponseEntity(map, HttpStatus.OK)
        }
        val map = mapOf("error" to message)
        return ResponseEntity(map, HttpStatus.OK)
    }
}