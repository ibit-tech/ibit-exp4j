package tech.ibit.exp4j.valuebean.impl;


import tech.ibit.exp4j.valuebean.IVariable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 结果Bean
 *
 * @author iBit程序猿
 */
public class ResultMapBean extends ErrorBeanImpl implements IVariable {

    /**
     * 结果map
     */
    private final Map<String, BigDecimal> resultMap;

    /**
     * 计算精度
     */
    private final int calculationScale;

    /**
     * 显示精度
     */
    private final int displayScale;

    /**
     * 默认值（value为null的情况下返回的值）
     */
    private BigDecimal defaultValue;


    /**
     * 私有构造函数
     *
     * @param calculationScale 计算精度
     * @param displayScale     显示精度
     * @param defaultValue     默认值
     */
    private ResultMapBean(int calculationScale, int displayScale, BigDecimal defaultValue) {
        resultMap = new HashMap<>();
        this.calculationScale = calculationScale;
        this.displayScale = displayScale;
        if (null != defaultValue) {
            this.defaultValue = defaultValue.setScale(calculationScale, BigDecimal.ROUND_HALF_EVEN);
        }
    }

    /**
     * 获取 ResultMapBean 实例
     *
     * @param calculationScale 计算精度
     * @param displayScale     显示精度
     * @param defaultValue     默认值
     * @return ResultMapBean 实例
     */
    public static ResultMapBean getInstance(int calculationScale, int displayScale, BigDecimal defaultValue) {
        return new ResultMapBean(calculationScale, displayScale, defaultValue);
    }

    /**
     * 获取默认 ResultMapBean 实例
     *
     * @return 默认 ResultMapBean 实例
     */
    public static ResultMapBean getDefaultInstance() {
        return getInstance(4, 2, BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getValue(String variable) {
        return resultMap.containsKey(variable)
                ? resultMap.get(variable)
                : getDefaultValue();
    }

    /**
     * 获取值，默认null
     *
     * @param variable 变量值
     * @return 值
     */
    @Override
    public BigDecimal getNullableValue(String variable) {
        return resultMap.get(variable);
    }

    @Override
    public void setValue(String variable, BigDecimal value) {
        if (null != value) {
            // 在此处设置精度
            resultMap.put(variable, value.setScale(getCalculationScale(), BigDecimal.ROUND_HALF_EVEN));
        } else {
            resultMap.remove(variable);
        }
    }

    @Override
    public int getCalculationScale() {
        return calculationScale;
    }

    @Override
    public int getDisplayScale() {
        return displayScale;
    }

    @Override
    public BigDecimal getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ResultMapBean.class.getSimpleName() + "[", "]")
                .add("resultMap=" + resultMap)
                .add("calculationScale=" + calculationScale)
                .add("displayScale=" + displayScale)
                .add("defaultValue=" + defaultValue)
                .toString();
    }
}
