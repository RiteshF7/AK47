import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.trex.rexandroidsecureclient.MyApplication
import com.trex.rexandroidsecureclient.utils.ClientSharedPrefs
import com.trex.rexandroidsecureclient.utils.PermissionHelper

class DeviceInfoUtil {
    private val TAG: String = DeviceInfoUtil::class.java.simpleName
    private val context = MyApplication.getAppContext()

    fun getDeviceModel(): String = Build.MODEL.ifBlank { "Unknown" }

    fun getDeviceName(): String = Settings.Global.getString(context.contentResolver, Settings.Global.DEVICE_NAME) ?: "Unknown"

    fun getAndroidVersion(): String = Build.VERSION.RELEASE.ifBlank { "Unknown" }

    fun getApiLevel(): Int = Build.VERSION.SDK_INT

    @SuppressLint("HardwareIds")
    fun getDeviceId(): String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            ?: "Unknown"

    @SuppressLint("MissingPermission", "HardwareIds")
    fun requestAndSaveImei(context: Activity) {
        var imei = ""
        PermissionHelper(context).checkAndRequestPermission(
            context,
            Manifest.permission.READ_PHONE_STATE,
            "This permission is required to get IMEI number",
        ) {
            if (it) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val telephonyManager =
                        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    imei = telephonyManager.imei ?: ClientSharedPrefs.IMEI_NOT_FOUND
                    ClientSharedPrefs().saveImei(imei)
                } else {
                    Log.e("IMEI ERROR: ", "getIMEI: Imei not available android < O")
                }
            }
        }
    }

    fun saveImei() {
        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    val telephonyManager =
                        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    val imei = telephonyManager.imei ?: ClientSharedPrefs.IMEI_NOT_FOUND
                    ClientSharedPrefs().saveImei(imei)
                } catch (_: Exception) {
                }
            } else {
                ClientSharedPrefs().saveImei(ClientSharedPrefs.IMEI_NOT_FOUND)
            }
        } else {
            ClientSharedPrefs().saveImei(ClientSharedPrefs.IMEI_NOT_FOUND)
        }
    }

    fun getManufacturer(): String = Build.MANUFACTURER.ifBlank { "Unknown" }

    fun getBrand(): String = Build.BRAND.ifBlank { "Unknown" }

    fun getProduct(): String = Build.PRODUCT.ifBlank { "Unknown" }

    fun isOwnerApp(): Boolean {
        val manager =
            context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        return manager.isDeviceOwnerApp(context.packageName)
    }

    fun saveFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            ClientSharedPrefs().saveFcmToken(token)
        }
    }

    fun getAllDeviceInfo(): Map<String, String> =
        mapOf(
            "Device Model" to getDeviceModel(),
            "Device Name" to getDeviceName(),
            "Android Version" to getAndroidVersion(),
            "API Level" to getApiLevel().toString(),
            "Device ID" to getDeviceId(),
            "Manufacturer" to getManufacturer(),
            "Brand" to getBrand(),
            "Product" to getProduct(),
        )
}
