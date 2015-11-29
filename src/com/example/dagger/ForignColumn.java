package com.example.dagger;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(CLASS) @Target(FIELD)
public @interface ForignColumn {
	String value();
	String[] cascade();

}
