package tech.ibit.exp4j.utils;

import net.objecthunter.exp4j.operator.Operator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 公式扩展工具类
 *
 * @author iBit程序猿
 */
public class OperatorEnhanceUtils {

    private OperatorEnhanceUtils() {
    }


    /**
     * 获取基础的操作符
     *
     * @return 基础操作符列表
     */
    public static List<Operator> getOperators() {
        return Arrays.asList(reciprocal(), expendDivision()
                , gt(), egt(), eq(), elt(), lt());
    }


    /**
     * 定义"$"符号校验分母操作符
     *
     * @return 操作符
     */
    public static Operator reciprocal() {
        return new Operator("$", 1, true, Operator.PRECEDENCE_DIVISION) {
            @Override
            public double apply(final double... args) {
                if (args[0] == 0) {
                    throw new ArithmeticException("Division by zero!");
                }
                return args[0];
            }
        };
    }

    /**
     * 扩展触发，如果分母为0，则返回0
     *
     * @return 扩展除法
     */
    public static Operator expendDivision() {
        return new Operator("//", 2, true, Operator.PRECEDENCE_DIVISION) {
            @Override
            public double apply(final double... args) {
                double arg0 = args[0];
                double arg1 = args[1];

                return new BigDecimal(arg1).compareTo(BigDecimal.ZERO) == 0 ? 0 : arg0 / arg1;
            }
        };
    }

    /**
     * 大于
     *
     * @return 大于操作符
     */
    public static Operator gt() {
        return new Operator(">", 2, true, Operator.PRECEDENCE_MULTIPLICATION) {
            @Override
            public double apply(double... args) {
                return args[0] > args[1] ? 1 : 0;
            }
        };
    }

    /**
     * 大于等于
     *
     * @return 大于等于操作符
     */
    public static Operator egt() {
        return new Operator(">=", 2, true, Operator.PRECEDENCE_MULTIPLICATION) {
            @Override
            public double apply(double... args) {
                return args[0] >= args[1] ? 1 : 0;
            }
        };
    }


    /**
     * 小于等于
     *
     * @return 小于等于操作符
     */
    public static Operator elt() {
        return new Operator("<=", 2, true, Operator.PRECEDENCE_MULTIPLICATION) {
            @Override
            public double apply(double... args) {
                return args[0] <= args[1] ? 1 : 0;
            }
        };
    }


    /**
     * 小于
     *
     * @return 小于操作符
     */
    public static Operator lt() {
        return new Operator("<", 2, true, Operator.PRECEDENCE_MULTIPLICATION) {
            @Override
            public double apply(double... args) {
                return args[0] < args[1] ? 1 : 0;
            }
        };
    }

    /**
     * 等于
     *
     * @return 等于操作符
     */
    public static Operator eq() {
        return new Operator("==", 2, true, Operator.PRECEDENCE_MULTIPLICATION) {
            @Override
            public double apply(double... args) {
                return args[0] == args[1] ? 1 : 0;
            }
        };
    }
}
