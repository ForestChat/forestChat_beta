package com.forest.forestchat.feature.ambassador

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.forest.forestchat.injection.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class AmbassadorActivityModule {

    @Provides
    fun provideIntent(activity: AmbassadorActivity): Intent = activity.intent

    @Provides
    @IntoMap
    @ViewModelKey(AmbassadorViewModel::class)
    fun provideAmbassadorViewModel(viewModel: AmbassadorViewModel): ViewModel = viewModel
}