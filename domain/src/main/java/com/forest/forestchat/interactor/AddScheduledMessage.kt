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
package com.forest.forestchat.interactor

import com.forest.forestchat.repository.ScheduledMessageRepository
import io.reactivex.Flowable
import javax.inject.Inject

class AddScheduledMessage @Inject constructor(
    private val scheduledMessageRepo: ScheduledMessageRepository,
    private val updateScheduledMessageAlarms: UpdateScheduledMessageAlarms
) : Interactor<AddScheduledMessage.Params>() {

    data class Params(
        val date: Long,
        val subId: Int,
        val recipients: List<String>,
        val sendAsGroup: Boolean,
        val body: String,
        val attachments: List<String>
    )

    override fun buildObservable(params: Params): Flowable<*> {
        return Flowable.just(params)
                .map {
                    scheduledMessageRepo.saveScheduledMessage(it.date, it.subId, it.recipients, it.sendAsGroup, it.body,
                            it.attachments)
                }
                .flatMap { updateScheduledMessageAlarms.buildObservable(Unit) }
    }

}