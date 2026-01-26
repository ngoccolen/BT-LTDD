//Phép toán trên tập hợp
fun main() {
    //Lặp lại tập hợp
    val peopleAges = mutableMapOf<String, Int>(
        "Fred" to 30,
        "Ann" to 23
    )
    peopleAges.forEach {print ("${it.key} is ${it.value}, ")}

    //Chuyển đổi từng mục trong 1 tập hợp
    println(peopleAges.map { "${it.key} is ${it.value}" }.joinToString(", "))

    //Lọc các mục trong 1 tập hợp
    val filteredNames = peopleAges.filter { it.key.length < 4 }
    println(filteredNames)

    //Cac phép toán khác trên tập hợp
    val words = listOf("about", "acute", "balloon", "best", "brief", "class")
    val filteredWords = words.filter { it.startsWith("b", ignoreCase = true) }
        .shuffled()
        .take(2)
        .sorted()

    //Hàm phạm vi
    val name: String? = "Kotlin"
    name?.let {
        println("Length of name is ${it.length}")
    }
}