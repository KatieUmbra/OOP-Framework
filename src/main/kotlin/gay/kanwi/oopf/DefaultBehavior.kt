package gay.kanwi.oopf

/**
 * This enum class is used to define the default behavior of the [Exercise.run] method.
 *
 * [FIRST] will run the first exercise in the list, ordered alphabetically.
 * [LAST] will run the last exercise in the list, ordered alphabetically.
 * [RANDOM] will run a random exercise in the list.
 * [ALL] will run all exercises in the list.
 * [NONE] will not run any exercises.
 * @since 1.0
 * @author Katherine C.
 */
enum class DefaultBehavior {
    FIRST,
    LAST,
    RANDOM,
    ALL,
    NONE;
    companion object {
        @Suppress("MemberVisibilityCanBePrivate")
        val values: List<String> = DefaultBehavior.values().map { it.name }
    }
}