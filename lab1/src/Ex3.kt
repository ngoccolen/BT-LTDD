//Lớp
fun main() {
    //Lop có thuộc tính và phương thức
    class Dice1 {
        var sides = 6
        fun roll() {
            val randomNumber = (1..6).random()
            println(randomNumber)
        }
    }
    class Dice2 (val numSides : Int) {
        fun roll() : Int {
            val randomNumber = (1..numSides).random()
            return randomNumber
        }
    }

    val dice1 = Dice1()
    dice1.roll()
    val myFirstDice = Dice2(6)
    println(myFirstDice.roll())

}
