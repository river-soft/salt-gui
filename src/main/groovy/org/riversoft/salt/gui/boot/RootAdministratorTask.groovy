package org.riversoft.salt.gui.boot

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.User
import org.riversoft.salt.gui.enums.Permission
import org.riversoft.salt.gui.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Slf4j
@Component
@CompileStatic
@SuppressWarnings("GroovyUnusedDeclaration")
class RootAdministratorTask implements CommandLineRunner {

    @Autowired
    UserRepository userRepository

    /**
     * Callback used to run the bean.
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    void run(String... args) throws Exception {

        User user = userRepository.findOne("root")

        if (!user) {

            userRepository.save(new User(
                    firstName: 'root',
                    lastName: 'super',
                    name: 'root',
                    password: new BCryptPasswordEncoder().encode('Minions'),
                    permissions: [Permission.ROLE_ROOT.name()],
            ))
        }
    }
}
