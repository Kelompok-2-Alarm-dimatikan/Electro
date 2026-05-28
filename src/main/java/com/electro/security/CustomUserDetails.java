package com.electro.security;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomUserDetails extends User implements OAuth2User, OidcUser {
    private final String email;
    private final String avatar;
    private final String provider;
    private Map<String, Object> attributes;
    private OidcIdToken idToken;
    private OidcUserInfo userInfo;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String email, String avatar, String provider) {
        super(username, password, authorities);
        this.email = email;
        this.avatar = avatar;
        this.provider = provider;
    }

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String email, String avatar, String provider, Map<String, Object> attributes) {
        super(username, password, authorities);
        this.email = email;
        this.avatar = avatar;
        this.provider = provider;
        this.attributes = attributes;
    }

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String email, String avatar, String provider, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
        super(username, password, authorities);
        this.email = email;
        this.avatar = avatar;
        this.provider = provider;
        this.attributes = attributes;
        this.idToken = idToken;
        this.userInfo = userInfo;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getProvider() {
        return provider;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return this.getUsername();
    }

    @Override
    public Map<String, Object> getClaims() {
        return attributes;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return idToken;
    }
}