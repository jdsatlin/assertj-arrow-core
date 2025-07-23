package `in`.rcard.assertj.arrowcore

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import `in`.rcard.assertj.arrowcore.errors.EitherShouldBeRight.Companion.shouldBeRight
import org.assertj.core.api.Assertions
import org.assertj.core.util.FailureMessages.actualIsNull
import org.junit.jupiter.api.Test

internal class EitherAssert_isRight_Test {

    @Test
    internal fun `should fail if either is null`() {
        // GIVEN
        val rightValue: Either<Nothing, Nothing>? = null
        // WHEN/THEN
        Assertions.assertThatThrownBy { EitherAssert.assertThat(rightValue).isRight() }
            .isInstanceOf(AssertionError::class.java)
            .hasMessage(actualIsNull())
    }

    @Test
    internal fun `should pass if either is right`() {
        // GIVEN
        val rightValue = 42.right()
        // WHEN/THEN
        EitherAssert.assertThat(rightValue).isRight()
    }

    @Test
    internal fun `should pass if either is right-sided null`() {
        // GIVEN
        val rightValue = null.right()
        // WHEN/THEN
        EitherAssert.assertThat(rightValue).isRight()
    }

    @Test
    internal fun `should fail if either is left`() {
        // GIVEN
        val leftValue = "42".left()
        // WHEN/THEN
        Assertions.assertThatThrownBy { EitherAssert.assertThat(leftValue).isRight() }
            .isInstanceOf(AssertionError::class.java)
            .hasMessage(shouldBeRight(leftValue).create())
    }

    @Test
    internal fun `should fail if either is left-sided null`() {
        // GIVEN
        val leftValue = null.left()
        // WHEN/THEN
        Assertions.assertThatThrownBy { EitherAssert.assertThat(leftValue).isRight() }
            .isInstanceOf(AssertionError::class.java)
            .hasMessage(shouldBeRight(leftValue).create())
    }
}
