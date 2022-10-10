package ODL.web.authservice.service.user;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ODL.web.authservice.dto.ResetPasswordDTO;
import ODL.web.authservice.dto.user.AccountDTO;
import ODL.web.authservice.dto.user.UserDTO;
import ODL.web.authservice.entity.UserToken;
import ODL.web.authservice.entity.user.User;
import ODL.web.authservice.exception.BusinessEntityNotFoundException;
import ODL.web.authservice.exception.BusinessException;
import ODL.web.authservice.repository.UserRepository;
import ODL.web.authservice.repository.UserTokenRepository;
import ODL.web.authservice.service.CrudImpl;
import ODL.web.authservice.service.NotificationService;
import ODL.web.authservice.service.mapper.AccountDTOMapper;
import ODL.web.authservice.service.mapper.UserDTOMapper;
import ODL.web.authservice.util.CommonUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * User service
 * 
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserService extends CrudImpl<User, UserRepository, UserDTO, UserDTOMapper> {

    @Value("${token.email.confirm.time.hours}")
    Long tokenExpireHours;

    final UserTokenRepository tokenRepository;
    final PasswordEncoder passwordEncoder;
    final AccountDTOMapper accountDTOMapper;
    final NotificationService notificationService;

    /**
     * Find user by email
     * 
     * @param email user email
     * @return {@link User}
     * @throws BusinessEntityNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    User findByEmail(String email) {
        return repository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new BusinessEntityNotFoundException("User not found"));
    }

    /**
     * Get all users
     * 
     * @param pageable Spring interface for pagination information
     * @return {@link User} array
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> getUsers(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDTO);
    }

    /**
     * Register new account
     * 
     * @param accountDTO {@link AccountDTO} for registration
     * @return registred {@link User} (converted into {@link AccountDTO})
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AccountDTO registerAccount(AccountDTO accountDTO) {
        User saved = saveUser(accountDTOMapper.toEntity(accountDTO));
        notificationService.userRegistered(generateUserToken(saved));
        return accountDTOMapper.toDTO(saved);
    }

    /**
     * Save user into DB
     * 
     * @param user {@link User} for save
     * @return saved {@link User}
     * @throws BusinessException if {@link User#id} is no uniq}
     */
    private User saveUser(User user) {
        if (repository.existsByEmailAndDeletedFalse(user.getEmail())) {
            throw new BusinessException("This email already exist!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    /**
     * Generate {@link UserToken} for {@link User}
     * 
     * @param user {@link User} for {@link UserToken} generation
     * @return generated {@link UserToken}
     */
    private UserToken generateUserToken(User user) {
        LocalDateTime createDate = LocalDateTime.now();
        LocalDateTime expiredDate = createDate.plusHours(tokenExpireHours);
        String token = CommonUtils.generateRandomString(80);
        return tokenRepository.save(new UserToken(token, user, createDate, expiredDate));
    }

    /**
     * Confirm user email
     * 
     * @param token {@link UserToken#token}
     * @throws BusinessException if token is no valid
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void confirmMail(String token) {
        UserToken emailToken = tokenRepository.getByTokenAndExpiredAfter(token, LocalDateTime.now())
                .orElseThrow(() -> new BusinessException("Token is not valid"));
        String email = emailToken.getUser().getEmail();
        repository.confirmUserEmail(email);
        tokenRepository.delete(emailToken);
    }

    /**
     * Resend email with ñonfirmation link
     * 
     * @param email {@link User#email}
     * @throws BusinessEntityNotFoundException if {@link UserToken} with
     *                                         {@link User#email} is no exist
     */
    @Transactional
    public void resendConfirmMail(String email) {
        UserToken emailToken = tokenRepository.getByUserEmail(email)
                .orElseThrow(() -> new BusinessEntityNotFoundException("Token not found"));

        notificationService.userRegistered(generateUserToken(emailToken.getUser()));
        tokenRepository.delete(emailToken);
    }

    /**
     * Reset {@link User#password}
     * 
     * @param resetPasswordDTO {@link ResetPasswordDTO}
     * @return {@link User} with new data (converted into {@link AccountDTO})
     */
    @Transactional
    public AccountDTO resetPassword(ResetPasswordDTO resetPasswordDTO) {
        UserToken emailToken = tokenRepository
                .getByTokenAndExpiredAfter(resetPasswordDTO.getToken(), LocalDateTime.now())
                .orElseThrow(() -> new BusinessEntityNotFoundException("Token not found"));
        User user = findByEmail(emailToken.getUser().getEmail());
        user.setLocked(false);
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
        tokenRepository.delete(emailToken);
        return accountDTOMapper.toDTO(repository.save(user));
    }

    /**
     * Generate new {@link UserToken} for drop {@link User#password}
     * 
     * @param email {@link User#email}
     */
    @Transactional
    public void dropPassword(String email) {
        notificationService.userResetPassword(generateUserToken(findByEmail(email)));
    }

    /**
     * Create new User (use for Admins)
     * 
     * @param userDTO {@link UserDTO}
     * @return created {@link User} (converted into {@link UserDTO})
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserDTO createAccount(UserDTO userDTO) {
        return mapper.toDTO(saveUser(mapper.toEntity(userDTO)));
    }

    // TODO: rewrite it!
    @Transactional
    public UserDTO patchUser(UUID userId, Map<String, Object> patchMap) {
        UserDTO user = mapper.toDTO(findByIdNotNull(userId));
        for (Map.Entry<String, Object> entry : patchMap.entrySet()) {
            try {
                String methodName = "set".concat(entry.getKey().substring(0, 1).toUpperCase())
                        .concat(entry.getKey().substring(1));
                Method method = user.getClass().getMethod(methodName, String.class);
                method.invoke(user, entry.getValue().toString());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new BusinessException("Patch for some fields can't be accepted");
            }
        }
        return update(userId, user);
    }

    /**
     * Find user by id
     * 
     * @param userId {@link User#id}
     * @return {@link User}
     * @throws BusinessEntityNotFoundException if {@link User} with {@link User#id}
     *                                         no exist
     */
    private User findByIdNotNull(UUID userId) {
        return repository.findById(userId).orElseThrow(() -> new BusinessEntityNotFoundException("User not found"));
    }

    /**
     * update user
     * 
     * @param userId  {@link User#id}
     * @param userDTO {@link UserDTO} with udates
     * @return updated {@link User} (converted into {@link UserDTO})
     */
    @Transactional
    public UserDTO update(UUID userId, @Valid UserDTO userDTO) {
        if (!repository.existsById(userId)) {
            throw new BusinessEntityNotFoundException("User not found");
        } else if (repository.existsByEmailAndIdNotAndDeletedFalse(userDTO.getEmail(), userId)) {
            throw new BusinessException("User email should be unique across system");
        } else if (!userDTO.getEmail().matches(CommonUtils.EMAIL_REGEX))
            throw new BusinessException("Email no have valid format");
        User user = mapper.toEntity(userDTO);
        user.setId(userId);
        return mapper.toDTO(repository.save(user));
    }

}
