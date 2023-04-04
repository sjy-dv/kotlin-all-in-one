package all.inner.one.models;

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*
/* Table SECTION */

@Entity
@Table(name = "root_user")
@EntityListeners(AuditingEntityListener::class)
data class User(
    @Id
    @Column(name = "id", length = 11, nullable = false)
    var id: String = "",

    @Column(name = "password", length = 190, nullable = false)
    var password: String = "",

    @Column(name = "nickname", length = 30, nullable = false)
    var nickname: String = "",

    @Column(name = "refreshtoken", length = 191, nullable = false)
    var refreshToken: String = "",

    @CreatedDate
    @Column(name = "createdAt", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    @Column(name = "updatedAt", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun output(token:String): LoginVo {
        var output = LoginVo(token, refreshToken)
        return output
    }
}

//*VO , DTO SECTION */

//코틀린은 하나의 코틀린파일에 여러개의 데이터 클래스를 생성할 수 있음
data class UserVo(var id:String, var password:String) {
    fun bind():PreUser {
        var user = PreUser(id, password, "aaa");
        return user
    }
}


data class PreUser(var id:String, var password: String, var nickname: String)


data class LoginVo(var xauth:String, var rxauth:String) 