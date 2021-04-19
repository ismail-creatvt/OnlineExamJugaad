package com.ismail.creatvt.onlineexamjugaad

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import java.io.ByteArrayOutputStream


fun ByteArray.getScaledByteArray(): ByteArray {
    //Convert your photo to a bitmap
    var photoBm = BitmapFactory.decodeByteArray(this, 0, size)
    //get its orginal dimensions
    val bmOriginalWidth = photoBm.width
    val bmOriginalHeight = photoBm.height
    val originalWidthToHeightRatio = 1.0 * bmOriginalWidth / bmOriginalHeight
    val originalHeightToWidthRatio = 1.0 * bmOriginalHeight / bmOriginalWidth
    //choose a maximum height
    val maxHeight = 1024
    //choose a max width
    val maxWidth = 1024
    //call the method to get the scaled bitmap
    photoBm = getScaledBitmap(
        photoBm, bmOriginalWidth, bmOriginalHeight,
        originalWidthToHeightRatio, originalHeightToWidthRatio,
        maxHeight, maxWidth
    )
    photoBm = photoBm.rotate(90f)

    val bytes = ByteArrayOutputStream()
    //compress the photo's bytes into the byte array output stream
    photoBm.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    return bytes.toByteArray()
}

fun Bitmap.rotate(angle: Float): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

private fun getScaledBitmap(
    bm: Bitmap,
    bmOriginalWidth: Int,
    bmOriginalHeight: Int,
    originalWidthToHeightRatio: Double,
    originalHeightToWidthRatio: Double,
    maxHeight: Int,
    maxWidth: Int
): Bitmap {
    var bm = bm
    if (bmOriginalWidth > maxWidth || bmOriginalHeight > maxHeight) {
        bm = if (bmOriginalWidth > bmOriginalHeight) {
            scaleDeminsFromWidth(bm, maxWidth, bmOriginalHeight, originalHeightToWidthRatio)
        } else {
            scaleDeminsFromHeight(bm, maxHeight, bmOriginalHeight, originalWidthToHeightRatio)
        }
    }
    return bm
}

private fun scaleDeminsFromHeight(
    bm: Bitmap,
    maxHeight: Int,
    bmOriginalHeight: Int,
    originalWidthToHeightRatio: Double
): Bitmap {
    var bm = bm
    val newHeight = Math.min(maxHeight.toDouble(), bmOriginalHeight * .55).toInt()
    val newWidth = (newHeight * originalWidthToHeightRatio).toInt()
    bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true)
    return bm
}

private fun scaleDeminsFromWidth(
    bm: Bitmap,
    maxWidth: Int,
    bmOriginalWidth: Int,
    originalHeightToWidthRatio: Double
): Bitmap {
    //scale the width
    var bm = bm
    val newWidth = Math.min(maxWidth.toDouble(), bmOriginalWidth * .75).toInt()
    val newHeight = (newWidth * originalHeightToWidthRatio).toInt()
    bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true)
    return bm
}