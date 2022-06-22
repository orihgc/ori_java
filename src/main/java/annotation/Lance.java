package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Target: 试用位置
 * Retention: 保留级别
 * */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Lance {
    String value() default "xxx";
}
