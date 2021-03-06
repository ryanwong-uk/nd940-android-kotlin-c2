package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var recyclerViewAdapter: AsteroidAdapter? = null
    private lateinit var binding: FragmentMainBinding

    // The ViewModel requires Application object to initialize. Needs a ViewModelFactory to do it.
    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, ViewModelFactory(activity.application))
            .get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        recyclerViewAdapter = AsteroidAdapter(AsteroidClickListener {
            // Use Navigation to navigate to the detail screen, with the selected Asteroid as safe-arg
            findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        })
        binding.asteroidRecycler.adapter = recyclerViewAdapter

        setHasOptionsMenu(false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.asteroidList.observe(viewLifecycleOwner, { asteroids ->
            asteroids?.apply {
                recyclerViewAdapter?.asteroids = asteroids
            }
        })

        viewModel.imageOfDayObject.observe(viewLifecycleOwner, { imageOfDayObject ->
            if ("image" == imageOfDayObject.mediaType) {
                Picasso.get().load(imageOfDayObject.url).into(binding.activityMainImageOfTheDay)
                binding.activityMainImageOfTheDay.contentDescription = imageOfDayObject.title
            }
        })
    }
}
