// DO NOT UPDATE THE COPY RIGHT
// THIS WAS TAKEN FROM APACHE OKHTTP

package io.gitlab.fsc_clam.fscwhereswhat.common.ext

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resumeWithException

/**
 * @see <a href="https://github.com/square/okhttp/blob/master/okhttp-coroutines/src/jvmMain/kotlin/okhttp3/JvmCallExtensions.kt">JvmCallExtensions</a>
 */
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun Call.executeAsync(): Response = suspendCancellableCoroutine { continuation ->
	continuation.invokeOnCancellation {
		this.cancel()
	}
	this.enqueue(object : Callback {
		override fun onFailure(call: Call, e: IOException) {
			continuation.resumeWithException(e)
		}

		override fun onResponse(call: Call, response: Response) {
			continuation.resume(value = response, onCancellation = { call.cancel() })
		}
	})
}