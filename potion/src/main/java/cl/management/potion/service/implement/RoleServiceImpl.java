package cl.management.potion.service.implement;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import cl.management.potion.dto.request.RoleRequest;
import cl.management.potion.dto.response.DefaultResponse;
import cl.management.potion.dto.response.RoleResponse;
import cl.management.potion.exception.ServiceException;
import cl.management.potion.model.RoleEntity;
import cl.management.potion.repository.RoleRepository;
import cl.management.potion.service.RoleService;
import cl.management.potion.util.ExceptionUtil;
import cl.management.potion.util.ValidationUtil;
import cl.management.potion.util.enums.ExceptionListEnum;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

  private RoleRepository roleRepository;

  public RoleServiceImpl(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @Override
  public Page<RoleResponse> listRoles(String token, PageRequest pageRequest) throws ServiceException {
    try {

      ValidationUtil.tokenValidation(token);

      PageRequest sortedPage = PageRequest.of(
          pageRequest.getPageNumber(), pageRequest.getPageSize(), Sort.by(Sort.Direction.ASC, "id"));

      var roleEntities = roleRepository.findAll(sortedPage);

      return roleEntities.map(
          (RoleEntity roleEntity) -> RoleResponse.builder()
              .id(roleEntity.getId())
              .name(roleEntity.getName())
              .roleIcon(roleEntity.getRoleIcon())
              .build());
    } catch (ServiceException exe) {
      log.error("Service Exception Error in 'listRoles': {}", exe);
      throw new ServiceException(exe.getStatusCode(), exe.getCode(), exe.getMessage());
    } catch (Exception ex) {
      log.error("Exception Error inside 'listRoles': {}", ex);
      throw new ServiceException(ExceptionListEnum.DEFAULT_ERROR);
    }

  }

  @Override
  @SuppressWarnings("null")
  public DefaultResponse createRole(String token, RoleRequest body) throws ServiceException {
    try {

      ValidationUtil.tokenValidation(token);
      ValidationUtil.roleRequestValidation(body);

      roleRepository.save(RoleEntity.builder().name(body.getName()).roleIcon(body.getRoleIcon()).build());

      return DefaultResponse.builder().code("200").message("ROLE REGISTERED").build();
    } catch (ServiceException exe) {
      log.error("Service Exception Error in 'createRole': {}", exe);
      throw new ServiceException(exe.getStatusCode(), exe.getCode(), exe.getMessage());
    } catch (DataIntegrityViolationException divx) {
      log.error("DataIntegrityViolationException Error inside 'createRole': {}", divx);
      throw new ServiceException(ExceptionUtil.dataIntegrityErrors(divx));
    } catch (Exception ex) {
      log.error("Exception Error inside 'createRole': {}", ex);
      throw new ServiceException(ExceptionListEnum.DEFAULT_ERROR);
    }
  }

  @Override
  public void activateRole(String token, long id) throws ServiceException {
    try {

      ValidationUtil.tokenValidation(token);

      var roleEntity = roleRepository.searchById(id);

      ValidationUtil.validateNotNull(roleEntity, ExceptionListEnum.ROLE_NOT_FOUND);

      if (Boolean.TRUE.equals(roleEntity.isActive())) {
        throw new ServiceException(ExceptionListEnum.ROLE_ALREADY_ACTIVATED);
      }

      roleEntity.setActive(true);

      roleRepository.save(roleEntity);
    } catch (ServiceException exe) {
      log.error("Service Exception Error in 'activateRole': {}", exe);
      throw new ServiceException(exe.getStatusCode(), exe.getCode(), exe.getMessage());
    } catch (Exception ex) {
      log.error("Exception Error inside 'activateRole': {}", ex);
      throw new ServiceException(ExceptionListEnum.DEFAULT_ERROR);
    }
  }

  @Override
  public void deactivateRole(String token, long id) throws ServiceException {
    try {

      ValidationUtil.tokenValidation(token);

      var roleEntity = roleRepository.searchById(id);

      ValidationUtil.validateNotNull(roleEntity, ExceptionListEnum.USER_NOT_FOUND);

      if (Boolean.FALSE.equals(roleEntity.isActive())) {
        throw new ServiceException(ExceptionListEnum.USER_ALREADY_DEACTIVATED);
      }

      roleEntity.setActive(false);

      roleRepository.save(roleEntity);
    } catch (ServiceException exe) {
      log.error("Service Exception Error in 'deactivateRole': {}", exe);
      throw new ServiceException(exe.getStatusCode(), exe.getCode(), exe.getMessage());
    } catch (Exception ex) {
      log.error("Exception Error inside 'deactivateRole': {}", ex);
      throw new ServiceException(ExceptionListEnum.DEFAULT_ERROR);
    }
  }

  @Override
  public DefaultResponse updateRole(String token, long id, RoleRequest body) throws ServiceException {
    try {

      ValidationUtil.tokenValidation(token);

      var roleEntity = roleRepository.searchById(id);
      ValidationUtil.validateNotNull(roleEntity, ExceptionListEnum.ROLE_NOT_FOUND);

      if (Boolean.FALSE.equals(roleEntity.isActive())) {
        throw new ServiceException(ExceptionListEnum.ROLE_DEACTIVATED_UPDATE);
      }

      roleEntity.setName(body.getName());
      roleEntity.setRoleIcon(body.getRoleIcon());

      roleRepository.save(roleEntity);

      return DefaultResponse.builder().code(HttpStatus.OK.toString()).message("ROLE UPDATED").build();
    } catch (ServiceException exe) {
      log.error("Service Exception Error in 'updateRole': {}", exe);
      throw new ServiceException(exe.getStatusCode(), exe.getCode(), exe.getMessage());
    } catch (DataIntegrityViolationException divx) {
      log.error("DataIntegrityViolationException Error inside 'updateRole': {}", divx);
      throw new ServiceException(ExceptionUtil.dataIntegrityErrors(divx));
    } catch (Exception ex) {
      log.error("Exception Error inside 'updateRole': {}", ex);
      throw new ServiceException(ExceptionListEnum.DEFAULT_ERROR);
    }
  }

}
