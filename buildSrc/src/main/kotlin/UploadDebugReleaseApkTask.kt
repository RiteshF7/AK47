import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.concurrent.TimeUnit

// Create custom task type
abstract class UploadDebugReleaseApkTask : DefaultTask() {
    @TaskAction
    fun uploadApk() {
        val client =
            OkHttpClient
                .Builder()
                .connectTimeout(160, TimeUnit.SECONDS)
                .writeTimeout(160, TimeUnit.SECONDS)
                .readTimeout(160, TimeUnit.SECONDS)
                .build()

        try {
            val releaseApk =
                File("${project.buildDir}/outputs/apk/debug")
                    .listFiles()
                    ?.find { it.name.endsWith("-debug.apk") }
                    ?: throw IllegalStateException("Release APK not found!")

            val requestBody =
                MultipartBody
                    .Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "file",
                        releaseApk.name,
                        releaseApk.asRequestBody("application/vnd.android.package-archive".toMediaType()),
                    ).addFormDataPart("version", "v1")
                    .addFormDataPart("buildType", "release")
                    .build()

            val request =
                Request
                    .Builder()
                    .url("https://shieldserver-00on.onrender.com/api/debug/upload")
                    .post(requestBody)
                    .build()

            client.newCall(request).enqueue(
                object : okhttp3.Callback {
                    override fun onFailure(
                        call: okhttp3.Call,
                        e: java.io.IOException,
                    ) {
                        println("Error uploading APK: ${e.message}")
                        e.printStackTrace()
                    }

                    override fun onResponse(
                        call: okhttp3.Call,
                        response: okhttp3.Response,
                    ) {
                        if (!response.isSuccessful) {
                            println("Upload failed: ${response.code} ${response.message}")
                            throw IllegalStateException("Upload failed: ${response.code} ${response.message}")
                        }
                        println("Successfully uploaded release APK")
                        response.body?.string()
                    }
                },
            )
        } catch (e: Exception) {
            println("Error uploading APK: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}
