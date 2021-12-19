package boring.owl.parapp.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint? = null
    @Autowired
    private val jwtUserDetailsService: UserDetailsService? = null
    @Autowired
    private val jwtRequestFilter: JwtRequestFilter? = null
    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder())
    }
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity.cors().and().csrf().disable()
                .authorizeRequests().antMatchers(
                "/auth",
                "/login",
                "/loggingout",
                "/oauth",
                "/refreshtoken",
                "/register",
                "/",
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**")
                .permitAll()
            .antMatchers(HttpMethod.GET, "/**").permitAll().anyRequest().authenticated()
            .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        httpSecurity.addFilterBefore(
            jwtRequestFilter,
            UsernamePasswordAuthenticationFilter::class.java
        )
    }
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.addAllowedHeader("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
