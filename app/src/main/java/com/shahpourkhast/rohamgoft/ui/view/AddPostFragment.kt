package com.shahpourkhast.rohamgoft.ui.view

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.shahpourkhast.rohamgoft.R
import com.shahpourkhast.rohamgoft.data.model.PostsData
import com.shahpourkhast.rohamgoft.databinding.CustomToastBinding
import com.shahpourkhast.rohamgoft.databinding.FragmentAddPostBinding
import com.shahpourkhast.rohamgoft.ui.viewModel.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddPostFragment : Fragment(R.layout.fragment_add_post) {
    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PostsViewModel by viewModels()
    private var originalStatusBarColor: Int = 0
    private var isOriginalLightStatusBar: Boolean = false
    private var isEditMode = false
    private var postId: String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAddPostBinding.bind(view)

        //----------------------------------------------------------------------------------

        arguments?.let { bundle ->
            isEditMode = true
            postId = bundle.getString("POST_ID")
            val authorToEdit = bundle.getString("POST_AUTHOR")
            val contentToEdit = bundle.getString("POST_CONTENT")

            binding.author.setText(authorToEdit)
            binding.content.setText(contentToEdit)
            binding.ersalPost.text = "ویرایش پست"

        }

        //----------------------------------------------------------------------------------

        //مقادیر اولیه نوار وضعیت(status bar)
        val window = requireActivity().window
        originalStatusBarColor = window.statusBarColor
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val wic = WindowCompat.getInsetsController(window, window.decorView)
            isOriginalLightStatusBar = wic.isAppearanceLightStatusBars
        }

        //----------------------------------------------------------------------------------

        binding.ersalPost.setOnClickListener {

            val author = binding.author.text.toString()
            val content = binding.content.text.toString()

            if (author.isNotBlank() && content.isNotBlank()) {

                if (isEditMode) {

                    val post = PostsData(id = postId!!, createdAt = "", author = author, content = content)

                    viewModel.updatePost(postId!!, post)

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, PostsFragment())
                        .addToBackStack(null)
                        .commit()

                } else {

                    viewModel.createPost(author, content)

                }
            } else {

                showCustomToast("لطفا تمامی فیلد ها را پر کنید")

            }
        }

        //----------------------------------------------------------------------------------

        flowViewModel()
    }

    //-------------------------------------------------

    override fun onResume() {
        super.onResume()

        setCustomStatusBar()

    }

    //-------------------------------------------------

    override fun onPause() {
        super.onPause()

        restoreOriginalStatusBar()

    }

    //-------------------------------------------------

    override fun onDestroyView() {

        super.onDestroyView()
        _binding = null

    }

    //-------------------------------------------------

    private fun setCustomStatusBar() {

        val window = requireActivity().window

        val customColor = ContextCompat.getColor(requireContext(), R.color.gold)
        window.statusBarColor = customColor
        val isLightStatusBar = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val wic = WindowCompat.getInsetsController(window, window.decorView)
            wic.isAppearanceLightStatusBars = isLightStatusBar
        }
    }

    //-------------------------------------------------

    private fun restoreOriginalStatusBar() {
        val window = requireActivity().window
        window.statusBarColor = originalStatusBarColor

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val wic = WindowCompat.getInsetsController(window, window.decorView)
            wic.isAppearanceLightStatusBars = isOriginalLightStatusBar
        }
    }

    //-------------------------------------------------

    private fun flowViewModel() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.createPostStatus.collect { isSuccess ->

                    when (isSuccess) {

                        true -> {

                            binding.author.text.clear()
                            binding.content.text.clear()
                            showCustomToast("پست با موفقیت ارسال شد!")

                        }

                        false -> showCustomToast("خطا در ارسال پست")

                    }

                }

            }

        }

    }

    //-------------------------------------------------

    private fun showCustomToast(message: String) {
        val inflater = layoutInflater
        val binding = CustomToastBinding.inflate(inflater)

        binding.toastMessage.text = message
        binding.toastIcon.setImageResource(R.drawable.ic_info)

        val toast = Toast(requireContext())
        toast.duration = Toast.LENGTH_SHORT
        toast.view = binding.root
        toast.show()
    }

}