package tech.ibit.exp4j;

import tech.ibit.exp4j.exception.CircularException;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

/**
 * 关系工具类
 *
 * @author iBit程序猿
 *
 */
public class RelationEvaluatorsTest {

    @Test
    public void testGetRelationPairs() {

        Map<String, String> originalFormulaMap = new LinkedHashMap<String, String>() {{
            put("#A", "#B/#C");
            put("#B", "#C-#D");
            put("#D", "#E+#F");
            put("#E", "#F+#H");
            put("#G", "1+2");
        }};
        List<FormulaEntity> formulaEntities = getFormulaEntities(originalFormulaMap);
        RelationEvaluators relationEvaluators = new RelationEvaluators(formulaEntities.toArray(new FormulaEntity[0]));
        containsAll(relationEvaluators.getRelationPairs(), 8,
                new RelationEffected("F", "E"),
                new RelationEffected("H", "E"),
                new RelationEffected("E", "D"),
                new RelationEffected("C", "B"),
                new RelationEffected("D", "B"),
                new RelationEffected("B", "A"),
                new RelationEffected("A"),
                new RelationEffected("G"));

        Set<String> effectiveNodeNames = relationEvaluators.getNodeNamesEffected("H");

        // 影响关系链
        Assert.assertArrayEquals(new String[] {"E", "D", "B", "A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getNodeNamesEffected("A");
        assertTrue(effectiveNodeNames.isEmpty());

        effectiveNodeNames = relationEvaluators.getNodeNamesEffected("C");
        Assert.assertArrayEquals(new String[] {"B", "A"}, effectiveNodeNames.toArray(new String[0]));
    }


    @Test
    public void testGetRelationPairs2() {

        List<FormulaEntity> formulaEntities = getFormulaEntities2();


        RelationEvaluators relationEvaluators = new RelationEvaluators(formulaEntities.toArray(new FormulaEntity[0]));
        containsAll(relationEvaluators.getRelationPairs(), 7,
                new RelationEffected("A"),
                new RelationEffected("F", "B"),
                new RelationEffected("G", "B"),
                new RelationEffected("B", "E"),
                new RelationEffected("D", "C"),
                new RelationEffected("E", "C"),
                new RelationEffected("C", "A"));

        Set<String> effectiveNodeNames = relationEvaluators.getNodeNamesEffected("G");
        Assert.assertArrayEquals(new String[] {"B", "E", "C", "A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getNodeNamesEffected("F");
        Assert.assertArrayEquals(new String[] {"B", "E", "C", "A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getNodeNamesEffected("D");
        Assert.assertArrayEquals(new String[] {"C", "A"}, effectiveNodeNames.toArray(new String[0]));


        relationEvaluators = new RelationEvaluators(relationEvaluators.getRelationPairs().toArray(new RelationEffected[0]));
        containsAll(relationEvaluators.getRelationPairs(), 7,
                new RelationEffected("A"),
                new RelationEffected("F", "B"),
                new RelationEffected("G", "B"),
                new RelationEffected("B", "E"),
                new RelationEffected("D", "C"),
                new RelationEffected("E", "C"),
                new RelationEffected("C", "A"));

        effectiveNodeNames = relationEvaluators.getNodeNamesEffected("G");
        Assert.assertArrayEquals(new String[] {"B", "E", "C", "A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getNodeNamesEffected("F");
        Assert.assertArrayEquals(new String[] {"B", "E", "C", "A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getNodeNamesEffected("D");
        Assert.assertArrayEquals(new String[] {"C", "A"}, effectiveNodeNames.toArray(new String[0]));
    }


    @Test
    public void testGetRelationPairs3() {

        Map<String, String> originalFormulaMap = new LinkedHashMap<String, String>() {{
            put("#A", "#B+#C");
            put("#B", "#D+#E");
            put("#C", "#D+#F");
        }};

        List<FormulaEntity> formulaEntities = getFormulaEntities(originalFormulaMap);

        RelationEvaluators relationEvaluators = new RelationEvaluators(formulaEntities.toArray(new FormulaEntity[0]));
        containsAll(relationEvaluators.getRelationPairs(), 7,
                new RelationEffected("A"),
                new RelationEffected("B", "A"),
                new RelationEffected("C", "A"),
                new RelationEffected("E", "B"),
                new RelationEffected("D", "B"),
                new RelationEffected("D", "C"),
                new RelationEffected("F", "C"));

        Set<String> effectiveNodeNames = relationEvaluators.getNodeNamesEffected("D");
        Assert.assertArrayEquals(new String[] {"B", "A", "C"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getNodeNamesEffected("F");
        Assert.assertArrayEquals(new String[] {"C", "A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getNodeNamesEffected("E");
        Assert.assertArrayEquals(new String[] {"B", "A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getNodeNamesEffected("C");
        Assert.assertArrayEquals(new String[] {"A"}, effectiveNodeNames.toArray(new String[0]));


        effectiveNodeNames = relationEvaluators.getNodeNamesEffected("B");
        Assert.assertArrayEquals(new String[] {"A"}, effectiveNodeNames.toArray(new String[0]));


        relationEvaluators = new RelationEvaluators(relationEvaluators.getRelationPairs().toArray(new RelationEffected[0]));
        containsAll(relationEvaluators.getRelationPairs(), 7,
                new RelationEffected("A"),
                new RelationEffected("B", "A"),
                new RelationEffected("C", "A"),
                new RelationEffected("E", "B"),
                new RelationEffected("D", "B"),
                new RelationEffected("D", "C"),
                new RelationEffected("F", "C"));

        effectiveNodeNames = relationEvaluators.getNodeNamesEffected("D");
        Assert.assertArrayEquals(new String[] {"B", "A", "C"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getNodeNamesEffected("F");
        Assert.assertArrayEquals(new String[] {"C", "A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getNodeNamesEffected("E");
        Assert.assertArrayEquals(new String[] {"B", "A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getNodeNamesEffected("C");
        Assert.assertArrayEquals(new String[] {"A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getNodeNamesEffected("B");
        Assert.assertArrayEquals(new String[] {"A"}, effectiveNodeNames.toArray(new String[0]));
    }

    @Test(expected = CircularException.class)
    public void testException() {
        Map<String, String> originalFormulaMap = new LinkedHashMap<String, String>() {{
            put("#A", "#B+1");
            put("#B", "#A+1");
        }};
        List<FormulaEntity> formulaEntities = new ArrayList<>();
        originalFormulaMap.forEach((formulaKey, formula) -> formulaEntities.add(new FormulaEntity("#", formulaKey, formula)));
        new RelationEvaluators(formulaEntities.toArray(new FormulaEntity[0]));
    }


    @Test(expected = CircularException.class)
    public void testException2() {
        new RelationEvaluators(new RelationEffected("A", "B"), new RelationEffected("B", "A"));
    }

    @Test
    public void testFindPaths() {
        List<FormulaEntity> formulaEntities = getFormulaEntities2();
        RelationEvaluators relationEvaluators = new RelationEvaluators(formulaEntities.toArray(new FormulaEntity[0]));

        // 获取从 B -> A 的全路径
        List<String> fullPaths = relationEvaluators.getRelationNode("B").getFullPaths("A");
        Assert.assertArrayEquals(new String[] {"B", "E", "C", "A"}, fullPaths.toArray(new String[0]));

        fullPaths = relationEvaluators.getRelationNode("B").getFullPaths("F");
        assertTrue(fullPaths.isEmpty());
    }

    private List<FormulaEntity> getFormulaEntities2() {
        Map<String, String> originalFormulaMap = new LinkedHashMap<String, String>() {{
            put("#A", "#B+#C+E");
            put("#B", "#F+#G");
            put("#C", "#D-#E");
            put("#E", "#B+#F");
            put("#G", "1+2");
        }};
        return getFormulaEntities(originalFormulaMap);
    }

    private List<FormulaEntity> getFormulaEntities(Map<String, String> originalFormulaMap) {
        List<FormulaEntity> formulaEntities = new ArrayList<>();
        originalFormulaMap.forEach((formulaKey, formula) -> formulaEntities.add(new FormulaEntity("#", formulaKey, formula)));
        return formulaEntities;
    }

    private void containsAll(List<RelationEffected> actualRelationEffecteds, int size, RelationEffected... expectedRelationEffecteds) {
        Set<String> actualRelationPairStrSet = actualRelationEffecteds
                .stream().map(RelationEffected::toString).collect(Collectors.toSet());
        Assert.assertEquals(size, actualRelationEffecteds.size());
        for (RelationEffected expectedRelationEffected : expectedRelationEffecteds) {
            assertTrue(actualRelationPairStrSet.contains(expectedRelationEffected.toString()));
        }
    }
}