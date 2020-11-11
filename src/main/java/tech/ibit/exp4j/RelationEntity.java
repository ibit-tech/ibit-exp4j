package tech.ibit.exp4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 关系节点
 *
 * @author iBit程序猿
 */
public class RelationEntity {

    /**
     * 节点名称
     */
    private final String nodeName;

    /**
     * 子节点
     */
    private final Map<String, RelationEntity> childrenMap;

    /**
     * 构造函数
     *
     * @param nodeName 节点名称
     */
    public RelationEntity(String nodeName) {
        this.nodeName = nodeName;
        childrenMap = new LinkedHashMap<>();
    }

    /**
     * 增加子节点
     *
     * @param child child
     */
    public void addChild(RelationEntity child) {
        if (!childrenMap.containsKey(child.getNodeName())) {
            childrenMap.put(child.getNodeName(), child);
        }
    }

    /**
     * 删除子节点
     *
     * @param nodeName 节点名称
     */
    public void removeChild(String nodeName) {
        childrenMap.remove(nodeName);
    }

    /**
     * 判断是否包含子节点
     *
     * @param childNodeName 子节点名称
     * @return 是否包含子节点
     */
    public boolean containsChild(String childNodeName) {
        return childrenMap.containsKey(childNodeName);
    }

    /**
     * 获取节点名称
     *
     * @return 节点名称
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * 获取子节点
     *
     * @return 子节点
     */
    public List<RelationEntity> getChildren() {
        return childrenMap.isEmpty()
                ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<>(childrenMap.values()));
    }

    /**
     * 递归获取子节点名称
     *
     * @return 子节点名称
     */
    public Set<String> getChildrenNodeNamesRecursive() {
        Set<String> childNodeNames = new LinkedHashSet<>();
        addChildNodeNamesRecursive(this, childNodeNames);
        return childNodeNames;
    }

    /**
     * 获取子节点名称
     *
     * @return 子节点名称
     */
    public Set<String> getChildrenNodeNames() {
        return getChildren().stream().map(RelationEntity::getNodeName).collect(Collectors.toCollection(LinkedHashSet::new));
    }


    /**
     * 查找路径
     *
     * @param childNodeName 子节点名称
     * @return 子节点路径
     */
    public List<String> getPaths(String childNodeName) {
        return findPathsRecursive(childNodeName, getChildren(), Collections.emptyList());
    }


    /**
     * 查找全路径
     *
     * @param childNodeName 子节点名称
     * @return 全路径
     */
    public List<String> getFullPaths(String childNodeName) {
        List<String> paths = getPaths(childNodeName);
        if (paths.isEmpty()) {
            return paths;
        }
        return new ArrayList<String>() {{
            add(nodeName);
            addAll(paths);
        }};
    }


    /**
     * 查找子节点路径
     *
     * @param childNodeName   查询的子节点名称
     * @param children        当前子节点
     * @param parentNodeNames 父节点名称列表
     * @return
     */
    private List<String> findPathsRecursive(String childNodeName, List<RelationEntity> children, List<String> parentNodeNames) {
        if (children.isEmpty()) {
            return Collections.emptyList();
        }

        // 广度优先
        for (RelationEntity child : children) {
            if (child.getNodeName().equals(childNodeName)) {
                List<String> result = new ArrayList<>(parentNodeNames);
                result.add(childNodeName);
                return result;
            }
        }

        // 遍历子节点
        for (RelationEntity child : children) {
            parentNodeNames = new ArrayList<>(parentNodeNames);
            parentNodeNames.add(child.getNodeName());
            List<String> result = findPathsRecursive(childNodeName, child.getChildren(), parentNodeNames);
            if (!result.isEmpty()) {
                return result;
            }
        }

        return Collections.emptyList();

    }

    /**
     * 递归增加子节点名称
     *
     * @param node           子节点
     * @param childNodeNames 子节点名称
     */
    private void addChildNodeNamesRecursive(RelationEntity node, Set<String> childNodeNames) {
        List<RelationEntity> children = node.getChildren();
        if (!children.isEmpty()) {
            children.forEach(child -> {
                childNodeNames.add(child.getNodeName());
                addChildNodeNamesRecursive(child, childNodeNames);
            });
        }

    }
}
