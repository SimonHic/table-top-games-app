/**
 * Gives implementation of one of two Serializers for saving and loading
 * objects from a file (Depending on which is commented in and out).
 *
 * The persistence package includes a JSONSerializer for reading and also writing the desired objects to
 * JSON files that can be called upon for each instance that the system is booted up.
 */
package persistence

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver
import models.Play
import models.Game
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class JSONSerializer(private val file: File) : Serializer {

    /**
     * This Reads the Serialized Game and Play objects from a JSON file.
     *It implements the Serializer interface for reading and writing both the Game and Play objects.
     */
    @Throws(Exception::class)
    override fun read(): Any {
        val xStream = XStream(JettisonMappedXmlDriver())
        xStream.allowTypes(arrayOf(Game::class.java, Play::class.java))
        val inputStream = xStream.createObjectInputStream(FileReader(file))
        val obj = inputStream.readObject() as Any
        inputStream.close()
        return obj
    }

    /**
     * This Writes the Serialized Game and Play objects to a JSON file.
     * It also throws an exception in the event of an error occurring during the serialization process.
     */
    @Throws(Exception::class)
    override fun write(obj: Any?) {
        val xStream = XStream(JettisonMappedXmlDriver())
        val outputStream = xStream.createObjectOutputStream(FileWriter(file))
        outputStream.writeObject(obj)
        outputStream.close()
    }
}
