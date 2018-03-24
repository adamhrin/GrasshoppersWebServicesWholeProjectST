/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomException;

/**
 *
 * @author Adam
 */
public class CustomException extends Exception {
    
    public static final int ERR_DATA_NOT_FOUND = 10000;
    public static final int ERR_DB_ENTITY_CLOSE = 10001;
    public static final int ERR_UPDATE_INSERT = 10002;
    public static final int ERR_SELECT = 10003;
    public static final int ERR_PREPARE_STATEMENT = 10004;
    public static final int ERR_CONFLICT = 10005;
    public static final int ERR_BAD_LOGIN_DATA = 10006;
    public static final int ERR_DELETE = 10007;
    
    private int errorCode;
    private String errorMessage;
    
    public CustomException(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    
}
