/**
 * Gives implementation of one of two Serializers for saving and loading
 * objects from a file (Depending on which is commented in and out).
 *
 * The persistence package includes a XMLSerializer for reading and also writing the desired objects to
 * XML files that can be called upon for each instance that the system is booted up.
 */
package persistence
/*
import java.io.File
import kotlin.Throws
import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import models.Note
import java.io.FileReader
import java.io.FileWriter
import java.lang.Exception

class XMLSerializer(private val file: File) : Serializer {

    /**
     * This Reads the Serialized Game and Play objects from an XML file.
     * It implements the Serializer interface for reading and writing both the Game and Play objects.
     */
    @Throws(Exception::class)
    override fun read(): Any {
        val xStream = XStream(DomDriver())
        xStream.allowTypes(arrayOf(Note::class.java, Item::class.java))
        val inputStream = xStream.createObjectInputStream(FileReader(file))
        val obj = inputStream.readObject() as Any
        inputStream.close()
        return obj
    }

    /**
     * This Writes the Serialized Game and Play objects to an XML file.
     * It also throws an exception in the event of an error occurring during the serialization process.
     */
    @Throws(Exception::class)
    override fun write(obj: Any?) {
        val xStream = XStream(DomDriver())
        val outputStream = xStream.createObjectOutputStream(FileWriter(file))
        outputStream.writeObject(obj)
        outputStream.close()
    }
}*/
