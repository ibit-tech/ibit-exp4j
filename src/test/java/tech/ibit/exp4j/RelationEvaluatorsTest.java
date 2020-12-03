package tech.ibit.exp4j;

import org.junit.Assert;
import org.junit.Test;
import tech.ibit.exp4j.exception.CircularException;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

/**
 * 关系工具类
 *
 * @author iBit程序猿
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
                new EffectedRelation("F", "E"),
                new EffectedRelation("H", "E"),
                new EffectedRelation("E", "D"),
                new EffectedRelation("C", "B"),
                new EffectedRelation("D", "B"),
                new EffectedRelation("B", "A"),
                new EffectedRelation("A"),
                new EffectedRelation("G"));

        Set<String> effectiveNodeNames = relationEvaluators.getEffectedNodeNames("H");

        // 影响关系链
        Assert.assertArrayEquals(new String[]{"E", "D", "B", "A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getEffectedNodeNames("A");
        assertTrue(effectiveNodeNames.isEmpty());

        effectiveNodeNames = relationEvaluators.getEffectedNodeNames("C");
        Assert.assertArrayEquals(new String[]{"B", "A"}, effectiveNodeNames.toArray(new String[0]));
    }


    @Test
    public void testGetRelationPairs2() {

        List<FormulaEntity> formulaEntities = getFormulaEntities2();


        RelationEvaluators relationEvaluators = new RelationEvaluators(formulaEntities.toArray(new FormulaEntity[0]));
        containsAll(relationEvaluators.getRelationPairs(), 7,
                new EffectedRelation("A"),
                new EffectedRelation("F", "B"),
                new EffectedRelation("G", "B"),
                new EffectedRelation("B", "E"),
                new EffectedRelation("D", "C"),
                new EffectedRelation("E", "C"),
                new EffectedRelation("C", "A"));

        Set<String> effectiveNodeNames = relationEvaluators.getEffectedNodeNames("G");
        Assert.assertArrayEquals(new String[]{"B", "E", "C", "A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getEffectedNodeNames("F");
        Assert.assertArrayEquals(new String[]{"B", "E", "C", "A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getEffectedNodeNames("D");
        Assert.assertArrayEquals(new String[]{"C", "A"}, effectiveNodeNames.toArray(new String[0]));


        relationEvaluators = new RelationEvaluators(relationEvaluators.getRelationPairs().toArray(new EffectedRelation[0]));
        containsAll(relationEvaluators.getRelationPairs(), 7,
                new EffectedRelation("A"),
                new EffectedRelation("F", "B"),
                new EffectedRelation("G", "B"),
                new EffectedRelation("B", "E"),
                new EffectedRelation("D", "C"),
                new EffectedRelation("E", "C"),
                new EffectedRelation("C", "A"));

        effectiveNodeNames = relationEvaluators.getEffectedNodeNames("G");
        Assert.assertArrayEquals(new String[]{"B", "E", "C", "A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getEffectedNodeNames("F");
        Assert.assertArrayEquals(new String[]{"B", "E", "C", "A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getEffectedNodeNames("D");
        Assert.assertArrayEquals(new String[]{"C", "A"}, effectiveNodeNames.toArray(new String[0]));
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
                new EffectedRelation("A"),
                new EffectedRelation("B", "A"),
                new EffectedRelation("C", "A"),
                new EffectedRelation("E", "B"),
                new EffectedRelation("D", "B"),
                new EffectedRelation("D", "C"),
                new EffectedRelation("F", "C"));

        Set<String> effectiveNodeNames = relationEvaluators.getEffectedNodeNames("D");
        Assert.assertArrayEquals(new String[]{"B", "A", "C"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getEffectedNodeNames("F");
        Assert.assertArrayEquals(new String[]{"C", "A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getEffectedNodeNames("E");
        Assert.assertArrayEquals(new String[]{"B", "A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getEffectedNodeNames("C");
        Assert.assertArrayEquals(new String[]{"A"}, effectiveNodeNames.toArray(new String[0]));


        effectiveNodeNames = relationEvaluators.getEffectedNodeNames("B");
        Assert.assertArrayEquals(new String[]{"A"}, effectiveNodeNames.toArray(new String[0]));


        relationEvaluators = new RelationEvaluators(relationEvaluators.getRelationPairs().toArray(new EffectedRelation[0]));
        containsAll(relationEvaluators.getRelationPairs(), 7,
                new EffectedRelation("A"),
                new EffectedRelation("B", "A"),
                new EffectedRelation("C", "A"),
                new EffectedRelation("E", "B"),
                new EffectedRelation("D", "B"),
                new EffectedRelation("D", "C"),
                new EffectedRelation("F", "C"));

        effectiveNodeNames = relationEvaluators.getEffectedNodeNames("D");
        Assert.assertArrayEquals(new String[]{"B", "A", "C"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getEffectedNodeNames("F");
        Assert.assertArrayEquals(new String[]{"C", "A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getEffectedNodeNames("E");
        Assert.assertArrayEquals(new String[]{"B", "A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getEffectedNodeNames("C");
        Assert.assertArrayEquals(new String[]{"A"}, effectiveNodeNames.toArray(new String[0]));

        effectiveNodeNames = relationEvaluators.getEffectedNodeNames("B");
        Assert.assertArrayEquals(new String[]{"A"}, effectiveNodeNames.toArray(new String[0]));
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
        new RelationEvaluators(new EffectedRelation("A", "B"), new EffectedRelation("B", "A"));
    }

    @Test
    public void testFindPaths() {
        List<FormulaEntity> formulaEntities = getFormulaEntities2();
        RelationEvaluators relationEvaluators = new RelationEvaluators(formulaEntities.toArray(new FormulaEntity[0]));

        // 获取从 B -> A 的全路径
        List<String> fullPaths = relationEvaluators.getRelationNode("B").getFullPaths("A");
        Assert.assertArrayEquals(new String[]{"B", "E", "C", "A"}, fullPaths.toArray(new String[0]));

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

    private void containsAll(List<EffectedRelation> actualEffectedRelations, int size, EffectedRelation... expectedEffectedRelations) {
        Set<String> actualRelationPairStrSet = actualEffectedRelations
                .stream().map(EffectedRelation::toString).collect(Collectors.toSet());
        Assert.assertEquals(size, actualEffectedRelations.size());
        for (EffectedRelation expectedEffectedRelation : expectedEffectedRelations) {
            assertTrue(actualRelationPairStrSet.contains(expectedEffectedRelation.toString()));
        }
    }
}