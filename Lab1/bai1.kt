package lab1

import kotlin.random.Random

fun printHello() {
    println("Hello Kotlin")
}

fun printBorder(border: String, timesToRepeat: Int) {
    repeat(timesToRepeat) {
        print(border)
    }
    println()
}

fun printBorder() {
    repeat(23) {
        print("=")
    }
    println()
}

fun roll(): Int {
    val randomNumber = (1..6).random()
    return randomNumber
}

fun rollAndPrint() {
    val anotherRandomNumber = (1..6).random()
    println(anotherRandomNumber)
}

fun printCakeBottom(age: Int, layers: Int) {
    repeat(layers) {
        repeat(age + 2) {
            print("@")
        }
        println()
    }
}

class DiceSimple {
    var sides = 6
    fun roll() {
        val randomNumber = (1..6).random()
        println("DiceSimple Rolled: $randomNumber")
    }
}

class Dice(val numSides: Int) {
    fun roll(): Int {
        val randomNumber = (1..numSides).random()
        return randomNumber
    }
}

object R {
    object drawable {
        val dice_1 = 1
        val dice_2 = 2
        val dice_3 = 3
        val dice_4 = 4
        val dice_5 = 5
        val dice_6 = 6
    }
}

fun main() {
    println("\n====== 1. BASIC PRINT & VARIABLES ======")
    println("Hello world!")
    println("This is the text to print!")

    val age = "5"
    val name = "Rover"

    println("You are already ${age}!")
    println("You are already ${age} days old, ${name}!")

    println("\n====== 2. FUNCTIONS ======")
    printHello()

    print("Border with args: ")
    printBorder("-", 10)

    print("Default border:   ")
    printBorder()

    println("\n====== 3. RANDOM & RETURN ======")
    val myRoll = roll()
    println("Roll result (return): $myRoll")

    val diceRange = 1..6
    val randomNumber = diceRange.random()
    println("Random number from range: $randomNumber")

    print("Roll and print function: ")
    rollAndPrint()

    println("\n====== 4. LOOPS (CAKE) ======")
    printCakeBottom(age.toInt(), 3)

    println("\n====== 5. IF / ELSE ======")
    val num = 4
    print("Checking number $num: ")
    if (num > 4) {
        println("The variable is greater than 4")
    } else if (num == 4) {
        println("The variable is equal to 4")
    } else {
        println("The variable is less than 4")
    }

    println("\n====== 6. WHEN STATEMENT ======")
    val rollResult = 4
    val luckyNumber = 5
    print("Roll result is $rollResult: ")

    when (rollResult) {
        luckyNumber -> println("You won!")
        1 -> println("So sorry! You rolled a 1. Try again!")
        2 -> println("Sadly, you rolled a 2. Try again!")
        3 -> println("Unfortunately, you rolled a 3. Try again!")
        4 -> println("No luck! You rolled a 4. Try again!")
        5 -> println("Don't cry! You rolled a 5. Try again!")
        6 -> println("Apologies! you rolled a 6. Try again!")
    }

    println("\n====== 7. WHEN EXPRESSION (RESOURCES) ======")
    val diceRoll = 3
    val drawableResource = when (diceRoll) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }
    println("Selected Dice: $diceRoll")
    println("Drawable Resource ID: $drawableResource")

    println("\n====== 8. CLASSES ======")
    val simple = DiceSimple()
    simple.roll()

    val myFirstDice = Dice(6)
    println("Dice (6 sides) result: ${myFirstDice.roll()}")
    
}