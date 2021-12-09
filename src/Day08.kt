fun <K, V> Map<K, V>.reversed(): Map<V, K> = entries.associateBy({ it.value }) { it.key }

val digits = mapOf("abcefg" to 0, "cf" to 1, "acdeg" to 2, "acdfg" to 3, "bcdf" to 4, "abdfg" to 5, "abdefg" to 6, "acf" to 7, "abcdefg" to 8, "abcdfg" to 9)
val digitsToInts = mapOf(0 to listOf(0, 1, 2, 4, 5, 6), 1 to listOf(2, 5), 2 to listOf(0, 2, 3, 4, 6), 3 to listOf(0, 2, 3, 5, 6), 4 to listOf(1, 3, 2, 5), 5 to listOf(0, 1, 3, 5, 6), 6 to listOf(0, 1, 3, 5, 6, 4), 7 to listOf(0, 2, 5), 8 to listOf(0, 1, 2, 3, 4, 5, 6), 9 to listOf(0, 1, 3, 2, 5, 6)).map { Pair(it.key, it.value.sorted()) }.toMap()
val intsToDigits = digitsToInts.reversed()

// Permutations algorithm: https://stackoverflow.com/a/40577843/318460
fun <T> permutations(list: List<T>): MutableList<List<T>> {
	if (list.isEmpty()) {
		return mutableListOf()
	}

	val result = mutableListOf<List<T>>()

	for (i in list.indices) {
		val elem = list[i]
		val copy = list.toMutableList()

		copy.removeAt(i)

		val permRest = permutations(copy)

		if (permRest.isEmpty()) {
			permRest.add(mutableListOf())
		}

		for (perm in permRest) {
			val permCopy = perm.toMutableList()
			permCopy.add(0, elem)
			result.add(permCopy)
		}
	}

	return result
}

fun permutationsLists(list: List<*>): MutableList<List<*>> {
	if (list.isEmpty()) {
		return mutableListOf()
	}

	if (list[0] !is List<*>) {
		return permutations(list)
	}

	val permutationsFirst = permutationsLists(list[0] as List<*>)
	val permutationsRest = permutationsLists(list.subList(1, list.size))

	if (permutationsRest.isEmpty()) {
		permutationsRest.add(mutableListOf<Any>())
	}

	val result = mutableListOf<List<*>>()

	for (pf in permutationsFirst) {
		for (pr in permutationsRest) {
			val copy = pr.toMutableList()
			copy.add(0, pf)
			result.add(copy)
		}
	}

	return result
}

fun <K, V> MutableMap<K, V>.swap(key1: K, key2: K) {
	val value1 = this[key1]
	val value2 = this[key2]

	if (value2 == null) {
		this.remove(key1)
	}
	else {
		this[key1] = value2
	}

	if (value1 == null) {
		this.remove(key2)
	}
	else {
		this[key2] = value1
	}
}

fun mergeIndicators(indicators: List<String>): String {
	val height = indicators.first().split("\n").size
	val mergedLines = mutableListOf<String>()

	repeat(height) { mergedLines.add("") }

	indicators.forEach {
		val lines = it.split("\n")

		lines.forEachIndexed { index, s ->
			mergedLines[index] += "$s "
		}
	}

	return mergedLines.joinToString("\n")
}

fun indicatorFromSegments(list: List<Int>): String {
	return buildString {
		if (list.contains(0)) {
			append(" ----- \n")
		}
		else {
			append("       \n")
		}

		if (list.contains(1) && list.contains(2)) {
			repeat(3) { append("|     |\n") }
		}
		else if (list.contains(1)) {
			repeat(3) { append("|      \n") }
		}
		else if (list.contains(2)) {
			repeat(3) { append("      |\n") }
		}

		if (list.contains(3)) {
			append(" ----- \n")
		}
		else {
			append("       \n")
		}

		if (list.contains(4) && list.contains(5)) {
			repeat(3) { append("|     |\n") }
		}
		else if (list.contains(4)) {
			repeat(3) { append("|      \n") }
		}
		else if (list.contains(5)) {
			repeat(3) { append("      |\n") }
		}

		if (list.contains(6)) {
			append(" ----- \n")
		}
		else {
			append("       \n")
		}
	}
}

class Indicator {
	private val mapping = mutableMapOf<Int, Char>()

	init {
		for (i in 0..6) {
			mapping[i] = '-'
		}
	}

	fun copy(): Indicator {
		val indicator = Indicator()
		indicator.mapping.putAll(this.mapping)
		return indicator
	}

	operator fun set(key: Int, value: Char) {
		mapping[key] = value
	}

	operator fun get(key: Int): Char {
		return mapping[key] ?: '-'
	}

	override fun toString(): String {
		return mapping.toString()
	}

	fun fromString(value: String): Int {
		val reversedMapping = mapping.reversed()
		return intsToDigits[value.map { reversedMapping[it]!! }.sorted()] ?: throw RuntimeException("Unsupported indicator sequence: $value")
	}

	fun toInts(value: String): List<Int> {
		val reversedMapping = mapping.reversed()

		return value.map {
			reversedMapping[it]!!
		}
	}

	fun swap(index1: Int, index2: Int) {
		mapping.swap(index1, index2)
	}
}

