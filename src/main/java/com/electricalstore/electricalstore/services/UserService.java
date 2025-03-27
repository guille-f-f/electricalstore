package com.electricalstore.electricalstore.services;

import com.electricalstore.electricalstore.entities.User;
import com.electricalstore.electricalstore.enums.Rol;
import com.electricalstore.electricalstore.exeptions.ObjectNotFoundException;
import com.electricalstore.electricalstore.exeptions.ValidateException;
import com.electricalstore.electricalstore.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(String email, String name, String lastName, String password, String repeatPassword) {
        validation(email, name, lastName, password, repeatPassword);
        User user = new User();
        return userRepository.save(populateUser(user, email, name, lastName, password));
    }

    @Transactional
    public User updateUser(String email, String name, String lastName) {
        User user = getUserOrThrow(email);
        return userRepository.save(populateUser(user, email, name, lastName, user.getPassword()));
    }

    @Transactional
    public String updatePassword(String email, String password, String repeatPassword) {
        validatePassword(password, repeatPassword);
        User user = getUserOrThrow(email);
        user.setPassword(passwordEncoder.encode(password));
        return "Password updated successfully.";
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User with email: " + email + " not found."));
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRol().toString()));
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.setAttribute("userSession", user);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        User user = getUserOrThrow(id);
        return user;
    }

    // =======================
    // Private methods
    // =======================

    private User populateUser(User user, String email, String name, String lastName, String password) {
        user.setEmail(email);
        user.setName(name);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode(password));
        user.setRol(Rol.USER);
        return user;
    }

    private void validation(String email, String name, String lastName, String password, String repeatPassword) {
        if (email == null || email.isEmpty()) {
            throw new ValidateException("Error, invalid email.");
        }

        if (name == null || name.isEmpty()) {
            throw new ValidateException("Error, invalid name.");
        }

        if (lastName == null || lastName.isEmpty()) {
            throw new ValidateException("Error, invalid last name.");
        }

        validatePassword(password, repeatPassword);

    }

    private void validatePassword(String password, String repeatPassword) {
        if (password == null || password.isEmpty()) {
            throw new ValidateException("Error, invalid password");
        }

        if (repeatPassword == null || repeatPassword.isEmpty()) {
            throw new ValidateException("Error, invalid repeat password.");
        }

        if (!password.equals(repeatPassword)) {
            throw new ValidateException("Error, the passwords entered do not match.");
        }
    }

    private User getUserOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User with email " + email + " not found."));
    }

    private User getUserOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User with id " + id + " not found."));
    }

}
