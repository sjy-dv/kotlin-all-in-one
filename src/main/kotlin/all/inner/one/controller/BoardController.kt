package all.inner.one.controller

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.*
import java.nio.ByteBuffer;
import all.inner.one.models.*;
import all.inner.one.repository.*;
import all.inner.one.utils.*;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
class BoardController(
    private val r: Response,
    private val jwt:Jwt,
    private val boardRepository:BoardRepository
) {

    @PostMapping("/bulk/board")
    fun bulkBoard(
    @RequestHeader(value="xauth") token: String,    
    @RequestBody boards: List<Board>): ResponseEntity<Any> {
        try{
            val id = jwt.VerifyToken(token)
            for (board in boards) {
                val uuid = UUID.randomUUID()
                val shortUuid = uuid.toString().replace("-", "").substring(0, 11)
                board.userId = id
                board.id = shortUuid
            }
            boardRepository.saveAll(boards)
            return r.SuccessReply()
        }catch(e:Exception) {
            return r.Ereply(e)
        }
    }

    @GetMapping("/myboard")
    fun myBoard(@RequestHeader(value="xauth") token:String): ResponseEntity<Any> {
        try {
            val id = jwt.VerifyToken(token)
            val reply = boardRepository.myBoard(id)
            return r.Reply(reply)
        } catch(e:Exception) {
            return r.Ereply(e)
        }
    }

}





