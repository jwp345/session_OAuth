package com.example.securityex1.config.auth.oatuh;

import com.example.securityex1.config.auth.PrincipalDetails;
import com.example.securityex1.config.auth.oatuh.provider.GoogleUserInfo;
import com.example.securityex1.config.auth.oatuh.provider.NaverUserInfo;
import com.example.securityex1.config.auth.oatuh.provider.OAuth2UserInfo;
import com.example.securityex1.model.User;
import com.example.securityex1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

// 함수 종료시 @AuthenticalPrincipals 어노테이션이 만들어진다.
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final UserRepository userRepository;

  PrincipalOauth2UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    System.out.println("getClientRegistration : " + userRequest.getClientRegistration());
    System.out.println("getAccessToken : " + userRequest.getAccessToken());

    OAuth2User oAuth2User = super.loadUser(userRequest);
    System.out.println("getAttributes : " + oAuth2User.getAttributes());

    OAuth2UserInfo oAuth2UserInfo = null;
    if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
      System.out.println("구글 로그인 요청");
      oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
    } else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
      System.out.println("네이버 로그인 요청");
      oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
    } else {
      System.out.println("현재 구글과 네이버만 지원 중입니다.");
    }
    String provider = oAuth2UserInfo.getProvider();
    String providerId = oAuth2UserInfo.getProviderId();
    String username = provider + "_" + providerId;
    String password = bCryptPasswordEncoder.encode("겟인데어"); // 의미X
    String email = oAuth2UserInfo.getEmail();
    String role = "ROLE_USER";

    User userEntity = userRepository.findByUsername(username);

    if(userEntity == null) {
      System.out.println("최초 로그인");
      userEntity = User.builder()
              .username(username)
              .password(password)
              .email(email)
              .role(role)
              .provider(provider)
              .providerId(providerId)
              .build();
      userRepository.save(userEntity);
    } else {
      System.out.println("로그인 한 적이 있습니다.");
    }

    return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
  }
}
