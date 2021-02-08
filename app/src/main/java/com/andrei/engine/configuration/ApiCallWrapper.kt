package com.andrei.engine.configuration

import retrofit2.Call

typealias CallWrapper<T> =Call<ApiResult<T>>