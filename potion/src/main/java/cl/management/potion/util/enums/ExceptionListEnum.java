package cl.management.potion.util.enums;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Util - Enums - Exception List Enum
 * 
 * Exception codes and errors for uses inside the system.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 09-06-2026
 * @version 1.0.0
 */
@AllArgsConstructor
@Getter
public enum ExceptionListEnum {

        // REQUEST ERRORs
        USER_REQUEST_USERNAME_ERROR(HttpStatus.BAD_REQUEST, "USERNAME IS NULL OR EMPTY"),
        USER_REQUEST_PASSWORD_ERROR(HttpStatus.BAD_REQUEST, "PASSWORD IS NULL OR EMPTY"),
        USER_REQUEST_EMAIL_ERROR(HttpStatus.BAD_REQUEST, "EMAIL IS NULL OR EMPTY"),
        USER_REQUEST_ROLE_ERROR(HttpStatus.BAD_REQUEST, "ROLE IS NULL OR EMPTY"),
        ROLE_REQUEST_NAME_ERROR(HttpStatus.BAD_REQUEST, "ROLE NAME IS NULL OR EMPTY"),
        ROLE_REQUEST_ICON_ERROR(HttpStatus.BAD_REQUEST, "ROLE ICON IS NULL OR EMPTY"),

        // ENTITY NOT FOUND ERRORs
        USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER NOT FOUND"),
        ROLE_NOT_FOUND(HttpStatus.NOT_FOUND,
                        "ROLE NOT FOUND"),

        // DUPLICATIONS ERRORs
        USER_USERNAME_ALREADY_EXIST(HttpStatus.CONFLICT, "USER 'USERNAME' ALREADY EXISTS"),
        USER_EMAIL_ALREADY_EXIST(HttpStatus.CONFLICT,
                        "USER 'EMAIL' ALREADY EXISTS"),
        ROLE_NAME_ALREADY_EXIST(HttpStatus.CONFLICT, "ROLE 'ROLE NAME' ALREADY EXISTS"),
        ROLE_ICON_ALREADY_EXIST(HttpStatus.CONFLICT, "ROLE 'ROLE ICON' ALREADY EXISTS"),

        // DEACTIVATIONS ERRORs
        USER_ALREADY_DEACTIVATED(HttpStatus.CONFLICT,
                        "USER ALREADY DEACTIVATED"),
        ROLE_ALREADY_DEACTIVATED(HttpStatus.CONFLICT,
                        "ROLE ALREADY DEACTIVATED"),
        USER_ALREADY_ACTIVATED(HttpStatus.CONFLICT,
                        "USER ALREADY ACTIVATED"),
        ROLE_ALREADY_ACTIVATED(HttpStatus.CONFLICT,
                        "ROLE ALREADY ACTIVATED"),
        USER_DEACTIVATED_SEARCH(HttpStatus.NOT_FOUND, "USER DEACTIVATED CANT BE SEARCHED"),
        USER_DEACTIVATED_UPDATE(HttpStatus.NOT_MODIFIED, "USER DEACTIVATED CANT BE UPDATED"),
        ROLE_DEACTIVATED_UPDATE(HttpStatus.NOT_MODIFIED, "ROLE DEACTIVATED CANT BE UPDATED"),

        // PASSWORD ERRORs
        USER_PASSWORD_INCORRECT(HttpStatus.CONFLICT,
                        "INCORRECT PASSWORD"),

        // DEFAULT EXCEPTIONS
        DEFAULT_AUTHORIZATION_ERROR(HttpStatus.CONFLICT, "AUTHORIZATION TOKEN IS INVALID, EMPTY, NULL OR DISTINCT"),
        DEFAULT_REQUEST_NULL_ERROR(HttpStatus.BAD_REQUEST, "REQUEST IS NULL"),
        DEFAULT_DATA_INTEGRITY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL DATA INTEGRITY ERROR"),
        DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER EXCEPTION");

        private final HttpStatus statusCode;
        private final String message;

}
