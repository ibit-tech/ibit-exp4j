package tech.ibit.exp4j;

import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.operator.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.ibit.exp4j.exception.CircularException;
import tech.ibit.exp4j.exception.ErrorEntity;
import tech.ibit.exp4j.valuebean.IVariable;

import java.math.BigDecimal;
import java.util.*;

/**
 * 公式计算器（多个公式）
 *
 * @author iBit程序猿
 */
public class FormulaEvaluators {

    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FormulaEvaluators.class);

    /**
     * 公式计算map
     */
    private final Map<String, FormulaEvaluator> formulaEvaluatorMap;


    /**
     * 计算顺序
     */
    private final Set<String> calculationOrders;

    /**
     * 构造函数
     *
     * @param variablePrefix    变量前缀
     * @param formulaProperties 公式配置
     */
    public FormulaEvaluators(String variablePrefix, Map<String, String> formulaProperties) {
        this(variablePrefix, formulaProperties, null, null);
    }

    /**
     * 构造函数
     *
     * @param variablePrefix    变量前缀
     * @param formulaProperties 公式配置
     * @param operators         自定义操作符
     * @param functions         公式配置
     */
    public FormulaEvaluators(String variablePrefix, Map<String, String> formulaProperties
            , List<Operator> operators, List<Function> functions) {
        formulaEvaluatorMap = new LinkedHashMap<>();
        calculationOrders = new LinkedHashSet<>();
        initCalculationOrders(variablePrefix, formulaProperties, operators, functions);
    }



    /**
     * 获取公式map
     *
     * @return 公式map
     */
    public Map<String, String> getFormulaMap() {
        Map<String, String> formulaMap = new LinkedHashMap<>(formulaEvaluatorMap.size());
        formulaEvaluatorMap.forEach((formulaKey, evaluator)
                -> formulaMap.put(formulaKey, evaluator.getFormulaEntity().getFormulaPart()));
        return Collections.unmodifiableMap(formulaMap);
    }


    /**
     * 对每一行记录进行处理，包括如下步骤
     * a)公式解析
     * b)变量赋值
     * c)计算
     * d)存储计算结果
     *
     * @param variable 变量
     */
    public void evaluateAll(IVariable variable) {
        for (String formulaKey : calculationOrders) {
            FormulaEvaluator evaluator = formulaEvaluatorMap.get(formulaKey);

            try {
                variable.setValue(formulaKey, evaluator.evaluate(variable));
            } catch (Exception e) {
                variable.setValue(formulaKey, (BigDecimal) null);
                variable.setError(formulaKey, new ErrorEntity(e));
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Error to evaluate {}.", formulaKey, e);
                }
            }

        }
    }

    /**
     * 初始化计算顺序
     */
    private void initCalculationOrders(String variablePrefix, Map<String, String> formulaProperties
            , List<Operator> operators, List<Function> functions) {
        if (null != formulaProperties && !formulaProperties.isEmpty()) {

            formulaProperties.forEach((formulaKey, formulaPart) -> {

                FormulaEvaluator evaluator = new FormulaEvaluator(variablePrefix, formulaKey, formulaPart, operators, functions);

                FormulaEntity formulaEntity = evaluator.getFormulaEntity();
                formulaEvaluatorMap.put(formulaEntity.getFormulaKey(), evaluator);

            });

            Set<String> orders = new LinkedHashSet<>();
            formulaEvaluatorMap.forEach((formulaKey, evaluator)
                    -> initCalculationOrdersRecursive(formulaKey, formulaKey, orders));
            calculationOrders.addAll(orders);
        }
    }

    /**
     * 递归初始化公式
     *
     * @param formulaKey  公式key
     * @param variable    变量名
     * @param existedKeys 排序字段
     */
    private void initCalculationOrdersRecursive(String formulaKey, String variable, Set<String> existedKeys) {

        if (!formulaEvaluatorMap.containsKey(variable) || existedKeys.contains(variable)) {
            return;
        }

        FormulaEvaluator evaluator = formulaEvaluatorMap.get(variable);
        Set<String> subVariables = evaluator.getFormulaEntity().getVariableNames();
        subVariables.forEach(subVariable -> {
            // 存在环
            if (subVariable.equals(formulaKey)) {
                throw new CircularException(formulaKey);
            }

            initCalculationOrdersRecursive(formulaKey, subVariable, existedKeys);
        });
        existedKeys.add(variable);
    }

    /**
     * 获取计算器
     *
     * @param formulaKey 公司key
     * @return 计算器
     */
    public FormulaEvaluator getEvaluator(String formulaKey) {
        return formulaEvaluatorMap.get(formulaKey);
    }

    /**
     * 获取计算顺序
     *
     * @return 计算顺序
     */
    public List<String> getCalculationOrders() {
        return Collections.unmodifiableList(new ArrayList<>(calculationOrders));
    }

    /**
     * 列举所有公式实体
     *
     * @return 公式实体列表
     */
    public List<FormulaEntity> listAllFormulaEntities() {
        List<FormulaEntity> formulaEntities = new ArrayList<>();
        formulaEvaluatorMap.forEach((formulaKey, formulaEvaluator) -> {
            formulaEntities.add(formulaEvaluator.getFormulaEntity());
        });
        return formulaEntities;
    }
}
