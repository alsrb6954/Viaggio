package com.kotlin.viaggio.view.camera

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.kotlin.viaggio.R
import com.kotlin.viaggio.data.`object`.PermissionError
import com.kotlin.viaggio.view.common.BaseFragment
import io.fotoapparat.Fotoapparat
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.*
import kotlinx.android.synthetic.main.fragment_camera.*
import org.jetbrains.anko.support.v4.toast

class CameraFragment:BaseFragment<CameraFragmentViewModel>() {
    private lateinit var fotoapparat: Fotoapparat
    lateinit var binding:com.kotlin.viaggio.databinding.FragmentCameraBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera, container, false)
        binding.viewModel = getViewModel()
        binding.viewHandler = ViewHandler()
        fotoapparat = Fotoapparat
            .with(context!!)
            .into(binding.cameraView)           // view which will draw the camera preview
            .previewScaleType(ScaleType.CenterCrop)  // we want the preview to fill the view
            .photoResolution(highestResolution())   // we want to have the biggest photo possible
            .lensPosition(back())       // we want back camera
            .focusMode(
                firstAvailable(  // (optional) use the first focus mode which is supported by device
                    continuousFocusPicture(),
                    autoFocus(), // in case if continuous focus is not available on device, auto focus will be used
                    fixed()             // if even auto focus is not available - fixed focus mode will be used
                )
            ).build()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { contextVal ->
            Glide.with(contextVal)
                .load(resources.getDrawable(R.drawable.empty_gallery, null))
                .into(cameraViewImage)
        }

        getViewModel().permissionRequestMsg.observe(this, Observer {
            when(it){
                PermissionError.STORAGE_PERMISSION->toast(resources.getString(R.string.storage_permission))
                else -> {}
            }
        })
    }

    inner class ViewHandler{
        fun shutter(){
            val scale = ScaleAnimation(0.8f,1f, 0.8f,1f)
            scale.duration = 200
            cameraViewShutter.animate()
            cameraViewShutter.startAnimation(scale)

            val photoResult = fotoapparat.takePicture()
            Log.d("hoho", "$photoResult")
//            getViewModel().savePicture(photoResult)
        }
        fun close(){
            fragmentPopStack()
        }
        fun imageOpen(){
            getViewModel().permissionCheck(rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE))
        }
    }

    override fun onStart() {
        super.onStart()
        fotoapparat.start()
    }

    override fun onStop() {
        super.onStop()
        fotoapparat.stop()
    }
}