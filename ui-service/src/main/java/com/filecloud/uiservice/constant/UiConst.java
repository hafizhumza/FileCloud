package com.filecloud.uiservice.constant;

public class UiConst {

    /**
     * Keys
     */
    public static final String KEY_LOGIN_MODEL = "loginModel";
    public static final String KEY_ERROR = "error";
    public static final String KEY_USER = "user";
    public static final String KEY_PROCESS_USER = "processUser";
    public static final String KEY_USERS = "users";
    public static final String KEY_ACTIVE_USERS_COUNT = "activeUsersCount";
    public static final String KEY_IN_ACTIVE_USERS_COUNT = "inActiveUsersCount";
    public static final String KEY_ALL_USERS = "allUsers";
    public static final String KEY_RESULT_MESSAGE = "ResultMessage";
    public static final String KEY_SESSION = "session";

    /**
     * Messages
     */
    public static final String MESSAGE_INVALID_LOGIN = "Invalid email or password";
    public static final String MESSAGE_ACCOUNT_LOCKED = "User account is locked";
    public static final String MESSAGE_LOGIN = "Please login to continue";

    /**
     * URLs
     */
    public static final String URL_AUTH_SERVER = "/aus/api/v1/";
    public static final String URL_ADMIN_SERVICE = "/as/api/v1/";
    public static final String URL_DOCUMENT_SERVICE = "/ds/api/v1/";

    /**
     * ROLE
     */
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    /**
     * SCOPE
     */
    public static final String SCOPE_WRITE = "WRITE";
    public static final String SCOPE_READ = "READ";
    public static final String SCOPE_DOCUMENT = "DOCUMENT";
    public static final String SCOPE_EMAIL = "EMAIL";

    /**
     * Other
     */
    public static final String FILE_EXTENSION_ENCRYPTED = "encrypted";

    /**
     * Session
     */
    public static final String SESSION_CURRENT_USER = "CurrentUserSession";

}
