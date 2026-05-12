package com.example.lab9.data

import android.content.Context
import android.net.Uri
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.lab9.KEY_BLUR_LEVEL
import com.example.lab9.KEY_IMAGE_URI
import com.example.lab9.TAG_OUTPUT
import com.example.lab9.getImageUri
import com.example.lab9.workers.BlurWorker
import com.example.lab9.workers.CleanupWorker
import com.example.lab9.workers.SaveImageToFileWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class WorkManagerBluromaticRepository(context: Context) : BluromaticRepository {

    private var imageUri: Uri = context.getImageUri()
    private val workManager = WorkManager.getInstance(context)

    override val outputWorkInfo: Flow<WorkInfo?> =
        workManager.getWorkInfosByTagFlow(TAG_OUTPUT).mapNotNull {
            if (it.isNotEmpty()) it.first() else null
        }

    override fun applyBlur(blurLevel: Int) {
        var workChain = workManager.beginWith(OneTimeWorkRequest.from(CleanupWorker::class.java))

        for (i in 0 until blurLevel) {
            val blurBuilder = OneTimeWorkRequestBuilder<BlurWorker>()
            if (i == 0) {
                blurBuilder.setInputData(createInputDataForWorkRequest(blurLevel, imageUri))
            }
            workChain = workChain.then(blurBuilder.build())
        }

        val saveRequest = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
            .addTag(TAG_OUTPUT)
            .build()

        workChain = workChain.then(saveRequest)
        workChain.enqueue()
    }

    override fun cancelWork() {
        workManager.cancelUniqueWork("Lab9_Work")
    }

    private fun createInputDataForWorkRequest(blurLevel: Int, imageUri: Uri): Data {
        val builder = Data.Builder()
        builder.putString(KEY_IMAGE_URI, imageUri.toString()).putInt(KEY_BLUR_LEVEL, blurLevel)
        return builder.build()
    }
}