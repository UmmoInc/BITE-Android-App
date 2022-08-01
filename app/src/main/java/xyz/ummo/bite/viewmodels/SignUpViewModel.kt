package xyz.ummo.bite.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import xyz.ummo.bite.repos.SignUpRepo
import java.io.IOException

class SignUpViewModel(private val signUpRepo: SignUpRepo, userSignUpInfo: JSONObject) :
    ViewModel() {

    suspend fun signUserUpDirectly(userSignUpInfo: JSONObject) {
        withContext(Dispatchers.IO) {
            try {
                signUpRepo.signUserUpDirectly(userSignUpInfo)
            } catch (IOE: IOException) {
                IOE.printStackTrace()
            }
        }
    }
}