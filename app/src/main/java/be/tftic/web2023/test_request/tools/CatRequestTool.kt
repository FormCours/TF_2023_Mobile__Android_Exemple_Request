package be.tftic.web2023.test_request.tools

import android.content.Context
import android.net.Uri
import be.tftic.web2023.test_request.R
import be.tftic.web2023.test_request.models.Cat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

const val BASE_SEARCH_URL = "https://api.thecatapi.com/v1/images/search"

/**
 * Tool pour faire des requetes sur la Cat API
 */
class CatRequestTool(private val context : Context, private val scope: CoroutineScope) {

    /**
     * Listener pour transmettre la réponse de la requete
     */
    interface OnResponseListener {
        fun onRequestResponseSuccess(result: List<Cat>)
        fun onRequestResponseError(message: String?)
    }


    /**
     * Fonction qui permet de cherche des chats par races
     * @param breed Race recherché
     */
    fun searchByBreed(breed: String, responseListener: OnResponseListener) {
        scope.launch {

            // Création de l'url avec des parametres (sans faire de concaténation)
            val urlBuilder = Uri.parse(BASE_SEARCH_URL).buildUpon().apply {
                appendQueryParameter("limit", "10")
                appendQueryParameter("breed_ids", breed)
                appendQueryParameter("api_key", context.getString(R.string.api_cat_key))
            }
            val url = URL(urlBuilder.build().toString())

            // Passage dans en "thread" IO
            withContext(Dispatchers.IO) {

                // Ouverture de la connexion
                val connection = url.openConnection() as? HttpURLConnection

                // Si l'ouverture de la connection à échoué -> Déclanchement du Listener "error"
                if (connection == null) {
                    withContext(Dispatchers.Main) {
                        responseListener.onRequestResponseError("Connection not open")
                    }
                    return@withContext
                }

                try {
                    // Execution de la requete
                    val json = connection.run {
                        requestMethod = "GET"
                        setRequestProperty("content-type", "application/json; charset=utf-8")
                        doInput = true

                        return@run inputStream.bufferedReader().lineSequence().joinToString("\n")
                    }

                    // Conversion du resultat "JSON" en objet Kotlin (Exemple : Une liste de "chat")
                    val typeToken = object : TypeToken<List<Cat>>() {}.type
                    val result = Gson().fromJson<List<Cat>>(json, typeToken)

                    // -> Déclanchement du Listener "Success"
                    withContext(Dispatchers.Main) {
                        responseListener.onRequestResponseSuccess(result)
                    }

                } catch (error: Exception) {
                    // -> Déclanchement du Listener "Error"
                    withContext(Dispatchers.Main) {
                        responseListener.onRequestResponseError(error.message)
                    }
                }
            }
        }
    }
}