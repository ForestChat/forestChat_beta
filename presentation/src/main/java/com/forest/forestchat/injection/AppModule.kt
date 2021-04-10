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

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.lifecycle.ViewModelProvider
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.forest.forestchat.blocking.BlockingClient
import com.forest.forestchat.blocking.BlockingManager
import com.forest.forestchat.common.ViewModelFactory
import com.forest.forestchat.common.util.BillingManagerImpl
import com.forest.forestchat.common.util.NotificationManagerImpl
import com.forest.forestchat.common.util.ShortcutManagerImpl
import com.forest.forestchat.feature.conversationinfo.injection.ConversationInfoComponent
import com.forest.forestchat.feature.themepicker.injection.ThemePickerComponent
import com.forest.forestchat.listener.ContactAddedListener
import com.forest.forestchat.listener.ContactAddedListenerImpl
import com.forest.forestchat.manager.ActiveConversationManager
import com.forest.forestchat.manager.ActiveConversationManagerImpl
import com.forest.forestchat.manager.AlarmManager
import com.forest.forestchat.manager.AlarmManagerImpl
import com.forest.forestchat.manager.AnalyticsManager
import com.forest.forestchat.manager.AnalyticsManagerImpl
import com.forest.forestchat.manager.BillingManager
import com.forest.forestchat.manager.ChangelogManager
import com.forest.forestchat.manager.ChangelogManagerImpl
import com.forest.forestchat.manager.KeyManager
import com.forest.forestchat.manager.KeyManagerImpl
import com.forest.forestchat.manager.NotificationManager
import com.forest.forestchat.manager.PermissionManager
import com.forest.forestchat.manager.PermissionManagerImpl
import com.forest.forestchat.manager.RatingManager
import com.forest.forestchat.manager.ReferralManager
import com.forest.forestchat.manager.ReferralManagerImpl
import com.forest.forestchat.manager.ShortcutManager
import com.forest.forestchat.manager.WidgetManager
import com.forest.forestchat.manager.WidgetManagerImpl
import com.forest.forestchat.mapper.CursorToContact
import com.forest.forestchat.mapper.CursorToContactGroup
import com.forest.forestchat.mapper.CursorToContactGroupImpl
import com.forest.forestchat.mapper.CursorToContactGroupMember
import com.forest.forestchat.mapper.CursorToContactGroupMemberImpl
import com.forest.forestchat.mapper.CursorToContactImpl
import com.forest.forestchat.mapper.CursorToConversation
import com.forest.forestchat.mapper.CursorToConversationImpl
import com.forest.forestchat.mapper.CursorToMessage
import com.forest.forestchat.mapper.CursorToMessageImpl
import com.forest.forestchat.mapper.CursorToPart
import com.forest.forestchat.mapper.CursorToPartImpl
import com.forest.forestchat.mapper.CursorToRecipient
import com.forest.forestchat.mapper.CursorToRecipientImpl
import com.forest.forestchat.mapper.RatingManagerImpl
import com.forest.forestchat.repository.BackupRepository
import com.forest.forestchat.repository.BackupRepositoryImpl
import com.forest.forestchat.repository.BlockingRepository
import com.forest.forestchat.repository.BlockingRepositoryImpl
import com.forest.forestchat.repository.ContactRepository
import com.forest.forestchat.repository.ContactRepositoryImpl
import com.forest.forestchat.repository.ConversationRepository
import com.forest.forestchat.repository.ConversationRepositoryImpl
import com.forest.forestchat.repository.MessageRepository
import com.forest.forestchat.repository.MessageRepositoryImpl
import com.forest.forestchat.repository.ScheduledMessageRepository
import com.forest.forestchat.repository.ScheduledMessageRepositoryImpl
import com.forest.forestchat.repository.SyncRepository
import com.forest.forestchat.repository.SyncRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(subcomponents = [
    ConversationInfoComponent::class,
    ThemePickerComponent::class])
class AppModule(private var application: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = application

    @Provides
    fun provideContentResolver(context: Context): ContentResolver = context.contentResolver

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @Singleton
    fun provideRxPreferences(preferences: SharedPreferences): RxSharedPreferences {
        return RxSharedPreferences.create(preferences)
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
    }

    @Provides
    fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory = factory

    // Listener

    @Provides
    fun provideContactAddedListener(listener: ContactAddedListenerImpl): ContactAddedListener = listener

    // Manager

    @Provides
    fun provideBillingManager(manager: BillingManagerImpl): BillingManager = manager

    @Provides
    fun provideActiveConversationManager(manager: ActiveConversationManagerImpl): ActiveConversationManager = manager

    @Provides
    fun provideAlarmManager(manager: AlarmManagerImpl): AlarmManager = manager

    @Provides
    fun provideAnalyticsManager(manager: AnalyticsManagerImpl): AnalyticsManager = manager

    @Provides
    fun blockingClient(manager: BlockingManager): BlockingClient = manager

    @Provides
    fun changelogManager(manager: ChangelogManagerImpl): ChangelogManager = manager

    @Provides
    fun provideKeyManager(manager: KeyManagerImpl): KeyManager = manager

    @Provides
    fun provideNotificationsManager(manager: NotificationManagerImpl): NotificationManager = manager

    @Provides
    fun providePermissionsManager(manager: PermissionManagerImpl): PermissionManager = manager

    @Provides
    fun provideRatingManager(manager: RatingManagerImpl): RatingManager = manager

    @Provides
    fun provideShortcutManager(manager: ShortcutManagerImpl): ShortcutManager = manager

    @Provides
    fun provideReferralManager(manager: ReferralManagerImpl): ReferralManager = manager

    @Provides
    fun provideWidgetManager(manager: WidgetManagerImpl): WidgetManager = manager

    // Mapper

    @Provides
    fun provideCursorToContact(mapper: CursorToContactImpl): CursorToContact = mapper

    @Provides
    fun provideCursorToContactGroup(mapper: CursorToContactGroupImpl): CursorToContactGroup = mapper

    @Provides
    fun provideCursorToContactGroupMember(mapper: CursorToContactGroupMemberImpl): CursorToContactGroupMember = mapper

    @Provides
    fun provideCursorToConversation(mapper: CursorToConversationImpl): CursorToConversation = mapper

    @Provides
    fun provideCursorToMessage(mapper: CursorToMessageImpl): CursorToMessage = mapper

    @Provides
    fun provideCursorToPart(mapper: CursorToPartImpl): CursorToPart = mapper

    @Provides
    fun provideCursorToRecipient(mapper: CursorToRecipientImpl): CursorToRecipient = mapper

    // Repository

    @Provides
    fun provideBackupRepository(repository: BackupRepositoryImpl): BackupRepository = repository

    @Provides
    fun provideBlockingRepository(repository: BlockingRepositoryImpl): BlockingRepository = repository

    @Provides
    fun provideContactRepository(repository: ContactRepositoryImpl): ContactRepository = repository

    @Provides
    fun provideConversationRepository(repository: ConversationRepositoryImpl): ConversationRepository = repository

    @Provides
    fun provideMessageRepository(repository: MessageRepositoryImpl): MessageRepository = repository

    @Provides
    fun provideScheduledMessagesRepository(repository: ScheduledMessageRepositoryImpl): ScheduledMessageRepository = repository

    @Provides
    fun provideSyncRepository(repository: SyncRepositoryImpl): SyncRepository = repository

}