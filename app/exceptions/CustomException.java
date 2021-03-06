package exceptions;

public class CustomException extends RuntimeException{

  private static final long serialVersionUID = 1L;

  public CustomException(String cause) {
    super(cause);
  }

  public CustomException(String msg, Throwable cause) {
    super(msg, cause);
  }

}
