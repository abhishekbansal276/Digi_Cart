package com.example.project_one

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

class PaymentActivity : AppCompatActivity() {
    var surfaceView: SurfaceView? = null
    var txtBarcodeValue: TextView? = null
    private var barcodeDetector: BarcodeDetector? = null
    private var cameraSource: CameraSource? = null
    private val REQUEST_CAMERA_PERMISSION = 201
    var btnAction: Button? = null
    var intentData = ""
    var isEmail = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

//        initViews();

    }

//    private fun initViews() {
//        txtBarcodeValue = findViewById(R.id.txtBarcodeValue)
//        surfaceView = findViewById(R.id.surfaceView)
//        btnAction = findViewById(R.id.btnAction)
//        btnAction!!.setOnClickListener(View.OnClickListener {
//            if (intentData.isNotEmpty()) {
//                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(intentData)))
//            }
//        })
//    }
//
//    private fun initialiseDetectorsAndSources() {
//        Toast.makeText(applicationContext, "Pay now", Toast.LENGTH_SHORT).show()
//        barcodeDetector = BarcodeDetector.Builder(this)
//            .setBarcodeFormats(Barcode.ALL_FORMATS)
//            .build()
//        cameraSource = CameraSource.Builder(this, barcodeDetector)
//            .setRequestedPreviewSize(1920, 1080)
//            .setAutoFocusEnabled(true) //you should add this feature
//            .build()
//        surfaceView!!.holder.addCallback(object : SurfaceHolder.Callback {
//            override fun surfaceCreated(holder: SurfaceHolder) {
//                try {
//                    if (ActivityCompat.checkSelfPermission(
//                            this@PaymentActivity,
//                            android.Manifest.permission.CAMERA
//                        ) == PackageManager.PERMISSION_GRANTED
//                    ) {
//                        cameraSource!!.start(surfaceView!!.holder)
//                    } else {
//                        ActivityCompat.requestPermissions(
//                            this@PaymentActivity,
//                            arrayOf(android.Manifest.permission.CAMERA),
//                            REQUEST_CAMERA_PERMISSION
//                        )
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//
//            override fun surfaceChanged(
//                holder: SurfaceHolder,
//                format: Int,
//                width: Int,
//                height: Int
//            ) {
//            }
//
//            override fun surfaceDestroyed(holder: SurfaceHolder) {
//                cameraSource!!.stop()
//            }
//        })
//        barcodeDetector!!.setProcessor(object : Detector.Processor<Barcode> {
//            override fun release() {
//                Toast.makeText(
//                    applicationContext,
//                    "To prevent memory leaks payment gateway has been stopped",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            override fun receiveDetections(detections: Detections<Barcode>) {
//                val barcodes = detections.detectedItems
//                if (barcodes.size() != 0) {
//                    txtBarcodeValue!!.post {
//                        isEmail = false
//                        btnAction!!.text = "PAY NOW"
//                        intentData = barcodes.valueAt(0).displayValue
//                        txtBarcodeValue!!.text = intentData
//                    }
//                }
//            }
//        })
//    }
//
//    override fun onPause() {
//        super.onPause()
//        cameraSource!!.release()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        initialiseDetectorsAndSources()
//    }

}