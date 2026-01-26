//Khác
fun main() {

    val example = Example()
    println(example.currentScrambledWord)

    val letterId = example.intent?.extras?.get("letter")
    println(letterId)

    val triple: (Int) -> Int = { a -> a * 3 }
    println(triple(5))

    println(DetailActivity.LETTER)

    println(example.viewModel.name)

    example.initWord()
    println(example.currentWord)

    var quantity: Int? = null
    println(quantity ?: 0)
    quantity = 4
    println(quantity ?: 0)
}

class Example {

    // Thuộc tính dự phòng
    private var _currentScrambledWord = "test"
    val currentScrambledWord: String
        get() = _currentScrambledWord

    // Fake intent để dùng safe call
    val intent: FakeIntent? = FakeIntent(mapOf("letter" to "A"))

    // Ủy quyền thuộc tính (delegate)
    val viewModel: GameViewModel by lazy {
        GameViewModel()
    }

    // Khởi tạo trễ
    lateinit var currentWord: String

    fun initWord() {
        currentWord = "Kotlin"
    }
}


class DetailActivity {
    companion object {
        const val LETTER = "letter"
    }
}

class FakeIntent(val extras: Map<String, String>?)

class GameViewModel {
    val name = "GameViewModel instance"
}
