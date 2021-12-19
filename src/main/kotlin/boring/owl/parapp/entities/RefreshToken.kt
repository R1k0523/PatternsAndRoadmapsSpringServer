package boring.owl.parapp.entities

import boring.owl.parapp.entities.user.User
import java.time.Instant
import javax.persistence.*

@Entity(name = "refreshtoken")
class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    var user: User? = null

    @Column(nullable = false, unique = true)
    var token: String = ""

    @Column(nullable = false)
    var expiryDate: Instant = Instant.now()
}