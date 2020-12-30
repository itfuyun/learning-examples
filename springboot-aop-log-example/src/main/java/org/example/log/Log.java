package org.example.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志操作类型注解，用于需要日记记录的地方
 *
 * @author <a href="mailto:itfuyun@gmail.com">Tanxh</a>
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    /**
     * 模块名称
     *
     * @return
     */
    String module();

    /**
     * 接口名称
     *
     * @return
     */
    String info() default "";
}
