package tech.ibit.exp4j;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.operator.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.ibit.exp4j.valuebean.IVariable;
import tech.ibit.exp4j.valuebean.impl.ResultMapBean;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 公式计算器（单个公式计算）
 *
 * @author iBit程序猿
 */
public class FormulaEvaluator {

    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FormulaEvaluator.class);

    /**
     * "强制计算"标识符
     */
    private static final String FORCE_COMPUTE_PREFIX = "!";

    /**
     * "任意变量为 null 则不计算"标识符
     */
    private static final String ANY_NULL_NOT_COMPUTE_PREFIX = "N!";

    /**
     * 表达式
     */
    private final Expression expression;

    /**
     * 公式实体
     */
    private final FormulaEntity formulaEntity;

    /**
     * 强制计算
     */
    private boolean forceCompute = false;

    /**
     * 任意变量为null，则不计算
     */
    private boolean anyNullNotCompute = false;

    /**
     * 构造函数
     *
     * @param formulaKey  公式键（等号左边）
     * @param formulaPart 公式部分（等号右边）
     */
    public FormulaEvaluator(String formulaKey, String formulaPart) {
        this(null, formulaKey, formulaPart, null, null);
    }

    /**
     * 构造函数
     *
     * @param variablePrefix 变量前缀（标识变量）
     * @param formulaKey     公式键（等号左边）
     * @param formulaPart    公式部分（等号右边）
     */
    public FormulaEvaluator(String variablePrefix, String formulaKey, String formulaPart) {
        this(variablePrefix, formulaKey, formulaPart, null, null);
    }

    /**
     * 构造函数
     *
     * @param formulaKey  公式键（等号左边）
     * @param formulaPart 公式部分（等号右边）
     * @param operators   自定义符号列表
     * @param functions   自定义函数列表
     */
    public FormulaEvaluator(String formulaKey, String formulaPart, List<Operator> operators, List<Function> functions) {
        this(null, formulaKey, formulaPart, operators, functions);
    }

    /**
     * 构造函数
     *
     * @param variablePrefix 变量前缀（标识变量）
     * @param formulaKey     公式键（等号左边）
     * @param formulaPart    公式部分（等号右边）
     * @param operators      自定义符号列表
     * @param functions      自定义函数列表
     */
    public FormulaEvaluator(String variablePrefix, String formulaKey, String formulaPart, List<Operator> operators, List<Function> functions) {


        if (formulaKey.startsWith(FORCE_COMPUTE_PREFIX)) {
            // 强制计算
            formulaKey = formulaKey.substring(FORCE_COMPUTE_PREFIX.length());
            forceCompute = true;
        } else if (formulaKey.startsWith(ANY_NULL_NOT_COMPUTE_PREFIX)) {
            // 任何变量为null，则计算
            formulaKey = formulaKey.substring(ANY_NULL_NOT_COMPUTE_PREFIX.length());
            anyNullNotCompute = true;
        }

        this.formulaEntity = new FormulaEntity(variablePrefix, formulaKey, formulaPart);

        String f = formulaEntity.getFormulaPart();

        try {
            expression = new ExpressionBuilder(f)
                    .functions(getFunctions(f, functions))
                    .operator(getOperators(f, operators))
                    .variables(formulaEntity.getVariableNames())
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error to build {}", formulaEntity, e);
            throw e;
        }
    }

    /**
     * 获取自定义函数
     *
     * @param formula   公式
     * @param functions 自定义函数列表
     * @return 自定义函数列表
     */
    private List<Function> getFunctions(String formula, List<Function> functions) {
        if (null == functions) {
            return Collections.emptyList();
        }
        return functions
                .stream()
                .filter(function -> formula.contains(function.getName()))
                .collect(Collectors.toList());
    }

    /**
     * 获取自定义符号
     *
     * @param formula   公式
     * @param operators 自定义符号列表
     * @return 自定义符号列表
     */
    private List<Operator> getOperators(String formula, List<Operator> operators) {
        if (null == operators) {
            return Collections.emptyList();
        }
        return operators
                .stream()
                .filter(operator -> formula.contains(operator.getSymbol()))
                .collect(Collectors.toList());
    }


    /**
     * 判断全部变量是否为null
     *
     * @param iVariable 变量值
     * @return 是否为null
     */
    private boolean isAllNull(IVariable iVariable) {
        Set<String> variables = formulaEntity.getVariableNames();
        if (null == variables || variables.isEmpty()) {
            return false;
        }

        if (null == iVariable) {
            return true;
        }

        for (String variable : variables) {
            if (null != iVariable.getNullableValue(variable)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否部分变量为null
     *
     * @param iVariable 变量值
     * @return 是否为null
     */
    private boolean isAnyNull(IVariable iVariable) {
        Set<String> variables = formulaEntity.getVariableNames();
        if (null == variables || variables.isEmpty()) {
            return false;
        }

        if (null == iVariable) {
            return true;
        }

        for (String variable : variables) {
            if (null == iVariable.getNullableValue(variable)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 计算
     *
     * @param iVariable 计算变量值
     * @return 计算结果
     */
    public Double evaluate(IVariable iVariable) {

        if (null == iVariable) {
            iVariable = ResultMapBean.getDefaultInstance();
        }

        // 如果全部变量都为null, 则返回null
        if (!forceCompute && isAllNull(iVariable)) {
            return null;
        }

        // 如果任意变量为 null，则返回null
        if (anyNullNotCompute && isAnyNull(iVariable)) {
            return null;
        }

        Set<String> variableNames = formulaEntity.getVariableNames();

        for (String variableName : variableNames) {
            double value = iVariable.getValue(variableName).doubleValue();
            expression.setVariable(variableName, value);
        }

        return expression.evaluate();
    }

    /**
     * 获取公式实体
     *
     * @return 公式实体
     */
    public FormulaEntity getFormulaEntity() {
        return formulaEntity;
    }
}
