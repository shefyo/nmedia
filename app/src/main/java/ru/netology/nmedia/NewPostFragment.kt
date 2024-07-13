package ru.netology.nmedia


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding

class NewPostFragment : Fragment() {
    companion object {
        private const val TEXT_KEY = "TEXT_KEY"
        var Bundle.textArg: String?
            set(value) = putString(TEXT_KEY, value)
            get() = getString(TEXT_KEY)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by viewModels (ownerProducer = ::requireParentFragment) {
            PostViewModelFactory(requireActivity().application)
        }

        binding.save.setOnClickListener {
            val content = binding.content.text.toString()
            if (content.isNotBlank()) {
                viewModel.changeContentAndSave(content)
                findNavController().previousBackStackEntry?.savedStateHandle?.set("postKey", content)
            }
            findNavController().navigateUp()
        }

        return binding.root
    }
}