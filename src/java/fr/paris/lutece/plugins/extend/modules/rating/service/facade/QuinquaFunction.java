package fr.paris.lutece.plugins.extend.modules.rating.service.facade;

import java.util.function.Function;

/**
 * Represents a function that accepts five arguments and produces a result.
 * This is the two-arity specialization of {@link Function}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object, Object, object, object, object)}.
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <V> the type of the third argument to the function
 * @param <W> the type of the fourth argument to the function
 * @param <X> the type of the fourth argument to the function
 * @param <R> the type of the result of the function
 *
 * @see Function
 * @since 1.8
 */
@FunctionalInterface
public interface QuinquaFunction<T, U, V, W, X ,R > {

	 /**
      * Applies this function to the given arguments.
      *
      * @param t the type of the first argument to the function
      * @param u the type of the second argument to the function
      * @param v the type of the third argument to the function
      * @param w the type of the fourth argument to the function
      * @param X the type of the fourth argument to the function
      * @return the function result
     */
    R apply(T t, U u, V v, W w, X x);
}