fun buildIndicator(segmentMapping: Map<Int, String>): Indicator {
	val indicator = Indicator()

	val one = segmentMapping[1]!!.apply {
		// one
		indicator[2] = this[0]
		indicator[5] = this[1]
	}

	val seven = segmentMapping[7]!!.apply {
		// seven
		indicator[0] = this.toList().minus(one.toList().toSet())[0]
	}

	val four = segmentMapping[4]!!.apply {
		// four
		val segments = this.toList().minus(one.toList().toSet())

		indicator[1] = segments[0]
		indicator[3] = segments[1]
	}

	segmentMapping[8]!!.apply {
		// eight
		val segments = this.toList().minus(seven.toList().toSet()).minus(four.toList().toSet())

		indicator[4] = segments[0]
		indicator[6] = segments[1]
	}

	println("Mapping: $indicator")

	return indicator
}

class Message(signal: List<String>, output: List<String>) {
	val signal: List<String>
	val output: List<String>

	init {
		this.signal = signal.map { it.toSortedSet().joinToString("") }
		this.output = output.map { it.toSortedSet().joinToString("") }
	}

	fun join(): List<String> = signal + output

	override fun toString(): String = "${signal.joinToString(" ")} | ${output.joinToString(" ")}"
}

fun main() {
	fun uniqueDigits(): List<String> {
		val counter = mutableMapOf<Int, Int>()

		digits.forEach {
			counter[it.key.length] = (counter[it.key.length] ?: 0) + 1
		}

		return counter.filter {
			it.value == 1
		}.flatMap { entry ->
			digits.keys.filter { it.length == entry.key }
		}
	}

	fun processInput(input: List<String>): List<Message> = input.mapNotNull {
		if (it.isEmpty()) {
			return@mapNotNull null
		}

		val entry = it.split(" | ")

		Message(signal = entry[0].split(" "), output = entry[1].split(" "))
	}

	val uniqueDigits = uniqueDigits()
	val uniqueLengths = uniqueDigits.map { it.length }

	fun part1(input: List<String>): Int {
		val messages = processInput(input)

		val uniqueBrokenMessages = messages.flatMap { msg ->
			msg.output.filter {
				uniqueLengths.contains(it.length)
			}
		}

		return uniqueBrokenMessages.size
	}

	fun calcValues(strings: List<String>, indicator: Indicator): List<Int>? {
		return strings.map {
			try {
				return@map indicator.fromString(it)
			}
			catch (e: RuntimeException) {
				return null
			}
		}
	}

	fun part2(input: List<String>): Int {
		val messages = processInput(input)
		val swappableSegments = listOf(listOf(1, 3), listOf(4, 6), listOf(2, 5))
		val possibleSwaps = permutationsLists(swappableSegments)
		val baseCombination = possibleSwaps[0]

		val outputs = messages.map { msg ->
			println("Encoded: $msg")

			val uniqueBrokenEntries = msg.join().filter { uniqueLengths.contains(it.length) }
			val decodedBrokenEntries = uniqueBrokenEntries.map { entry -> digits[uniqueDigits.find { it.length == entry.length }]!! }
			val segmentMapping = uniqueBrokenEntries.mapIndexed { index, s -> Pair(decodedBrokenEntries[index], s) }.toMap()

			println("Easy mapping: $segmentMapping")

			val indicator = buildIndicator(segmentMapping)
			var swappedIndicator: Indicator? = null

			val outputValues = run {
				for (i in 0 until possibleSwaps.size) {
					swappedIndicator = indicator.copy()

					val swaps = possibleSwaps[i]

					for (j in swaps.indices) {
						val possibleSwap = swaps[j] as List<Int>

						if (possibleSwap != baseCombination[j]) {
							swappedIndicator!!.swap(possibleSwap[0], possibleSwap[1])
							println("Swapping ${possibleSwap[0]} and ${possibleSwap[1]}")
						}
					}

					val check = calcValues(msg.signal, swappedIndicator!!)
					val result = calcValues(msg.output, swappedIndicator!!)

					if (check != null && result != null) {
						return@run result
					}
				}

				return@run null
			} ?: throw RuntimeException("Failed to determine correct indicator mapping")

			println("Created indicator:")

			println("=== Signal ===")

			println(mergeIndicators(msg.signal.map {
				indicatorFromSegments(swappedIndicator!!.toInts(it))
			}))

			println("=== Output ===")

			println(mergeIndicators(msg.output.map {
				indicatorFromSegments(swappedIndicator!!.toInts(it))
			}))

			val mapping: (String) -> String = {
				swappedIndicator!!.fromString(it).toString()
			}

			val signal = msg.signal.map(mapping)
			val output = msg.output.map(mapping)

			val decodedMessage = Message(signal = signal, output = output)

			println("Decoded: $decodedMessage")

			println("--")

			return@map outputValues
		}

		return outputs.sumOf { it.joinToString("").toInt() }
	}

	val smallTestInput = readInput("Day08_test_small")
	println(smallTestInput)
	check(part2(smallTestInput) == 5353)

	val testInput = readInput("Day08_test")
	println(testInput)
	check(part1(testInput) == 26)
	check(part2(testInput) == 61229)

	val input = readInput("Day08")
	println(part1(input))
	println(part2(input))
}
