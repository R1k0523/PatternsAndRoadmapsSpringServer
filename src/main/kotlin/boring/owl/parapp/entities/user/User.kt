package boring.owl.parapp.entities.user

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*
import javax.persistence.*

@Entity
@Table(name="users")
@ApiModel("Модель пользователя")
class User {
    @Id @GeneratedValue @Column(name="user_id", nullable = false)
    @ApiModelProperty(notes = "Идентификационный номер пользователя")
    var userId: UUID? = null
    @Column @ApiModelProperty(notes = "Биография пользователя")
    var bio: String? = ""
    @Column @ApiModelProperty(notes = "Логин/Имя пользователя")
    var login: String? = ""
    @Column @JsonIgnore
    var password: String? = ""
    @Enumerated(EnumType.STRING) @ApiModelProperty(notes = "Роль пользователя")
    var role: Roles = Roles.USER
    @Column @ApiModelProperty(notes = "Место работы")
    var company: String? = ""
    @Column @ApiModelProperty(notes = "Электронная почта")
    var email: String? = ""
    @Column @ApiModelProperty(notes = "Ссылка на GitHub")
    var url: String? = ""
    @Column(name="avatar_url") @ApiModelProperty(notes = "Ссылка на файл изображения")
    var avatarUrl: String? = ""
    @Column @ApiModelProperty(notes = "Полное имя пользователя")
    var name: String? = ""
    @Column @ApiModelProperty(notes = "Место проживания")
    var location: String? = ""
    @Column @ApiModelProperty(notes = "Ссылка на блог")
    var blog: String? = ""
    @Column @JsonIgnore
    var token: String = ""

    enum class Roles(val title: String) {
        ADMIN("admin"), MODERATOR("mod"), USER("user");
        fun getRole(title: String): Roles {
            return when(title) {
                "admin" -> ADMIN
                "mod" -> MODERATOR
                else -> USER
            }
        }
    }
}