package com.kotlin.viaggio.view.traveling.detail

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.kotlin.viaggio.R
import com.kotlin.viaggio.android.ArgName
import com.kotlin.viaggio.view.common.BaseFragment
import com.kotlin.viaggio.view.traveling.TravelCardBottomSheetDialogFragment
import com.kotlin.viaggio.view.traveling.TravelingDeleteActionDialogFragment
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrPosition
import kotlinx.android.synthetic.main.fragment_traveling_detail.*
import kotlinx.android.synthetic.main.item_traveling_pager_img.view.*
import java.io.File


class TravelingDetailFragment:BaseFragment<TravelingDetailFragmentViewModel>() {
    lateinit var binding:com.kotlin.viaggio.databinding.FragmentTravelingDetailBinding
    override fun onResume() {
        super.onResume()
        if(sliderInterface == null)
            sliderInterface = Slidr.replace(travelingDetailLayout, SlidrConfig.Builder()
                .position(SlidrPosition.LEFT)
                .build())
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_traveling_detail, container, false)
        binding.viewModel = getViewModel()
        binding.viewHandler = ViewHandler()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imgDir = File(context?.filesDir, "images/")
        getViewModel().travelOfDayCardImageListLiveData.observe(this, Observer {
            it.getContentIfNotHandled()?.let { imageNames ->
                if(imageNames.isNotEmpty()){
                    val params = appBar.layoutParams
                    params.width = width
                    params.height = width
                    getViewModel().imageShow.set(true)
                }else{
                    val params = appBar.layoutParams
                    params.width = width
                    params.height = resources.getDimension(R.dimen.traveling_floating_size).toInt()
                }
                travelingDetailDayImg.adapter = object :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                        object :RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_traveling_pager_img, parent, false)){}
                    override fun getItemCount() = imageNames.size
                    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                        imageNames[position].let { themeImageName ->
                            if(TextUtils.isEmpty(themeImageName).not()){
                                Glide.with(holder.itemView.travelingPagerImg)
                                    .load(themeImageName)
                                    .into(holder.itemView.travelingPagerImg)
                            }
                        }
                    }
                }
            }
        })
        travelingDetailDayImg.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                getViewModel().currentImageSize.set(position + 1)
                getViewModel().timeDisposable?.dispose()
                getViewModel().imageShow.set(true)
                getViewModel().showNotice()
                if(position == 0){
                    enableSliding(true)
                }else{
                    enableSliding(false)
                }
            }
        })

        getViewModel().changeCardLiveData.observe(this, Observer {
            it.getContentIfNotHandled()?.let {value ->
                if(value){
                    val frag = TravelingDetailActionDialogFragment()
                    val arg = Bundle()
                    arg.putIntArray(ArgName.TRAVEL_CARD_LOCATION.name, getViewModel().modifyLocation)
                    frag.arguments = arg
                    frag.show(fragmentManager!!, TravelingDetailActionDialogFragment.TAG)
                }else{
                    val frag = TravelingDeleteActionDialogFragment()
                    val arg = Bundle()
                    arg.putBoolean(ArgName.TRAVEL_CARD_MODE.name, true)
                    frag.arguments = arg
                    frag.show(fragmentManager!!, TravelingDeleteActionDialogFragment.TAG)
                }
            }
        })

        getViewModel().completeLiveData.observe(this, Observer {
            it.getContentIfNotHandled()?.let {value ->
                if(value){
                    stopLoading()
                    fragmentPopStack()
                } else {
                    showLoading()
                }
            }
        })
    }
    inner class ViewHandler{
        fun back(){
            fragmentPopStack()
        }
        fun more(){
            val location = IntArray(2)
            travelingDetailDayTravelCardList.getLocationOnScreen(location)
            getViewModel().modifyLocation = location
            TravelCardBottomSheetDialogFragment().show(fragmentManager!!, TravelCardBottomSheetDialogFragment.TAG)
        }
    }
}