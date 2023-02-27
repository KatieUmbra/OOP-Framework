package gay.kanwi.oopfTest

import gay.kanwi.oopf.Exercise
import gay.kanwi.oopf.IsExercise

@IsExercise
class Algoritmo: Exercise {
    override fun run() {
        println("Hey, this is running!")
    }
}