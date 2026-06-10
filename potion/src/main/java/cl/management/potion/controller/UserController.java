package cl.management.potion.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.management.potion.dto.request.UserRequest;
import cl.management.potion.dto.response.DefaultResponse;
import cl.management.potion.exception.ServiceException;
import cl.management.potion.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PutMapping;

/**
 * Rest Controller for the User part of the solution.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 09-06-2026
 * @version 1.0.1
 */
@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {

  private UserService service;

  /**
   * Public Constructor of User Controller
   * 
   * @param service User Service for management of it's data.
   */
  public UserController(UserService service) {
    this.service = service;
  }

  /**
   * Register a new user on the database.
   * 
   * @param body Request DTO for user info.
   * @return Response an default response object.
   * @throws ServiceException Custom exception for services.
   */
  @Tag(name = "registers", description = "Data registers endpoints")
  @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<DefaultResponse> registerUser(@RequestBody(required = true) UserRequest body)
      throws ServiceException {
    return ResponseEntity.ok().body(service.registerUser(body));
  }

  /**
   * List all users sorted by Username.
   * 
   * @param size Param for page size.
   * @param page Param paging place.
   * @return Response an default response object.
   * @throws ServiceException Custom exception for services.
   */
  @Tag(name = "searchs", description = "Searchs endpoints")
  @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> listUsers(
      @RequestHeader(name = "Authorization", required = true) String token,
      @RequestParam(required = false, defaultValue = "20") Integer size,
      @RequestParam(required = false, defaultValue = "0") Integer page) throws ServiceException {
    return ResponseEntity.ok().body(service.listUsers(token, PageRequest.of(page, size)));
  }

  /**
   * Search an user by its username.
   * 
   * @param username Username to search.
   * @return Response an default response object.
   * @throws ServiceException Custom exception for services.
   */
  @Tag(name = "searchs", description = "Searchs endpoints")
  @GetMapping(path = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<DefaultResponse> searchUser(
      @RequestHeader(name = "Authorization", required = true) String token,
      @PathVariable(name = "username", required = true) String username) throws ServiceException {
    return ResponseEntity.ok().body(service.searchUser(token, username));
  }

  /**
   * Update an existent user.
   * 
   * @param userId Id of an user.
   * @param body   Request DTO for user info.
   * @return Response an default response object.
   * @throws ServiceException Custom exception for services.
   */
  @Tag(name = "modifications", description = "Modifications to data endpoints")
  @PutMapping(path = "/{user_id}")
  public ResponseEntity<Object> updateUser(
      @RequestHeader(name = "Authorization", required = true) String token,
      @PathVariable(name = "user_id") Long userId,
      @RequestBody(required = true) UserRequest body) throws ServiceException {

    return ResponseEntity.ok().body(service.updateUser(token, userId, body));
  }

  /**
   * Deactivate an existen user.
   * 
   * @param userId Id of an user.
   * @return Response an default response object.
   * @throws ServiceException Custom exception for services.
   */
  @Tag(name = "deletions", description = "Endpoints designed for deletions")
  @DeleteMapping(path = "/{user_id}")
  public ResponseEntity<Object> deactivateUser(
      @RequestHeader(name = "Authorization", required = true) String token,
      @PathVariable(name = "user_id") Long userId) throws ServiceException {

    service.deactivateUser(token, userId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Activate an existen user.
   * 
   * @param userId Id of an user.
   * @return Response an default response object.
   * @throws ServiceException Custom exception for services.
   */
  @Tag(name = "modifications", description = "Modifications to data endpoints")
  @PatchMapping(path = "/{user_id}/activate")
  public ResponseEntity<Object> activateUser(
      @RequestHeader(name = "Authorization", required = true) String token,
      @PathVariable(name = "user_id") Long userId) throws ServiceException {

    service.activateUser(token, userId);
    return ResponseEntity.noContent().build();
  }
}
