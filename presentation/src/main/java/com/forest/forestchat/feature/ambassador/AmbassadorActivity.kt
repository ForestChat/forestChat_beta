/*
 * Copyright (C) 2021 Matthieu <matthieu@forestchat.org>
 *
 * This file is part of ForestChat.
 *
 * ForestChat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ForestChat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ForestChat.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.forest.forestchat.feature.ambassador

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.*
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.forest.forestchat.R
import com.forest.forestchat.common.Navigator
import com.forest.forestchat.common.base.QkThemedActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.ambassador_activity.*
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject

class AmbassadorActivity : QkThemedActivity(), AmbassadorView {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory)[AmbassadorViewModel::class.java] }

    @Inject
    lateinit var navigator: Navigator

    private var isOnStop : Boolean = false
    private var isInvitationCall : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ambassador_activity)
        showBackButton(true)
        viewModel.bindView(this)

        fabAmbassador.setOnClickListener {
            requestAmbassador()
        }
        fabInvitation.setOnClickListener {
            isInvitationCall = true
            navigator.showInvite(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            requestCode == 42400 && isInvitationCall && isOnStop -> {
                val sharedPreferences: SharedPreferences = getSharedPreferences(
                        "shared_preferences_invitation",
                        Context.MODE_PRIVATE
                )
                val numberInvitation = sharedPreferences.getInt("invitationKey", 0) + 1
                sharedPreferences.edit().putInt("invitationKey", numberInvitation).apply()

                showAmbassador(numberInvitation)
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }

        isOnStop = false
        isInvitationCall = false
    }

    override fun onStop() {
        super.onStop()
        if (isInvitationCall) {
            isOnStop = true
        }
    }

    override fun render(state: AmbassadorState) {
        title = getString(R.string.ambassador_title)

        val sharedPreferences: SharedPreferences = getSharedPreferences(
                "shared_preferences_invitation",
                Context.MODE_PRIVATE
        )
        val number = sharedPreferences.getInt("invitationKey", 0)

        showAmbassador(number)
    }

    private fun showAmbassador(numberOfShare: Int) {
        ambassadorShareNumber.text = numberOfShare.toString()
        when (numberOfShare > 0) {
            true -> {
                fabInvitation.visibility = View.GONE
                fabAmbassador.visibility = View.VISIBLE
                ambassadorNoResult.visibility = View.GONE
                ambassadorResult.visibility = View.VISIBLE
            }
            false -> {
                fabInvitation.visibility = View.VISIBLE
                fabAmbassador.visibility = View.GONE
                ambassadorNoResult.visibility = View.VISIBLE
                ambassadorResult.visibility = View.GONE
            }
        }
    }

    private fun requestAmbassador() {
        val drawable = when (Locale.getDefault().displayLanguage) {
            "french" -> R.drawable.fr_ambassador
            "français" -> R.drawable.fr_ambassador
            else -> R.drawable.en_ambassador
        }

        val sharedPreferences: SharedPreferences = getSharedPreferences(
                "shared_preferences_invitation",
                Context.MODE_PRIVATE
        )
        val number = sharedPreferences.getInt("invitationKey", 0)

        drawTextToBitmap(this, drawable, number.toString())?.let {
            val storageDir = File(this.cacheDir, "documents")
            val document = getFileReference(storageDir, "ambassador.png",
                    createDirIfNeeded = true, deleteExistingFile = true)
            val out = FileOutputStream(document)
            it.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()

            navigator.shareFile(document)
        }
    }

    private fun getFileReference(dir: File, filename: String, createDirIfNeeded: Boolean, deleteExistingFile: Boolean): File {
        if (createDirIfNeeded && !dir.exists()) {
            dir.mkdir()
        }
        val file = File(dir, filename)
        if (deleteExistingFile && file.exists()) {
            file.delete()
        }
        return file
    }

    private fun drawTextToBitmap(mContext: Context, resourceId: Int, mText: String): Bitmap? =
            try {
                val resources: Resources = mContext.resources
                val scale: Float = resources.displayMetrics.density
                var bitmap = BitmapFactory.decodeResource(resources, resourceId)
                val bitmapConfig = bitmap.config
                // so we need to convert it to mutable one
                bitmap = bitmap.copy(bitmapConfig, true)
                val canvas = Canvas(bitmap)
                // new antialised Paint
                val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                // text color - #FFFFFF
                paint.color = Color.rgb(255, 255, 255)
                // text size in pixels
                paint.textSize = 40 * scale
                // text shadow
                paint.setShadowLayer(4f, 0f, 4f, Color.DKGRAY)

                // draw text to the Canvas center
                val bounds = Rect()
                paint.getTextBounds(mText, 0, mText.length, bounds)
                val x = (bitmap.width / 2).toFloat() - (bounds.width() / 2).toFloat()
                val y = (bitmap.height / 2).toFloat() + (bounds.height() / 2).toFloat()
                canvas.drawText(mText, x, y, paint)
                bitmap
            } catch (e: java.lang.Exception) {
                null
            }

}