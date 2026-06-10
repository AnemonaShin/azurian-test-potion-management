package cl.management.potion.service.implement;

import java.util.Base64;
import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.management.potion.dto.response.DefaultResponse;
import cl.management.potion.exception.ServiceException;
import cl.management.potion.repository.UserRepository;
import cl.management.potion.service.LoginService;
import cl.management.potion.util.ValidationUtil;
import cl.management.potion.util.enums.ExceptionListEnum;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class LoginServiceImpl implements LoginService {

  private UserRepository userRepository;

  public LoginServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public DefaultResponse getLoginToken(String token) throws ServiceException {

    try {
      token = token.replace("Basic ", "");

      var decryptedToken = new String(Base64.getDecoder().decode(token));

      var splitedToken = decryptedToken.split(":");

      var user = splitedToken[0];
      var secret = splitedToken[1];

      var userEntity = userRepository.searchByUsername(user);

      if (userEntity == null) {
        throw new ServiceException(ExceptionListEnum.USER_NOT_FOUND);
      }

      ValidationUtil.tokenValidation(token, secret, userEntity.getPassword());

      token = splitedToken[1] + ":" + userEntity.getPassword();

      var newEncryptedToken = Base64.getEncoder().encode(token.getBytes());

      var response = new HashMap<String, Object>();

      response.put("id", userEntity.getId());
      response.put("token", newEncryptedToken);
      response.put("role", userEntity.getRole().getName());
      response.put("username", userEntity.getUsername());

      return DefaultResponse.builder().code("200").message("TOKEN GENERATED").response(response).build();
    } catch (ServiceException exe) {
      log.error("Service Exception Error in 'getLoginToken': {}", exe);
      throw new ServiceException(exe.getStatusCode(), exe.getCode(), exe.getMessage());
    } catch (Exception ex) {
      log.error("Exception Error in 'getLoginToken': {}", ex);
      throw new ServiceException(ExceptionListEnum.DEFAULT_ERROR);
    }
  }

}
