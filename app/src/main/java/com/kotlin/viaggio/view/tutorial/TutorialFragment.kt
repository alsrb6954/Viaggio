package com.kotlin.viaggio.view.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.kotlin.viaggio.R
import com.kotlin.viaggio.view.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_tutorial.*
import kotlinx.android.synthetic.main.item_tutorial.view.*

class TutorialFragment:BaseFragment<TutorialFragmentViewModel>() {
    private lateinit var binding:com.kotlin.viaggio.databinding.FragmentTutorialBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tutorial, container, false)
        binding.viewModel = getViewModel()
        binding.viewHandler = ViewHolder()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModel().tutorialList.observe(this, Observer {
            tutorialPager.adapter = object: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                    TutorialViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_tutorial, parent, false))
                override fun getItemCount() = it.size
                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                    holder as TutorialViewHolder
                    holder.binding?.data = it[position]

                    holder.itemView.tutorialAnim.setAnimation(it[position].animRes)
                }
            }
            tutorialPagerIndicator.setCurrPageNumber(0)
            tutorialPagerIndicator.setTotalPageNumber(it.size)
        })

        tutorialPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tutorialPagerIndicator.setCurrPageNumber(position)
            }
        })
    }

    inner class ViewHolder{
        fun skip(){
        }
        fun login(){
        }
    }

    inner class TutorialViewHolder(view:View):RecyclerView.ViewHolder(view){
        val binding = DataBindingUtil.bind<com.kotlin.viaggio.databinding.ItemTutorialBinding>(view)
    }
}