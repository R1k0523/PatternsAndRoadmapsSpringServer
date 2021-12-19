package boring.owl.parapp.config

import boring.owl.parapp.entities.request.AuthRequestEntity
import boring.owl.parapp.entities.user.User
import boring.owl.parapp.service.JwtUserDetailsService
import boring.owl.parapp.service.PatternService
import boring.owl.parapp.service.PostsService
import boring.owl.parapp.service.TopicsService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class DataLoader(val userDetailsService: JwtUserDetailsService,
                 val topicsService: TopicsService,
                 val patternService: PatternService,
                 val postsService: PostsService,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        val admin = AuthRequestEntity("administrator", "123123123")
        val moderator = AuthRequestEntity("moderator", "123123123")
        val adminUser = userDetailsService.register(admin, User.Roles.ADMIN)
        val modUser = userDetailsService.register(moderator, User.Roles.MODERATOR)
        Mocks.topics.forEach {
            it.creator = adminUser
            it.moderators = mutableListOf(modUser)
            topicsService.add(it)
        }
        Mocks.patterns.forEach {
            patternService.add("administrator", it)
        }
        Mocks.notes.forEach {
            it.creator = modUser
            it.topic = topicsService.getAll()[0]
            postsService.add(it)
        }
        Mocks.roadmaps.forEach {
            it.creator = modUser
            it.topic = topicsService.getAll()[0]
            postsService.add(it)
        }
    }
}