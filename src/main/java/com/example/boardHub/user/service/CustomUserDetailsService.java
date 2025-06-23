package com.example.boardHub.user.service;

import com.example.boardHub.user.model.User;
import com.example.boardHub.user.repository.UserRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // User 엔티티에서 userId가 username 역할을 하도록 조회
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUserId(),           // 로그인할 때
                user.getPassword(),         // 암호화된 비밀번호
                AuthorityUtils.createAuthorityList("ROLE_USER") // 권한 설정
        );
    }
}
