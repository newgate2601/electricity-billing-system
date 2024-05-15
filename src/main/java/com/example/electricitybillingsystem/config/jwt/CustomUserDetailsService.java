//package com.example.electricitybillingsystem.config.jwt;
//
//import com.example.electricitybillingsystem.model.User;
//import com.example.electricitybillingsystem.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> userOptional = userRepository.findByUsername(username);
//        return userOptional
//                .map(CustomUserDetails::new)
//                .orElseThrow(() -> new UsernameNotFoundException("user not found with username :" + username));
//    }
//}
