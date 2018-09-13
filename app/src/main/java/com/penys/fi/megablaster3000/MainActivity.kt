package com.penys.fi.megablaster3000

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.core.Pose
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.CompletableFuture


class MainActivity : AppCompatActivity() {

    lateinit var testRenderable: ModelRenderable
    lateinit var fragment: ArFragment
    lateinit var renderableFuture: CompletableFuture<ModelRenderable>
    lateinit var modelUri: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as
                ArFragment

        modelUri = Uri.parse("ducky.sfb")

        renderableFuture = ModelRenderable.builder()
                .setSource(this, modelUri).build()

        renderableFuture.thenAccept { it -> testRenderable = it }



        fragment.arSceneView.scene.addOnUpdateListener {
            if(fragment.arSceneView.arFrame.camera.trackingState == TrackingState.TRACKING) {
                addObject()
                
            } else {

            }
        }
    }

    val pose = Pose.makeTranslation(0.0f, 0.0f, -1.3f)

    private fun addObject() {
        val frame =  fragment.arSceneView.arFrame
        val pt = getScreenCenter()
        val hits: List<HitResult>
        if(frame != null && testRenderable != null) {
            hits = frame.hitTest(0.2f, 0.3f)
            for(hit in hits) {
                val trackable = hit.trackable
                if (trackable is Plane) {
                    val anchor = hit!!.createAnchor()
                    val anchorNode = AnchorNode(anchor)
                    anchorNode.setParent(fragment.arSceneView.scene)
                    val mNode = TransformableNode(fragment.transformationSystem)
                    mNode.setParent(anchorNode)
                    mNode.renderable = testRenderable
                    mNode.select()
                    break
                }
            }
        }
    }

    private fun getScreenCenter(): android.graphics.Point {
        val vw = findViewById<View>(android.R.id.content)
        return android.graphics.Point(vw.width/2, vw.height/ 2)
    }
}

