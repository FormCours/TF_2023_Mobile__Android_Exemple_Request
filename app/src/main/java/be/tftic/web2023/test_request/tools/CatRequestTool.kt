package be.tftic.web2023.test_request.tools

import android.content.Context
import android.net.Uri
import android.util.Log
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

/**
 * Tool pour faire des requetes sur la Cat API
 */
class CatRequestTool(private val context : Context, private val scope: CoroutineScope) {

    /**
     * Listener pour transmettre la réponse de la requete
     */
    interface OnResponseListener <T> {
        fun <T> onRequestResponseSuccess(result: T)
        fun onRequestResponseError(message: String?)
    }


    /**
     * Fonction qui permet de cherche des chats par races
     * @param breed Race recherché
     */
    fun searchByBreed(breed: String, responseListener: OnResponseListener<List<Cat>>) {
        val SEARCH_URL = context.getString(R.string.url_cat_search)
        val API_KEY = context.getString(R.string.api_cat_key)

        scope.launch {

            // Création de l'url avec des parametres (sans faire de concaténation)
            val urlBuilder = Uri.parse(SEARCH_URL).buildUpon().apply {
                appendQueryParameter("limit", "10")
                appendQueryParameter("breed_ids", breed)
                appendQueryParameter("api_key", API_KEY)
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

    /**
     * Fonction qui permet d'obtenir les infos d'in chat
     * @param catId L'id du chat
     */
    fun searchById(catId : String, responseListener: OnResponseListener<Cat>) {
        val FETCH_URL = context.getString(R.string.url_cat_fetch)
        val API_KEY = context.getString(R.string.api_cat_key)

        scope.launch {

            // Création de l'url avec des parametres (sans faire de concaténation)
            val urlBuilder = Uri.parse(FETCH_URL).buildUpon().apply {
                appendPath(catId)
                appendQueryParameter("api_key", API_KEY)
            }
            val url = URL(urlBuilder.build().toString())
            Log.d("TEST_TEST", url.toString())

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

                    // Conversion du resultat "JSON" en objet Kotlin (Exemple : Un objet de "Chat")
                    val typeToken = Cat::class.java
                    val result = Gson().fromJson<Cat>(json, typeToken)

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