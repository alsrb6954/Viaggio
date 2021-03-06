package com.kotlin.viaggio.view.traveling.detail

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.kotlin.viaggio.R
import com.kotlin.viaggio.android.ArgName
import com.kotlin.viaggio.extenstions.baseIntent
import com.kotlin.viaggio.extenstions.imageName
import com.kotlin.viaggio.extenstions.showDialog
import com.kotlin.viaggio.view.common.BaseDialogFragment
import com.kotlin.viaggio.view.common.BaseFragment
import com.kotlin.viaggio.view.traveling.TravelCardBottomSheetDialogFragment
import com.kotlin.viaggio.view.traveling.TravelingDeleteActionDialogFragment
import com.kotlin.viaggio.view.traveling.option.TravelingCitiesActionDialogFragment
import com.kotlin.viaggio.view.traveling.option.TravelingDayCountActionDialogFragment
import com.kotlin.viaggio.view.traveling.option.TravelingThemesActionDialogFragment
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrPosition
import kotlinx.android.synthetic.main.fragment_traveling_detail.*
import kotlinx.android.synthetic.main.item_traveling_pager_img.view.*
import java.io.File


class TravelingDetailFragment : BaseFragment<TravelingDetailFragmentViewModel>() {
    lateinit var binding: com.kotlin.viaggio.databinding.FragmentTravelingDetailBinding
//    override fun onResume() {
//        super.onResume()
//        if (sliderInterface == null)
//            sliderInterface = Slidr.replace(
//                travelingDetailLayout, SlidrConfig.Builder()
//                    .position(SlidrPosition.LEFT)
//                    .build()
//            )
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_traveling_detail, container, false)
        binding.viewModel = getViewModel()
        binding.viewHandler = ViewHandler()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        travelingDetailDayImg.isNestedScrollingEnabled = false
        getViewModel().travelOfDayCardImageListLiveData.observe(this.viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { imageNames ->
                instaDotView.noOfPages = imageNames.size
                if (imageNames.isNotEmpty()) {
                    val params = appBar.layoutParams
                    params.width = width
                    params.height = width
                    getViewModel().imageShow.set(true)
                } else {
                    val params = appBar.layoutParams
                    params.width = width
                    params.height = resources.getDimension(R.dimen.traveling_floating_size).toInt()
                }
                travelingDetailDayImg.adapter =
                    object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                            object : RecyclerView.ViewHolder(
                                LayoutInflater.from(parent.context).inflate(
                                    R.layout.item_traveling_pager_img,
                                    parent,
                                    false
                                )
                            ) {}

                        override fun getItemCount() = imageNames.size
                        override fun onBindViewHolder(
                            holder: RecyclerView.ViewHolder,
                            position: Int
                        ) {
                            if (TextUtils.isEmpty(imageNames[position]).not()) {
                                Glide.with(holder.itemView.travelingPagerImg)
                                    .load(requireContext().imageName(imageNames[position]))
                                    .into(holder.itemView.travelingPagerImg)
                            }
                            holder.itemView.travelingPagerImg.setOnClickListener {
                                val frag = TravelingImageDetailActionDialogFragment()
                                val arg = Bundle()
                                arg.putInt(ArgName.TRAVEL_CARD_IMG_POSITION.name, position + 1)
                                frag.arguments = arg
                                showDialog(frag, TravelingImageDetailActionDialogFragment.TAG)
                            }
                        }
                    }
            }
        })
        travelingDetailDayImg.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                instaDotView.onPageChange(position)
                getViewModel().currentImageSize.set(position + 1)
                getViewModel().timeDisposable?.dispose()
                getViewModel().imageShow.set(true)
                getViewModel().showNotice()
                if (position == 0) {
                    enableSliding(true)
                } else {
                    enableSliding(false)
                }
            }
        })

        getViewModel().changeCardLiveData.observe(this, Observer {
            it.getContentIfNotHandled()?.let { value ->
                when (value) {
                    0 -> {
                        val frag = TravelingDetailActionDialogFragment()
                        val arg = Bundle()
                        arg.putIntArray(
                            ArgName.TRAVEL_CARD_LOCATION.name,
                            getViewModel().modifyLocation
                        )
                        frag.arguments = arg
                        showDialog(frag, TravelingDetailActionDialogFragment.TAG)
                    }
                    1 -> {
                        optionTraveling(
                            TravelingDeleteActionDialogFragment(),
                            TravelingDeleteActionDialogFragment.TAG,
                            ArgName.TRAVEL_CARD_MODE.name
                        )
                    }
                    2 -> {
                        baseIntent("http://viaggio.kotlin.com/traveling/image/modify/")
                    }
                    3 -> {
                        getViewModel().selectedCountry()
                        optionTraveling(
                            TravelingCitiesActionDialogFragment(),
                            TravelingCitiesActionDialogFragment.TAG,
                            ArgName.TRAVEL_CARD_CHANGE_MODE.name
                        )
                    }
                    4 -> {
                        optionTraveling(
                            TravelingThemesActionDialogFragment(),
                            TravelingThemesActionDialogFragment.TAG,
                            ArgName.TRAVEL_CARD_CHANGE_MODE.name
                        )
                    }
                    5 -> {
                        optionTraveling(
                            TravelingDayCountActionDialogFragment(),
                            TravelingDayCountActionDialogFragment.TAG,
                            ArgName.TRAVEL_CARD_CHANGE_MODE.name
                        )
                    }
                }
            }
        })

        getViewModel().completeLiveData.observe(this, Observer {
            it.getContentIfNotHandled()?.let { value ->
                if (value) {
                    stopLoading()
                    fragmentPopStack()
                } else {
                    showLoading()
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        if(getViewModel().reloadingImage) {
            getViewModel().initialize()
        }
    }

    override fun onStop() {
        super.onStop()
        if(getViewModel().reloadingImage.not()){
            getViewModel().reloadingImage = true
        }

    }

    private fun optionTraveling(frag: BaseDialogFragment<*>, tag: String, key: String) {
        val arg = Bundle()
        arg.putBoolean(key, true)
        frag.arguments = arg
        showDialog(frag, tag)
    }

    inner class ViewHandler {
        fun back() {
            fragmentPopStack()
        }

        fun more() {
            val location = IntArray(2)
            travelingDetailDayTravelCardList.getLocationOnScreen(location)
            getViewModel().modifyLocation = location
            showDialog(
                TravelCardBottomSheetDialogFragment(),
                TravelCardBottomSheetDialogFragment.TAG
            )
        }
    }
}