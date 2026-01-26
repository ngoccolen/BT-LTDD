// Lớp trừu tượng
abstract class Dwelling {
    abstract val buildingMaterial: String
    abstract fun floorArea(): Double
}

// Lớp có thể kế thừa
open class RoundHut(
    val residents: Int
) : Dwelling() {

    override val buildingMaterial = "Straw"

    override fun floorArea(): Double {
        return 50.0
    }
}

// Lớp con
class SquareCabin(
    residents: Int
) : Dwelling() {

    override val buildingMaterial = "Wood"

    override fun floorArea(): Double {
        return 100.0
    }
}

// Ghi đè phương thức
class RoundTower(
    residents: Int,
    val floors: Int
) : RoundHut(residents) {

    override fun floorArea(): Double {
        return super.floorArea() * floors
    }
}

fun main() {
    val tower = RoundTower(4, 3)
    println("Material: ${tower.buildingMaterial}")
    println("Floor area: ${tower.floorArea()}")
}
