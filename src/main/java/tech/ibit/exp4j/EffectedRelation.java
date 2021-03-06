package tech.ibit.exp4j;


/**
 * 关系影响
 *
 * @author iBit程序猿
 */
public class EffectedRelation {

    /**
     * 节点名称
     */
    private final String nodeName;

    /**
     * 受影响节点名称
     */
    private final String effectedNodeName;

    public EffectedRelation(String nodeName) {
        this(nodeName, null);
    }

    public EffectedRelation(String nodeName, String effectedNodeName) {
        this.nodeName = nodeName;
        this.effectedNodeName = effectedNodeName;
    }

    /**
     * 判断是否没有影响其他节点
     *
     * @return 判断结果
     */
    public boolean isUnaffected() {
        return null == effectedNodeName;
    }

    @Override
    public String toString() {
        return nodeName + " -> " + (isUnaffected() ? "!NONE" : effectedNodeName);
    }

    /**
     * Gets the value of nodeName
     *
     * @return the value of nodeName
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * Gets the value of nodeNameEffected
     *
     * @return the value of nodeNameEffected
     */
    public String getEffectedNodeName() {
        return effectedNodeName;
    }
}
