package com.keyvalueserver.project.security;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // use hardcoded username and password to compare with the one sent by the client
        final String hardcodedUsername = "julian";
        final String hardcodedPassword = "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6";

        /*
        if the username sent by the client is the same as the hardcoded one, return the user
        this mechanism is used to mimic the database call to get the user details
         */

        if (hardcodedUsername.equals(username)) {
            return new User(hardcodedUsername, hardcodedPassword,
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

}