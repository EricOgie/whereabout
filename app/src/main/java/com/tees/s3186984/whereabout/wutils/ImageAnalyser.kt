package com.tees.s3186984.whereabout.wutils

import android.graphics.ImageFormat
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer

/**
 * ImageAnalyser is an implementation of ImageAnalysis.Analyzer that processes camera frames
 * to detect and decode QR codes. It uses the ZXing library for decoding QR codes and supports
 * specific YUV image formats.
 *
 * @property onQRCodeScan A callback function invoked when a QR code is successfully scanned twice consecutively.
 */
class ImageAnalyser(private  val onQRCodeScan: (String) -> Unit): ImageAnalysis.Analyzer {

    // Supported image formats
    private val supportImageFormats = listOf(
        ImageFormat.YUV_444_888,
        ImageFormat.YUV_422_888,
        ImageFormat.YUV_420_888
    )

    private var lastScannedResult: String? = null   // Tracks the last successfully scanned QR code text
    private var scanCount = 0                      // Counts consecutive successful scans of the same QR code


    /**
     * Analyzes the provided image for QR codes.
     *
     * @param image The image frame from the camera to be analyzed.
     */
    override fun analyze(image: ImageProxy) {
        if (image.format in supportImageFormats){
            val imagePlanesAsBytes = image.planes.first().buffer.toByteArray()
            val source = PlanarYUVLuminanceSource(
                imagePlanesAsBytes, image.width, image.height, 0, 0,
                image.width, image.height, false
            )

            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

            // Decode binaryBitmap into actual connection Request data as JSON string
            try {
                val result = MultiFormatReader().apply {
                    setHints(
                        mapOf(DecodeHintType.POSSIBLE_FORMATS to arrayListOf(BarcodeFormat.QR_CODE))
                    )
                }.decode(binaryBitmap )

                if (result.text == lastScannedResult){
                    scanCount++
                }else{
                    lastScannedResult = result.text
                    scanCount = 1
                }

                if (scanCount >= 2 ) onQRCodeScan(result.text)

            }catch (e: Exception){
                Log.d("DECODING ERROR", "analyze: ${e.message}")
                e.printStackTrace()
            } finally {
                image.close()
            }

        }

    }

    /**
     * Converts a ByteBuffer into a ByteArray.
     *
     * @receiver ByteBuffer The ByteBuffer to convert.
     * @return A ByteArray containing the buffer's data.
     */
    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        return ByteArray(remaining()).also { result -> get(result) }
    }

}