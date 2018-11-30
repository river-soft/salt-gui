package org.riversoft.salt.gui.service

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.User
import org.riversoft.salt.gui.enums.Permission
import org.riversoft.salt.gui.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Slf4j
@Service
@CompileStatic
class UserService {

    @Autowired private UserRepository userRepository

    void createUser(String userName, String firstName, String lastName, String password, List<Permission> permissions) {

        User user = userRepository.findOne(userName)

        if (!user) {
            userRepository.save(new User(
                    firstName: firstName,
                    lastName: lastName,
                    name: userName,
                    password: new BCryptPasswordEncoder().encode(password),
                    permissions: permissions.collect { return it.name() }
            ))
        }
    }
}
