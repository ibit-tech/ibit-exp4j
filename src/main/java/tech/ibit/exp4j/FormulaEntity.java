package tech.ibit.exp4j;


import org.apache.commons.lang.StringUtils;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公式实体
 *
 * @author iBit程序猿
 */
public class FormulaEntity {

    /**
     * 默认变量前缀，#
     */
    private static final String DEFAULT_VARIABLE_PREFIX = "#";

    /**
     * 默认变量前缀正则
     */
    private static final String DEFAULT_VARIABLE_PREFIX_PATTERN = "([A-Za-z0-9_]+)";

    /**
     * 正则组1
     */
    private static final Integer VARIABLE_GROUP_1 = 1;

    /**
     * 公式key（等号左边部分）
     */
    private final String formulaKey;

    /**
     * 公式部分（等号右边部分）
     */
    private final String formulaPart;


    /**
     * 变量名称
     */
    private Set<String> variableNames;

    /**
     * 变量名称前缀
     */
    private final String variablePrefix;

    /**
     * 变量正则加前缀
     */
    private final String variablePatternWithPrefix;


    /**
     * 构造函数
     *
     * @param formulaKey  公式键（等号左边）
     * @param formulaPart 公式部分（等号右边）
     */
    public FormulaEntity(String formulaKey, String formulaPart) {
        this(null, formulaKey, formulaPart);
    }

    /**
     * 构造函数
     *
     * @param variablePrefix 变量前缀（标识变量）
     * @param formulaKey     公式键（等号左边）
     * @param formulaPart    公式部分（等号右边）
     */
    public FormulaEntity(String variablePrefix, String formulaKey, String formulaPart) {
        this.variablePrefix = StringUtils.isBlank(variablePrefix) ? DEFAULT_VARIABLE_PREFIX : variablePrefix.trim();
        this.variablePatternWithPrefix = this.variablePrefix + DEFAULT_VARIABLE_PREFIX_PATTERN;
        this.formulaKey = removePrefix(formulaKey);
        this.formulaPart = removePrefix(formulaPart);
        initVariables(formulaPart);
    }

    /**
     * 删除变量前缀
     *
     * @param variableName 变量名称
     * @return 删除前缀后的变量名称
     */
    private String removePrefix(String variableName) {
        // 注意 '\u00A0' 特殊的空格
        return variableName.replace(variablePrefix, StringUtils.EMPTY)
                .replaceAll("\\s+", StringUtils.EMPTY)
                .replace("\u00A0", StringUtils.EMPTY);
    }

    /**
     * 初始化变量
     *
     * @param formula 公式
     */
    private void initVariables(String formula) {
        variableNames = new LinkedHashSet<>();
        Pattern pattern = Pattern.compile(variablePatternWithPrefix);
        Matcher matcher = pattern.matcher(formula);
        while (matcher.find()) {
            variableNames.add(matcher.group(VARIABLE_GROUP_1));
        }
    }

    /**
     * 获取公式key（等号左边部分）
     *
     * @return 公式key（等号左边部分）
     */
    public String getFormulaKey() {
        return formulaKey;
    }

    /**
     * 公式部分（等号右边部分）
     *
     * @return 公式部分（等号右边部分）
     */
    public String getFormulaPart() {
        return formulaPart;
    }

    /**
     * 获取变量名称（等号右边部分）
     *
     * @return 获取变量名称（等号右边部分）
     */
    public Set<String> getVariableNames() {
        return variableNames;
    }

    /**
     * 判断是否无变量名称
     *
     * @return 是否无变量名称
     */
    boolean isNoVariableNames() {
        return variableNames.isEmpty();
    }

    /**
     * 获取变量前缀
     *
     * @return 变量前缀
     */
    public String getVariablePrefix() {
        return variablePrefix;
    }
}
