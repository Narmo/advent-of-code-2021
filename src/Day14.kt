fun main() {
	data class Template(val template: String, val rules: Map<String, String>)

	fun loadTemplate(input: List<String>): Template {
		val template = input.first()
		val rules = mutableMapOf<String, String>()

		for (i in 2 until input.size) {
			val ruleParams = input[i].split(" -> ")
			rules[ruleParams[0]] = ruleParams[1]
		}

		return Template(template, rules)
	}

	fun task(input: List<String>, steps: Int): Long {
		val template = loadTemplate(input)
		val polymer = mutableMapOf<String, Long>()
		val elements = mutableMapOf<String, Long>()

		template.template.windowed(2, 1) {
			it.toString().run {
				polymer[this] = (polymer[this] ?: 0L) + 1L
			}

			it.forEach { element ->
				elements[element.toString()] = (elements[element.toString()] ?: 0L) + 1L
			}
		}

		repeat(steps) { step ->
			val insertions = mutableMapOf<String, Long>()
			val removals = mutableListOf<String>()

			polymer.keys.forEach { pair ->
				val rule = template.rules[pair] ?: return@forEach
				val counter = polymer[pair]!!

				removals.add(pair)

				val elementsInPair = pair.toCharArray()

				"${elementsInPair[0]}${rule}".run {
					insertions[this] = (insertions[this] ?: 0L) + counter
				}

				"${rule}${elementsInPair[1]}".run {
					insertions[this] = (insertions[this] ?: 0L) + counter
				}

				elements[rule] = (elements[rule] ?: 0) + counter
			}

			removals.forEach {
				polymer.remove(it)
			}

			insertions.forEach {
				polymer[it.key] = (polymer[it.key] ?: 0L) + it.value
			}

			println("Step $step, polymer elements count is $elements")
		}

		val mostCommon = elements.maxByOrNull { it.value }!!.value
		val leastCommon = elements.minByOrNull { it.value }!!.value

		return mostCommon - leastCommon
	}

	fun part1(input: List<String>): Long {
		return task(input, 10)
	}

	fun part2(input: List<String>): Long {
		return task(input, 40)
	}

	val testInput = readInput("Day14_test")
	println(testInput)
	check(part1(testInput) == 1588L)
	check(part2(testInput) == 2188189693529)

	val input = readInput("Day14")
	println(part1(input))
	println(part2(input))
}
