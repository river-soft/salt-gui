package org.riversoft.salt.gui.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class UserPrincipal extends User {

    String[] roles = []

    /**
     * Calls the more complex constructor with all boolean arguments set to {@code true}.
     * @param username
     * @param password
     * @param authorities
     */
    UserPrincipal(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities)
        this.roles = authorities
    }

    String[] getRoles() {
        authorities.collect { it.authority }
    }
}
