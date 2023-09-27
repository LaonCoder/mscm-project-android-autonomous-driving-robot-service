package com.example.mscmproject.feature_main.presentation.dispatch

import androidx.lifecycle.ViewModel
import com.example.mscmproject.feature_main.domain.repository.DispatchRepository
import com.example.mscmproject.feature_main.domain.repository.HomeRepository
import javax.inject.Inject

class DispatchViewModel @Inject constructor(
    private val dispatchRepository: DispatchRepository
): ViewModel() {

}