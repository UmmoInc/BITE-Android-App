package xyz.ummo.bite.repos

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import xyz.ummo.bite.utils.eventBusClasses.RegistrationCallbackEvent
import java.io.IOException


class SignUpRepo {
    private var client = OkHttpClient()
    private val user = JSONObject()
    private val registrationCallbackEvent = RegistrationCallbackEvent()

    init {
    }

    fun signUserUpDirectly(userSignUpDetails: JSONObject) {

        val requestBody = userSignUpDetails.toString().toRequestBody(MEDIA_TYPE_JSON)
        val request = Request.Builder()
            .url("https://bite-api-dev.herokuapp.com/auth/signup")
            .header("Content-Type", "application/json")
            .post(requestBody)
            .build()

        /*client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $request")
            Timber.e("BITE API Sign-in -> $response")
        }*/

        val response: Response?
        try {
            response = client.newCall(request).execute()
            val responseString = response.body!!.string()
            val responseCode = response.code
            try {
                if (responseCode == 201) {
                    Timber.e("SIGN-IN SUCCESSFUL -> $responseString")
                    registrationCallbackEvent.responseStatusCode = responseCode
                    EventBus.getDefault().post(registrationCallbackEvent)
                } else {
                    val responseMessage = JSONObject(responseString).getString("message")
                    registrationCallbackEvent.responseStatusCode = responseCode
                    registrationCallbackEvent.responseStatusMessage = responseMessage
                    EventBus.getDefault().post(registrationCallbackEvent)
                    Timber.e("ISSUE DETECTED -> $responseString CODE -> $responseCode")
                }
            } catch (JSE: JSONException) {
                throw JSE
            }
        } catch (IOE: IOException) {
            //TODO: Implement a UX handler for this exception
            Timber.e("Exception $IOE")
        }

    }

    fun signUserUpWithFacebook(facebookAccessToken: String) {
        val request = Request.Builder()
            .url("")
            .post(facebookAccessToken.toRequestBody(MEDIA_TYPE_MARKDOWN))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            Timber.e("FB Sign-in -> $response")
        }
    }

    companion object {
        val MEDIA_TYPE_MARKDOWN = "text/x-markdown; charset=utf-8".toMediaType()
        val MEDIA_TYPE_JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    }
}