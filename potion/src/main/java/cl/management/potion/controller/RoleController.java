package cl.management.potion.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.management.potion.dto.request.RoleRequest;
import cl.management.potion.exception.ServiceException;
import cl.management.potion.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Service Interface designed for the control of role's data.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 09-06-2026
 * @version 1.0.0
 */
@RestController
@RequestMapping(path = "/roles")
public class RoleController {

  private RoleService service;

  public RoleController(RoleService service) {
    this.service = service;
  }

  /**
   * Register an new ROLE for the System.
   * 
   * @param body Request DTO for role info.
   * @return Response an default response object.
   * @throws ServiceException Custom exception for services.
   */
  @Tag(name = "registers", description = "Data registers endpoints")
  @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createRole(
      @RequestHeader(name = "Authorization", required = true) String token,
      @RequestBody(required = true) RoleRequest body) throws ServiceException {
    return ResponseEntity.ok().body(service.createRole(token, body));
  }

  /**
   * List all roles sorted by ID.
   * 
   * @param size Param for page size.
   * @param page Param paging place.
   * @return Response an default response object.
   * @throws ServiceException Custom exception for services.
   */
  @Tag(name = "searchs", description = "Searchs endpoints")
  @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> listRoles(
      @RequestHeader(name = "Authorization", required = true) String token,
      @RequestParam(required = false, defaultValue = "10") Integer size,
      @RequestParam(required = true, defaultValue = "0") Integer page) throws ServiceException {
    return ResponseEntity.ok().body(service.listRoles(token, PageRequest.of(page, size)));
  }

  /**
   * Update an existent role.
   * 
   * @param roleId Id of an Role.
   * @param body   Request DTO for Role info.
   * @return Response an default response object.
   * @throws ServiceException Custom exception for services.
   */
  @Tag(name = "modifications", description = "Modifications to data endpoints")
  @PutMapping(path = "/{role_id}")
  public ResponseEntity<Object> updateRole(
      @RequestHeader(name = "Authorization", required = true) String token,
      @PathVariable(name = "role_id") Long roleId,
      @RequestBody(required = true) RoleRequest body) throws ServiceException {

    return ResponseEntity.ok().body(service.updateRole(token, roleId, body));
  }

  /**
   * Deactivate an existen role.
   * 
   * @param roleId Id of an role.
   * @return Response an default response object.
   * @throws ServiceException Custom exception for services.
   */
  @Tag(name = "deletions", description = "Endpoints designed for deletions")
  @DeleteMapping(path = "/{role_id}")
  public ResponseEntity<Object> deactivateRole(
      @RequestHeader(name = "Authorization", required = true) String token,
      @PathVariable(name = "role_id") Long roleId) throws ServiceException {

    service.deactivateRole(token, roleId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Activate an existen role.
   * 
   * @param roleId Id of an role.
   * @return Response an default response object.
   * @throws ServiceException Custom exception for services.
   */
  @Tag(name = "modifications", description = "Modifications to data endpoints")
  @PatchMapping(path = "/{role_id}/activate")
  public ResponseEntity<Object> activateUser(
      @RequestHeader(name = "Authorization", required = true) String token,
      @PathVariable(name = "role_id") Long roleId) throws ServiceException {

    service.activateRole(token, roleId);
    return ResponseEntity.noContent().build();
  }
}
