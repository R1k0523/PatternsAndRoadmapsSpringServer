package boring.owl.parapp.service

import boring.owl.parapp.entities.user.User
import boring.owl.parapp.repository.UserRepo
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(val userRepo: UserRepo) {


    fun getAll(): List<User?> {
        val entities = userRepo.findAll().toList()
        val users = arrayListOf<User>()
        return users
    }

    @Throws(Exception::class)
    fun getOne(id: UUID): User? {
        if (!userRepo.existsById(id)) {
            throw Exception("Запись с таким id не найдена")
        }
        return userRepo.findById(id).get()
    }


    @Throws(Exception::class)
    fun getOne(login: String): User {
        val user = userRepo.findUserEntityByLogin(login)
            ?: throw Exception("Запись с таким id не найдена")
        return user
    }


    @Throws(Exception::class)
    fun delete(id: UUID) {
        if (!userRepo.existsById(id)) {
            throw Exception("Запись с таким id не найдена")
        }
        val user = userRepo.findById(id).get()
        userRepo.delete(user)
    }

    @Throws(Exception::class)
    fun checkCredentials(username: String, password: String): User {
        val user = userRepo.findUserEntityByLogin(username)
            ?: throw Exception()
        if (user.password != password) {
            throw Exception()
        } else {
            return user
        }
    }

    @Throws(UsernameNotFoundException::class)
    fun loadUserByLogin(username: String): UserDetails {
        val user: User = userRepo.findUserEntityByLogin(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")
        return org.springframework.security.core.userdetails.User(
            user.login, user.password, ArrayList()
        )
    }

    fun hasUser(username: String): Boolean = userRepo.existsByLogin(username)

}