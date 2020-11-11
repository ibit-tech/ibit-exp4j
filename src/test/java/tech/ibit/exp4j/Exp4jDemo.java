package tech.ibit.exp4j;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;
import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.operator.Operator;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;


/**
 * Exp4jDemo
 *
 * @author iBit程序猿
 *
 */
public class Exp4jDemo {


    //Evaluating an expression
    @Test
    public void example1() {
        Expression e = new ExpressionBuilder("3 * sin(y) - 2 / (x - 2)")
                .variables("x", "y")
                .build()
                .setVariable("x", 2.3)
                .setVariable("y", 3.14);
        double result = e.evaluate();
        System.out.println(result);
    }

    // Evaluating an expression asynchronously
    @Test
    public void example2() throws ExecutionException, InterruptedException {
        ExecutorService exec = Executors.newFixedThreadPool(1);
        Expression e = new ExpressionBuilder("3log(y)/(x+1)")
                .variables("x", "y")
                .build()
                .setVariable("x", 2.3)
                .setVariable("y", 3.14);
        Future<Double> future = e.evaluateAsync(exec);
        double result = future.get();
        System.out.println(result);
    }

    // Implicit multiplication
    @Test
    public void example3() {
        double result = new ExpressionBuilder("2cos(xy)")
                .variables("x","y")
                .build()
                .setVariable("x", 0.5d)
                .setVariable("y", 0.25d)
                .evaluate();
        assertEquals(2d * Math.cos(0.5d * 0.25d), result, 0d);
    }

    // Numerical constants
    @Test
    public void example4() {
        String expr = "pi+π+e+φ";
        double expected = 2*Math.PI + Math.E + 1.61803398874d;
        Expression e = new ExpressionBuilder(expr).build();
        assertEquals(expected, e.evaluate(),0d);
    }

    // Scientific notation
    @Test
    public void example5() {
        String expr = "7.2973525698e-3";
        double expected = Double.parseDouble(expr);
        Expression e = new ExpressionBuilder(expr).build();
        assertEquals(expected, e.evaluate(),0d);
    }

    // Custom functions
    @Test
    public void example6() {
        Function logb = new Function("logb", 2) {
            @Override
            public double apply(double... args) {
                return Math.log(args[0]) / Math.log(args[1]);
            }
        };
        double result = new ExpressionBuilder("logb(8, 2) * 3")
                .function(logb)
                .build()
                .evaluate();
        double expected = 9;
        assertEquals(expected, result, 0d);
    }

    @Test
    public void example7() {
        Function avg = new Function("avg", 4) {

            @Override
            public double apply(double... args) {
                double sum = 0;
                for (double arg : args) {
                    sum += arg;
                }
                return sum / args.length;
            }
        };
        double result = new ExpressionBuilder("avg(1,2,3,4)")
                .function(avg)
                .build()
                .evaluate();

        double expected = 2.5d;
        assertEquals(expected, result, 0d);
    }

    // Create a custom operator for calculating the factorial
    @Test
    public void example8() {
        Operator factorial = new Operator("!", 1, true, Operator.PRECEDENCE_POWER + 1) {

            @Override
            public double apply(double... args) {
                final int arg = (int) args[0];
                if ((double) arg != args[0]) {
                    throw new IllegalArgumentException("Operand for factorial has to be an integer");
                }
                if (arg < 0) {
                    throw new IllegalArgumentException("The operand of the factorial can not be less than zero");
                }
                double result = 1;
                for (int i = 1; i <= arg; i++) {
                    result *= i;
                }
                return result;
            }
        };

        double result = new ExpressionBuilder("3!")
                .operator(factorial)
                .build()
                .evaluate();

        double expected = 6d;
        assertEquals(expected, result, 0d);
    }

    @Test
    public void example9() {
        Operator gteq = new Operator(">=", 2, true, Operator.PRECEDENCE_ADDITION - 1) {

            @Override
            public double apply(double[] values) {
                if (values[0] >= values[1]) {
                    return 1d;
                } else {
                    return 0d;
                }
            }
        };

        Expression e = new ExpressionBuilder("1>=2").operator(gteq)
                .build();
        assertTrue(0d == e.evaluate());
        e = new ExpressionBuilder("2>=1").operator(gteq)
                .build();
        assertTrue(1d == e.evaluate());
    }


    // Division by zero in operations and functions
    @Test
    public void example10() {
        Operator reciprocal = new Operator("$", 1, true, Operator.PRECEDENCE_DIVISION) {
            @Override
            public double apply(final double... args) {
                if (args[0] == 0d) {
                    throw new ArithmeticException("Division by zero!");
                }
                return 1d / args[0];
            }
        };
        Expression e = new ExpressionBuilder("20$").operator(reciprocal).build();
        e.evaluate(); // <- this call will throw an ArithmeticException
    }

    // Precedence of unary minus and power operators
    // Validation of expression
    @Test
    public void example11() {
        Expression e = new ExpressionBuilder("x")
                .variable("x")
                .build();

        ValidationResult res = e.validate();
        assertFalse(res.isValid());
        assertEquals(1, res.getErrors().size());

        e.setVariable("x",1d);
        res = e.validate();
        assertTrue(res.isValid());
    }


    @Test
    public void example12() {
        Expression e = new ExpressionBuilder("x")
                .variable("x")
                .build();

        ValidationResult res = e.validate(false);
        assertTrue(res.isValid());
        assertNull(res.getErrors());
    }

    @Test
    public void match() {
        String func = "x = logb(8, 2)";
        System.out.println(func.matches("logb\\(.*\\)"));
    }


    // Division by zero in operations and functions
    @Test
    public void example13() {
        Function isHeavy = new Function("__isHeavy", 0) {
            @Override
            public double apply(double... args) {
                return 1d;
            }
        };
        Expression e = new ExpressionBuilder("__isHeavy() * 100").functions(isHeavy).build();
        e.evaluate(); // <- this call will throw an ArithmeticException
    }


}
