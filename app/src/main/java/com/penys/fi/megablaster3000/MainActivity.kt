package com.penys.fi.megablaster3000

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
class MainActivity : AppCompatActivity() {

    lateinit var fragment: ArFragment
    lateinit var fitToScanImageView: ImageView
    lateinit var duckyRenderable: ModelRenderable
    lateinit var miniRenderable: ModelRenderable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragment = supportFragmentManager.findFragmentById(R.id.arimage_fragment) as ArFragment
        fitToScanImageView = findViewById(R.id.fit_to_scan_img)


        val ducky = ModelRenderable.builder()
                .setSource(this, Uri.parse("ducky.sfb"))
                .build()

        ducky.thenAccept { it -> duckyRenderable = it }

        val mini = ModelRenderable.builder()
                .setSource(this, Uri.parse("mini_obj.sfb"))
                .build()

        mini.thenAccept { it -> miniRenderable = it }

        fragment.arSceneView.scene.addOnUpdateListener { frameTime ->
            onUpdate(frameTime)

        }
    }

    private fun onUpdate(frameTime: FrameTime) {
        fragment.onUpdate(frameTime)
        val arFrame = fragment.arSceneView.arFrame
        if (arFrame == null || arFrame.camera.trackingState != TrackingState.TRACKING) {
            return
        }
        val updatedAugmentedImages = arFrame.getUpdatedTrackables(AugmentedImage::class.java)
        updatedAugmentedImages.forEach {
            when (it.trackingState) {
                TrackingState.PAUSED -> {

                }

                TrackingState.STOPPED
                -> {

                }
            }
        }

        updatedAugmentedImages.forEach {
            when (it.trackingState) {

                TrackingState.TRACKING
                -> {
                    var anchors = it.anchors
                    if (anchors.isEmpty()) {
                        fitToScanImageView.visibility = View.GONE
                        val pose = Pose.makeTranslation(0.0f, 0.0f, -1.3f)
                        val anchor = it.createAnchor(pose)
                        val anchorNode = AnchorNode(anchor)
                        anchorNode.setParent(fragment.arSceneView.scene)
                        val imgNode = TransformableNode(fragment.transformationSystem)
                        imgNode.setParent(anchorNode)
                        if (it.name == "tits") {
                            imgNode.renderable = duckyRenderable
                        } else {
                            imgNode.renderable = miniRenderable
                        }
                    }

                }
            }
        }
    }
}
