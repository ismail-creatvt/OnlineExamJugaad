package com.ismail.creatvt.onlineexamjugaad

import android.content.Context
import android.graphics.Rect
import android.hardware.Camera
import android.hardware.Camera.AutoFocusCallback
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException


class CameraPreview(
    context: Context,
    private val mCamera: Camera
) : SurfaceView(context), SurfaceHolder.Callback {


    private val myAutoFocusCallback =
        AutoFocusCallback { arg0, arg1 ->
            if (arg0) {
                mCamera.cancelAutoFocus()
            }
        }

    private val mHolder: SurfaceHolder = holder.apply {
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        addCallback(this@CameraPreview)
        // deprecated setting, but required on Android versions prior to 3.0
        setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        mCamera.apply {
            try {
                setPreviewDisplay(holder)
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                setDisplayOrientation(90);
                startPreview()
            } catch (e: IOException) {
                Log.d("CameraPreview", "Error setting camera preview: ${e.message}")
            }
        }
    }


    /**
     * Called from PreviewSurfaceView to set touch focus.
     *
     * @param - Rect - new area for auto focus
     */
    fun doTouchFocus(tfocusRect: Rect?) {
        try {
            val focusList: MutableList<Camera.Area> = ArrayList()
            val focusArea = Camera.Area(tfocusRect, 1000)
            focusList.add(focusArea)
            val param = mCamera.parameters
            param.focusAreas = focusList
            param.meteringAreas = focusList
            mCamera.parameters = param
            mCamera.autoFocus(myAutoFocusCallback)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
//        onFocusListener.onFocused()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.x
            val y = event.y
            val touchRect = Rect(
                (x - 100).toInt(),
                (y - 100).toInt(),
                (x + 100).toInt(),
                (y + 100).toInt()
            )
            val targetFocusRect = Rect(
                touchRect.left * 2000 / this.width - 1000,
                touchRect.top * 2000 / this.height - 1000,
                touchRect.right * 2000 / this.width - 1000,
                touchRect.bottom * 2000 / this.height - 1000
            )
            doTouchFocus(targetFocusRect)
        }
        return false
    }


    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mCamera.stopPreview();
        holder.removeCallback(this);
        mCamera.release();
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        if (mHolder.surface == null) {
            // preview surface does not exist
            return
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview()
        } catch (e: Exception) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        mCamera.apply {
            try {
                setPreviewDisplay(mHolder)
                startPreview()
            } catch (e: Exception) {
                Log.d("CameraPreview", "Error starting camera preview: ${e.message}")
            }
        }
    }
}