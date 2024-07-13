package ru.netology.nmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.EditPostFragment.Companion.EXTRA_POST_ID
import ru.netology.nmedia.EditPostFragment.Companion.EXTRA_POST_TEXT
import ru.netology.nmedia.databinding.FragmentEditPostBinding

class EditPostFragment : Fragment() {

    companion object {
        const val EXTRA_POST_ID = "post_id"
        const val EXTRA_POST_TEXT = "post_text"
        const val RESULT_EDITED_POST_TEXT = "edited_post_text"
    }

    private var postId: Long = 0L
    private var postText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postId = it.getLong(EXTRA_POST_ID)
            postText = it.getString(EXTRA_POST_TEXT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditPostBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment) {
            PostViewModelFactory(requireActivity().application)
        }

        postText?.let {
            binding.content.setText(it)
        }

        binding.save1.setOnClickListener {
            val editedText = binding.content.text.toString()
            if (editedText.isNotBlank()) {
                parentFragmentManager.setFragmentResult("requestKey", Bundle().apply {
                    putString(RESULT_EDITED_POST_TEXT, editedText)
                    putLong(EXTRA_POST_ID, postId)
                })
                viewModel.changeContentAndSave(editedText)
            }
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }
}