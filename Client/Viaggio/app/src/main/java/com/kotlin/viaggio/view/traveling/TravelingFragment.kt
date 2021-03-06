package com.kotlin.viaggio.view.traveling

import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlin.viaggio.R
import com.kotlin.viaggio.data.obj.TravelCard
import com.kotlin.viaggio.data.obj.TravelCardValue
import com.kotlin.viaggio.databinding.ItemTravelingBinding
import com.kotlin.viaggio.databinding.ItemTravelingDayCountBinding
import com.kotlin.viaggio.extenstions.baseIntent
import com.kotlin.viaggio.extenstions.imageName
import com.kotlin.viaggio.view.common.BaseFragment
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrPosition
import kotlinx.android.synthetic.main.fragment_traveling.*
import kotlinx.android.synthetic.main.item_traveling.view.*
import kotlin.random.Random


class TravelingFragment : BaseFragment<TravelingFragmentViewModel>() {
    companion object {
        val TAG: String = TravelingFragment::class.java.simpleName
    }

//    override fun onResume() {
//        super.onResume()
//        if (sliderInterface == null)
//            sliderInterface = Slidr.replace(
//                travelingContainer, SlidrConfig.Builder()
//                    .position(SlidrPosition.LEFT)
//                    .build()
//            )
//    }

