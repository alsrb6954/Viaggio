package com.kotlin.viaggio.view.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin.viaggio.R
import com.kotlin.viaggio.ioc.module.common.HasAndroidXFragmentInjector
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import java.lang.ref.WeakReference
import javax.inject.Inject

abstract class BaseActivity<E : ViewModel> : AppCompatActivity(), HasAndroidXFragmentInjector {
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    internal lateinit var viewModel: E

    var viewModelProvider: WeakReference<ViewModelProvider>? = null
    var loadingDialogFragment: LoadingDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        (getViewModel() as BaseViewModel).initialize()
    }

    override fun androidXFragmentInjector() = fragmentInjector

    fun getViewModel(): E =
        viewModelProvider.let { vmpRef ->
            vmpRef?.get()?.let {
                it
            } ?: getNewViewModelProvider()
        }.get(viewModel::class.java)

    private fun getNewViewModelProvider(): ViewModelProvider {
        val nonNullViewModelProviderVal = ViewModelProvider(viewModelStore, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                try {
                    @Suppress("UNCHECKED_CAST")
                    return viewModel as T
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
        })
        viewModelProvider = WeakReference(nonNullViewModelProviderVal)
        return nonNullViewModelProviderVal
    }

    fun baseShowFragment(fragment:BaseFragment<*>){
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, fragment, null)
            .commit()
    }
    fun baseShowAddBackFragment(fragment:BaseFragment<*>){
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.content_frame, fragment, null)
            .commit()
    }

    fun showLoading() {
        val loadingDialogFragment1Val = loadingDialogFragment?.run {
            return
        }?:supportFragmentManager.findFragmentByTag(LoadingDialogFragment.TAG)
        val loadingDialogFragmentVal = loadingDialogFragment1Val?.run {
            return
        }?:LoadingDialogFragment()
        loadingDialogFragment = loadingDialogFragmentVal
        loadingDialogFragmentVal.show(supportFragmentManager, LoadingDialogFragment.TAG)
    }

    fun stopLoading() {
        loadingDialogFragment = null
        val loadingDialogFragment1Val = supportFragmentManager.findFragmentByTag(LoadingDialogFragment.TAG)
        loadingDialogFragment1Val?.let {
            (it as LoadingDialogFragment).dismiss()
        }
    }
}