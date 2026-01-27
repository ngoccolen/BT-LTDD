//Khác
object DataProviderManager {
    fun printInfo() {
        println("DataProviderManager is working")
    }
}
enum class Direction {
    NORTH, SOUTH, WEST, EAST
}
fun main() {

    // Truy cập object
    DataProviderManager.printInfo()

    // Phát hiện và xử lý ngoại lệ
    try {
        val x = 10 / 0
    } catch (exception: Exception) {
        println("Exception caught: ${exception.message}")
    }

    // Truy cập vào một giá trị enum
    val direction = Direction.NORTH

    // Kiểm tra các giá trị enum
    when (direction) {
        Direction.NORTH -> println("Go North")
        Direction.SOUTH -> println("Go South")
        Direction.WEST  -> println("Go West")
        Direction.EAST  -> println("Go East")
    }
}
