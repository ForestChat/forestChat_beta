/*
 * Copyright (C) 2017 Moez Bhatti <moez.bhatti@gmail.com>
 *
 * This file is part of QKSMS.
 *
 * QKSMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * QKSMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with QKSMS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.forest.forestchat.injection

import com.forest.forestchat.common.QKApplication
import com.forest.forestchat.common.QkDialog
import com.forest.forestchat.common.util.QkChooserTargetService
import com.forest.forestchat.common.widget.*
import com.forest.forestchat.feature.backup.BackupController
import com.forest.forestchat.feature.blocking.BlockingController
import com.forest.forestchat.feature.blocking.manager.BlockingManagerController
import com.forest.forestchat.feature.blocking.messages.BlockedMessagesController
import com.forest.forestchat.feature.blocking.numbers.BlockedNumbersController
import com.forest.forestchat.feature.compose.editing.DetailedChipView
import com.forest.forestchat.feature.conversationinfo.injection.ConversationInfoComponent
import com.forest.forestchat.feature.settings.SettingsController
import com.forest.forestchat.feature.settings.about.AboutController
import com.forest.forestchat.feature.settings.swipe.SwipeActionsController
import com.forest.forestchat.feature.themepicker.injection.ThemePickerComponent
import com.forest.forestchat.feature.widget.WidgetAdapter
import com.forest.forestchat.injection.android.ActivityBuilderModule
import com.forest.forestchat.injection.android.BroadcastReceiverBuilderModule
import com.forest.forestchat.injection.android.ServiceBuilderModule
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityBuilderModule::class,
    BroadcastReceiverBuilderModule::class,
    ServiceBuilderModule::class])
interface AppComponent {

    fun conversationInfoBuilder(): ConversationInfoComponent.Builder
    fun themePickerBuilder(): ThemePickerComponent.Builder

    fun inject(application: QKApplication)

    fun inject(controller: AboutController)
    fun inject(controller: BackupController)
    fun inject(controller: BlockedMessagesController)
    fun inject(controller: BlockedNumbersController)
    fun inject(controller: BlockingController)
    fun inject(controller: BlockingManagerController)
    fun inject(controller: SettingsController)
    fun inject(controller: SwipeActionsController)

    fun inject(dialog: QkDialog)

    fun inject(service: WidgetAdapter)

    /**
     * This can't use AndroidInjection, or else it will crash on pre-marshmallow devices
     */
    fun inject(service: QkChooserTargetService)

    fun inject(view: AvatarView)
    fun inject(view: DetailedChipView)
    fun inject(view: PagerTitleView)
    fun inject(view: PreferenceView)
    fun inject(view: RadioPreferenceView)
    fun inject(view: QkEditText)
    fun inject(view: QkSwitch)
    fun inject(view: QkTextView)

}
