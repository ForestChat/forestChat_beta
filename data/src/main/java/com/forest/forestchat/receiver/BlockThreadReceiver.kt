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
package com.forest.forestchat.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.forest.forestchat.blocking.BlockingClient
import com.forest.forestchat.interactor.MarkBlocked
import com.forest.forestchat.repository.ConversationRepository
import com.forest.forestchat.util.Preferences
import dagger.android.AndroidInjection
import javax.inject.Inject

class BlockThreadReceiver : BroadcastReceiver() {

    @Inject lateinit var blockingClient: BlockingClient
    @Inject lateinit var conversationRepo: ConversationRepository
    @Inject lateinit var markBlocked: MarkBlocked
    @Inject lateinit var prefs: Preferences

    override fun onReceive(context: Context, intent: Intent) {
        AndroidInjection.inject(this, context)

        val pendingResult = goAsync()
        val threadId = intent.getLongExtra("threadId", 0)
        val conversation = conversationRepo.getConversation(threadId)!!
        val blockingManager = prefs.blockingManager.get()

        blockingClient
                .block(conversation.recipients.map { it.address })
                .andThen(markBlocked.buildObservable(MarkBlocked.Params(listOf(threadId), blockingManager, null)))
                .subscribe { pendingResult.finish() }
    }

}
