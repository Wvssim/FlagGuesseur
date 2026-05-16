package com.example.quiz_app_wvssim.data.remote

import com.example.quiz_app_wvssim.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OpenRouterDataSource {

    private val api: OpenRouterApi = Retrofit.Builder()
        .baseUrl("https://openrouter.ai/api/v1/")
        .client(
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    chain.proceed(
                        chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer ${BuildConfig.OPENROUTER_API_KEY}")
                            .addHeader("HTTP-Referer", "com.example.quiz_app_wvssim")
                            .addHeader("X-Title", "FlagGuesseur")
                            .build()
                    )
                }
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.NONE
                })
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OpenRouterApi::class.java)

    suspend fun getExplanation(question: String, correctAnswer: String): String {
        val prompt = """Tu es LIA, un assistant éducatif pour une appli de quiz géographique.
Explique en 2 phrases courtes et intéressantes pourquoi la réponse à "$question" est "$correctAnswer".
Sois informatif et engageant. Réponds uniquement en français, sans introduction."""

        return runCatching {
            val response = api.chat(
                OpenRouterRequest(
                    messages = listOf(OpenRouterMessage("user", prompt)),
                    maxTokens = 150
                )
            )
            response.choices.firstOrNull()?.message?.content?.trim()
                ?: "LIA n'a pas pu générer d'explication pour cette question."
        }.getOrElse { "❌ LIA est hors ligne. Vérifie ta connexion internet." }
    }

    suspend fun askLia(userMessage: String): String {
        return runCatching {
            val response = api.chat(
                OpenRouterRequest(
                    messages = listOf(
                        OpenRouterMessage(
                            "system",
                            "Tu es LIA, une assistante éducative spécialisée en géographie, culture générale et sciences. " +
                            "Réponds toujours en français, de façon claire, pédagogique et engageante en 3 à 5 phrases maximum."
                        ),
                        OpenRouterMessage("user", userMessage)
                    ),
                    maxTokens = 350
                )
            )
            response.choices.firstOrNull()?.message?.content?.trim()
                ?: "Je n'ai pas pu générer de réponse pour cette question."
        }.getOrElse { e ->
            android.util.Log.e("OpenRouter", "API Error: ${e.message}")
            "❌ Erreur API: ${e.message?.take(30) ?: "Connexion impossible"}. Vérifie ta clé OpenRouter dans build.gradle."
        }
    }

    suspend fun generateQcmOnTopic(topic: String): AiGeneratedQuestion? {
        val prompt = """Génère une question QCM éducative sur le thème : "$topic"
Format JSON strict (sans markdown ni backticks) :
{"question":"...","options":["opt1","opt2","opt3","opt4"],"correctIndex":0,"explanation":"..."}
- 4 options distinctes, correctIndex entre 0 et 3
- Réponses courtes (1-5 mots)
- Tout en français"""

        return runCatching {
            val response = api.chat(
                OpenRouterRequest(
                    messages = listOf(OpenRouterMessage("user", prompt)),
                    maxTokens = 300
                )
            )
            val content = response.choices.firstOrNull()?.message?.content?.trim()
                ?: return@runCatching null
            val start = content.indexOf('{')
            val end = content.lastIndexOf('}')
            if (start < 0 || end < 0 || end <= start) {
                android.util.Log.w("OpenRouter", "JSON parsing failed for topic: $topic. Response: $content")
                return@runCatching null
            }
            val json = content.substring(start, end + 1)
            com.google.gson.Gson().fromJson(json, AiGeneratedQuestion::class.java)
        }.onFailure { e ->
            android.util.Log.e("OpenRouter", "generateQcmOnTopic failed: ${e.message}")
        }.getOrNull()
    }

    fun getDefaultQcms(): Map<String, List<AiGeneratedQuestion>> = mapOf(
        "France" to listOf(
            AiGeneratedQuestion("Quelle est la capitale de la France ?", listOf("Lyon", "Paris", "Marseille", "Toulouse"), 1, "Paris est la capitale depuis le 10e siècle."),
            AiGeneratedQuestion("Quelle est la monnaie de la France ?", listOf("Franc", "Euro", "Livre", "Couronne"), 1, "La France utilise l'Euro depuis 2002.")
        ),
        "Espagne" to listOf(
            AiGeneratedQuestion("Quelle est la capitale de l'Espagne ?", listOf("Barcelone", "Madrid", "Séville", "Valence"), 1, "Madrid est la capitale et la plus grande ville d'Espagne."),
            AiGeneratedQuestion("Quel est le plus grand musée d'art de Madrid ?", listOf("Thyssen", "Reina Sofia", "Prado", "Picasso"), 2, "Le Prado est l'un des plus importants musées d'art du monde.")
        ),
        "Italie" to listOf(
            AiGeneratedQuestion("Quelle est la capitale de l'Italie ?", listOf("Milan", "Rome", "Venise", "Florence"), 1, "Rome est la capitale historique depuis plus de 2000 ans."),
            AiGeneratedQuestion("Combien de régions compte l'Italie ?", listOf("16", "18", "20", "25"), 2, "L'Italie compte 20 régions administratives.")
        ),
        "Allemagne" to listOf(
            AiGeneratedQuestion("Quelle est la capitale de l'Allemagne ?", listOf("Munich", "Hambourg", "Berlin", "Francfort"), 2, "Berlin est la capitale depuis la réunification en 1990."),
            AiGeneratedQuestion("Quel fleuve traverse Berlin ?", listOf("Rhin", "Spree", "Elbe", "Danube"), 1, "La Spree traverse Berlin et est un symbole historique.")
        ),
        "Royaume-Uni" to listOf(
            AiGeneratedQuestion("Quelle est la capitale du Royaume-Uni ?", listOf("Manchester", "Liverpool", "Londres", "Édimbourg"), 2, "Londres est la capitale depuis environ 2000 ans."),
            AiGeneratedQuestion("Quel est le parlement du Royaume-Uni ?", listOf("Conseil", "Commons", "Westminster", "Lords"), 2, "Westminster est le siège du Parlement britannique.")
        ),
        "Japon" to listOf(
            AiGeneratedQuestion("Quelle est la capitale du Japon ?", listOf("Kyoto", "Tokyo", "Osaka", "Nagoya"), 1, "Tokyo est la plus grande métropole du monde."),
            AiGeneratedQuestion("Combien d'îles principales le Japon compte-t-il ?", listOf("3", "4", "5", "8"), 1, "Le Japon compte 4 îles principales.")
        ),
        "Chine" to listOf(
            AiGeneratedQuestion("Quelle est la capitale de la Chine ?", listOf("Shanghai", "Canton", "Pékin", "Xi'an"), 2, "Pékin est la capitale depuis 1949."),
            AiGeneratedQuestion("Quelle est la plus longue muraille du monde ?", listOf("Muraille des Andes", "Grande Muraille de Chine", "Muraille de Hadrien", "Muraille Ping Yao"), 1, "La Grande Muraille de Chine s'étend sur plus de 21 000 km.")
        ),
        "Brésil" to listOf(
            AiGeneratedQuestion("Quelle est la capitale du Brésil ?", listOf("Rio de Janeiro", "Salvador", "Brasília", "São Paulo"), 2, "Brasília est la capitale construite en 1960."),
            AiGeneratedQuestion("Quel est le plus grand fleuve du Brésil ?", listOf("Paraná", "Amazone", "Paraguay", "Tapajós"), 1, "L'Amazone est le plus grand fleuve du monde.")
        ),
        "États-Unis" to listOf(
            AiGeneratedQuestion("Quelle est la capitale des États-Unis ?", listOf("New York", "Los Angeles", "Washington D.C.", "Chicago"), 2, "Washington D.C. est la capitale depuis 1800."),
            AiGeneratedQuestion("Combien d'états les États-Unis comptent-ils ?", listOf("48", "49", "50", "52"), 2, "Les États-Unis comptent 50 états.")
        ),
        "Australie" to listOf(
            AiGeneratedQuestion("Quelle est la capitale de l'Australie ?", listOf("Sydney", "Melbourne", "Canberra", "Brisbane"), 2, "Canberra est la capitale depuis 1927."),
            AiGeneratedQuestion("Quel animal est emblématique de l'Australie ?", listOf("Autruche", "Koala", "Pingouin", "Kangourou"), 3, "Le kangourou est unique à l'Australie.")
        )
    )

    suspend fun generateQuestion(categoryName: String, countryName: String, flagCode: String): AiGeneratedQuestion? {
        val prompt = """Génère une question QCM éducative sur $countryName ($categoryName) pour un quiz géographique.
Format JSON strict (sans markdown) :
{"question":"...","options":["opt1","opt2","opt3","opt4"],"correctIndex":0,"explanation":"..."}
- 4 options, correctIndex entre 0 et 3
- Question sur : capitale, langue, monnaie, culture, géographie, histoire
- Réponses courtes (1-4 mots)
- Tout en français"""

        return runCatching {
            val response = api.chat(
                OpenRouterRequest(
                    messages = listOf(OpenRouterMessage("user", prompt)),
                    maxTokens = 200
                )
            )
            val content = response.choices.firstOrNull()?.message?.content?.trim() ?: return@runCatching null
            val json = content.substringAfter("{").let { "{$it" }.substringBefore("}").let { "$it}" }
            val gson = com.google.gson.Gson()
            gson.fromJson(json, AiGeneratedQuestion::class.java)?.copy(flagCode = flagCode)
        }.getOrNull()
    }
}

data class AiGeneratedQuestion(
    val question: String = "",
    val options: List<String> = emptyList(),
    val correctIndex: Int = 0,
    val explanation: String = "",
    val flagCode: String = ""
)
