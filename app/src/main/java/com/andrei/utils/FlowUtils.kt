package com.andrei.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn


fun <T> Flow<T>.defaultSharedFlow(coroutineScope: CoroutineScope):SharedFlow<T> = this.shareIn(scope = coroutineScope, replay = 0, started = SharingStarted.WhileSubscribed())