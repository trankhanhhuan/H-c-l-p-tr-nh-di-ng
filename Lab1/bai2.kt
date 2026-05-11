package lab1


import kotlin.math.PI

abstract class Dwelling(private var residents: Int) {
    abstract val buildingMaterial: String
    abstract fun floorArea(): Double

    fun hasRoom(): Boolean {
        return residents < 5
    }
}

class SquareCabin(residents: Int, val length: Double) : Dwelling(residents) {
    override val buildingMaterial = "Wood"
    override fun floorArea(): Double {
        return length * length
    }
}

class RoundHut(residents: Int, val radius: Double) : Dwelling(residents) {
    override val buildingMaterial = "Straw"
    override fun floorArea(): Double {
        return PI * radius * radius
    }
}



fun main() {
    println("\n====== 1. ABSTRACT CLASSES & INHERITANCE ======")

    val squareCabin = SquareCabin(6, 50.0)
    val roundHut = RoundHut(3, 10.0)

    println("Square Cabin Material: ${squareCabin.buildingMaterial}")
    println("Square Cabin Floor Area: ${squareCabin.floorArea()}")
    println("Square Cabin Has Room? ${squareCabin.hasRoom()}")

    println("\n--- With Statement ---")
    with(roundHut) {
        println("Round Hut Material: $buildingMaterial")
        println("Round Hut Floor Area: ${floorArea()}")
        println("Round Hut Has Room? ${hasRoom()}")
    }


    println("\n====== 2. LISTS ======")

    val numbers = listOf(1, 2, 3, 4, 5, 6)
    println("List size: ${numbers.size}")
    println("First item: ${numbers[0]}")
    println("Reversed list: ${numbers.reversed()}")

    val entrees = mutableListOf<String>()
    println("Empty mutable list: $entrees")

    entrees.add("spaghetti")
    println("Added spaghetti: $entrees")

    entrees.add("lasagna")
    println("Added lasagna: $entrees")

    entrees[0] = "pasta"
    println("Modified first item: $entrees")

    entrees.remove("pasta")
    println("Removed pasta: $entrees")


    println("\n====== 3. LOOPS ======")

    val myList = listOf("Car", "Bike", "Plane")

    print("For Loop: ")
    for (element in myList) {
        print("$element ")
    }
    println()

    print("While Loop: ")
    var index = 0
    while (index < myList.size) {
        print("${myList[index]} ")
        index++
    }
    println()


    println("\n====== 4. STRINGS & MATH ======")

    val name = "Android"
    println("String length of '$name': ${name.length}")

    val number = 10
    val groups = 5
    println("String Template: $number people")
    println("Math Expression in Template: ${number * groups} people")

    val radius = 5.0
    println("Math Lib PI calculation: ${PI * radius * radius}")

}