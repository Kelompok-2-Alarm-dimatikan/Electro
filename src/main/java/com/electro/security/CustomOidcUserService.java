package com.electro.security;
import com.electro.model.User;
import com.electro.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomOidcUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getEmail();
        String picture = oidcUser.getPicture();

        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("Email is required for Google login");
        }

        boolean isRegister = false;
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes)
                    RequestContextHolder.currentRequestAttributes();
            HttpSession session = attrs.getRequest().getSession(false);
            if (session != null) {
                Object flag = session.getAttribute("oauth_register");
                if (Boolean.TRUE.equals(flag)) {
                    isRegister = true;
                    session.removeAttribute("oauth_register");
                }
            }
        } catch (Exception ignored) {}

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getAvatar() == null || user.getAvatar().trim().isEmpty()) {
                if (picture != null && !picture.isEmpty()) {
                    user.setAvatar(picture);
                    userRepository.save(user);
                }
            }
            return buildUserDetails(user, oidcUser);
        }

        if (!isRegister) {
            throw new OAuth2AuthenticationException(
                new OAuth2Error("account_not_found"),
                "Akun dengan email " + email + " tidak ditemukan. Silakan daftar terlebih dahulu."
            );
        }

        // Register baru via Google
        User user = new User();
        user.setEmail(email);
        String baseUsername = email.split("@")[0];
        String username = baseUsername;
        int counter = 1;
        while (userRepository.existsByUsername(username)) {
            username = baseUsername + counter;
            counter++;
        }
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setRole("USER");
        user.setProvider("GOOGLE");
        user.setAvatar(picture);
        userRepository.save(user);

        return buildUserDetails(user, oidcUser);
    }

    private CustomUserDetails buildUserDetails(User user, OidcUser oidcUser) {
        return new CustomUserDetails(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole())),
                user.getEmail(),
                user.getAvatar(),
                user.getProvider(),
                oidcUser.getAttributes(),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo());
    }
}