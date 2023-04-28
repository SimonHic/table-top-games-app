import org.junit.jupiter.api.Test
import utils.ScannerInput
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

class CategoryUtilityTest {

    @Test
    fun testReadNextInt_validInput() {
        val input = "123"
        val expected = 123

        System.setIn(ByteArrayInputStream(input.toByteArray()))
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val actual = ScannerInput.readNextInt("Enter an integer: ")

        assertEquals(expected, actual)
        assertEquals("Enter an integer: ", outputStream.toString().substringBefore('\n'))
    }

    @Test
    fun testReadNextDouble_validInput() {
        val input = "3.14"
        val expected = 3.14

        System.setIn(ByteArrayInputStream(input.toByteArray()))
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val actual = ScannerInput.readNextDouble("Enter a double: ")

        assertEquals(expected, actual, 0.0)
        assertEquals("Enter a double: ", outputStream.toString().substringBefore('\n'))
    }

    @Test
    fun testReadNextLine() {
        val input = "This is a line of text\n"
        val expected = "This is a line of text"

        System.setIn(ByteArrayInputStream(input.toByteArray()))
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val actual = ScannerInput.readNextLine("Enter a line of text: ")

        assertEquals(expected, actual)
        assertEquals("Enter a line of text: ", outputStream.toString().substringBefore('\n'))
    }

    @Test
    fun testReadNextChar() {
        val input = "a"
        val expected = 'a'

        System.setIn(ByteArrayInputStream(input.toByteArray()))
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val actual = ScannerInput.readNextChar("Enter a character: ")

        assertEquals(expected, actual)
        assertEquals("Enter a character: ", outputStream.toString().substringBefore('\n'))
    }
}
