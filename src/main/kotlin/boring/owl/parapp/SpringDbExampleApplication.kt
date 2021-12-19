package boring.owl.parapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringDbExampleApplication {
    companion object {
        fun main(args: Array<String>) {
            runApplication<SpringDbExampleApplication>(*args)
        }
    }
}


