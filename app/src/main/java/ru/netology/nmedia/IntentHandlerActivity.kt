package ru.netology.nmedia

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.databinding.IntentHandlerActivityBinding
import ru.netology.nmedia.ui.theme.NmediaTheme


class IntentHandlerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = IntentHandlerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }

                val text = it.getStringExtra(Intent.EXTRA_TEXT)
                if(text.isNullOrBlank()) {
                    Snackbar.make(binding.root, "Error empty content", LENGTH_INDEFINITE)
                        .setAction("Ok") {
                            finish()
                        }
                        .show()
                    return@let
                }
            }
        }
    }