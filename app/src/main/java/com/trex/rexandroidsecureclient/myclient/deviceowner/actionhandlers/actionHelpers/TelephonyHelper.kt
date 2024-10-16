import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat

class TelephonyHelper(
    private val context: Context,
) {
    // Class-level variable for TelephonyManager
    private val telephonyManager: TelephonyManager? by lazy {
        context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
    }

    // General permission check function that will return null if permission is not granted
    private fun <T> checkPermissionsAndExecute(action: () -> T?): T? =
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            action() // If permission is granted, execute the provided function
        } else {
            Log.i("TelephonyHelper", "Permission not granted for accessing telephony information")
            null // Return null if permission is not granted
        }

    // Get the phone number
    fun getPhoneNumber(): String? =
        checkPermissionsAndExecute {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_SMS,
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_NUMBERS,
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@checkPermissionsAndExecute "Phone permission not granted"
            }
            telephonyManager?.line1Number
        }

    // Get the IMEI
    fun getImei(): String? =
        checkPermissionsAndExecute {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                telephonyManager?.imei
            } else {
                telephonyManager?.deviceId // Deprecated in newer versions, but needed for older ones
            }
        }

    // Get SIM country ISO
    fun getSimCountryIso(): String? =
        checkPermissionsAndExecute {
            telephonyManager?.simCountryIso
        }

    // Get SIM operator name
    fun getSimOperatorName(): String? =
        checkPermissionsAndExecute {
            telephonyManager?.simOperatorName
        }

    // Get network operator name
    fun getNetworkOperatorName(): String? =
        checkPermissionsAndExecute {
            telephonyManager?.networkOperatorName
        }

    // Get device software version
    fun getDeviceSoftwareVersion(): String? =
        checkPermissionsAndExecute {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@checkPermissionsAndExecute "Phone permission not granted"
            }
            telephonyManager?.deviceSoftwareVersion
        }
}
