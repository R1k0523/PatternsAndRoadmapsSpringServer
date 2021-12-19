package boring.owl.parapp.service

import boring.owl.parapp.entities.posts.Post
import boring.owl.parapp.entities.posts.notes.Note
import boring.owl.parapp.entities.posts.roadmaps.Roadmap
import boring.owl.parapp.entities.posts.roadmaps.RoadmapNode
import boring.owl.parapp.repository.*
import org.springframework.stereotype.Service
import java.util.*

@Service
class PostsService(val noteRepo: NoteRepo, val nodeRepo: NodeRepo, val sectionRepo: SectionRepo, val roadmapRepo: RoadmapRepo, val userRepo: UserRepo, val topicRepo: TopicRepo) {


    fun getAllByTopicId(topicId: UUID): List<Post> {
        val topic = topicRepo.findById(topicId).get()
        val notes = noteRepo.getAllByTopic(topic).toList()
        val roadmaps = roadmapRepo.getAllByTopic(topic).toList()
        val posts = arrayListOf<Post>()
        posts.addAll(notes)
        posts.addAll(roadmaps)
        posts.sortBy { it.publicationDateTime }
        return posts
    }

    @Throws(Exception::class)
    fun <T : Post> getOne(id: UUID): T {
        if (!noteRepo.existsById(id)) {
            if (!roadmapRepo.existsById(id)) {
                throw Exception("Запись с таким id не найдена")
            }
            return roadmapRepo.findById(id).get() as T
        }
        return noteRepo.findById(id).get() as T
    }

    @Throws(Exception::class)
    fun delete(id: UUID) {
        if (!noteRepo.existsById(id)) {
            if (!roadmapRepo.existsById(id)) {
                throw Exception("Запись с таким id не найдена")
            }
            val roadmap = roadmapRepo.findById(id).get()
            roadmapRepo.delete(roadmap)
        }
        val note = noteRepo.findById(id).get()
        noteRepo.delete(note)
    }

    @Throws(Exception::class)
    fun update(note: Note) : Note {
        if (note.postId == null || !noteRepo.existsById(note.postId!!)) {
            throw Exception("Запись с таким id не найдена")
        }
        return noteRepo.save(note)
    }
    @Throws(Exception::class)
    fun update(roadmap: Roadmap) : Roadmap {
        if (roadmap.postId == null || !roadmapRepo.existsById(roadmap.postId!!)) {
            throw Exception("Запись с таким id не найдена")
        }
        return roadmapRepo.save(roadmap)
    }
    @Throws(Exception::class)
    fun add(note: Note) : Note {
        var savedSections = sectionRepo.saveAll(note.sections).toMutableList()
        note.postId = UUID.randomUUID()
        note.sections = savedSections
        return noteRepo.save(note)
    }
    @Throws(Exception::class)
    fun add(roadmap: Roadmap) : Roadmap {
        roadmap.postId = UUID.randomUUID()
        roadmap.root = roadmap.root?.let { saveNode(it) }
        return roadmapRepo.save(roadmap)
    }

    fun saveNode(node: RoadmapNode): RoadmapNode {
        val saved = arrayListOf<RoadmapNode>()
        node.childrenNodes.forEach {
            saved.add(saveNode(it))
        }
        node.childrenNodes = saved
        node.id = UUID.randomUUID()
        return nodeRepo.save(node)
    }

}