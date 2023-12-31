package com.bignerdranch.android.a5criminalintent

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import kotlin.math.roundToInt

fun getScaleBitmap(path: String, desWidth: Int, desHeight: Int): Bitmap {

    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, options)

    val srcWidth = options.outWidth.toFloat()
    val srcHeight = options.outHeight.toFloat()

    val sampleSize = if (srcHeight <= desHeight && srcWidth <= desWidth) {
        1
    } else {
        val heightScale = srcHeight / desHeight
        val widthScale = srcWidth / desWidth

        minOf(heightScale,widthScale).roundToInt()
    }

    val sampleBitmap = BitmapFactory.decodeFile(path, BitmapFactory.Options().apply {
        inSampleSize = sampleSize
    })

    val matrix = Matrix()
    if (sampleBitmap.height < sampleBitmap.width) {
        matrix.postRotate(90F)
    } else {
        matrix.postRotate(0F)
    }
    return Bitmap.createBitmap(
        sampleBitmap,
        0,
        0,
        sampleBitmap.width,
        sampleBitmap.height,
        matrix,
        true
    )
}