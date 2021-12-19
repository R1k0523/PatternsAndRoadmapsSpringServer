package boring.owl.parapp.security

import boring.owl.parapp.entities.user.User
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class UserDetailsImpl(
    val id: UUID,
    private val username: String,
    @field:JsonIgnore private val password: String,
    authorities: Collection<GrantedAuthority?>,
    val role: User.Roles,
) : UserDetails {

    private val authorities: Collection<GrantedAuthority?>
    override fun getAuthorities() = authorities

    override fun getPassword() = password

    override fun getUsername() = username

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val user = other as UserDetailsImpl
        return Objects.equals(id, user.id)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + authorities.hashCode()
        return result
    }

    companion object {
        private const val serialVersionUID = 1L
        fun build(user: User): UserDetailsImpl {
            val authorities: List<GrantedAuthority> = listOf(SimpleGrantedAuthority(user.role.name))
            return UserDetailsImpl(
                user.userId!!,
                user.login!!,
                user.password!!,
                authorities,
                user.role
            )
        }
    }

    init {
        this.authorities = authorities
    }
}