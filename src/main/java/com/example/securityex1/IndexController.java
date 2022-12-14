package com.example.securityex1;

import com.example.securityex1.config.auth.PrincipalDetails;
import com.example.securityex1.model.User;
import com.example.securityex1.repository.UserRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  IndexController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @GetMapping("/test/login")
  public @ResponseBody String testLogin(Authentication authentication,
                                        @AuthenticationPrincipal PrincipalDetails userDetails) { // DI (의존성 주입)
    System.out.println("/test/login =============");
    PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
    System.out.println("authentication : " + principalDetails.getUser());

    System.out.println("userDetails : " + userDetails.getUser());
    return "세션 정보 확인하기";
  }

  @GetMapping("/test/oauth/login")
  public @ResponseBody String testOAuthLogin(Authentication authentication,
                                       @AuthenticationPrincipal OAuth2User oauth) { // DI (의존성 주입)
    System.out.println("/test/login =============");
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    System.out.println("authentication : " + oAuth2User.getAttributes());
    System.out.println("oAuth2User : " + oauth.getAttributes());

    return "OAuth 세션 정보 확인하기";
  }

  @GetMapping({"","/"})
  public String index() {
    return "index";
  }

  @GetMapping("/user")
  public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
    System.out.println("principalDetails : " + principalDetails.getUser());
    return "user";
  }

  @GetMapping("/admin")

  public @ResponseBody String admin() {
    return "어드민 페이지입니다.";
  }

  @GetMapping("/manager")
  public @ResponseBody String manager() {
    return "매니저 페이지입니다.";
  }

  @GetMapping("/loginForm")
  public String loginForm() {
    return "loginForm";
  }

  @GetMapping("/joinForm")
  public String joinForm() {
    return "joinForm";
  }

  @PostMapping("/join")
  public String join(User user) {
    System.out.println(user);
    user.setRole("ROLE_USER");
    String rawPassword = user.getPassword();
    String encPassword = bCryptPasswordEncoder.encode(rawPassword);
    user.setPassword(encPassword);
    userRepository.save(user);
    return "redirect:/loginForm";
  }

  @Secured("ROLE_ADMIN")
  @GetMapping("/info")
  public @ResponseBody String info() {
    return "개인정보";
  }

  @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
  @GetMapping("/data")
  public @ResponseBody String data() {
    return "데이터정보";
  }
}
