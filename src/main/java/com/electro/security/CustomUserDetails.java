package com.electro.security;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

public class CustomUserDetails extends User {
    private final String email;
    private final String avatar;
    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String email, String avatar) {
        super(username, password, authorities);
        this.email = email;
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }
}