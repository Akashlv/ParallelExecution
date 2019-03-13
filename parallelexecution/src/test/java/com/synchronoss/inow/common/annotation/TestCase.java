package com.synchronoss.inow.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)

public @interface TestCase {
	String Author() default "QA";

} // will be retained at runtime
