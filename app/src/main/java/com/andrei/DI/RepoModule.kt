package com.andrei.DI

import com.andrei.engine.repository.implementation.LoginRepositoryImpl
import com.andrei.engine.repository.implementation.SignUpRepositoryImpl
import com.andrei.engine.repository.interfaces.LoginRepository
import com.andrei.engine.repository.interfaces.SignUpRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: LoginRepositoryImpl): LoginRepository

    @Binds
    abstract fun bindSignUpRepository(signUpRepositoryImpl: SignUpRepositoryImpl) : SignUpRepository

}