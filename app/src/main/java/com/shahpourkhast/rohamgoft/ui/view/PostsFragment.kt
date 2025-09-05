package com.shahpourkhast.rohamgoft.ui.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahpourkhast.rohamgoft.R
import com.shahpourkhast.rohamgoft.databinding.CustomToastBinding
import com.shahpourkhast.rohamgoft.databinding.DialogDeleteBinding
import com.shahpourkhast.rohamgoft.databinding.FragmentPostsBinding
import com.shahpourkhast.rohamgoft.ui.adapter.PostsAdapter
import com.shahpourkhast.rohamgoft.ui.viewModel.PostsState
import com.shahpourkhast.rohamgoft.ui.viewModel.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostsFragment : androidx.fragment.app.Fragment(R.layout.fragment_posts) {
    private var _binding: FragmentPostsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PostsViewModel by viewModels()
    private lateinit var postAdapter: PostsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentPostsBinding.bind(view)

        setupRecyclerView()

        flowViewModel()

        updatePost()

    }

    override fun onDestroyView() {

        super.onDestroyView()
        _binding = null

    }

    //------------------------------------------

    private fun setupRecyclerView() {

        postAdapter = PostsAdapter()

        binding.recyclerView.apply {

            adapter = postAdapter
            layoutManager = LinearLayoutManager(requireContext())

        }

        //-----------------------------------------------------------------

        postAdapter.onPostLongClick = { post ->

            val dialog = AlertDialog.Builder(requireContext()).create()
            val dialogBinding = DialogDeleteBinding.inflate(layoutInflater)
            dialog.setView(dialogBinding.root)
            dialog.setCancelable(true)
            dialog.show()

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialogBinding.hazf.setOnClickListener {

                viewModel.deletePost(post.id)

                dialog.dismiss()

            }

            dialogBinding.enseraf.setOnClickListener {

                dialog.dismiss()

            }

        }

    }

    //------------------------------------------

    private fun updatePost(){

        postAdapter.onPostClick = { post ->

            val fragment = AddPostFragment()

            val bundle = Bundle()

            bundle.putString("POST_ID", post.id)
            bundle.putString("POST_AUTHOR", post.author)
            bundle.putString("POST_CONTENT", post.content)

            fragment.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .addToBackStack(null)
                .commit()

        }

    }

    //------------------------------------------

    private fun flowViewModel() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {

                    viewModel.posts.collect { state ->

                        when (state) {

                            is PostsState.Idle -> Unit

                            is PostsState.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                                binding.recyclerView.isEnabled = false
                            }

                            is PostsState.Success -> {
                                binding.progressBar.visibility = View.GONE
                                binding.recyclerView.isEnabled = true
                                postAdapter.submitList(state.data)
                            }

                            is PostsState.Error -> {
                                binding.progressBar.visibility = View.GONE
                                binding.recyclerView.isEnabled = true
                                showCustomToast(state.message)
                            }

                        }

                    }

                }

        //---------------------------------------------------------------------

        launch {

            viewModel.deletePostStatus.collect { isSuccess ->

                when (isSuccess) {

                    true -> showCustomToast("پست با موفقیت حذف شد")
                    false -> showCustomToast("خطا در حذف پست")

                    }
                }
            }
        }

        }


    }

    //------------------------------------------

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