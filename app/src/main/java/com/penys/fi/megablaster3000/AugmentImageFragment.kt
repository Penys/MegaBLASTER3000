package com.penys.fi.megablaster3000

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment


class AugmentImageFragment: ArFragment() {

    private val SAMPLE_IMAGE_DATABASE = "sample_database.imgdb"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
    Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)
        arSceneView.planeRenderer.isEnabled = false
        return view
    }

    override fun getSessionConfiguration(session: Session?): Config {
        val config = super.getSessionConfiguration(session)
        setupAugmentedImageDatabase(config, session)
        return config
    }

    private fun setupAugmentedImageDatabase(config: Config, session: Session?) {
        val augmentedImageDb: AugmentedImageDatabase
        val assetManager = context!!.assets
        val inputStream1 = assetManager.open("sea.jpg")
        val augmentedImageBitmap1 = BitmapFactory.decodeStream(inputStream1)
        val inputStream2 = assetManager.open("tits.jpg")
        val augmentedImageBitmap2 = BitmapFactory.decodeStream(inputStream2)
        augmentedImageDb = AugmentedImageDatabase(session)
        augmentedImageDb.addImage("sea", augmentedImageBitmap1)
        augmentedImageDb.addImage("tits", augmentedImageBitmap2)
        config.augmentedImageDatabase = augmentedImageDb
    }
}