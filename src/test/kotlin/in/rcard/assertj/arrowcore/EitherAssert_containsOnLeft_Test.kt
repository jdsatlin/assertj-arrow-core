package `in`.rcard.assertj.arrowcore

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import `in`.rcard.assertj.arrowcore.errors.EitherShouldBeLeft.Companion.shouldBeLeft
import `in`.rcard.assertj.arrowcore.errors.EitherShouldContain.Companion.shouldContainOnLeft
import `in`.rcard.assertj.arrowcore.errors.EitherShouldContain.Companion.shouldContainOnRight
import org.assertj.core.api.Assertions
import org.assertj.core.util.FailureMessages.actualIsNull
import org.junit.jupiter.api.Test

internal class EitherAssert_containsOnLeft_Test {
    @Test
    internal fun `should fail when either is null`() {
        val leftValue: Either<String, Nothing>? = null
        Assertions.assertThatThrownBy {
            EitherAssert.assertThat(leftValue).containsOnLeft(
                "something",
            )
        }
            .isInstanceOf(AssertionError::class.java)
            .hasMessage(actualIsNull())
    }

    @Test
    internal fun `should pass if either contains expected value on left side`() {
        val leftValue = "something".left()
        EitherAssert.assertThat(leftValue).containsOnLeft("something")
    }

    @Test
    internal fun `should pass if either contains an expected null on left side`() {
        val leftValue = null.left()
        EitherAssert.assertThat(leftValue).containsOnLeft(null)
    }

    @Test
    internal fun `should fail if either does not contain expected value on left side`() {
        val actual: Either<String, Nothing> = "something".left()
        val expectedValue = "nothing"
        Assertions.assertThatThrownBy {
            EitherAssert.assertThat(actual).containsOnLeft(
                expectedValue,
            )
        }
            .isInstanceOf(AssertionError::class.java)
            .hasMessage(shouldContainOnLeft(actual, expectedValue).create())
    }

    @Test
    internal fun `should fail with informative error message if null is contained on left side and a value was expected`() {
        val actual: Either<String?, Nothing> = null.left()
        val expectedValue = "something"
        Assertions.assertThatThrownBy {
            EitherAssert.assertThat(actual).containsOnLeft(
                expectedValue,
            )
        }
            .isInstanceOf(AssertionError::class.java)
            .hasMessage(shouldContainOnLeft(actual, expectedValue).create())
    }

    @Test
    internal fun `should fail if either is right`() {
        val actual: Either<String, String> = "nothing".right()
        val expectedValue = "something"
        Assertions.assertThatThrownBy {
            EitherAssert.assertThat(actual).containsOnLeft(
                expectedValue,
            )
        }
            .isInstanceOf(AssertionError::class.java)
            .hasMessage(shouldBeLeft(actual).create())
    }

    @Test
    internal fun `should fail if either right-sided value is null`() {
        val actual: Either<String, String?> = null.right()
        val expectedValue = "something"
        Assertions.assertThatThrownBy {
            EitherAssert.assertThat(actual).containsOnLeft(
                expectedValue,
            )
        }
            .isInstanceOf(AssertionError::class.java)
            .hasMessage(shouldBeLeft(actual).create())
    }
}
