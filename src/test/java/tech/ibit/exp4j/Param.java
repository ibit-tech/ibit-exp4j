package tech.ibit.exp4j;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.StringJoiner;

/**
 * 定义参数类
 *
 * @author iBit程序猿
 */
public class Param {
    private BigDecimal a;
    private BigDecimal b;
    private BigDecimal c;
    private BigDecimal d;
    private BigDecimal e;
    private BigDecimal f;
    private BigDecimal g;


    /**
     * Gets the value of a
     *
     * @return the value of a
     */
    public BigDecimal getA() {
        return a;
    }

    /**
     * Sets the a
     * <p>You can use getA() to get the value of a</p>
     *
     * @param a a
     */
    public void setA(BigDecimal a) {
        this.a = a;
    }

    /**
     * Gets the value of b
     *
     * @return the value of b
     */
    public BigDecimal getB() {
        return b;
    }

    /**
     * Sets the b
     * <p>You can use getB() to get the value of b</p>
     *
     * @param b b
     */
    public void setB(BigDecimal b) {
        this.b = b;
    }

    /**
     * Gets the value of c
     *
     * @return the value of c
     */
    public BigDecimal getC() {
        return c;
    }

    /**
     * Sets the c
     * <p>You can use getC() to get the value of c</p>
     *
     * @param c c
     */
    public void setC(BigDecimal c) {
        this.c = c;
    }

    /**
     * Gets the value of d
     *
     * @return the value of d
     */
    public BigDecimal getD() {
        return d;
    }

    /**
     * Sets the d
     * <p>You can use getD() to get the value of d</p>
     *
     * @param d d
     */
    public void setD(BigDecimal d) {
        this.d = d;
    }

    /**
     * Gets the value of e
     *
     * @return the value of e
     */
    public BigDecimal getE() {
        return e;
    }

    /**
     * Sets the e
     * <p>You can use getE() to get the value of e</p>
     *
     * @param e e
     */
    public void setE(BigDecimal e) {
        this.e = e;
    }

    /**
     * Gets the value of f
     *
     * @return the value of f
     */
    public BigDecimal getF() {
        return f;
    }

    /**
     * Sets the f
     * <p>You can use getF() to get the value of f</p>
     *
     * @param f f
     */
    public void setF(BigDecimal f) {
        this.f = f;
    }

    /**
     * Gets the value of g
     *
     * @return the value of g
     */
    public BigDecimal getG() {
        return g;
    }

    /**
     * Sets the g
     * <p>You can use getG() to get the value of g</p>
     *
     * @param g g
     */
    public void setG(BigDecimal g) {
        this.g = g;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Param.class.getSimpleName() + "[", "]")
                .add("a=" + a)
                .add("b=" + b)
                .add("c=" + c)
                .add("d=" + d)
                .add("e=" + e)
                .add("f=" + f)
                .add("g=" + g)
                .toString();
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Param param = new Param();
        BeanUtils.setProperty(param, "c", BigDecimal.TEN);
        System.out.println(param);
    }
}
