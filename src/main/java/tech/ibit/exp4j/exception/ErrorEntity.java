package tech.ibit.exp4j.exception;


/**
 * 定义异常类
 *
 * @author iBit程序猿
 */
public class ErrorEntity {

    /**
     * 异常类
     */
    private final Class<?> exceptionClass;


    /**
     * 错误信息
     */
    private final String errorMessage;

    /**
     * 构造函数
     *
     * @param exceptionClass 异常类
     * @param errorMessage   错误信息
     */
    public ErrorEntity(Class<?> exceptionClass, String errorMessage) {
        this.exceptionClass = exceptionClass;
        this.errorMessage = errorMessage;
    }

    /**
     * 构造函数
     *
     * @param t 异常
     */
    public ErrorEntity(Throwable t) {
        this.exceptionClass = t.getClass();
        this.errorMessage = t.getMessage();
    }

    /**
     * Gets the value of exceptionClass
     *
     * @return the value of exceptionClass
     */
    public Class<?> getExceptionClass() {
        return exceptionClass;
    }


    /**
     * Gets the value of errorMessage
     *
     * @return the value of errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

}
