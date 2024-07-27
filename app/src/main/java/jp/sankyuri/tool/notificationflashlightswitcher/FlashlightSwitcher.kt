package jp.sankyuri.tool.notificationflashlightswitcher
import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager

import android.os.Build
import android.widget.Toast
import java.lang.Exception


class FlashlightSwitcher {

    companion object {

        var IsTorchOn: Boolean = false


        private var cm_cam: android.hardware.Camera? = null








        fun turn( context: Context, flag: Boolean ) {
            if (IsTorchOn == flag) {
                return
            }
            if (Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
                val localCameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                val camId = localCameraManager.cameraIdList[0]
                if (flag != IsTorchOn) {
                    IsTorchOn = ! IsTorchOn
                    try {
                        localCameraManager.setTorchMode( camId, IsTorchOn )
                    }
                    catch ( e: CameraAccessException ) {
                        Toast.makeText( context, e.message, Toast.LENGTH_SHORT ).show()
                    }
                }
            }
            else {
                throw Exception( "If API version is >=23 than you can't use it!" )
            }

        }




        fun turnOn() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (cm_cam == null) {
                    cm_cam = android.hardware.Camera.open()
                }

                val params = cm_cam!!.parameters
                params.flashMode = android.hardware.Camera.Parameters.FLASH_MODE_TORCH
                cm_cam!!.parameters = params
                cm_cam!!.startPreview()
            }
            else {
                throw Exception( "If API version is <23 than you can't use it!" )
            }
        }




        fun turnOff() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (cm_cam == null) {
                    cm_cam = android.hardware.Camera.open()
                }

                val params = cm_cam!!.parameters
                params.flashMode = android.hardware.Camera.Parameters.FLASH_MODE_OFF
                cm_cam!!.parameters = params
                cm_cam!!.stopPreview()
            }
            else {
                throw Exception( "If API version is <23 than you can't use it!" )
            }
        }


    } // companion object


}



