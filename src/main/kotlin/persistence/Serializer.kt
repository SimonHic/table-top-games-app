/**
 * This interface defines methods for serializing and deserializing objects.
 */
package persistence

interface Serializer {
    /**
     * Writes a saved object to a file.
     *
     * @param obj the object to write
     * @throws Exception if there is an error writing this object
     */
    @Throws(Exception::class)
    fun write(obj: Any?)

    /**
     * Reads an object from a file.
     *
     * @return the deserialized object or null if no object is found in the system.
     * @throws Exception if there is an error reading the object.
     */
    @Throws(Exception::class)
    fun read(): Any?
}
