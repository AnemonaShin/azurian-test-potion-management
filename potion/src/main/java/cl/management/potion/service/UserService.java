package cl.management.potion.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import cl.management.potion.dto.request.UserRequest;
import cl.management.potion.dto.response.DefaultResponse;
import cl.management.potion.exception.ServiceException;

/**
 * Service Interface designed for the control of user's data.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 08-06-2026
 * @version 1.0.0
 */
@Service
public interface UserService {

  /**
   * Method for Registry of users.
   * 
   * @param body Request User DTO with info inserted by front.
   * @return Default Response with user data.
   * @throws ServiceException Custom exception for services.
   */
  public DefaultResponse registerUser(UserRequest body) throws ServiceException;

  /**
   * Method that page list all users on the application sort by username's.
   * 
   * @param pageRequest Pageable object to list data.
   * @return Paginated Object sorted by username's.
   * @throws ServiceException Custom exception for services.
   */
  public Page<Object> listUsers(PageRequest pageRequest) throws ServiceException;

  /**
   * Method that search an unique user by his username.
   * 
   * @param username Username to search.
   * @return Default Response with user data.
   * @throws ServiceException Custom exception for services.
   */
  public DefaultResponse searchUser(String username) throws ServiceException;

  /**
   * Method that update a user's data.
   * 
   * @param id   Identificator of an user.
   * @param body Request User DTO with updated from front.
   * @throws ServiceException Custom exception for services.
   */
  public void updateUser(Long id, UserRequest body) throws ServiceException;

  /**
   * Method that deactivate an existent user.
   * 
   * @param id Identificator of an user.
   * @throws ServiceException Custom exception for services.
   */
  public void deactivateUser(long id) throws ServiceException;

}
