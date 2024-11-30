package com.tees.s3186984.whereabout.wutils

import android.graphics.Bitmap
import com.google.zxing.BinaryBitmap
import android.graphics.Color
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.WriterException
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import com.google.zxing.qrcode.QRCodeWriter


/**
 * QRCodeUtils provides utility functions for generating and decoding QR codes.
 * It uses the ZXing library to handle QR code operations.
 */
class QRCodeUtils(){

    companion object{

        /**
         * Generates a QR code as a Bitmap from the provided data.
         *
         * @param data The data to encode in the QR code.
         * @param width The width of the generated QR code bitmap. Default is 250 pixels.
         * @param height The height of the generated QR code bitmap. Default is 250 pixels.
         * @return A Bitmap representing the QR code, or null if an error occurs.
         */
        fun generateQRCode(data: String, width: Int = 250, height: Int = 250): ImageBitmap? {
            return try {
                val writer = QRCodeWriter()
                val bitMapMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height)
                var bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

                for (stripX in 0 until width){
                    for (stripY in 0 until height){
                        bitmap.setPixel(stripX, stripY, if(bitMapMatrix[stripX, stripY]) Color.BLACK else Color.WHITE)
                    }
                }

                bitmap.asImageBitmap()

            }catch (e: WriterException){
                Log.d("QR Code generate Error", "Error: ${e.message}")
                null
            }

        }

        /**
         * Decodes a Bitmap containing a QR code into a String.
         *
         * @param bitmap The Bitmap containing the QR code to decode.
         * @return The decoded text from the QR code, or null if decoding fails.
         */
        fun decodeQRBitMap(bitmap: Bitmap): String? {
            val reader = QRCodeReader()
            val intArray = IntArray(bitmap.width * bitmap.height)
            bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
            val source = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

            return try {
                val result = reader.decode(binaryBitmap)
                result.text

            }catch(e: Exception){
                Log.d("QR Code Reader Error", "Error: ${e.message}")
                return null
            }
        }





    }
}