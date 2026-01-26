//Chương trình kotlin cơ bản
fun main() {
    println("Hello, world!")
    println("This is the text to print!")

    val age = "5"
    val name = "Rover"

    println("You are already $age!")
    println("You are already $age days old, $name!")

    // Hàm không có đối số
    fun printHello() {
        println("Hello Kotlin")
    }
    printHello()

    // Hàm có đối số
    fun printBorder(border: String, timesToRepeat: Int) {
        repeat(timesToRepeat) {
            print(border)
        }
        println()
    }
    printBorder("=", 10)

    // Hàm trả về giá trị
    fun rollDice(): Int {
        return (1..6).random()
    }

    val diceResult = rollDice()
    println("Dice result: $diceResult")
}
