package gay.kanwi.oopf

import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfoList
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.jvm.jvmName

/**
 * This class is used to run all exercises.
 * It scans the classpath for classes annotated with [IsExercise] and
 * runs the [Exercise.run] method of each of them.
 *
 * to use this class, add the following code to the main function:
 * ```
 * fun main(args: Array<String>) = Setup.runExercises(args)
 * ```
 * @since 1.0
 * @author Katherine C.
 */
object Setup {

    private fun run(behavior: DefaultBehavior, list: List<Any?>) {
        when (behavior) {
            DefaultBehavior.FIRST -> {
                (list[0] as Exercise).run()
            }
            DefaultBehavior.LAST -> {
                (list[list.size - 1] as Exercise).run()
            }
            DefaultBehavior.RANDOM -> {
                (list.random() as Exercise).run()
            }
            DefaultBehavior.ALL -> {
                list.forEach {
                    (it as Exercise).run()
                }
            }
            DefaultBehavior.NONE -> {
                println("No exercises to run.")
            }
        }
    }

    private fun getAllAnnotatedWith(annotation: KClass<out Annotation>): ClassInfoList? {
        val name = annotation.jvmName
        val classGraph = ClassGraph()
            .enableAllInfo()
            .scan()
        return classGraph.getClassesWithAnnotation(name)
    }

    /**
     * @see Setup
     * @since 1.0
     * @author Katherine C.
     */
    fun runExercises(
        args: Array<String> = arrayOf(),
        default: DefaultBehavior = DefaultBehavior.ALL
    ){
        val classes = mutableListOf<Any?>()

        getAllAnnotatedWith(IsExercise::class)?.filter {
            it.implementsInterface(Exercise::class.jvmName)
        }?.forEach {
            classes += if (it.loadClass().kotlin.objectInstance == null) {
                it.loadClass().kotlin.createInstance()
            } else {
                it.loadClass()
            }
        }

        val classNames = classes.map { it!!::class.simpleName }

        args.forEach {
            if (it !in classNames) throw InvalidArgument("Invalid argument: $it" + "\nValidArguments = ${classNames.joinToString()}")
        }

        classes.sortBy { it!!::class.java.name.toString() }

        if (args.isEmpty()) {
            run(default, classes)
        } else {
            args.forEach {
                classes.forEach { clazz ->
                    if (clazz!!::class.simpleName == it) {
                        (clazz as Exercise).run()
                    }
                }
            }
        }
    }
}