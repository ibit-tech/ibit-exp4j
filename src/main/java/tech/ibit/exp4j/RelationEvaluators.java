package tech.ibit.exp4j;

import tech.ibit.exp4j.exception.CircularException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 关系计算器
 *
 * @author iBit程序猿
 */
public class RelationEvaluators {

    /**
     * 关系节点Map
     */
    private Map<String, RelationEntity> relationNodeMap;

    /**
     * 构造函数
     *
     * @param formulaEntities 公式实体
     */
    public RelationEvaluators(FormulaEntity... formulaEntities) {
        initRelationNodeMapByFormulaEntities(formulaEntities);
    }

    /**
     * 构造函数
     *
     * @param relationEffecteds 关系对
     */
    public RelationEvaluators(RelationEffected... relationEffecteds) {
        initRelationNodeMapByRelationPairs(relationEffecteds);
    }


    /**
     * 列举关系对
     *
     * @return 关系对列表
     */
    public List<RelationEffected> getRelationPairs() {
        List<RelationEffected> relationEffecteds = new ArrayList<>();
        relationNodeMap.forEach((nodeName, node) -> {
            Set<String> childNames = node.getChildrenNodeNames();
            if (childNames.isEmpty()) {
                relationEffecteds.add(new RelationEffected(nodeName));
            } else {
                childNames.forEach(childName -> relationEffecteds.add(new RelationEffected(nodeName, childName)));
            }
        });
        return relationEffecteds;
    }

    /**
     * 获取受影响的节点名称
     *
     * @param nodeName 节点名称
     * @return 受影响的节点名称列表
     */
    public Set<String> getNodeNamesEffected(String nodeName) {
        RelationEntity relationEntity = relationNodeMap.get(nodeName);
        return null == relationEntity
                ? Collections.emptySet()
                : relationEntity.getChildrenNodeNamesRecursive();
    }

    /**
     * 获取关系节点
     *
     * @param nodeName 节点名称
     * @return 关系节点
     */
    public RelationEntity getRelationNode(String nodeName) {
        return relationNodeMap.get(nodeName);
    }


    /**
     * 初始化关系节点map
     *
     * @param formulaEntities 公式实体
     */
    private void initRelationNodeMapByFormulaEntities(FormulaEntity[] formulaEntities) {
        relationNodeMap = new LinkedHashMap<>();

        if (null != formulaEntities) {
            for (FormulaEntity formulaEntity : formulaEntities) {
                String formulaKey = formulaEntity.getFormulaKey();
                if (!relationNodeMap.containsKey(formulaKey)) {
                    relationNodeMap.put(formulaKey, new RelationEntity(formulaKey));
                }

                // 等式左边作为子节点
                RelationEntity formulaKeyNode = relationNodeMap.get(formulaKey);
                Set<String> formulaKeyNodeChildrenNames = formulaKeyNode.getChildrenNodeNamesRecursive();

                Set<String> variables = formulaEntity.getVariableNames();
                variables.forEach(variable -> addVariableNode(formulaKeyNode, formulaKeyNodeChildrenNames, variable));
            }
        }
    }


    /**
     * 初始化关系节点map
     *
     * @param relationEffecteds 公式实体
     */
    private void initRelationNodeMapByRelationPairs(RelationEffected[] relationEffecteds) {
        relationNodeMap = new LinkedHashMap<>();

        if (null != relationEffecteds) {
            for (RelationEffected relationEffected : relationEffecteds) {
                // nodeName相当于变量， nodeNameEffected相当于等式左边
                String variable = relationEffected.getNodeName();

                if (relationEffected.isUnaffected()) {
                    if (!relationNodeMap.containsKey(variable)) {
                        relationNodeMap.put(variable, new RelationEntity(variable));
                    }
                    continue;
                }

                String formulaKey = relationEffected.getNodeNameEffected();
                if (!relationNodeMap.containsKey(formulaKey)) {
                    relationNodeMap.put(formulaKey, new RelationEntity(formulaKey));
                }

                // 等式左边作为子节点
                RelationEntity formulaKeyNode = relationNodeMap.get(formulaKey);
                Set<String> formulaKeyNodeChildrenNames = formulaKeyNode.getChildrenNodeNamesRecursive();

                addVariableNode(formulaKeyNode, formulaKeyNodeChildrenNames, variable);
            }
        }
    }

    /**
     * 增加变量节点
     *
     * @param formulaKeyNode              公式节点
     * @param formulaKeyNodeChildrenNames 公式节点关联子节点名称
     * @param variable                    变量名
     */
    private void addVariableNode(RelationEntity formulaKeyNode
            , Set<String> formulaKeyNodeChildrenNames, String variable) {

        String formulaKey = formulaKeyNode.getNodeName();

        // 形成环
        if (formulaKeyNodeChildrenNames.contains(variable)) {
            throw new CircularException(variable, formulaKeyNode.getFullPaths(variable));
        }

        if (!relationNodeMap.containsKey(variable)) {
            RelationEntity variableNode = new RelationEntity(variable);
            variableNode.addChild(formulaKeyNode);

            relationNodeMap.put(variable, variableNode);
        } else {
            RelationEntity variableNode = relationNodeMap.get(variable);
            if (!variableNode.containsChild(formulaKey)) {

                // 判断是否存在父节点路径中，是则无需添加
                Set<String> variableNodeChildrenNames = variableNode.getChildrenNodeNamesRecursive();
                if (variableNodeChildrenNames.contains(formulaKey)) {
                    return;
                }

                // 如果子节点路径中包含父节点的子节点，则父节点的子节点删除
                Set<String> removeNodeNames = formulaKeyNodeChildrenNames.isEmpty()
                        ? Collections.emptySet()
                        : variableNodeChildrenNames.stream().filter(formulaKeyNodeChildrenNames::contains).collect(Collectors.toSet());
                removeNodeNames.forEach(variableNode::removeChild);

                variableNode.addChild(formulaKeyNode);
            }

        }
    }
}
