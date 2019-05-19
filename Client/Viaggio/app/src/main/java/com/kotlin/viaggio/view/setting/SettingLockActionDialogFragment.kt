package com.kotlin.viaggio.view.setting

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.CycleInterpolator
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.kotlin.viaggio.R
import com.kotlin.viaggio.android.ArgName
import com.kotlin.viaggio.view.common.BaseDialogFragment
import kotlinx.android.synthetic.main.fragment_action_dialog_setting_lock.*
import kotlinx.android.synthetic.main.fragment_action_dialog_traveling_finish.containerPop

class SettingLockActionDialogFragment:BaseDialogFragment<SettingLockActionDialogFragmentViewModel>(){
    companion object {
        val TAG: String = SettingLockActionDialogFragment::class.java.simpleName
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.let {
            getViewModel().enrollMode.set(it.getBoolean(ArgName.LOCK_ENROLL_MODE.name, false))
        }
    }
    lateinit var binding:com.kotlin.viaggio.databinding.FragmentActionDialogSettingLockBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_action_dialog_setting_lock, container, false)
        binding.viewModel = getViewModel()
        binding.viewHandler = ViewHandler()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(getViewModel().enrollMode.get().not()){
            isCancelable = false
        }
        getViewModel().completeLiveDate.observe(this, Observer {
            it.getContentIfNotHandled()?.let { value ->
                if(value) {
                    dismiss()
                } else{
                    var animator = password1.animate()
                        .setDuration(100)
                        .x(0f)
                        .xBy(10f)
                        .setInterpolator(CycleInterpolator(5f))
                    animator.start()
                    animator = password2.animate()
                        .setDuration(100)
                        .x(0f)
                        .xBy(10f)
                        .setInterpolator(CycleInterpolator(5f))
                    animator.start()
                    animator = password3.animate()
                        .setDuration(100)
                        .x(0f)
                        .xBy(10f)
                        .setInterpolator(CycleInterpolator(5f))
                    animator.start()
                    animator = password4.animate()
                        .setDuration(100)
                        .x(0f)
                        .xBy(10f)
                        .setInterpolator(CycleInterpolator(5f))
                    animator.start()

                }
            }
        })
    }


    inner class ViewHandler{
        fun choose(num:Int){
            getViewModel().choose(num)
        }
        fun cancel(){
            getViewModel().cancel()
        }
        fun close(){
            dismiss()
        }
    }
}