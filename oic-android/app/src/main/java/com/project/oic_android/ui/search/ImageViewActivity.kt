package com.project.oic_android.ui.search

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64.NO_WRAP
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.project.oic_android.R
import com.project.oic_android.WordDetailActivity
import com.project.oic_android.databinding.ActivityImageViewBinding
import com.project.oic_android.ui.ArFragment
import okhttp3.MediaType
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.dnn.Dnn
import org.opencv.dnn.Net
import org.opencv.imgproc.Imgproc
import org.opencv.utils.Converters
import java.io.*
import java.util.*

class ImageViewActivity : AppCompatActivity() {

    //var tinyYolo: Net? = null

    companion object { private const val TAG = "ImageViewActivity" }

    private lateinit var binding: ActivityImageViewBinding
    private var currentImageURL: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListener()
        getImageURL()
    }

    private fun setListener() {
        binding.backIcon.setOnClickListener { finish() }
        binding.imageSearchButton.setOnClickListener{ uploadImgToServer() }
//        binding.imageSearchButton.setOnClickListener{ DetectObject() }
    }

    private fun getImageURL() {
        currentImageURL = intent.getParcelableExtra("uri")
        binding.imageViewPreview.setImageURI(currentImageURL)
    }

    private fun uploadImgToServer() {
        val intent = Intent(this, WordDetailActivity::class.java)
        intent.putExtra("data2", "chair")
        intent.putExtra("dataKr2", "??????")
        startActivity(intent)
    }