    lateinit var binding: com.kotlin.viaggio.databinding.FragmentTravelingBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_traveling, container, false)
        binding.viewModel = getViewModel()
        binding.viewHandler = ViewHandler()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        travelingList.layoutManager = LinearLayoutManager(context)
        travelingList.addItemDecoration(TravelingItemDecoration())
        val adapter = TravelCardAdapter()
        travelingList.adapter = adapter
        getViewModel().travelCardPagedLiveData.observe(this, Observer(adapter::submitList))

        getViewModel().completeLiveData.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                baseIntent("http://viaggio.kotlin.com/home/main/")
            }
        })
    }

    inner class ViewHandler {
        fun close() {
            fragmentPopStack()
        }

        fun enroll() {
            baseIntent("http://viaggio.kotlin.com/traveling/enroll/card/")
        }
    }

    inner class TravelCardAdapter :
        PagedListAdapter<TravelCard, RecyclerView.ViewHolder>(object :
            DiffUtil.ItemCallback<TravelCard>() {
            override fun areItemsTheSame(oldItem: TravelCard, newItem: TravelCard) = oldItem.localId == newItem.localId
            override fun areContentsTheSame(oldItem: TravelCard, newItem: TravelCard) = oldItem == newItem
        }) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            when(viewType){
                0 -> TravelCardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_traveling, parent, false))
                else -> TravelCardCountViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_traveling_day_count, parent, false))
            }
        override fun getItemViewType(position: Int): Int {
            return if(position == 0){
                1
            }else{
                if(getItem(position - 1)?.travelOfDay?:0 != getItem(position)?.travelOfDay?:0){
                    1
                }else{
                    0
                }
            }
        }


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            getViewModel().notEmpty.set(true)
            val travelCardVal = getItem(position)
            val item = travelCardVal?.let {
                TravelCardValue().apply {
                    id = it.localId
                    content = it.content
                    country = it.country
                    theme = if(it.theme.isNotEmpty()) it.theme.joinToString(", ") else resources.getString(R.string.base_theme)
                    imageName =
                        if (it.imageNames.isNotEmpty()) it.imageNames[Random.nextInt(it.imageNames.size)] else ""
                    travelId = it.travelLocalId
                    travelOfDay = it.travelOfDay
                }
            }
            when(holder){
                is TravelCardViewHolder ->{
                    holder.binding?.data = item
                    holder.binding?.viewHandler = holder.TravelCardViewHandlerImp()
                    holder.round()
                    holder.loadImage(item?.imageName)
                }
                is TravelCardCountViewHolder ->{
                    holder.binding?.data = item
                    holder.binding?.viewHandler = holder.TravelCardCountViewHandlerImp()
                    holder.round()
                    holder.loadImage(item?.imageName)
                }
            }
        }
    }

    inner class TravelCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ItemTravelingBinding>(view)
        private val imageList = listOf(
            ResourcesCompat.getDrawable(resources, R.drawable.base_image1, null),
            ResourcesCompat.getDrawable(resources, R.drawable.base_image2, null),
            ResourcesCompat.getDrawable(resources, R.drawable.base_image3, null),
            ResourcesCompat.getDrawable(resources, R.drawable.base_image4, null),
            ResourcesCompat.getDrawable(resources, R.drawable.base_image5, null),
            ResourcesCompat.getDrawable(resources, R.drawable.base_image6, null),
            ResourcesCompat.getDrawable(resources, R.drawable.base_image7, null)
        )
        fun round(){
            val drawable = context?.getDrawable(R.drawable.round_bg) as GradientDrawable
            itemView.travelingItemThemeImg.background = drawable
            itemView.travelingItemThemeImg.clipToOutline = true
        }
        fun loadImage(imageName: String?) {
            if (TextUtils.isEmpty(imageName).not()) {
                Glide.with(itemView)
                    .load(requireContext().imageName(imageName!!))
                    .into(itemView.travelingItemThemeImg)
            } else{
                Glide.with(itemView)
                    .load(imageList[java.util.Random().nextInt(imageList.size)])
                    .into(itemView.travelingItemThemeImg)
            }
        }

        inner class TravelCardViewHandlerImp:TravelCardViewHandler {
            override fun detail() {
                getViewModel().setSelectedTravelCard(binding?.data?.id)
                baseIntent("http://viaggio.kotlin.com/traveling/detail/")
            }
        }
    }
    inner class TravelCardCountViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ItemTravelingDayCountBinding>(view)
        private val imageList = listOf(
            ResourcesCompat.getDrawable(resources, R.drawable.base_image1, null),
            ResourcesCompat.getDrawable(resources, R.drawable.base_image2, null),
            ResourcesCompat.getDrawable(resources, R.drawable.base_image3, null),
            ResourcesCompat.getDrawable(resources, R.drawable.base_image4, null),
            ResourcesCompat.getDrawable(resources, R.drawable.base_image5, null),
            ResourcesCompat.getDrawable(resources, R.drawable.base_image6, null),
            ResourcesCompat.getDrawable(resources, R.drawable.base_image7, null)
        )
        fun round(){
            val drawable = context?.getDrawable(R.drawable.round_bg) as GradientDrawable
            itemView.travelingItemThemeImg.background = drawable
            itemView.travelingItemThemeImg.clipToOutline = true
        }
        fun loadImage(imageName: String?) {
            if (TextUtils.isEmpty(imageName).not()) {
                Glide.with(itemView)
                    .load(requireContext().imageName(imageName!!))
                    .into(itemView.travelingItemThemeImg)
            } else{
                Glide.with(itemView)
                    .load(imageList[java.util.Random().nextInt(imageList.size)])
                    .into(itemView.travelingItemThemeImg)
            }
        }
        inner class TravelCardCountViewHandlerImp:TravelCardViewHandler {
            override fun detail() {
                getViewModel().setSelectedTravelCard(binding?.data?.id)
                baseIntent("http://viaggio.kotlin.com/traveling/detail/")
            }
        }
    }
}
interface TravelCardViewHandler{
    fun detail()
}


class TravelingItemDecoration :
    RecyclerView.ItemDecoration() {
    private var firstHorMargin: Float? = null
    private var remainHorMargin: Float? = null

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        if (parent.getChildAdapterPosition(view) == 0) {
            val firstHorMarginVal1 = firstHorMargin
                ?: (parent.context.resources.getDimension(R.dimen.common_margin))
            firstHorMargin = firstHorMarginVal1
            outRect.top = firstHorMarginVal1.toInt()
        } else {
            val remainHorMargin1 = remainHorMargin
                ?: (parent.context.resources.getDimension(R.dimen.traveling_remain_top))
            remainHorMargin = remainHorMargin1
            outRect.top = (remainHorMargin1.toInt() * -1)
        }
    }
}