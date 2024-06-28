package ru.netology.nmedia

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.databinding.EditPostActivityBinding
import ru.netology.nmedia.ui.theme.NmediaTheme

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = EditPostActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val postId = intent.getLongExtra("post_id", 0L)
        val postText = intent.getStringExtra("post_text")

        binding.content.setText(postText)

        binding.save.setOnClickListener {
            val editedText = binding.content.text.toString()

            if (editedText.isNotBlank()) {
                val resultIntent = Intent().apply {
                    putExtra("edited_post_text", editedText)
                }

                setResult(Activity.RESULT_OK, resultIntent)
            } else {
                setResult(Activity.RESULT_CANCELED)
            }

            finish()
        }
    }
}

object EditPostContract: ActivityResultContract<String, String?>() {
    override fun createIntent(context: Context, input: String) = Intent(context, EditPostActivity::class.java).also { it.putExtra(Intent.EXTRA_TEXT, input) }

    override fun parseResult(resultCode: Int, intent: Intent?) = intent?.getStringExtra(Intent.EXTRA_TEXT)
}