package org.rhasspy.mobile.nativeutils

import io.ktor.utils.io.ByteWriteChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.rhasspy.mobile.getWavHeaderForSize
import java.io.File
import java.io.FileInputStream

actual class FileStream(val file: File) : FileInputStream(file) {

    private val wavHeader = file.length().getWavHeaderForSize()

    actual val length: Long = file.length() + wavHeader.size

    actual suspend fun copyTo(out: ByteWriteChannel, bufferSize: Int): Long {
        var bytesCopied: Long = 0
        val buffer = ByteArray(bufferSize)
        //copy wav header into buffer
        wavHeader.copyInto(buffer)
        //fill buffer with data of stream
        val offset = wavHeader.size
        var bytes = withContext(Dispatchers.IO) {
            //initially offset data
            read(buffer, offset, bufferSize - offset)
        }

        while (bytes >= 0) {
            out.writeFully(buffer, 0, bytes)
            bytesCopied += bytes
            bytes = withContext(Dispatchers.IO) {
                read(buffer)
            }
        }
        return bytesCopied
    }

}