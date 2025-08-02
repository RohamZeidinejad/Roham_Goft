package com.shahpourkhast.rohamgoft.ui.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahpourkhast.rohamgoft.R
import com.shahpourkhast.rohamgoft.databinding.CustomToastBinding
import com.shahpourkhast.rohamgoft.databinding.DialogDeleteBinding
import com.shahpourkhast.rohamgoft.databinding.FragmentPostsBinding
import com.shahpourkhast.rohamgoft.ui.adapter.PostsAdapter
import com.shahpourkhast.rohamgoft.ui.viewModel.PostsViewModel

class PostsFragment : Fragment(R.layout.fragment_posts) {
    private var _binding: FragmentPostsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PostsViewModel by viewModels()
    private lateinit var postAdapter: PostsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentPostsBinding.bind(view)

        setupRecyclerView()

        observeViewModel()

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

        postAdapter.onItemClick = { post ->

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

    private fun observeViewModel() {

        viewModel.posts.observe(viewLifecycleOwner) { posts ->

            postAdapter.submitList(posts)

        }

        //---------------------------------------------------------------------

        viewModel.isLoadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {

                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerView.isEnabled = false

            } else {

                binding.progressBar.visibility = View.GONE
                binding.recyclerView.isEnabled = true

            }

        }

        //---------------------------------------------------------------------

        viewModel.deletePostStatus.observe(viewLifecycleOwner) { isSuccess ->

            if (isSuccess) {

                showCustomToast("پست با موفقیت حذف شد")


            } else {

                showCustomToast("خطا در حذف پست")

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