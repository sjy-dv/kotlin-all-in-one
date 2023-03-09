package all.inner.one.controller

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import all.inner.one.vo.*;

@Controller
class StartController {

    @GetMapping("/")
    fun start(): ResponseEntity<HashMap<String, String>> {
        val map= HashMap<String,String>()
        map.put("result","hello world!!")
        return ResponseEntity(map, HttpStatus.OK)
    }

    @GetMapping("/q")
    fun getQ(
        @RequestParam("q") q: String
    ): ResponseEntity<HashMap<String,String>> {
        val map= HashMap<String,String>()
        map.put("query",q)
        return ResponseEntity(map, HttpStatus.OK)
    }


    @PostMapping("/body")
    fun postBody(@RequestBody req: SimpleVo):ResponseEntity<SimpleVo> {
        return ResponseEntity(req,HttpStatus.OK)
    }
}