package ODL.web.authservice.entity.user;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.util.StringUtils;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "oauth_client")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Client implements ClientDetails {

    @Id
    @Column(name = "client_id", nullable = false, length = 50)
    String clientId;

    @Column(name = "client_secret", nullable = false, length = 60)
    String clientSecret;

    @Column(name = "resource_ids", nullable = false)
    String resourceIds;

    @Column(name = "scope", nullable = false)
    String scope;

    @Column(name = "grant_types", nullable = false)
    String grantTypes;

    @Column(name = "authorities")
    String authorities;

    @Column(name = "access_token_expiration", nullable = false)
    Integer accessTokenExpiration;

    @Column(name = "refresh_token_expiration")
    Integer refreshTokenExpiration;

    @Column(name = "additional_information")
    String additionalInformation;

    @Column(name = "auto_approve")
    String autoApprove;

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public boolean isScoped() {
        return !getScope().isEmpty();
    }

    @Override
    public Set<String> getScope() {
        Set<String> result = new HashSet<>();
        if (StringUtils.hasText(scope)) {
            result = Arrays.stream(scope.split(",")).collect(Collectors.toSet());
        }
        return result;
    }

    @Override
    public Set<String> getResourceIds() {
        Set<String> result = new HashSet<>();
        if (StringUtils.hasText(resourceIds)) {
            result = Arrays.stream(resourceIds.split(",")).collect(Collectors.toSet());
        }
        return result;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        Set<String> result = new HashSet<>();
        if (StringUtils.hasText(grantTypes)) {
            result = Arrays.stream(grantTypes.split(",")).collect(Collectors.toSet());
        }
        return result;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return new HashSet<>();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> result = new HashSet<>();
        if (StringUtils.hasText(authorities)) {
            result = Arrays.stream(authorities.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        }
        return result;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenExpiration;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenExpiration;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        boolean autoApproveScope = false;
        if (StringUtils.hasText(autoApprove)) {
            autoApproveScope = Arrays.stream(autoApprove.split(",")).anyMatch(scope::matches);
        }
        return autoApproveScope;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return new HashMap<>();
    }
}
