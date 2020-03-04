package am.neovision.api.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  ROLE_CLIENT("ROLE_CLIENT"),
  ROLE_ADMIN("ROLE_ADMIN");

  String name;

  Role(String name) {
    this.name = name;
  }

  @Override
  public String getAuthority() {
    return name;
  }
}
