package lab1

class Bundle {
    fun getString(key: String): String? {
        return if (key == "letter") "A" else null
    }
}

class Intent {
    val extras: Bundle? = Bundle()
}

class Fragment {
    var letterId: String = ""
}

open class AppCompatActivity

class DetailActivity : AppCompatActivity() {
    companion object {
        const val LETTER = "letter"
    }
}

// --- MAIN PROGRAM ---

fun main() {
    println("\n====== 1. SETS (No Duplicates) ======")

    val numbers = listOf(0, 3, 8, 4, 0, 5, 5, 8, 9, 2)
    val setOfNumbers = numbers.toSet()
    println("Original List: $numbers")
    println("Set (Unique): $setOfNumbers")

    val set1 = setOf(1, 2, 3)
    val set2 = setOf(3, 4, 5)
    println("Set 1: $set1")
    println("Set 2: $set2")
    println("Union: ${set1.union(set2)}")
    println("Intersect: ${set1.intersect(set2)}")


    println("\n====== 2. MAPS (Key-Value) ======")

    val peopleAges = mutableMapOf(
        "Fred" to 30,
        "Ann" to 23
    )
    println("Initial Map: $peopleAges")

    peopleAges.put("Barbara", 42)
    peopleAges["Joe"] = 51
    println("Updated Map: $peopleAges")

    println("--- Map Operations ---")
    peopleAges.forEach { print("${it.key} is ${it.value}, ") }
    println()

    val mapString = peopleAges.map { "${it.key} is ${it.value}" }.joinToString(", ")
    println("Map to String: $mapString")

    val filteredNames = peopleAges.filter { it.key.length < 4 }
    println("Filtered (Name < 4 chars): $filteredNames")


    println("\n====== 3. COLLECTION OPERATIONS ======")

    val words = listOf("about", "acute", "balloon", "best", "brief", "class")
    val filteredWords = words.filter { it.startsWith("b", ignoreCase = true) }
        .shuffled()
        .take(2)
        .sorted()
    println("Words starting with 'b' (Random 2 & Sorted): $filteredWords")


    println("\n====== 4. SCOPE FUNCTIONS (Let & Apply) ======")

    val intent = Intent()
    val arguments = intent.extras
    var letterId = ""

    arguments?.let {
        letterId = it.getString("letter").toString()
        println("Let: Retrieved letterId = $letterId")
    }

    val fragment = Fragment()
    fragment.apply {
        this.letterId = "B"
        println("Apply: Configured fragment with letterId = ${this.letterId}")
    }


    println("\n====== 5. LAMBDAS ======")

    val triple: (Int) -> Int = { a: Int -> a * 3 }
    println("Triple of 5 is: ${triple(5)}")


    println("\n====== 6. COMPANION OBJECT ======")

    println("Accessing Companion Constant: ${DetailActivity.LETTER}")


    println("\n====== 7. ELVIS OPERATOR (Null Safety) ======")

    var quantity: Int? = null
    var result = quantity ?: 0
    println("When null (quantity ?: 0): $result")

    quantity = 4
    result = quantity ?: 0
    println("When not null (quantity ?: 0): $result")

}