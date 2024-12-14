//
//// FCMService.kt
//class FCMService : FirebaseMessagingService() {
//    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
//    private lateinit var workManager: WorkManager
//    private var wakeLock: PowerManager.WakeLock? = null
//
//    companion object {
//        private const val KEEP_ALIVE_INTERVAL = 15L // minutes
//        private const val WAKELOCK_TIMEOUT = 10L // minutes
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        workManager = WorkManager.getInstance(this)
//        setupKeepAliveJob()
//        setupFCMHealthCheck()
//    }
//
//    private fun setupKeepAliveJob() {
//        val keepAliveRequest = PeriodicWorkRequestBuilder<FCMKeepAliveWorker>(
//            KEEP_ALIVE_INTERVAL, TimeUnit.MINUTES
//        ).setConstraints(
//            Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .build()
//        ).build()
//
//        workManager.enqueueUniquePeriodicWork(
//            "fcm_keepalive",
//            ExistingPeriodicWorkPolicy.REPLACE,
//            keepAliveRequest
//        )
//    }
//
//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//        scope.launch {
//            updateTokenOnServer(token)
//        }
//    }
//
//    private fun updateTokenOnServer(token: String) {
//        val tokenUpdateWork = OneTimeWorkRequestBuilder<TokenUpdateWorker>()
//            .setInputData(workDataOf("token" to token))
//            .setBackoffCriteria(
//                BackoffPolicy.EXPONENTIAL,
//                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
//                TimeUnit.MILLISECONDS
//            )
//            .build()
//
//        workManager.enqueue(tokenUpdateWork)
//    }
//
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        super.onMessageReceived(remoteMessage)
//        acquireWakeLock()
//        scope.launch {
//            try {
//                processMessage(remoteMessage)
//            } finally {
//                releaseWakeLock()
//            }
//        }
//    }
//
//    private fun acquireWakeLock() {
//        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
//            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "FCMService:WakeLock").apply {
//                acquire(TimeUnit.MINUTES.toMillis(WAKELOCK_TIMEOUT))
//            }
//        }
//    }
//
//    private fun releaseWakeLock() {
//        wakeLock?.let {
//            if (it.isHeld) {
//                it.release()
//            }
//        }
//        wakeLock = null
//    }
//
//    private suspend fun processMessage(remoteMessage: RemoteMessage) {
//        withContext(Dispatchers.Default) {
//            // Process your message here
//            // Example: Update UI using Flow
//            messageFlow.emit(remoteMessage.data)
//        }
//    }
//
//    companion object {
//        private val messageFlow = MutableSharedFlow<Map<String, String>>()
//
//        fun observeMessages(): Flow<Map<String, String>> = messageFlow.asSharedFlow()
//    }
//}
//
//// FCMKeepAliveWorker.kt
//class FCMKeepAliveWorker(
//    context: Context,
//    params: WorkerParameters
//) : CoroutineWorker(context, params) {
//
//    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
//        try {
//            FirebaseMessaging.getInstance().subscribeToTopic("admin_channel").await()
//            Result.success()
//        } catch (e: Exception) {
//            requestTokenRefresh()
//            Result.retry()
//        }
//    }
//
//    private suspend fun requestTokenRefresh() {
//        try {
//            FirebaseMessaging.getInstance().deleteToken().await()
//            FirebaseMessaging.getInstance().token.await()
//        } catch (e: Exception) {
//            // Handle error
//        }
//    }
//}
//
//// TokenUpdateWorker.kt
//class TokenUpdateWorker(
//    context: Context,
//    params: WorkerParameters
//) : CoroutineWorker(context, params) {
//
//    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
//        val token = inputData.getString("token") ?: return@withContext Result.failure()
//
//        try {
//            // Implement your token update logic here
//            updateTokenOnBackend(token)
//            Result.success()
//        } catch (e: Exception) {
//            Result.retry()
//        }
//    }
//
//    private suspend fun updateTokenOnBackend(token: String) {
//        // Implement your API call here
//    }
//}
//
//// Application class
//class YourApplication : Application() {
//    override fun onCreate() {
//        super.onCreate()
//        initializeFCM()
//    }
//
//    private fun initializeFCM() {
//        FirebaseMessaging.getInstance().apply {
//            isAutoInitEnabled = true
//
//            lifecycleScope.launch {
//                try {
//                    val token = token.await()
//                    // Send token to server with high priority flag
//                } catch (e: Exception) {
//                    // Handle error
//                }
//            }
//        }
//    }
//}
//
//// ViewModel example for observing messages
//class FCMViewModel : ViewModel() {
//    private val _messages = MutableStateFlow<Map<String, String>>(emptyMap())
//    val messages: StateFlow<Map<String, String>> = _messages.asStateFlow()
//
//    init {
//        viewModelScope.launch {
//            FCMService.observeMessages().collect { message ->
//                _messages.value = message
//            }
//        }
//    }
//}
//
//// build.gradle.kts
//dependencies {
//    implementation("com.google.firebase:firebase-messaging-ktx:23.4.0")
//    implementation("androidx.work:work-runtime-ktx:2.9.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
//}
