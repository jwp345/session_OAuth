package com.example.securityex1.config.auth.oatuh.provider;

public interface OAuth2UserInfo {
  String getProviderId();
  String getProvider();
  String getEmail();
  String getName();
}
