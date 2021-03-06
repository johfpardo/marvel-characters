package com.johfpardo.marvelcharacters.utils.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> Fragment.getViewModel(
    factory: ViewModelProvider.Factory, body: T.() -> Unit = {}
): T {
    val vm = ViewModelProvider(this, factory)[T::class.java]
    vm.body()
    return vm
}
