package tech.ibit.exp4j;

import tech.ibit.exp4j.utils.MathFunctionEnhanceUtils;
import tech.ibit.exp4j.utils.OperatorEnhanceUtils;
import tech.ibit.exp4j.valuebean.IVariable;
import tech.ibit.exp4j.valuebean.impl.ResultMapBean;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * 公式计算TestCase
 *
 * @author iBit程序猿
 *
 */
public class FormulaEvaluatorTest {

    // 自定义变量前缀
    @Test
    public void evaluate1() {
        IVariable variable = ResultMapBean.getInstance(4, 2, BigDecimal.ZERO);
        variable.setValue("a", BigDecimal.ZERO);
        variable.setValue("b", BigDecimal.TEN);

        FormulaEvaluator evaluator = new FormulaEvaluator("#c", "#a + #b");

        assertEquals(BigDecimal.TEN.doubleValue(), evaluator.evaluate(variable), 0);
    }

    @Test
    public void evaluate2() {
        FormulaEvaluator evaluator = new FormulaEvaluator("#c", "#a + #b");
        assertNull(evaluator.evaluate(null));
    }

    @Test
    public void evaluate3() {
        FormulaEvaluator evaluator = new FormulaEvaluator("#c", "10");
        assertEquals(BigDecimal.TEN.doubleValue(), evaluator.evaluate(null), 0);
    }

    @Test
    public void evaluate4() {
        FormulaEvaluator evaluator = new FormulaEvaluator("!#c", "#a + #b");
        assertEquals(BigDecimal.ZERO.doubleValue(), evaluator.evaluate(null), 0);

        evaluator = new FormulaEvaluator("!#c", "#a + #b");
        IVariable variable = ResultMapBean.getInstance(4, 2, BigDecimal.ZERO);
        variable.setValue("a", BigDecimal.TEN);
        assertEquals(BigDecimal.TEN.doubleValue(), evaluator.evaluate(variable), 0);

    }


    @Test
    public void evaluate5() {
        FormulaEvaluator evaluator = new FormulaEvaluator("N!#c", "#a + #b");
        assertNull(evaluator.evaluate(null));

        evaluator = new FormulaEvaluator("N!#c", "#a + #b");
        IVariable variable = ResultMapBean.getInstance(4, 2, BigDecimal.ZERO);
        variable.setValue("a", BigDecimal.TEN);
        assertNull(evaluator.evaluate(null));
    }




    // 默认变量前缀
    @Test
    public void evaluate6() {
        IVariable variable = ResultMapBean.getInstance(4, 2, BigDecimal.ZERO);
        variable.setValue("a", BigDecimal.ZERO);
        variable.setValue("b", BigDecimal.TEN);

        FormulaEvaluator evaluator = new FormulaEvaluator("__v_", "__v_c", "__v_a + __v_b");

        assertEquals(BigDecimal.TEN.doubleValue(), evaluator.evaluate(variable), 0);
    }


    // 自定义符号
    @Test(expected = ArithmeticException.class)
    public void evaluate7() {
        IVariable variable = ResultMapBean.getInstance(4, 2, BigDecimal.ZERO);
        variable.setValue("a", BigDecimal.TEN);
        variable.setValue("b", BigDecimal.ZERO);


        FormulaEvaluator evaluator = new FormulaEvaluator("#", "#c", "#a / #b$"
                , OperatorEnhanceUtils.getOperators(), null);

        evaluator.evaluate(variable);
    }

    //
    @Test
    public void evaluate8() {
        IVariable variable = ResultMapBean.getInstance(4, 2, BigDecimal.ZERO);
        variable.setValue("a", BigDecimal.TEN);
        variable.setValue("b", BigDecimal.TEN);

        FormulaEvaluator evaluator = new FormulaEvaluator("#", "#c", "#a / #b$"
                , OperatorEnhanceUtils.getOperators(), null);

        assertEquals(BigDecimal.ONE.doubleValue(), evaluator.evaluate(variable), 0);
    }


    @Test
    public void evaluate9() {
        IVariable variable = ResultMapBean.getInstance(4, 2, BigDecimal.ZERO);
        variable.setValue("a", BigDecimal.TEN);
        variable.setValue("b", BigDecimal.ONE);


        FormulaEvaluator evaluator = new FormulaEvaluator("#", "#c", "min(#a, #b$)"
                , OperatorEnhanceUtils.getOperators(), MathFunctionEnhanceUtils.getFunctions());

        assertEquals(BigDecimal.ONE.doubleValue(), evaluator.evaluate(variable), 0);

        evaluator = new FormulaEvaluator("#", "#c", "abs(#b)"
                , OperatorEnhanceUtils.getOperators(), MathFunctionEnhanceUtils.getFunctions());

        assertEquals(BigDecimal.ONE.doubleValue(), evaluator.evaluate(variable), 0);
    }

    @Test
    public void evaluate10() {
        IVariable variable = ResultMapBean.getInstance(4, 2, BigDecimal.ZERO);
        variable.setValue("a", BigDecimal.TEN);
        variable.setValue("b", BigDecimal.ONE);

        FormulaEvaluator evaluator = new FormulaEvaluator( "#c", "#a>#b"
                , OperatorEnhanceUtils.getOperators(), null);
        assertEquals(BigDecimal.ONE.doubleValue(), evaluator.evaluate(variable), 0);

        evaluator = new FormulaEvaluator( "#c", "#a>=#b"
                , OperatorEnhanceUtils.getOperators(), null);
        assertEquals(BigDecimal.ONE.doubleValue(), evaluator.evaluate(variable), 0);

        evaluator = new FormulaEvaluator( "#c", "#a==#b"
                , OperatorEnhanceUtils.getOperators(), null);
        assertEquals(BigDecimal.ZERO.doubleValue(), evaluator.evaluate(variable), 0);

        evaluator = new FormulaEvaluator( "#c", "#a<#b"
                , OperatorEnhanceUtils.getOperators(), null);
        assertEquals(BigDecimal.ZERO.doubleValue(), evaluator.evaluate(variable), 0);

        evaluator = new FormulaEvaluator( "#c", "#a<=#b"
                , OperatorEnhanceUtils.getOperators(), null);
        assertEquals(BigDecimal.ZERO.doubleValue(), evaluator.evaluate(variable), 0);

        variable.setValue("a", (Double) null);
        evaluator = new FormulaEvaluator( "#c", "#a<=#b"
                , OperatorEnhanceUtils.getOperators(), null);
        assertEquals(BigDecimal.ONE.doubleValue(), evaluator.evaluate(variable), 0);

        evaluator = new FormulaEvaluator( "N!#c", "#a<=#b"
                , OperatorEnhanceUtils.getOperators(), null);
        assertNull(evaluator.evaluate(variable));
    }



}