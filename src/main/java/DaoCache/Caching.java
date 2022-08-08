package DaoCache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Caching
{
		Class<? extends Source> source();

		boolean searchFibonacciSequence() default false;

		String url() default "jdbc:h2:~/test";

		String user() default "sa";

		String password() default "";
}
