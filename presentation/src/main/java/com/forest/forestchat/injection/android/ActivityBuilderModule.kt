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
package com.forest.forestchat.injection.android

import com.forest.forestchat.feature.ambassador.AmbassadorActivity
import com.forest.forestchat.feature.ambassador.AmbassadorActivityModule
import com.forest.forestchat.feature.backup.BackupActivity
import com.forest.forestchat.feature.blocking.BlockingActivity
import com.forest.forestchat.feature.compose.ComposeActivity
import com.forest.forestchat.feature.compose.ComposeActivityModule
import com.forest.forestchat.feature.contacts.ContactsActivity
import com.forest.forestchat.feature.contacts.ContactsActivityModule
import com.forest.forestchat.feature.conversationinfo.ConversationInfoActivity
import com.forest.forestchat.feature.gallery.GalleryActivity
import com.forest.forestchat.feature.gallery.GalleryActivityModule
import com.forest.forestchat.feature.main.MainActivity
import com.forest.forestchat.feature.main.MainActivityModule
import com.forest.forestchat.feature.notificationprefs.NotificationPrefsActivity
import com.forest.forestchat.feature.notificationprefs.NotificationPrefsActivityModule
import com.forest.forestchat.feature.qkreply.QkReplyActivity
import com.forest.forestchat.feature.qkreply.QkReplyActivityModule
import com.forest.forestchat.feature.scheduled.ScheduledActivity
import com.forest.forestchat.feature.scheduled.ScheduledActivityModule
import com.forest.forestchat.feature.settings.SettingsActivity
import com.forest.forestchat.injection.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    abstract fun bindBackupActivity(): BackupActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [AmbassadorActivityModule::class])
    abstract fun bindAmbassadorActivity(): AmbassadorActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ComposeActivityModule::class])
    abstract fun bindComposeActivity(): ComposeActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ContactsActivityModule::class])
    abstract fun bindContactsActivity(): ContactsActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    abstract fun bindConversationInfoActivity(): ConversationInfoActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [GalleryActivityModule::class])
    abstract fun bindGalleryActivity(): GalleryActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [NotificationPrefsActivityModule::class])
    abstract fun bindNotificationPrefsActivity(): NotificationPrefsActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [QkReplyActivityModule::class])
    abstract fun bindQkReplyActivity(): QkReplyActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ScheduledActivityModule::class])
    abstract fun bindScheduledActivity(): ScheduledActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    abstract fun bindSettingsActivity(): SettingsActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    abstract fun bindBlockingActivity(): BlockingActivity

}
