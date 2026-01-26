//Vòng lặp
fun main() {

    val myList = listOf("A", "B", "C", "D")

    //lặp lại các mục trong danh sách
    for (element in myList) {
        println(element)
    }

    var index = 0
    while (index < myList.size) {
        println(myList[index])
        index++
    }
}