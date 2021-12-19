package boring.owl.parapp.service

import boring.owl.parapp.entities.patterns.Pattern
import boring.owl.parapp.entities.user.User
import boring.owl.parapp.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PatternService(val userRepo: UserRepo, val patternRepo: PatternRepo, val featureRepo: FeatureRepo) {

    fun getAll(): List<Pattern> {
        return patternRepo.findAll().toList()
    }

    @Throws(Exception::class)
    fun getOne(id: UUID) : Pattern {
        val pattern = patternRepo.findById(id)
        if (pattern.isEmpty) {
            throw Exception("Запись с таким id не найдена")
        }
        return pattern.get()
    }

    @Throws(Exception::class)
    fun add(username: String, pattern: Pattern) : Pattern {
        if (!hasPermission(username, pattern))
            throw Exception("Нет прав")
        pattern.patternId = null
        return patternRepo.save(pattern)
    }
    @Throws(Exception::class)
    @Transactional
    fun update(username: String, pattern: Pattern) : Pattern {
        if (!hasPermission(username, pattern))
            throw Exception("Нет прав")
        if (pattern.patternId == null || !patternRepo.existsById(pattern.patternId!!)) {
            throw Exception("Запись с таким id не найдена")
        }
        val oldPattern = patternRepo.findById(pattern.patternId!!).get()
        featureRepo.deleteAll(oldPattern.features!!)
        return patternRepo.save(pattern)
    }
    @Throws(Exception::class)
    fun delete(username: String, id: UUID) {
        val pattern = patternRepo.findById(id)
        if (pattern.isEmpty) {
            throw Exception("Запись с таким id не найдена")
        }
        if (!hasPermission(username, pattern.get()))
            throw Exception("Нет прав")
        patternRepo.delete(pattern.get())
    }


    fun hasPermission(username: String, pattern: Pattern): Boolean {
        val user = userRepo.findUserEntityByLogin(username)
        return user != null && user.role != User.Roles.USER
    }

}