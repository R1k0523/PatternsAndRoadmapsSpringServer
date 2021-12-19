package boring.owl.parapp.repository

import boring.owl.parapp.entities.patterns.Pattern
import boring.owl.parapp.entities.posts.Topic
import boring.owl.parapp.entities.posts.notes.Note
import boring.owl.parapp.entities.posts.roadmaps.Roadmap
import boring.owl.parapp.entities.user.User
import boring.owl.parapp.entities.RefreshToken
import boring.owl.parapp.entities.patterns.PatternFeature
import boring.owl.parapp.entities.posts.notes.NoteSection
import boring.owl.parapp.entities.posts.roadmaps.RoadmapNode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.CrudRepository
import java.util.*

interface PatternRepo : CrudRepository<Pattern, UUID>
interface FeatureRepo : CrudRepository<PatternFeature, UUID> {
}
interface NoteRepo : CrudRepository<Note, UUID>{
    fun getAllByTopic(topic: Topic): List<Note>
    fun deleteAllByTopic_TopicId(topicId: UUID)
}
interface SectionRepo : CrudRepository<NoteSection, UUID>
interface RoadmapRepo : CrudRepository<Roadmap, UUID> {
    fun getAllByTopic(topic: Topic): List<Roadmap>
    fun deleteAllByTopic_TopicId(topicId: UUID)
}
interface NodeRepo : CrudRepository<RoadmapNode, UUID>
interface TopicRepo : CrudRepository<Topic, UUID>
interface UserRepo : CrudRepository<User, UUID> {
    fun findUserEntityByLogin(username: String): User?
    fun existsByLogin(username: String): Boolean
}
interface RefreshTokenRepository : JpaRepository<RefreshToken?, Long?> {
    fun findFirstByTokenLike(token: String?): RefreshToken?
    @Modifying
    fun deleteByUser(user: User?): Int
}