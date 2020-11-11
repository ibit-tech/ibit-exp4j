package tech.ibit.exp4j.utils;

import net.objecthunter.exp4j.function.Function;

import java.util.Arrays;
import java.util.List;

/**
 * 数学函数扩展工具类
 * <p>
 * exp4j 内置函数列表 </br>
 * <p>abs（绝对值），acos（反余弦），asin（反正弦），atan（反正切），cbrt（立方根），ceil（最接近的上界整数），
 * cos（余弦），cosh（双曲余弦），exp（e^x），floor（最接近的下界整数），log（自然对数(底数 e)），log10（对数( 底数0)），
 * log2（对数(底数2)），sin（正弦），sinh（双曲正弦），sqrt（平方根），tan（正切），tanh（双曲正切），signum（signum 函数）</p>
 *
 * @author iBit程序猿
 */
public class MathFunctionEnhanceUtils {


    private MathFunctionEnhanceUtils() {

    }

    /**
     * 获取公式列表
     *
     * @return 公式列表
     */
    public static List<Function> getFunctions() {
        return Arrays.asList(
                getRintFunction(), getPowFunction(), getMinFunction(), getMaxFunction());
    }

    /**
     * "最接近参数并等于某一整数的 double 值。如果两个同为整数的 double 值都同样接近，那么结果取偶数"函数
     *
     * @return "最接近参数并等于某一整数的 double 值。如果两个同为整数的 double 值都同样接近，那么结果取偶数"函数
     * @see Math#rint(double)
     */
    public static Function getRintFunction() {
        return new Function("rint", 1) {
            @Override
            public double apply(double... args) {
                return Math.rint(args[0]);
            }
        };
    }

    /**
     * 幂函数
     *
     * @return 幂函数
     * @see Math#pow(double, double)
     */
    public static Function getPowFunction() {
        return new Function("pow", 2) {
            @Override
            public double apply(double... args) {
                return Math.pow(args[0], args[1]);
            }
        };
    }


    /**
     * 获取最小值函数
     *
     * @return 最小值函数
     * @see Math#min(double, double)
     */
    public static Function getMinFunction() {
        return new Function("min", 2) {
            @Override
            public double apply(double... args) {
                return Math.min(args[0], args[1]);
            }
        };
    }

    /**
     * 获取最大值函数
     *
     * @return 最大值
     */
    public static Function getMaxFunction() {
        return new Function("max", 2) {
            @Override
            public double apply(double... args) {
                return Math.max(args[0], args[1]);
            }
        };
    }


}
