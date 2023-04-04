package all.inner.one.controller

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import all.inner.one.models.*;
import all.inner.one.repository.*;
import all.inner.one.utils.*;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
class UserController(
private val bcrypt:Bcrypt, 
private val jwt:Jwt,
private val userRepository:UserRepository,
private val r: Response,
private val smtp:Smtp
) {

    @PostMapping("/bind1")
    fun bind1(@RequestBody req: UserVo):ResponseEntity<PreUser> {
        var user = PreUser("","","");
        user.id = req.id;
        user.password = req.password;
        user.nickname = "aaa";
        return ResponseEntity(user, HttpStatus.OK);
    }

    @PostMapping("/bind2")
    fun bind2(@RequestBody req:UserVo):ResponseEntity<PreUser> {
        req.password = bcrypt.hashPassword(req.password);
        return ResponseEntity(req.bind(), HttpStatus.OK);
    }

    @PostMapping("/issuetoken")
    fun issuetoken(@RequestBody req:UserVo):ResponseEntity<ViewToken> {
        var token = ViewToken(jwt.CreateToken(req.id, req.password))
        return ResponseEntity(token, HttpStatus.OK);
    }


    @PostMapping("/signup")
    fun signup(@RequestBody req:User):ResponseEntity<LoginVo> {
        req.password = bcrypt.hashPassword(req.password);
        req.refreshToken = jwt.CreateRToken(req.id);
        var token = jwt.CreateToken(req.id, req.nickname);
        userRepository.save(req)
        return ResponseEntity(req.output(token), HttpStatus.OK)
    }

    @PostMapping("/signin")
    fun signin(@RequestBody req:UserVo):ResponseEntity<Any> {
        val user = userRepository.findById(req.id)
        return if (user.isPresent && bcrypt.compareHash(req.password, user.get().password)) {
            var output = LoginVo(jwt.CreateToken(user.get().id, user.get().nickname), jwt.CreateRToken(user.get().id))
            return ResponseEntity(output, HttpStatus.OK)            
        } else {
            ResponseEntity("계정이 존재하지 않거나 비밀번호가 틀렸습니다.", HttpStatus.UNAUTHORIZED)
        }
    }

    @PostMapping("/solution/signin")
    fun solution_signin(@RequestBody req:UserVo):ResponseEntity<Any> {
        try {
            val user = userRepository.findById(req.id)
            if (!user.isPresent) {
                return r.Notice("가입된 계정이 아닙니다.")
            }
            if (!bcrypt.compareHash(req.password, user.get().password)) {
                return r.Notice("비밀번호가 일치하지 않습니다.")
            }
            var output = LoginVo(jwt.CreateToken(user.get().id, user.get().nickname), jwt.CreateRToken(user.get().id))
            userRepository.updateUserRefreshToken(req.id,output.rxauth)
            return r.Reply(output)
        } catch(e:Exception) {
            return r.Ereply(e)
        }
    }

    @GetMapping("/token/value")
    fun header_token_value(@RequestHeader(value="xauth") token: String): ResponseEntity<Any> {
        try {
            val value = jwt.VerifyToken(token)
            return r.Reply(value)
        } catch(e:Exception) {
            return r.Ereply(e)
        }
    }

    @GetMapping("token/new")
    fun new_issued_token(@RequestHeader(value="rxauth") token:String): ResponseEntity<Any> {
        try {
            val id = jwt.VerifyRToken(token)
            val user = userRepository.findById(id)
            if (token == user.get().refreshToken) {
                var output = LoginVo(jwt.CreateToken(user.get().id, user.get().nickname), jwt.CreateRToken(user.get().id))
                userRepository.updateUserRefreshToken(user.get().id,output.rxauth)
                return r.Reply(output)
            }
            return r.FailReply()
        }catch(e:Exception) {
          return r.Ereply(e)
        }
    }

    @PostMapping("/mail")
    fun SendMail():ResponseEntity<Any> {
        try{
            smtp.sendEmail("sendmail", "하이", "테스트")
            return r.SuccessReply()
        }catch(e:Exception) {
            return r.Ereply(e)
        }
    }
}