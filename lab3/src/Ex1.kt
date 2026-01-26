//Nhóm
fun main() {
    //Tạo 1 nhom từ danh sách
    val numbers = listOf(0, 3, 8, 4, 0, 5, 5, 8, 9, 2)
    val setOfNumbers = numbers.toSet()
    println("$setOfNumbers")

    //Xác định 1 nhóm
    val set1 = setOf(1, 2, 3)
    val set2 = mutableSetOf(3, 4, 5)
    println("$set1")
    println("$set2")

    //Phép toán trên nhóm
    val intersection = set1.intersect(set2)
    val union = set1.union(set2)
    println("$intersection")
    println("$union")

}