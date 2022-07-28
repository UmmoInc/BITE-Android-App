package xyz.ummo.bite.repos

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException


class SignUpRepo {
    private var client = OkHttpClient()
    private val user = JSONObject()

    init {
    }

    fun signUserUpDirectly(userSignUpDetails: JSONObject) {

        val requestBody = userSignUpDetails.toString().toRequestBody(MEDIA_TYPE_JSON)
        val request = Request.Builder()
            .url("https://bite-api-dev.herokuapp.com/auth/signup")/*.header("Content-Type", "application/json")*/
//            .post(userSignUpDetails.toString().toRequestBody(MEDIA_TYPE_JSON))
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
            Timber.e("RESPONSE STRING -> $responseString")
        } catch (IOE: IOException) {
            Timber.e("$IOE")
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