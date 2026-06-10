package cl.management.potion.service.implement;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import cl.management.potion.dto.request.UserRequest;
import cl.management.potion.dto.response.DefaultResponse;
import cl.management.potion.dto.response.RoleResponse;
import cl.management.potion.dto.response.UserResponse;
import cl.management.potion.exception.ServiceException;
import cl.management.potion.model.RoleEntity;
import cl.management.potion.model.UserEntity;
import cl.management.potion.repository.RoleRepository;
import cl.management.potion.repository.UserRepository;
import cl.management.potion.service.UserService;
import cl.management.potion.util.ExceptionUtil;
import cl.management.potion.util.PasswordUtil;
import cl.management.potion.util.ValidationUtil;
import cl.management.potion.util.enums.ExceptionListEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * Service Implementation of Interface designed for the control of user's data.
 * 
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 09-06-2026
 * @version 1.0.1
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

  private UserRepository userRepository;
  private RoleRepository roleRepository;

  public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  @Override
  public void activateUser(String token, long id) throws ServiceException {
    try {

      ValidationUtil.tokenValidation(token);

      var userEntity = userRepository.searchById(id);

      ValidationUtil.validateNotNull(userEntity, ExceptionListEnum.USER_NOT_FOUND);

      if (Boolean.TRUE.equals(userEntity.isActive())) {
        throw new ServiceException(ExceptionListEnum.USER_ALREADY_ACTIVATED);
      }

      userEntity.setActive(true);

      userRepository.save(userEntity);
    } catch (ServiceException exe) {
      log.error("Service Exception Error in 'activateUser': {}", exe);
      throw new ServiceException(exe.getStatusCode(), exe.getCode(), exe.getMessage());
    } catch (Exception ex) {
      log.error("Exception Error inside 'activateUser': {}", ex);
      throw new ServiceException(ExceptionListEnum.DEFAULT_ERROR);
    }
  }

  @Override
  public void deactivateUser(String token, long id) throws ServiceException {
    try {

      ValidationUtil.tokenValidation(token);

      var userEntity = userRepository.searchById(id);

      ValidationUtil.validateNotNull(userEntity, ExceptionListEnum.USER_NOT_FOUND);

      if (Boolean.FALSE.equals(userEntity.isActive())) {
        throw new ServiceException(ExceptionListEnum.USER_ALREADY_DEACTIVATED);
      }

      userEntity.setActive(false);

      userRepository.save(userEntity);
    } catch (ServiceException exe) {
      log.error("Service Exception Error in 'deactivateUser': {}", exe);
      throw new ServiceException(exe.getStatusCode(), exe.getCode(), exe.getMessage());
    } catch (Exception ex) {
      log.error("Exception Error inside 'deactivateUser': {}", ex);
      throw new ServiceException(ExceptionListEnum.DEFAULT_ERROR);
    }
  }

  @Override
  public Page<UserResponse> listUsers(String token, PageRequest pageRequest) throws ServiceException {
    try {

      ValidationUtil.tokenValidation(token);

      PageRequest sortedPage = PageRequest.of(
          pageRequest.getPageNumber(), pageRequest.getPageSize(), Sort.by(Sort.Direction.ASC, "username"));

      var userEntities = userRepository.findAll(sortedPage);

      return userEntities.map(
          (UserEntity userEntity) -> UserResponse.builder()
              .id(userEntity.getId())
              .username(userEntity.getUsername())
              .email(userEntity.getEmail())
              .active(userEntity.isActive())
              .role(RoleResponse.builder()
                  .id(userEntity.getRole().getId())
                  .name(userEntity.getRole().getName())
                  .roleIcon(userEntity.getRole().getRoleIcon())
                  .build())
              .build());
    } catch (ServiceException exe) {
      log.error("Service Exception Error in 'listUsers': {}", exe);
      throw new ServiceException(exe.getStatusCode(), exe.getCode(), exe.getMessage());
    } catch (Exception ex) {
      log.error("Exception Error inside 'listUsers': {}", ex);
      throw new ServiceException(ExceptionListEnum.DEFAULT_ERROR);
    }

  }

  @Override
  @SuppressWarnings("null")
  public DefaultResponse registerUser(UserRequest body) throws ServiceException {
    try {
      log.info("STARTING 'registerUser' WITH BODY: {}", body.toString());

      ValidationUtil.userRequestValidation(body);

      log.info("SEARCHING ROLE");
      RoleEntity role = roleRepository.searchById(body.getRole().getId());
      ValidationUtil.validateNotNull(role, ExceptionListEnum.ROLE_NOT_FOUND);
      log.info("ROLE {} OBTAINED", role.getName());

      log.info("PREPARING DATA TO INSERT TO DB");
      var user = UserEntity.builder()
          .username(body.getUsername())
          .password(PasswordUtil.passwordEncrypt(body.getPassword()))
          .email(body.getEmail())
          .role(role)
          .build();

      userRepository.save(user);

      log.info("FINAL - DATA INSERTED TO DB");
      return DefaultResponse.builder().code(String.valueOf(HttpStatus.OK.value())).message("USER REGISTERED").build();
    } catch (ServiceException exe) {
      log.error("Service Exception Error in 'registerUser': {}", exe);
      throw new ServiceException(exe.getStatusCode(), exe.getCode(), exe.getMessage());
    } catch (DataIntegrityViolationException divx) {
      log.error("DataIntegrityViolationException Error inside 'registerUser': {}", divx);
      throw new ServiceException(ExceptionUtil.dataIntegrityErrors(divx));
    } catch (Exception ex) {
      log.error("Exception Error inside 'registerUser': {}", ex);
      throw new ServiceException(ExceptionListEnum.DEFAULT_ERROR);
    }
  }

  @Override
  public DefaultResponse searchUser(String token, String username) throws ServiceException {
    try {

      ValidationUtil.tokenValidation(token);

      var userEntity = userRepository.searchByUsername(username);
      ValidationUtil.validateNotNull(userEntity, ExceptionListEnum.USER_NOT_FOUND);

      var userResponse = UserResponse.builder()
          .id(userEntity.getId())
          .username(userEntity.getUsername())
          .email(userEntity.getEmail())
          .role(RoleResponse.builder().name(userEntity.getRole().getName()).roleIcon(
              userEntity.getRole().getRoleIcon()).build())
          .build();

      return DefaultResponse.builder().code(HttpStatus.OK.toString()).message("USER SEARCHED").response(userResponse)
          .build();
    } catch (ServiceException exe) {
      log.error("Service Exception Error in 'searchUser': {}", exe);
      throw new ServiceException(exe.getStatusCode(), exe.getCode(), exe.getMessage());
    } catch (Exception ex) {
      log.error("Exception Error inside 'searchUser': {}", ex);
      throw new ServiceException(ExceptionListEnum.DEFAULT_ERROR);
    }
  }

  @Override
  public DefaultResponse updateUser(String token, long id, UserRequest body) throws ServiceException {
    try {

      ValidationUtil.tokenValidation(token);

      var userEntity = userRepository.searchById(id);
      ValidationUtil.validateNotNull(userEntity, ExceptionListEnum.USER_NOT_FOUND);

      if (Boolean.FALSE.equals(userEntity.isActive())) {
        throw new ServiceException(ExceptionListEnum.USER_DEACTIVATED_UPDATE);
      }

      userEntity.setUsername(body.getUsername() != null ? body.getUsername() : userEntity.getUsername());
      userEntity.setPassword(
          body.getPassword() != null ? PasswordUtil.passwordEncrypt(body.getPassword()) : userEntity.getPassword());
      userEntity.setEmail(body.getEmail() != null ? body.getEmail() : userEntity.getEmail());
      userEntity
          .setRole(body.getRole() != null ? roleRepository.searchById(body.getRole().getId()) : userEntity.getRole());

      userRepository.save(userEntity);

      return DefaultResponse.builder().code(HttpStatus.OK.toString()).message("USER UPDATED").build();
    } catch (ServiceException exe) {
      log.error("Service Exception Error in 'updateUser': {}", exe);
      throw new ServiceException(exe.getStatusCode(), exe.getCode(), exe.getMessage());
    } catch (DataIntegrityViolationException divx) {
      log.error("DataIntegrityViolationException Error inside 'updateUser': {}", divx);
      throw new ServiceException(ExceptionUtil.dataIntegrityErrors(divx));
    } catch (Exception ex) {
      log.error("Exception Error inside 'searchUser': {}", ex);
      throw new ServiceException(ExceptionListEnum.DEFAULT_ERROR);
    }
  }

}
