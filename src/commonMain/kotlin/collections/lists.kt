package collections

fun <T> list(capacity: Int, supplier: (x: Int) -> T): List<T> = (0 until capacity).map(supplier)
