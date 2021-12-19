package boring.owl.parapp.service

import boring.owl.parapp.entities.posts.Topic
import boring.owl.parapp.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TopicsService(val userRepo: UserRepo, val topicRepo: TopicRepo, val noteRepo: NoteRepo, val roadmapRepo: RoadmapRepo) {

    fun getAll(): List<Topic> {
        return topicRepo.findAll().toList()
    }

    @Throws(Exception::class)
    fun getOne(id: UUID) : Topic {
        val topic = topicRepo.findById(id)
        if (topic.isEmpty) {
            throw Exception("Запись с таким id не найдена")
        }
        return topic.get()
    }

    @Throws(Exception::class)
    @Transactional
    fun delete(id: UUID) {
        val topic = topicRepo.findById(id)
        if (topic.isEmpty) {
            throw Exception("Запись с таким id не найдена")
        }
        noteRepo.deleteAllByTopic_TopicId(id)
        roadmapRepo.deleteAllByTopic_TopicId(id)
        topicRepo.delete(topic.get())
    }

    @Throws(Exception::class)
    fun update(topic: Topic) : Topic {
        if (topic.topicId == null || !topicRepo.existsById(topic.topicId!!)) {
            throw Exception("Запись с таким id не найдена")
        }
        return topicRepo.save(topic)
    }

    @Throws(Exception::class)
    fun add(topic: Topic) : Topic {
        topic.topicId = UUID.randomUUID()
        return topicRepo.save(topic)
    }
}