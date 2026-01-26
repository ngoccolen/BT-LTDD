//Sơ đồ
fun main() {
    //Xác định 1 sơ đồ có thể thay đổi
    val peopleAges = mutableMapOf<String, Int>(
        "Fred" to 30,
        "Ann" to 23
    )
    println("Ban đầu: $peopleAges")


    //Đặt 1 giá trị trong sơ đồ có thể thay đổi
    peopleAges.put("Barbara", 42)
    peopleAges["Joe"] = 51
    println("Sau khi thêm phần tử: $peopleAges")

}