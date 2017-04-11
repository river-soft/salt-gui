package org.riversoft.salt.gui.service

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.User
import org.riversoft.salt.gui.model.UserPrincipal
import org.riversoft.salt.gui.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Slf4j
@Service
@Transactional
@CompileStatic
@SuppressWarnings("GroovyUnusedDeclaration")
class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     *
     * @return a fully populated user record (never <code>null</code>)
     *
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException if the user could not be found or the user has no
     * GrantedAuthority
     */
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("Searching user by username: [${username}]")
        User user = userRepository.findOne(username)
        if (!user) {

            log.error("User by name: [${username}] not found in database.")
            throw new UsernameNotFoundException("User by name: ${username} not found")
        }

        log.debug("Username: ${username} found in database.")
        def authorities = user.permissions?.collect { new SimpleGrantedAuthority(it) }

        new UserPrincipal(user.name, user.password, authorities)
    }
}
