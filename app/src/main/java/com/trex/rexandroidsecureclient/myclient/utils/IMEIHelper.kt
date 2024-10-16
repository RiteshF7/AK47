import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat

class IMEIHelper(
    private val context: Activity,
) {
    companion object {
        const val PERMISSION_REQUEST_CODE = 100
    }

    // Check if permission is granted
    fun isPermissionGranted(): Boolean =
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE,
        ) == PackageManager.PERMISSION_GRANTED

    // Request permission
    fun requestIMEIPermission() {
        if (!isPermissionGranted()) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                PERMISSION_REQUEST_CODE,
            )
        }
    }

    fun getIMEINumber(): String? {
        if (isPermissionGranted()) {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                telephonyManager.imei
            } else {
                telephonyManager.deviceId
            }
        }
        return null
    }
}