/*
    private fun DetectObject() {
        currentImageURL
        val tinyYoloCfg = getPath("yolov3-tiny.cfg", this) //???????????? ?????? ????????? ??????
        val tinyYoloWeights = getPath("yolov3-tiny.weights", this)
        tinyYolo = Dnn.readNetFromDarknet(tinyYoloCfg, tinyYoloWeights)

        val intent = Intent(this, WordDetailActivity::class.java)
    //intent.putExtra("data", imageWord)
    //intent.putExtra("dataKr", imageWordKr)
    startActivity(intent)
    } // ????????? ?????? ?????????

    private fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
        //?????? ????????? ??????, ????????? ??????????????? ?????? ???????????? ??????????????? rgb ???????????? ?????? ??????(?????? ???????????? ??? ??? ??????)
        //Mat??? ???????????? ???????????? ???????????? ???????????? ???????????? ????????? ??? ??????
        val frame = inputFrame.rgba() //????????? ??????
        val frame2 = getPath(currentImageURL.toString(), this)
        val img: Bitmap
        Utils.bitmapToMat(img, currentImageURL)
        //Imgproc??? ????????? ????????? ??????????????? ??????.
//        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB) //rgba ????????? rgb??? ??????
        Imgproc.cvtColor(frame2, frame2, Imgproc.COLOR_RGBA2RGB) //rgba ????????? rgb??? ??????
        //Imgproc.Canny(frame, frame, 100, 200);
        //Mat gray=Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY)
        val imageBlob = Dnn.blobFromImage(
            frame,
            0.00392,
            Size(416.0, 416.0),
            Scalar(0.0, 0.0, 0.0),  /*swapRB*/
            false,  /*crop*/
            false
        )
        //?????? ??????????????? ????????? ??????
        tinyYolo!!.setInput(imageBlob)

        //cfg ???????????? yolo layer number??? ???????????? ?????? ???????????? ????????????.
        //yolv3-tiny??? yolo layer??? 2????????? initialCapacity??? 2??? ??????.
        val result: List<Mat> = ArrayList(2)
        val outBlobNames: MutableList<String> = ArrayList()
        outBlobNames.add(0, "yolo_16")
        outBlobNames.add(1, "yolo_23")

        //???????????? ??????
        tinyYolo!!.forward(result, outBlobNames)

        //30%????????? ????????? ???????????????.
        val confThreshold = 0.3f

        //class id
        val clsIds: MutableList<Int> = ArrayList()
        //
        val confs: MutableList<Float> = ArrayList()
        //draw rectanglelist
        val rects: MutableList<Rect2d> = ArrayList()
        for (i in result.indices) {
            val level = result[i]
            for (j in 0 until level.rows()) { //iterate row
                val row = level.row(j)
                val scores = row.colRange(5, level.cols())
                val mm = Core.minMaxLoc(scores)
                val confidence = mm.maxVal.toFloat()

                //???????????? ???????????? ?????? ?????? ???????????? ??????(?????????) ????????? ???????????? ????????????.
                val classIdPoint = mm.maxLoc
                if (confidence > confThreshold) {
                    val centerX = (row[0, 0][0] * frame.cols()).toInt()
                    val centerY = (row[0, 1][0] * frame.rows()).toInt()
                    val width = (row[0, 2][0] * frame.cols()).toInt()
                    val height = (row[0, 3][0] * frame.rows()).toInt()
                    val left = centerX - width / 2
                    val top = centerY - height / 2
                    clsIds.add(classIdPoint.x.toInt())
                    confs.add(confidence)
                    rects.add(Rect2d(left.toDouble(), top.toDouble(), width.toDouble(), height.toDouble()))
                }
            }
        }
        val ArrayLength = confs.size
        if (ArrayLength >= 1) {
            // Apply non-maximum suppression procedure.
            val nmsThresh = 0.2f
            val confidences = MatOfFloat(Converters.vector_float_to_Mat(confs))
            val boxesArray: Array<Rect2d> = rects.toTypedArray<Rect2d>()
            val boxes = MatOfRect2d(*boxesArray)
            val indices = MatOfInt()
            Dnn.NMSBoxes(boxes, confidences, confThreshold, nmsThresh, indices)

            // Draw result boxes:
            val ind = indices.toArray()
            for (i in ind.indices) {
                val idx = ind[i]
                val box = boxesArray[idx]
                val idGuy = clsIds[idx]
                val conf = confs[idx]
                val cocoNames = Arrays.asList(
                    "a person", "a bicycle", "a motorbike", "an airplane", "a bus", "a train",
                    "a truck", "a boat", "a traffic light", "a fire hydrant", "a stop sign",
                    "a parking meter", "a car", "a bench", "a bird", "a cat", "a dog", "a horse", "a sheep",
                    "a cow", "an elephant", "a bear", "a zebra", "a giraffe", "a backpack", "an umbrella",
                    "a handbag", "a tie", "a suitcase", "a frisbee", "skis", "a snowboard",
                    "a sports ball", "a kite", "a baseball bat", "a baseball glove", "a skateboard",
                    "a surfboard", "a tennis racket", "a bottle", "a wine glass", "a cup",
                    "a fork", "a knife", "a spoon", "a bowl", "a banana", "an apple", "a sandwich",
                    "an orange", "broccoli", "a carrot", "a hot dog", "a pizza", "a doughnut",
                    "a cake", "a chair", "a sofa", "a potted plant", "a bed", "a dining table",
                    "a toilet", "a TV monitor", "a laptop", "a computer mouse", "a remote control",
                    "a keyboard", "a cell phone", "a microwave", "an oven", "a toaster",
                    "a sink", "a refrigerator", "a book", "a clock", "a vase", "a pair of scissors",
                    "a teddy bear", "a hair drier", "a toothbrush"
                )
                //
                // List<String> cocoNames = Arrays.asList("sign");
                val intConf = (conf * 100).toInt()
                Imgproc.putText(
                    frame,
                    cocoNames[idGuy] + " " + intConf + "%",
                    box.tl(),
                    Core.FONT_HERSHEY_SIMPLEX,
                    2.0,
                    Scalar(255.0, 255.0, 0.0),
                    2
                )
                Imgproc.rectangle(frame, box.tl(), box.br(), Scalar(255.0, 0.0, 0.0), 2)
            }
        }
        return frame //????????? ??????
    }

    companion object {
        private const val TAG = "ImageViewActivity"
        private fun getPath(file: String, context: Context): String {
            val assetManager = context.assets
            var inputStream: BufferedInputStream? = null
            try {
                inputStream = BufferedInputStream(assetManager.open(file))
                val data = ByteArray(inputStream.available())
                inputStream.read(data)
                inputStream.close()
                val outFile = File(context.filesDir, file)
                val os = FileOutputStream(outFile)
                os.write(data)
                os.close()
                return outFile.absolutePath
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return ""
        }
    }
*/
}
