package com.trex.rexandroidsecureclient.deviceowner

class ScanQrCode {
    /*
    TODO

   // you are doing good you can do this !
  // take callback from main activity if user is saved or not
  // if yes then exit main activity
  // send a call using fcm token
   //and play audio to notify user

   find a way to sync files and file type across server and android app
//   example :: message dto and devices data classes
//   implement all actions ui and their functions
//   try imp some error checks

   you are doing good :)

   do background service work for continous and on lock actions
   have done lot of work on this project
   good job !


     * */

    /*{
  "android.app.extra.PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME": "com.trex.rexandroidsecureclient/.deviceowner.RexDeviceOwnerReceiver",
  "android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_NAME": "com.trex.rexandroidsecureclient",
  "android.app.extra.PROVISIONING_WIFI_SSID": "YourWifiSSID",
  "android.app.extra.PROVISIONING_WIFI_PASSWORD": "YourWifiPassword",
  "android.app.extra.PROVISIONING_WIFI_SECURITY_TYPE": "WPA",
  "android.app.extra.PROVISIONING_LOCALE": "en_US",
  "android.app.extra.PROVISIONING_TIME_ZONE": "America/Los_Angeles",
  "android.app.extra.PROVISIONING_ADMIN_EXTRAS_BUNDLE": {
    "shipId": "+919910000163"
  }
}



class RexDeviceOwnerReceiver : DeviceAdminReceiver() {

    override fun onProfileProvisioningComplete(
        context: Context,
        intent: Intent
    ) {
        super.onProfileProvisioningComplete(context, intent)

        val manager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val componentName = ComponentName(context.applicationContext, RexDeviceOwnerReceiver::class.java)

        // Set the profile name
        manager.setProfileName(componentName, context.getString(R.string.profile_name))

        // Retrieve the extra data passed in the provisioning QR code
        val extras = intent.getBundleExtra(DevicePolicyManager.EXTRA_PROVISIONING_ADMIN_EXTRAS_BUNDLE)

        extras?.let {
            val extraParam1 = it.getString("extra_param1")
            val extraParam2 = it.getString("extra_param2")

            // Handle the extra data in your app
            Toast.makeText(context, "Received extra data: $extraParam1, $extraParam2", Toast.LENGTH_LONG).show()
        }

        // Optionally, launch the main activity or perform further setup
        val launchIntent = Intent(context, MainActivity::class.java)
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(launchIntent)
    }
}



*/
}
