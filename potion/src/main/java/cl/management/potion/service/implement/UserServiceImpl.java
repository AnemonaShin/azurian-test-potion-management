package cl.management.potion.service.implement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import cl.management.potion.dto.request.UserRequest;
import cl.management.potion.dto.response.DefaultResponse;
import cl.management.potion.exception.ServiceException;
import cl.management.potion.service.UserService;

/**
 * Service Implementation of Interface designed for the control of user's data.
 * 
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 08-06-2026
 * @version 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

  @Override
  public void deactivateUser(long id) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public Page<Object> listUsers(PageRequest pageRequest) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DefaultResponse registerUser(UserRequest body) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DefaultResponse searchUser(String username) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void updateUser(Long id, UserRequest body) throws ServiceException {
    // TODO Auto-generated method stub

  }

}
