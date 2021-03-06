package com.task.koinztask.ui.photos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableChar
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.task.koinztask.R
import com.task.koinztask.ui.fullscreenphoto.FullScreenPhotoFragment
import com.task.koinztask.ui.photos.adapter.PhotosAdapter
import com.task.koinztask.ui.photos.adapter.PhotosLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhotosFragment : Fragment() {

    companion object {
        fun newInstance() = PhotosFragment()
    }

    private val viewModel: PhotosViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.photos_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photosAdapter = PhotosAdapter(this::onPhotoClicked)
        val rvPhotos = view.findViewById<RecyclerView>(R.id.rvPhotos)

        rvPhotos?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = photosAdapter.withLoadStateHeaderAndFooter(
                PhotosLoadStateAdapter(photosAdapter::retry),
                PhotosLoadStateAdapter(photosAdapter::retry)
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.photosLiveData.collectLatest {
                photosAdapter.submitData(lifecycle, it)
            }
        }
    }

    private fun onPhotoClicked(url: String) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, FullScreenPhotoFragment.newInstance(url))
            .addToBackStack(FullScreenPhotoFragment::class.java.simpleName)
            .commit()
    }

}
