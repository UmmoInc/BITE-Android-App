package xyz.ummo.bite.ui.foodcourt.order_tracker

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import timber.log.Timber
import xyz.ummo.bite.R
import xyz.ummo.bite.databinding.FragmentOrderTrackerBinding


class OrderTracker : Fragment() {
    private lateinit var _binding: FragmentOrderTrackerBinding
    private val binding get() = _binding!!
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding= FragmentOrderTrackerBinding.inflate(inflater,container,false)
        rootView = binding.root
        return rootView


    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.order_tracker_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share_order_diner -> {
                Timber.e("Sharing Order")
                true
            }
            R.id.diner_order_support -> {
                Timber.e("Diner Order Support")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}