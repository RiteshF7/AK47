import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings
import com.trex.rexandroidsecureclient.MyApplication

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

    fun getManufacturer(): String = Build.MANUFACTURER.ifBlank { "Unknown" }

    fun getBrand(): String = Build.BRAND.ifBlank { "Unknown" }

    fun getProduct(): String = Build.PRODUCT.ifBlank { "Unknown" }

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
