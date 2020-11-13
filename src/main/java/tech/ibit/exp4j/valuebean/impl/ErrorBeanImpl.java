package tech.ibit.exp4j.valuebean.impl;

import tech.ibit.exp4j.exception.ErrorEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * 错误bean实现
 *
 * @author iBit程序猿
 */
public class ErrorBeanImpl {

    /**
     * 错误map
     */
    private final Map<String, ErrorEntity> errorMap = new HashMap<>();

    /**
     * 获取计算错误内容
     *
     * @param variable 变量
     * @return 计算错误内容
     */
    public ErrorEntity getError(String variable) {
        return errorMap.get(variable);
    }


    /**
     * 设置计算错误内容
     *
     * @param variable 变量
     * @param error    错误信息
     */
    public void setError(String variable, ErrorEntity error) {
        errorMap.put(variable, error);
    }

    /**
     * 获取错误信息
     *
     * @param variable 变量
     * @return 错误信息
     */
    public String getErrorMessage(String variable) {
        ErrorEntity errorEntity = errorMap.get(variable);
        return null == errorEntity ? null : errorEntity.getErrorMessage();
    }

}
