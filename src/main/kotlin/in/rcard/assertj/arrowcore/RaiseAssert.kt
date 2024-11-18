@file:OptIn(ExperimentalTypeInference::class)

package `in`.rcard.assertj.arrowcore

import arrow.core.raise.Raise
import arrow.core.raise.fold
import `in`.rcard.assertj.arrowcore.errors.RaiseShouldFailButSucceeds.Companion.shouldFailButSucceedsWith
import `in`.rcard.assertj.arrowcore.errors.RaiseShouldThrowAnException.Companion.shouldThrowAnException
import org.assertj.core.api.AbstractObjectAssert
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions
import org.assertj.core.api.Fail
import org.assertj.core.internal.Failures
import kotlin.experimental.ExperimentalTypeInference

/**
 * Assertions for functions within a [Raise] context.
 *
 * @param VALUE type of the value returned by the function.
 * @param ERROR type of the logical error raised by the function.
 * @author Riccardo Cardin
 *
 * @since 0.2.0
 */
class RaiseAssert<ERROR : Any, VALUE : Any>(
    raiseResult: RaiseResult<ERROR, VALUE>,
) : AbstractRaiseAssert<RaiseAssert<ERROR, VALUE>, ERROR, VALUE>(raiseResult) {
    companion object {
        inline fun <ERROR : Any, VALUE : Any> assertThat(
            @BuilderInference lambda: Raise<ERROR>.() -> VALUE,
        ): RaiseAssert<ERROR, VALUE> {
            val raiseResult =
                fold(
                    block = lambda,
                    catch = { throwable -> RaiseResult.FailureWithException(throwable) },
                    recover = { error -> RaiseResult.Failure(error) },
                    transform = { value -> RaiseResult.Success(value) },
                )
            return RaiseAssert(raiseResult)
        }

        /**
         * Verifies that the function in the [Raise] context throws an exception.
         * ### Example:
         * ```kotlin
         * val throwsException: Raise<String>.() -> Int = {
         *     throw RuntimeException("AN EXCEPTION")
         * }
         * assertThatThrownBy { throwsException() }
         *     .isInstanceOf(RuntimeException::class.java)
         *     .hasMessage("AN EXCEPTION")
         * ```
         *
         * @param shouldRaiseThrowable the function to be executed in the [Raise] context.
         * @return the [AbstractThrowableAssert] to be used to verify the exception.
         */
        inline fun <ERROR : Any, VALUE : Any> assertThatThrownBy(
            @BuilderInference shouldRaiseThrowable: Raise<ERROR>.() -> VALUE,
        ): AbstractThrowableAssert<*, out Throwable> {
            val throwable: Throwable? =
                fold(
                    block = shouldRaiseThrowable,
                    recover = { null },
                    transform = { null },
                    catch = { exception -> exception },
                )

            @Suppress("KotlinConstantConditions")
            return throwable?.let { return Assertions.assertThat(throwable) } ?: throw Failures
                .instance()
                .failure(Assertions.assertThat(throwable).writableAssertionInfo, shouldThrowAnException())
        }

        /**
         * Verifies that the function in the [Raise] context raises an error.
         * ### Example:
         * ```kotlin
         * val raisesError: Raise<String>.() -> Int = { raise("LOGICAL ERROR") }
         * assertThatRaisedBy { raisesError() }.isEqualTo("LOGICAL ERROR")
         * ```
         * @param shouldRaiseError the function to be executed in the [Raise] context.
         * @return the [AbstractObjectAssert] to be used to verify the error.
         */
        inline fun <ERROR : Any, VALUE : Any> assertThatRaisedBy(
            @BuilderInference shouldRaiseError: Raise<ERROR>.() -> VALUE,
        ): AbstractObjectAssert<*, out ERROR> {
            val error =
                fold(
                    block = shouldRaiseError,
                    recover = { error -> error },
                    transform = { value ->
                        Fail.fail(
                            shouldFailButSucceedsWith(value).create(),
                        )
                    },
                )
            return Assertions.assertThat(error)
        }
    }
}
