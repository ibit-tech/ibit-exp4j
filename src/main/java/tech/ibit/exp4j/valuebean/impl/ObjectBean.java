package tech.ibit.exp4j.valuebean.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import tech.ibit.exp4j.valuebean.IVariable;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

/**
 * Object Bean
 *
 * @author iBit程序猿
 */
public class ObjectBean extends ErrorBeanImpl implements IVariable {

    /**
     * 对象
     */
    private final Object object;

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

    public ObjectBean(Object object, int calculationScale, int displayScale, BigDecimal defaultValue) {
        this.object = object;
        this.calculationScale = calculationScale;
        this.displayScale = displayScale;
        if (null != defaultValue) {
            this.defaultValue = defaultValue.setScale(calculationScale, BigDecimal.ROUND_HALF_EVEN);
        }
    }

    @Override
    public BigDecimal getValue(String variable) {

        try {
            String value = BeanUtils.getProperty(object, variable);
            return StringUtils.isBlank(value)
                    ? getDefaultValue()
                    : new BigDecimal(value);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ignore) {
            // ignore
        }
        return getDefaultValue();
    }

    /**
     * 获取值，默认null
     *
     * @param variable 变量值
     * @return 值
     */
    @Override
    public BigDecimal getNullableValue(String variable) {
        try {
            String value = BeanUtils.getProperty(object, variable);
            return StringUtils.isBlank(value)
                    ? null
                    : new BigDecimal(value);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ignore) {
            // ignore
        }
        return null;
    }

    @Override
    public void setValue(String variable, BigDecimal value) {
        try {
            BeanUtils.setProperty(object, variable, null == value ? null : value.setScale(getCalculationScale(), BigDecimal.ROUND_HALF_EVEN));
        } catch (IllegalAccessException | InvocationTargetException ignore) {
            // ignore
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
        return object.toString();
    }
}
