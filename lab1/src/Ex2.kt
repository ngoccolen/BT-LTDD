//Kiểm soát luồng chương trình
fun main() {
    //Lặp lại 1 thao tác bằng repeat()
    fun printBorder() {
        repeat(23) {
            print("=")
        }
    }
    //Lồng ghép vòng lặp repeat()
    fun printCakeBottom(age: Int, layers: Int) {
        repeat(layers) {
            repeat(age + 2) {
                print("@")
            }
            println()
        }
    }

    //Câu lệnh if/else
    fun checkNumber() {
        val num = 4
        if (num > 4) {
            println("The variable is greater than 4")
        } else if (num == 4) {
            println("The variable is equal to 4")
        } else {
            println("The variable is less than 4")
        }
    }

    printBorder()
    printCakeBottom(4, 2)
    checkNumber()


    //Câu lệnh có điều kiện với when
    val luckyNumber = 6
    val rollResult = (1..6).random()
    when (rollResult) {
        luckyNumber -> println("You won")
        1 -> println("So sorry! You rolled a 1. Try again!")
        2 -> println("Sadly, you rolled a 2. Try again!")
        3 -> println("Unfortunately, you rolled a 3. Try again!")
        4 -> println("No luck! You rolled a 4. Try again!")
        5 -> println("Don't cry! You rolled a 5. Try again!")
        6 -> println("Apologies! you rolled a 6. Try again!")
    }

    //Chỉ định kết quả của câu lệnh when cho 1 biến
    val diceRoll = rollResult
    val drawableResource = when (diceRoll) {
        1 -> "dice_1"
        2 -> "dice_2"
        3 -> "dice_3"
        4 -> "dice_4"
        5 -> "dice_5"
        else -> "dice_6"
    }
}