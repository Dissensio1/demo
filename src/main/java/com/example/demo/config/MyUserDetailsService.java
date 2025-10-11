// package com.example.demo.config;

// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

// import com.example.demo.model.User;
// import com.example.demo.repository.UserRepository;

// @Service
// public class MyUserDetailsService implements UserDetailsService{
    
//     @Autowired
//     private UserRepository userRepository;

//     @Override
//     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
//         Optional<User> user = userRepository.findByName(username);

//         return user.map(MyUserDetails::new)
//             .orElseThrow(() -> new UsernameNotFoundException(username + "There is not suck user in REPO"));
//     }
// }
