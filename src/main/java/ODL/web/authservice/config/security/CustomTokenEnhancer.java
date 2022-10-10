package ODL.web.authservice.config.security;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import ODL.web.authservice.entity.user.User;

public class CustomTokenEnhancer extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (!authentication.isClientOnly()) {
            User user = (User) authentication.getPrincipal();
            final Map<String, Object> additionalInfo = new HashMap<>();

            additionalInfo.put("roles",
                    user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet()));
            additionalInfo.put("email", user.getEmail());
            additionalInfo.put("fio", user.getFio());
            additionalInfo.put("id_from_source", user.getIdFromSource());

            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        }
        return super.enhance(accessToken, authentication);
    }
}
