package com.example.securityex1.config.auth;

import com.example.securityex1.model.User;
import com.example.securityex1.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login");
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IOC 되어 있는 loadUserByUsername 함수가 실행
// 함수 종료시 @AuthenticalPrincipals 어노테이션이 만들어진다.
@Service
public class PrincipalDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  PrincipalDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    System.out.println("username:" + username);
    User userEntity = userRepository.findByUsername(username);
    if(userEntity == null) {
      return null;
    } else {
      return new PrincipalDetails(userEntity);
    }
  }
}
