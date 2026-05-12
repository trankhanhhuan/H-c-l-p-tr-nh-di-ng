package com.example.lab9.workers

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.lab9.CHANNEL_ID
import com.example.lab9.NOTIFICATION_ID
import com.example.lab9.NOTIFICATION_TITLE
import com.example.lab9.OUTPUT_PATH
import com.example.lab9.R
import com.example.lab9.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
import com.example.lab9.VERBOSE_NOTIFICATION_CHANNEL_NAME
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

private const val TAG = "Lab9_Utils"

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun makeStatusNotification(message: String, context: Context) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                VERBOSE_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            notificationManager?.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    } catch (e: SecurityException) {
        // Bắt lỗi Android 13+ chặn thông báo để Worker không bị chết oan
        Log.e(TAG, "Không có quyền hiện thông báo, nhưng kệ cứ chạy tiếp!", e)
    }
}

@WorkerThread
fun blurBitmap(bitmap: Bitmap, blurLevel: Int): Bitmap {
    return try {
        val safeBlurLevel = if (blurLevel > 0) blurLevel else 1

        // Hạ hệ số xuống 10 hoặc 15 thôi, cao quá ảnh thành cục pixel
        val scaleFactor = safeBlurLevel * 10

        val targetWidth = (bitmap.width / scaleFactor).coerceAtLeast(1)
        val targetHeight = (bitmap.height / scaleFactor).coerceAtLeast(1)

        val input = Bitmap.createScaledBitmap(
            bitmap,
            targetWidth,
            targetHeight,
            true
        )
        Bitmap.createScaledBitmap(input, bitmap.width, bitmap.height, true)

    } catch (e: ArithmeticException) {
        Log.e(TAG, "Lỗi tính toán: Chia cho 0", e)
        bitmap
    } catch (e: Exception) {
        Log.e(TAG, "Lỗi khi xử lý ảnh", e)
        bitmap
    }
}

@Throws(FileNotFoundException::class)
fun writeBitmapToFile(applicationContext: Context, bitmap: Bitmap): Uri {
    val name = String.format("lab9-output-%s.png", UUID.randomUUID().toString())
    val outputDir = File(applicationContext.filesDir, OUTPUT_PATH)

    if (!outputDir.exists()) {
        outputDir.mkdirs()
    }

    val outputFile = File(outputDir, name)
    var out: FileOutputStream? = null

    try {
        out = FileOutputStream(outputFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, out)
    } finally {
        out?.let {
            try {
                it.close()
            } catch (e: IOException) {
                Log.e(TAG, e.message.toString())
            }
        }
    }
    return Uri.fromFile(outputFile)
}