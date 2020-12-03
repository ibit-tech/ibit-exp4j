package tech.ibit.exp4j;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

/**
 * 公式实体TestCase
 *
 * @author iBit程序猿
 */
public class FormulaEntityTest {

    @Test
    public void test() {
        FormulaEntity formulaEntity = new FormulaEntity("#x", "#y+#z");
        Assert.assertEquals("x", formulaEntity.getFormulaKey());
        Assert.assertEquals("y+z", formulaEntity.getFormulaPart());

        Set<String> variableNames = formulaEntity.getVariableNames();
        Assert.assertEquals(2, variableNames.size());
        Assert.assertTrue(variableNames.contains("y"));
        Assert.assertTrue(variableNames.contains("z"));


        formulaEntity = new FormulaEntity("~", "~x", "~y + ~z");
        Assert.assertEquals("x", formulaEntity.getFormulaKey());
        Assert.assertEquals("y+z", formulaEntity.getFormulaPart());

        variableNames = formulaEntity.getVariableNames();
        Assert.assertEquals(2, variableNames.size());
        Assert.assertTrue(variableNames.contains("y"));
        Assert.assertTrue(variableNames.contains("z"));

        formulaEntity = new FormulaEntity("#ic33",
                "#ic38  / __preMonth_c38(1) - \u00A01");
        System.out.println(formulaEntity.getFormulaPart());
        System.out.println(formulaEntity.getVariableNames());

        System.out.println((int) "\u00A0".charAt(0));


    }

}