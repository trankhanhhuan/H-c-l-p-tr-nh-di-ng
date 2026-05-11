package lab1


enum class Direction {
    NORTH, SOUTH, WEST, EAST
}


fun main() {
    println("\n====== 1. ENUM CLASSES ======")

    val direction = Direction.NORTH
    println("Current Direction: $direction")

    print("Checking Direction: ")
    when (direction) {
        Direction.NORTH -> println("Heading North")
        Direction.SOUTH -> println("Heading South")
        Direction.WEST -> println("Heading West")
        Direction.EAST -> println("Heading East")
    }


    println("\n====== 2. EXCEPTION HANDLING (Try/Catch) ======")

    try {
        println("Attempting division by zero...")
        val result = 10 / 0
        println("Result: $result")
    } catch (e: Exception) {
        println("Caught Exception: ${e.message}")
    } finally {
        println("Finally block executed")
    }


    println("\n====== 3. COROUTINES (Simulation) ======")
    println("(Note: Real Coroutines require 'kotlinx-coroutines-core' dependency)")

    println("Starting Coroutine simulation...")
    simulateSuspendFunction()
}


fun simulateSuspendFunction() {
    println("Suspend function started")

    val data = getValue()
    println("Data retrieved: $data")

    processValue(data)
}

fun getValue(): Double {
    return 55.5
}

fun processValue(value: Double) {
    println("Processing value: ${value * 2}")
}