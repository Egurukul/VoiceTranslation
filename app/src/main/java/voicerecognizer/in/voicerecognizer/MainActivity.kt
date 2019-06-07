package voicerecognizer.`in`.voicerecognizer

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


class MainActivity : AppCompatActivity() {

    private val REQ_CODE_SPEECH_INPUT = 100
    val textView: TextView by lazy {
        findViewById<TextView>(R.id.txtSpeechInput)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSpeak.setOnClickListener {
            promptSpeechInput()
        }


//        val translateOptions = TranslateOptions.newBuilder().setApiKey("AIzaSyA5tN8V2fTyXr6RyfZ0rnsQLZcoCsljnHc")
//            .build()
//        translate = TranslateOptions.getDefaultInstance().service
    }


    /**
     * Showing google speech input dialog
     */
    private fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            getString(R.string.speech_prompt)
        )
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (a: ActivityNotFoundException) {
            Toast.makeText(
                applicationContext,
                getString(R.string.speech_not_supported),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    /**
     * Receiving speech input
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("onActivityResult:", "onActivityResult")

        when (requestCode) {
            REQ_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && null != data) {

                    val result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
//                    txtSpeechInput.setText(result[0])
                    Log.e("onActivityResult:", result[0])
                    translateText(result[0])

                }
            }
        }
    }


    private fun translateText(text: String, source: String = "en", target: String = "hi") {

        doAsync {
            Log.e("translateText:", text)

            val options = TranslateOptions.newBuilder()
                .setApiKey("AIzaSyA5tN8V2fTyXr6RyfZ0rnsQLZcoCsljnHc")
                .build()
            val translate = options.service
            val translation = translate.translate(
                text,
                Translate.TranslateOption.sourceLanguage("source"),
                Translate.TranslateOption.targetLanguage("target")
            )

            Log.e("translateText:", "dewewr" + translation.translatedText)


            Log.e("translateText:", translation.translatedText)

            uiThread {
                textView.text = translation.translatedText

            }

        }


    }

}
