package tech.ibit.exp4j.valuebean;

import tech.ibit.exp4j.exception.ErrorEntity;

import java.math.BigDecimal;

/**
 * 定义变量接口
 *
 * @author iBit程序猿
 */
public interface IVariable {


    /**
     * 获取计算错误内容
     *
     * @param variable 变量
     * @return 计算错误内容
     */
    ErrorEntity getError(String variable);


    /**
     * 设置计算错误内容
     *
     * @param variable 变量
     * @param error    错误信息
     */
    void setError(String variable, ErrorEntity error);


    /**
     * 获取错误信息
     *
     * @param variable 变量
     * @return 错误信息
     */
    String getErrorMessage(String variable);

    /**
     * 获取值
     *
     * @param variable 变量名称
     * @return 值
     */
    BigDecimal getValue(String variable);

    /**
     * 获取值，默认null
     *
     * @param variable 变量值
     * @return 值
     */
    BigDecimal getNullableValue(String variable);

    /**
     * 设置值
     *
     * @param variable 变量名称
     * @param value    值
     */
    void setValue(String variable, BigDecimal value);

    /**
     * 设置值
     *
     * @param variable 变量名
     * @param value    值
     */
    default void setValue(String variable, Double value) {
        setValue(variable, null == value ? null : BigDecimal.valueOf(value));
    }

    /**
     * 获取显示值（与getValue可能精度不一致）
     *
     * @param variable 变量名称
     * @return 显示值
     */
    default BigDecimal getDisplayValue(String variable) {
        // 显示值需要四舍五入
        BigDecimal value = getValue(variable);
        return null == value ? null : value.setScale(getDisplayScale(), BigDecimal.ROUND_HALF_UP);
    }


    /**
     * 获取计算精度
     *
     * @return 计算精度
     */
    int getCalculationScale();

    /**
     * 获取显示精度
     *
     * @return 显示精度
     */
    int getDisplayScale();

    /**
     * 获取默认值（value为null的情况下返回的值）
     *
     * @return 默认值
     */
    default BigDecimal getDefaultValue() {
        return null;
    }

}
