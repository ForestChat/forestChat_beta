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
package com.forest.forestchat.feature.themepicker

import com.f2prateek.rx.preferences2.Preference
import com.forest.forestchat.common.Navigator
import com.forest.forestchat.common.base.QkPresenter
import com.forest.forestchat.common.util.Colors
import com.forest.forestchat.manager.WidgetManager
import com.forest.forestchat.util.Preferences
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.withLatestFrom
import javax.inject.Inject
import javax.inject.Named

class ThemePickerPresenter @Inject constructor(
    prefs: Preferences,
    @Named("recipientId") private val recipientId: Long,
    private val colors: Colors,
    private val navigator: Navigator,
    private val widgetManager: WidgetManager
) : QkPresenter<ThemePickerView, ThemePickerState>(ThemePickerState(recipientId = recipientId)) {

    private val theme: Preference<Int> = prefs.theme(recipientId)

    override fun bindIntents(view: ThemePickerView) {
        super.bindIntents(view)

        theme.asObservable()
                .autoDisposable(view.scope())
                .subscribe { color -> view.setCurrentTheme(color) }

        // Update the theme when a material theme is clicked
        view.themeSelected()
                .autoDisposable(view.scope())
                .subscribe { color ->
                    theme.set(color)
                    if (recipientId == 0L) {
                        widgetManager.updateTheme()
                    }
                }

        // Update the color of the apply button
        view.hsvThemeSelected()
                .doOnNext { color -> newState { copy(newColor = color) } }
                .map { color -> colors.textPrimaryOnThemeForColor(color) }
                .doOnNext { color -> newState { copy(newTextColor = color) } }
                .autoDisposable(view.scope())
                .subscribe()

        // Toggle the visibility of the apply group
        Observables.combineLatest(theme.asObservable(), view.hsvThemeSelected()) { old, new -> old != new }
                .autoDisposable(view.scope())
                .subscribe { themeChanged -> newState { copy(applyThemeVisible = themeChanged) } }

        // Update the theme, when apply is clicked
        view.applyHsvThemeClicks()
                .withLatestFrom(view.hsvThemeSelected()) { _, color -> color }
                .autoDisposable(view.scope())
                .subscribe { color ->
                    theme.set(color)
                    if (recipientId == 0L) {
                        widgetManager.updateTheme()
                    }
                }

        // Reset the theme
        view.clearHsvThemeClicks()
                .withLatestFrom(theme.asObservable()) { _, color -> color }
                .autoDisposable(view.scope())
                .subscribe { color -> view.setCurrentTheme(color) }
    }

}