//Coroutine
import kotlinx.coroutines.*

fun main() = runBlocking {

    // Khai báo hàm tạm ngưng
    suspend fun getValue(): Double {
        delay(500)
        return 10.5
    }

    // Chạy một hàm tạm ngưng trong GlobalScope
    GlobalScope.launch {
        val output = getValue()
        println("GlobalScope output = $output")
    }

    // Gọi hàm tạm ngưng từ một hàm tạm ngưng khác
    suspend fun processValue() {
        val value = getValue()
        println("Processed value = $value")
    }
    processValue()

    // Truy cập vào một Tác vụ coroutine
    val job: Job = GlobalScope.launch {
        val output = getValue()
        println("Job output = $output")
    }

    // Hủy một Tác vụ coroutine
    job.cancel()

    // Chạy một hàm tạm ngưng và chặn luồng hiện tại cho đến khi hoàn tất
    val blockingOutput = getValue()
    println("Blocking output = $blockingOutput")

    // Sử dụng hàm không đồng bộ (async / await)
    val deferred = async {
        getValue()
    }
    println("Async output = ${deferred.await()}")
}
