package tech.ibit.exp4j;

import tech.ibit.exp4j.utils.MathFunctionEnhanceUtils;
import tech.ibit.exp4j.utils.OperatorEnhanceUtils;
import tech.ibit.exp4j.valuebean.IVariable;
import tech.ibit.exp4j.valuebean.impl.ObjectBean;
import tech.ibit.exp4j.valuebean.impl.ResultMapBean;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 计算工具类TestCase
 *
 * @author iBit程序猿
 *
 */
public class FormulaEvaluatorsTest {

    @Test
    public void evaluateAll() {

        Map<String, String> originalFormulaMap = new LinkedHashMap<String, String>() {{
            put("#D", "#E+#F");
            put("#A", "#B/#C");
            put("#B", "#C-#D");
            put("#G", "1+2");
        }};

        System.out.println("配置公式：" + originalFormulaMap);

        FormulaEvaluators formulaEvaluators = new FormulaEvaluators("#", originalFormulaMap);
        Map<String, String> formulaMap = formulaEvaluators.getFormulaMap();
        System.out.println("转换公式：" + formulaMap);

        IVariable variable = ResultMapBean.getInstance(4, 2, BigDecimal.ZERO);
        variable.setValue("C", 5.8);
        variable.setValue("E", 2.2);
        //variable.setValue("F", 1.0);
        System.out.println("参数：" + variable);

        formulaEvaluators.evaluateAll(variable);
        System.out.println("结果：" + variable);

    }


    @Test
    public void evaluateAll2() {

        // BeanUtils 实现有点傻，首字母不能是大写
        Map<String, String> originalFormulaMap = new LinkedHashMap<String, String>() {{
            put("#d", "#e+#f");
            put("#a", "#b/#c");
            put("#b", "#c-#d");
            put("#g", "1+2");
        }};

        System.out.println("配置公式：" + originalFormulaMap);

        FormulaEvaluators formulaEvaluators = new FormulaEvaluators("#", originalFormulaMap);
        Map<String, String> formulaMap = formulaEvaluators.getFormulaMap();
        System.out.println("转换公式：" + formulaMap);


        IVariable variable = ObjectBean.getInstance(new Param(), 4, 2, BigDecimal.ZERO);
        variable.setValue("c", 5.8);
        variable.setValue("e", 2.2);
        //variable.setValue("F", 1.0);
        System.out.println("参数：" + variable);

        formulaEvaluators.evaluateAll(variable);
        System.out.println("结果：" + variable);
        System.out.println();


    }


    @Test
    public void evaluateAll3() {

        Map<String, String> originalFormulaMap = new LinkedHashMap<String, String>() {{
            put("#D", "#E+#F");
            put("#A", "#B/#C$");
            put("#B", "#C-#D");
            put("#G", "min(1+2, 5)");
        }};

        System.out.println("配置公式：" + originalFormulaMap);

        FormulaEvaluators formulaEvaluators = new FormulaEvaluators("#", originalFormulaMap

                , Collections.singletonList(OperatorEnhanceUtils.reciprocal())
                , Collections.singletonList(MathFunctionEnhanceUtils.getMinFunction()));
        Map<String, String> formulaMap = formulaEvaluators.getFormulaMap();
        System.out.println("转换公式：" + formulaMap);

        IVariable variable = ResultMapBean.getInstance(4, 2, BigDecimal.ZERO);
        variable.setValue("C", 5.8);
        variable.setValue("E", 2.2);
        //variable.setValue("F", 1.0);
        System.out.println("参数：" + variable);

        formulaEvaluators.evaluateAll(variable);
        System.out.println("结果：" + variable);

    }


    @Test
    public void evaluateAll4() {

        Map<String, String> originalFormulaMap = getOriginalFormulaMap();

        System.out.println("配置公式：" + originalFormulaMap);

        FormulaEvaluators formulaEvaluators = new FormulaEvaluators("#", originalFormulaMap
                , Collections.singletonList(OperatorEnhanceUtils.reciprocal())
                , Collections.singletonList(MathFunctionEnhanceUtils.getMinFunction()));
        Map<String, String> formulaMap = formulaEvaluators.getFormulaMap();
        System.out.println("转换公式：" + formulaMap);

        IVariable variable = ResultMapBean.getInstance(4, 2, BigDecimal.ZERO);
        variable.setValue("C", 0.0);
        variable.setValue("E", 0.0);
        //variable.setValue("F", 1.0);
        System.out.println("参数：" + variable);

        formulaEvaluators.evaluateAll(variable);
        System.out.println("结果：" + variable);

        System.out.println("计算错误: " + variable.getError("A"));

    }

    @Test
    public void evaluateAll5() {

        Map<String, String> originalFormulaMap = new LinkedHashMap<String, String>() {{
            put("#D", "#E+#F");
            put("#A", "#B/#C");
            put("#B", "#C-#D");
            put("#G", "min(1+2, 5)");
            put("#H", "0//1");
            put("#I", "#C>#B");
            put("#J", "1>1");
            put("#K", "1>=1");
            put("#L", "1==1");
            put("#M", "1<1");
            put("#N", "1<=1");
        }};

        System.out.println("配置公式：" + originalFormulaMap);

        FormulaEvaluators formulaEvaluators = new FormulaEvaluators("#", originalFormulaMap
                , OperatorEnhanceUtils.getOperators()
                , Collections.singletonList(MathFunctionEnhanceUtils.getMinFunction()));
        Map<String, String> formulaMap = formulaEvaluators.getFormulaMap();
        System.out.println("转换公式：" + formulaMap);

        IVariable variable = ResultMapBean.getInstance(4, 2, BigDecimal.ZERO);
        variable.setValue("C", 5.8);
        variable.setValue("E", 2.2);
        //variable.setValue("F", 1.0);
        System.out.println("参数：" + variable);

        formulaEvaluators.evaluateAll(variable);
        System.out.println("结果：" + variable);

    }

    private Map<String, String> getOriginalFormulaMap() {
        return new LinkedHashMap<String, String>() {{
            put("#D", "#E+#F");
            put("#A", "#B/#C$");
            put("#B", "#C-#D");
            put("#G", "min(1+2, 5)");
        }};
    }

}