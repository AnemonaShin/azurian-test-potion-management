package cl.management.potion.service;

import org.springframework.stereotype.Service;

import cl.management.potion.dto.response.DefaultResponse;
import cl.management.potion.exception.ServiceException;

@Service
public interface LoginService {

    public DefaultResponse getLoginToken(String token) throws ServiceException;

}
