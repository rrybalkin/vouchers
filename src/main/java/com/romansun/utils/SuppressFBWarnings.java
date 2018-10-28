package com.romansun.utils;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.CLASS;

@Retention(value=CLASS)
public @interface SuppressFBWarnings {

    String[] value();

    String justification() default "";
}
