//khác
package com.example.affirmations.model

// Nhập từ thư viện toán học Kotlin
import kotlin.math.PI

// ----------- Ví dụ lớp để dùng cho with -----------
class SquareCabin(
    val capacity: Int,
    val buildingMaterial: String
) {
    fun hasRoom(): Boolean {
        return capacity > 0
    }
}

class Text {
    override fun toString(): String {
        return "100"
    }
}

class CostOfService {
    val text = Text()
}

class Binding {
    val costOfService = CostOfService()
}

// ----------- Hàm có số lượng đối số thay đổi -----------
fun addToppings(vararg toppings: String) {
    for (topping in toppings) {
        println("Topping: $topping")
    }
}

// ----------- Hàm main -----------
fun main() {

    // Thao tác chỉ định tăng cường
    var a = 10
    val b = 5

    a += b   // a = a + b
    a -= b   // a = a - b
    a *= b   // a = a * b
    a /= b   // a = a / b

    println("Giá trị của a: $a")

    // Sử dụng with để đơn giản hóa việc truy cập vào một đối tượng
    val squareCabin = SquareCabin(
        capacity = 4,
        buildingMaterial = "Wood"
    )

    with(squareCabin) {
        println("Capacity: $capacity")
        println("Material: $buildingMaterial")
        println("Has room? ${hasRoom()}")
    }

    // Sử dụng tên đủ điều kiện cho một hằng số trong thư viện toán học Kotlin
    val radius = 3.0
    val area = kotlin.math.PI * radius * radius
    println("Area: $area")

    // Xâu chuỗi các lệnh gọi với nhau
    val binding = Binding()
    val stringInTextField = binding.costOfService.text.toString()
    println("Cost of service: $stringInTextField")

    // Gọi hàm vararg
    addToppings("Cheese", "Olives", "Mushrooms")
}
