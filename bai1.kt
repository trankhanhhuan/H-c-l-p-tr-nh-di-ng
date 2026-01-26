fun bai1() {
    println("Hello, world!")

    // Assign once, cannot change.
    val age = "5"
    val name = "Rover"

    // Assign and change as needed.
    var roll = 6
    var rolledValue: Int = 4

    println("You are already ${age}!")
    println("You are already ${age} days old, ${name}!")
}
// Hàm ko đối số
fun printHello () {
    println ("Hello Kotlin")
}

// Hàm có đối số
fun printBorder(border: String, timesToRepeat: Int) {
    repeat(timesToRepeat) {
        print(border)
    }
    println()
}


//Tạo số ngẫu nhiên
val randomNumber = diceRange.random()

fun roll() {
    val anotherRandomNumber = (1..6).random()
    println(randomNumber)
}

//Lặp lại một thao tác bằng repeat()
fun printBorder() {
    repeat(23) {
        print("=")
    }
}

//Lồng ghép vòng lặp
fun printCakeBottom(age: Int, layers: Int) {
    repeat(layers) {
        repeat(age + 2) {
            print("@")
        }
        println()
    }
}

//Câu lệnh có điều kiện với if/else
fun main() {
    val num = 4
    if (num > 4) {
        println("The variable is greater than 4")
    } else if (num == 4) {
        println("The variable is equal to 4")
    } else {
        println("The variable is less than 4")
    }
}

//Câu lệnh có điều kiện với when
when (rollResult) {
    luckyNumber -> println("You won!")
    1 -> println("So sorry! You rolled a 1. Try again!")
    2 -> println("Sadly, you rolled a 2. Try again!")
    3 -> println("Unfortunately, you rolled a 3. Try again!")
    4 -> println("No luck! You rolled a 4. Try again!")
    5 -> println("Don't cry! You rolled a 5. Try again!")
    6 -> println("Apologies! you rolled a 6. Try again!")
}