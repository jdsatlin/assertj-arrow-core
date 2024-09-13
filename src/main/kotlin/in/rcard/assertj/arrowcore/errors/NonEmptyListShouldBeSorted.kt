package `in`.rcard.assertj.arrowcore.errors

import arrow.core.NonEmptyList
import org.assertj.core.error.BasicErrorMessageFactory

/**
 * Build error message when a [NonEmptyList] is not sorted.
 *
 * @author Hamza Faraji
 * @since 1.2.0
 */
class NonEmptyListShouldBeSorted private constructor(message: String, actual: NonEmptyList<Any?>) :
    BasicErrorMessageFactory(message, actual) {
    companion object {
        private const val EXPECTING_TO_BE_SORTED =
            "%nExpecting:%n  <%s>%nto be sorted, but it is not."

        internal fun <ELEMENT : Any> shouldBeSorted(
            actual: NonEmptyList<ELEMENT?>
        ): NonEmptyListShouldBeSorted = NonEmptyListShouldBeSorted(
            EXPECTING_TO_BE_SORTED,
            actual
        )
    }
}