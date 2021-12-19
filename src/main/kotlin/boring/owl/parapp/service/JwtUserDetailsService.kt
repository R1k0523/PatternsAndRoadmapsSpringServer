package boring.owl.parapp.service

import boring.owl.parapp.security.UserDetailsImpl
import boring.owl.parapp.entities.request.AuthRequestEntity
import boring.owl.parapp.entities.user.User
import boring.owl.parapp.repository.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class JwtUserDetailsService(private val userRepo: UserRepo,
                            private val gitHubService: GitHubService) : UserDetailsService {

    @Autowired
    private val bcryptEncoder: PasswordEncoder? = null

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetailsImpl {
        val user: User = userRepo.findUserEntityByLogin(username)
                ?: throw UsernameNotFoundException("User not found with username: $username")
        return UserDetailsImpl(user.userId!!, user.login!!, user.password!!, ArrayList<GrantedAuthority>(), user.role)
    }

    fun register(user: AuthRequestEntity, role: User.Roles = User.Roles.USER): User {
        val newUser = User()
        newUser.login = user.username
        newUser.password = bcryptEncoder!!.encode(user.password)
        newUser.role = role
        return userRepo.save(newUser)
    }

    /**
     * Метод для изменения объекта класса User, если объект не найден, бросает UserNotFoundException
     * @param user User объект для обновления
     */
    @Throws(Exception::class)
    fun updatePassword(user: User, newPassword: String) {
        if (user.userId == null || !userRepo.existsById(user.userId!!)) {
            throw Exception("Запись с таким id не найдена")
        }
        val oldUser = userRepo.findById(user.userId!!).get()
        oldUser.apply {
            password = bcryptEncoder!!.encode(newPassword)
        }
        userRepo.save(oldUser)
    }

    fun userByCode(token: String) : User {
        val user = gitHubService.getUserInfo(token)

        if (user.login != null) {
            val oldUser = userRepo.findUserEntityByLogin(user.login!!)
            return if (oldUser == null) {
                userRepo.save(user)
            } else {
                user.userId = oldUser.userId
                user.role = oldUser.role
                user.password = oldUser.password
                userRepo.save(user)
            }
        }
        throw Exception("Code is invalid")
    }

}