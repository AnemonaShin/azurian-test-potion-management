package cl.management.potion.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import cl.management.potion.dto.request.RoleRequest;
import cl.management.potion.dto.response.DefaultResponse;
import cl.management.potion.dto.response.RoleResponse;
import cl.management.potion.exception.ServiceException;

/**
 * Service Interface designed for the control of roles's data.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 09-06-2026
 * @version 1.0.0
 */
@Service
public interface RoleService {

  /**
   * Method designed for the Creation of roles.
   * 
   * @param body Request Role DTO with info inserted by front.
   * @return Default Response with role data.
   * @throws ServiceException Custom exception for services.
   */
  public DefaultResponse createRole(String token, RoleRequest body) throws ServiceException;

  /**
   * Method that page list all roles on the application sort by ID's.
   * 
   * @param pageRequest Pageable object to list data.
   * @return Paginated Object sorted by ID's.
   * @throws ServiceException Custom exception for services.
   */
  public Page<RoleResponse> listRoles(String token, PageRequest pageRequest) throws ServiceException;

  /**
   * Method that update a role's data.
   * 
   * @param id   Identificator of an role.
   * @param body Request User DTO with updated from front.
   * @throws ServiceException Custom exception for services.
   */
  public DefaultResponse updateRole(String token, long id, RoleRequest body) throws ServiceException;

  /**
   * Method that deactivate an existent role.
   * 
   * @param id Identificator of an role.
   * @throws ServiceException Custom exception for services.
   */
  public void deactivateRole(String token, long id) throws ServiceException;

  /**
   * Method that activate an existent role.
   * 
   * @param id Identificator of an role.
   * @throws ServiceException Custom exception for services.
   */
  public void activateRole(String token, long id) throws ServiceException;

}
