//Danh sách
fun main() {
    //xác định 1 danh sách chỉ có thể đọc
    val numbers = listOf(1, 2, 3, 4, 5, 6)
    //Lấy kích thước của danh sách
    println(numbers.size)
    //Lấy mục đầu tiên trong danh sách
    println(numbers[0])

    //Lấy bản sao của danh sách theo thứ tự đảo ngược
    println(listOf("red", "blue", "green").reversed())

    //Xác định 1 danh sách các chuỗi có thể thay đổi
    val entrees = mutableListOf<String>()

    //Thêm 1 mục vào danh sách có thể thay đổi
    entrees.add("spaghetti")
    println(entrees)

    //Sửa đổi một mục trong danh sách có thể thay đổi
    entrees[0] = "lasagna"
    println(entrees)

    //Xóa 1 mục khỏi danh sách có thể thay đổi
    entrees.remove("lasagna")
    println(entrees)

}