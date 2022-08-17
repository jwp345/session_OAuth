package com.example.securityex1.config.auth.oatuh.provider;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo{

  private Map<String, Object> attributes; // oauth2.getAttributes()

  public NaverUserInfo(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  @Override
  public String getProviderId() {
    return (String) attributes.get("id");
  }

  @Override
  public String getProvider() {
    return "naver";
  }

  @Override
  public String getEmail() {
    return (String) attributes.get("email");
  }

  @Override
  public String getName() {
    return (String) attributes.get("name");
  }
}
