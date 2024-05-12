import kotlin.math.abs
import kotlin.random.Random

fun main() {
    var field = randomBattleField()
    printField(field)
    val emptyField = List(9) { "_" }
//    val sequence = readSequence()
    field = createBattleField(emptyField)
    printField(field)
    var resultPlay = checkWinner(field)
    println(resultPlay)
    var xOrO = 9
    while (resultPlay == "Game not finished") {
        field = makeMove(field, (xOrO % 2 == 1))
        printField(field)
        resultPlay = checkWinner(field)
        println(resultPlay)
        xOrO --
    }
}

fun randomBattleField(): List<List<String>> =
    List(3) {
        List(3) { if (Random.nextBoolean()) "X" else "O" }
    }

fun createBattleField(sequence: List<String>): List<List<String>> =
     listOf (
        sequence.subList(0, 3),
        sequence.subList(3, 6),
        sequence.subList(6, 9)
    )

fun checkWinner(field: List<List<String>>): String {
    fun countOfElement(value: String): Int {
        var sum = 0; field.forEach { sum += it.count { valueElem -> valueElem == value } }
        return sum
    }
    fun isWin (field: List<List<String>>, value: String): Boolean =
        (field[0][0] == value && ((field[1][0] == value && field[2][0] == value) || (field[0][1] == value && field[0][2] == value) || (field[1][1] == value && field[2][2] == value))) ||
                (field[0][1] == value && (field[1][1] == value && field[2][1] == value)) ||
                (field[0][2] == value && (field[1][2] == value && field[2][2] == value || (field[1][1] == value && field[2][0] == value))) ||
                (field[1][0] == value && field[1][1] == value && field[1][2] == value) ||
                (field[2][0] == value && field[2][1] == value && field[2][2] == value)

    val xWin = isWin(field, "X")
    val oWin = isWin(field, "O")
    val countX = countOfElement("X")
    val countO = countOfElement("O")

    return when {
        xWin && oWin || abs(countX - countO) >= 2 -> "Impossible"
        xWin -> "X Win"
        oWin -> "O Win"
        countO + countX == 9 -> "Draw"

        else -> "Game not finished"
    }
}

fun makeMove(field: List<List<String>>, isX: Boolean): List<MutableList<String>> {
    val newField = field.map { it.toMutableList() }
    while(true) {
        try {
            val (i, j) = readln().split(" ").map { it.toInt() }
            when {
                i > 3 || j > 3 || i < 1 || j < 1 -> println("Coordinates should be from 1 to 3!")
                field[i - 1][j - 1] == "O" || field[i - 1][j - 1] == "X" -> println("This cell is occupied! Choose another one!")
                else -> {
                    newField[i - 1][j - 1] = if (isX) "X" else "O"
                    break
                }
            }
        } catch (e: Exception) {
            println("You should enter numbers!")
        }
    }
    return newField
}

fun printField(field: List<List<String>>) {
    val fieldSpace = List(3) { MutableList(3) {" "} }

    for(i in field.indices) {
        field[i].forEach {
            if (it != "_") fieldSpace[i][field[i].indexOf(it)] = it
        }

    }
    println(
        """
        It's battle of field
        ---------
        | ${fieldSpace[0].joinToString(" ")} |
        | ${fieldSpace[1].joinToString(" ")} |
        | ${fieldSpace[2].joinToString(" ")} |
        ---------
        """.trimIndent()
    )
}

fun readSequence(): MutableList<String> {
    println("\nTo play, please enter sequence of 9: \"X\", \"0\" or \"_\"")
    val sequence = readln().split("").toMutableList()
    sequence.removeFirst()
    sequence.removeLast()
    return sequence
}