package tech.ibit.exp4j;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import tech.ibit.exp4j.exception.CircularException;
import tech.ibit.exp4j.utils.MathFunctionEnhanceUtils;
import tech.ibit.exp4j.utils.OperatorEnhanceUtils;
import tech.ibit.exp4j.valuebean.IVariable;
import tech.ibit.exp4j.valuebean.impl.ObjectBean;
import tech.ibit.exp4j.valuebean.impl.ResultMapBean;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertNull;

/**
 * 计算工具类TestCase
 *
 * @author iBit程序猿
 */
public class FormulaEvaluatorsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void evaluateAll() {

        Map<String, String> originalFormulaMap = new LinkedHashMap<String, String>() {{
            put("#D", "#E+#F");
            put("#A", "#B/#C");
            put("#B", "#C-#D");
            put("#G", "1+2");
        }};

        FormulaEvaluators formulaEvaluators = new FormulaEvaluators("#", originalFormulaMap);
        Map<String, String> formulaMap = formulaEvaluators.getFormulaMap();
        Assert.assertEquals(
                new HashMap<String, String>() {{
                    put("D", "E+F");
                    put("A", "B/C");
                    put("B", "C-D");
                    put("G", "1+2");
                }},
                formulaMap
        );

        IVariable variable = ResultMapBean.getInstance(4, 2, BigDecimal.ZERO);
        variable.setValue("C", 5.8);
        variable.setValue("E", 2.2);
        variable.setValue("F", 1.0);

        assertEquals(variable, "C", 5.8, "E", 2.2, "F", 1.0);

        formulaEvaluators.evaluateAll(variable);
        assertEquals(variable,"A", 0.4483, "B", 2.6, "C", 5.8, "D", 3.2, "E", 2.2, "F", 1.0, "G", 3.0);

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

        FormulaEvaluators formulaEvaluators = new FormulaEvaluators("#", originalFormulaMap);
        Map<String, String> formulaMap = formulaEvaluators.getFormulaMap();

        Assert.assertEquals(
                new HashMap<String, String>() {{
                    put("d", "e+f");
                    put("a", "b/c");
                    put("b", "c-d");
                    put("g", "1+2");
                }},
                formulaMap
        );

        IVariable variable = ObjectBean.getInstance(new Param(), 4, 2, BigDecimal.ZERO);
        variable.setValue("c", 5.8);
        variable.setValue("e", 2.2);
        variable.setValue("f", 1.0);
        assertEquals(variable, "a", null, "b", null, "c", 5.8, "d", null, "e", 2.2, "f", 1.0, "g", null);

        formulaEvaluators.evaluateAll(variable);
        assertEquals(variable, "a", 0.4483, "b", 2.6, "c", 5.8, "d", 3.2, "e", 2.2, "f", 1.0, "g", 3.0);

    }


    @Test
    public void evaluateAll3() {

        Map<String, String> originalFormulaMap = new LinkedHashMap<String, String>() {{
            put("#D", "#E+#F");
            put("#A", "#B/#C$");
            put("#B", "#C-#D");
            put("#G", "min(1+2, 5)");
        }};

        FormulaEvaluators formulaEvaluators = new FormulaEvaluators("#", originalFormulaMap
                , Collections.singletonList(OperatorEnhanceUtils.reciprocal())
                , Collections.singletonList(MathFunctionEnhanceUtils.getMinFunction()));
        Map<String, String> formulaMap = formulaEvaluators.getFormulaMap();

        Assert.assertEquals(
                new HashMap<String, String>() {{
                    put("D", "E+F");
                    put("A", "B/C$");
                    put("B", "C-D");
                    put("G", "min(1+2,5)");
                }},
                formulaMap
        );

        IVariable variable = ResultMapBean.getInstance(4, 2, BigDecimal.ZERO);
        variable.setValue("C", 5.8);
        variable.setValue("E", 2.2);
        variable.setValue("F", 1.0);
        assertEquals(variable, "C", 5.8, "E", 2.2, "F", 1.0);

        formulaEvaluators.evaluateAll(variable);
        assertEquals(variable,"A", 0.4483, "B", 2.6, "C", 5.8, "D", 3.2, "E", 2.2, "F", 1.0, "G", 3.0);

    }


    @Test
    public void evaluateAll4() {

        Map<String, String> originalFormulaMap = getOriginalFormulaMap();

        FormulaEvaluators formulaEvaluators = new FormulaEvaluators("#", originalFormulaMap
                , Collections.singletonList(OperatorEnhanceUtils.reciprocal())
                , Collections.singletonList(MathFunctionEnhanceUtils.getMinFunction()));
        Map<String, String> formulaMap = formulaEvaluators.getFormulaMap();
        Assert.assertEquals(
                new HashMap<String, String>() {{
                    put("D", "E+F");
                    put("A", "B/C$");
                    put("B", "C-D");
                    put("G", "min(1+2,5)");
                }},
                formulaMap
        );

        IVariable variable = ResultMapBean.getInstance(4, 2, BigDecimal.ZERO);
        variable.setValue("C", 0.0);
        variable.setValue("E", 0.0);
        assertEquals(variable, "C", 0.0, "E", 0.0);

        formulaEvaluators.evaluateAll(variable);
        assertEquals(variable, "B", 0.0, "C", 0.0, "D", 0.0, "E", 0.0, "G", 3.0);

        Assert.assertEquals("Division by zero!", variable.getErrorMessage("A"));

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

        FormulaEvaluators formulaEvaluators = new FormulaEvaluators("#", originalFormulaMap
                , OperatorEnhanceUtils.getOperators()
                , Collections.singletonList(MathFunctionEnhanceUtils.getMinFunction()));
        Map<String, String> formulaMap = formulaEvaluators.getFormulaMap();

        Assert.assertEquals(
                new HashMap<String, String>() {{
                    put("D", "E+F");
                    put("A", "B/C");
                    put("B", "C-D");
                    put("G", "min(1+2,5)");
                    put("H", "0//1");
                    put("I", "C>B");
                    put("J", "1>1");
                    put("K", "1>=1");
                    put("L", "1==1");
                    put("M", "1<1");
                    put("N", "1<=1");
                }},
                formulaMap
        );

        IVariable variable = ResultMapBean.getInstance(4, 2, BigDecimal.ZERO);
        variable.setValue("C", 5.8);
        variable.setValue("E", 2.2);
        assertEquals(variable, "C", 5.8, "E", 2.2);

        formulaEvaluators.evaluateAll(variable);
        assertEquals(variable, "A", 0.6207, "B", 3.6, "C", 5.8, "D", 2.2, "E", 2.2,
                "G", 3.0, "H", 0.0, "I", 1.0, "J", 0.0, "K", 1.0, "L", 1.0, "M", 0.0, "N", 1.0);

    }

    @Test
    public void evaluateAll6() {

        // 公式形成环
        Map<String, String> originalFormulaMap = new LinkedHashMap<String, String>() {{
            put("#d", "#e+#f");
            put("#e", "#d/#g");
            put("#g", "1+2");

        }};
        thrown.expect(CircularException.class);
        new FormulaEvaluators("#", originalFormulaMap);

    }

    private void assertEquals(IVariable variable, Object... kvPairs) {
        for (int i = 0; i < kvPairs.length; i += 2) {
            String key = (String) kvPairs[i];
            Double value = (Double) kvPairs[i + 1];

            BigDecimal culValue = variable.getNullableValue(key);
            if (null == culValue) {
                assertNull(value);
            } else {
                Assert.assertEquals(value, culValue.doubleValue(), 0.0001);

            }
        }
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