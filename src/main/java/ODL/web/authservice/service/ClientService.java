package ODL.web.authservice.service;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import ODL.web.authservice.exception.BusinessEntityNotFoundException;
import ODL.web.authservice.repository.ClientRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * 
 * Client service. Used only for SringSecurity (oauth2) configuration
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClientService implements ClientDetailsService {

    ClientRepository repository;

    @Override
    public ClientDetails loadClientByClientId(final String clientId) throws ClientRegistrationException {
        return repository.findByClientId(clientId)
                .orElseThrow(() -> new BusinessEntityNotFoundException("Client not found"));
    }
}
