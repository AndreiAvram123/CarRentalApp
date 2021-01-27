package com.andrei.engine.configuration

import com.andrei.engine.DTOEntities.ApiResult
import retrofit2.Call

typealias CallWrapper<T> =Call<ApiResult<T>>