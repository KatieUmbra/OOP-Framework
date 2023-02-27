package gay.kanwi.oopf

/**
 * This annotation is used to mark classes as exercises.
 * It is used by [Setup] to find all exercises.
 *
 * to use this annotation, add the following code to the class:
 * ```
 * @IsExercise
 * class MyExercise : Exercise {
 *    override fun run() {
 *    // do something
 *    }
 * }
 * ```
 * @see Setup
 * @since 1.0
 * @author Katherine C.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class IsExercise
