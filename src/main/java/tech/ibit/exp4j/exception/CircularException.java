package tech.ibit.exp4j.exception;

import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * 环异常
 *
 * @author iBit程序猿
 */
public class CircularException extends RuntimeException {

    public CircularException(String formulaKey) {
        super("Circular formula on field: " + formulaKey);
    }

    public CircularException(String variable, List<String> paths) {
        super("Circular variable: " + variable + ", paths: " + StringUtils.join(paths, "->"));
    }
}
