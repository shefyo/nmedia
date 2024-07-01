package ru.netology.nmedia

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContract
import ru.netology.nmedia.databinding.EditPostActivityBinding

class EditPostActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_POST_ID = "post_id"
        const val EXTRA_POST_TEXT = "post_text"
        const val EXTRA_EDITED_POST_TEXT = "edited_post_text"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = EditPostActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val postId = intent.getLongExtra(EXTRA_POST_ID, 0L)
        val postText = intent.getStringExtra(EXTRA_POST_TEXT)
        binding.content.setText(postText)

        binding.save1.setOnClickListener {
            val editedText = binding.content.text.toString()

            if (editedText.isNotBlank()) {
                val resultIntent = Intent().apply {
                    putExtra(EXTRA_EDITED_POST_TEXT, editedText)
                    putExtra(EXTRA_POST_ID, postId)
                }

                setResult(Activity.RESULT_OK, resultIntent)
            } else {
                setResult(Activity.RESULT_CANCELED)
            }
            finish()
        }
    }
}

object EditPostContract: ActivityResultContract<Pair<Long, String>, String?>() {
    override fun createIntent(context: Context, input: Pair<Long, String>): Intent {
        return Intent(context, EditPostActivity::class.java).apply {
            putExtra(EditPostActivity.EXTRA_POST_ID, input.first)
            putExtra(EditPostActivity.EXTRA_POST_TEXT, input.second)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        if (resultCode == Activity.RESULT_OK) {
            return intent?.getStringExtra(EditPostActivity.EXTRA_EDITED_POST_TEXT)
        }
        return null
    }
}