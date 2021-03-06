package com.kotlin.viaggio.view.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.kotlin.viaggio.R
import com.kotlin.viaggio.view.common.BaseDialogFragment
import com.kotlin.viaggio.view.common.BaseViewModel
import kotlinx.android.synthetic.main.fragment_action_dialog_traveling_finish.*
import javax.inject.Inject

class SyncActionDialogFragment:BaseDialogFragment<SyncActionDialogFragmentViewModel>(){
    companion object {
        val TAG: String = UploadCheckActionDialogFragment::class.java.simpleName
    }
    lateinit var binding:com.kotlin.viaggio.databinding.FragmentActionDialogSyncBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_action_dialog_sync, container, false)
        binding.viewModel = getViewModel()
        binding.viewHandler = ViewHandler()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        containerPop.setOnClickListener { dismiss() }
    }

    inner class ViewHandler{
        fun cancel(){
            dismiss()
        }
        fun sync(){
            getViewModel().sync()
            dismiss()
        }
    }
}
class SyncActionDialogFragmentViewModel @Inject constructor() : BaseViewModel(){
    fun sync(){
        rxEventBus.sync.onNext(true)
    }
}