package com.olmez.mya.springsecurity;

import java.rmi.UnexpectedException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olmez.mya.model.User;
import com.olmez.mya.model.enums.UserType;
import com.olmez.mya.repositories.UserRepository;
import com.olmez.mya.springsecurity.config.SecurityConfig;
import com.olmez.mya.springsecurity.config.UserDetailsImpl;
import com.olmez.mya.springsecurity.securityutiliy.JwtUtils;
import com.olmez.mya.springsecurity.securityutiliy.PasswordUtility;
import com.olmez.mya.springsecurity.securityutiliy.SigninRequest;
import com.olmez.mya.springsecurity.securityutiliy.SignupRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthRestController {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;

    // AUTH
    @PostMapping("/signup")
    public ResponseEntity<String> signupUser(@RequestBody SignupRequest signupRequest) {

        User user = userRepository.findByUsername(signupRequest.getUsername());
        if (user != null) {
            String errorMsg = String.format("Error: %s is already in use!", signupRequest.getUsername());
            return ResponseEntity.badRequest().body(errorMsg);
        }
        user = userRepository.findUserByEmail(signupRequest.getEmail());
        if (user != null) {
            String errorMsg = String.format("Error: %s is already in use!", signupRequest.getEmail());
            return ResponseEntity.badRequest().body(errorMsg);
        }

        // Create a new user's account
        user = new User(signupRequest.getUsername(), signupRequest.getFirstName(), signupRequest.getLastName(),
                signupRequest.getEmail());
        user.setPasswordHash(PasswordUtility.hashPassword(signupRequest.getPassword()));
        user = userRepository.save(user);
        return ResponseEntity.ok("Created " + user.getName());
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody SigninRequest signinRequest) throws UnexpectedException {

        UserDetailsImpl userDetailsImpl = grantAuthentication(signinRequest);
        User curUser = userDetailsImpl.getUser();
        if (curUser == null) {
            return ResponseEntity.status(400).body(SecurityConfig.EXCEPTION_MESSAGE + signinRequest.getUsername());
        }
        String jwt = createJWTForUser(userDetailsImpl);
        log.info("Granted jwt:{} to user:{}", jwt, curUser);
        return ResponseEntity.ok(jwt);
    }

    private UserDetailsImpl grantAuthentication(SigninRequest signinRequest) throws UnexpectedException {
        // no need to use email
        // String email = signinRequest.getEmail();
        String username = signinRequest.getUsername();
        String password = signinRequest.getPassword();
        var aToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authResult = authManager.authenticate(aToken);
        SecurityContextHolder.getContext().setAuthentication(authResult); // ? Do I need this?
        return getPrincipalFromAuthentication(authResult);
    }

    private UserDetailsImpl getPrincipalFromAuthentication(Authentication authResult) throws UnexpectedException {
        Object principal = authResult.getPrincipal();
        if (!(principal instanceof UserDetailsImpl)) {
            throw new UnexpectedException(
                    String.format("Unexpected authentication principal:%s", principal.getClass().getName()));
        }
        return (UserDetailsImpl) principal;
    }

    private String createJWTForUser(UserDetails userDetails) {
        return JwtUtils.generateToken(userDetails);
    }

}