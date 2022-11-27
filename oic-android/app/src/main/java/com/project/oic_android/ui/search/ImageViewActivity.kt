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
import org.opencv.core.*
import org.opencv.dnn.Dnn
import org.opencv.dnn.Net
import org.opencv.imgproc.Imgproc
import org.opencv.utils.Converters
import java.io.*
import java.util.*

class ImageViewActivity : AppCompatActivity() {

//    var tinyYolo: Net? = null

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
//        binding.imageSearchButton.setOnClickListener{ uploadImgToServer() }
        binding.imageSearchButton.setOnClickListener{ uploadImgToServer() }
    }

    private fun getImageURL() {
        currentImageURL = intent.getParcelableExtra("uri")
        binding.imageViewPreview.setImageURI(currentImageURL)
    }

    private fun uploadImgToServer() {
        val intent = Intent(this, WordDetailActivity::class.java)
        startActivity(intent)
    }
//    private fun uploadImgToServer() { } // 서버로 사진 업로드
/*    private fun DetectObject() {
        currentImageURL
        val tinyYoloCfg = getPath("yolov3-tiny.cfg", this) //핸드폰내 외부 저장소 경로
        val tinyYoloWeights = getPath("yolov3-tiny.weights", this)
        tinyYolo = Dnn.readNetFromDarknet(tinyYoloCfg, tinyYoloWeights)

        val intent = Intent(this, WordDetailActivity::class.java)
    //intent.putExtra("data", imageWord)
    //intent.putExtra("dataKr", imageWordKr)
    startActivity(intent)
    } // 서버로 사진 업로드

    private fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
        //가장 중요한 함수, 여기서 캡쳐하거나 다른 이미지를 삽입하거나 rgb 바꾸거나 등등 수행(여러 트리거를 줄 수 있음)
        //Mat을 활용하여 이미지를 파이썬의 매트릭스 배열처럼 저장할 수 있다
        val frame = inputFrame.rgba() //프레임 받기
        val frame2 = getPath(currentImageURL.toString(), this)
        //Imgproc을 이용해 이미지 프로세싱을 한다.
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB) //rgba 체계를 rgb로 변경
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
        //뉴런 네트워크에 이미지 넣기
        tinyYolo!!.setInput(imageBlob)

        //cfg 파일에서 yolo layer number을 확인하여 이를 순전파에 넣어준다.
        //yolv3-tiny는 yolo layer가 2개라서 initialCapacity를 2로 준다.
        val result: List<Mat> = ArrayList(2)
        val outBlobNames: MutableList<String> = ArrayList()
        outBlobNames.add(0, "yolo_16")
        outBlobNames.add(1, "yolo_23")

        //순전파를 진행
        tinyYolo!!.forward(result, outBlobNames)

        //30%이상의 확률만 출력해준다.
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

                //여러개의 클래스들 중에 가장 정확도가 높은(유사한) 클래스 아이디를 찾아낸다.
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
                    "a person",
                    "a bicycle",
                    "a motorbike",
                    "an airplane",
                    "a bus",
                    "a train",
                    "a truck",
                    "a boat",
                    "a traffic light",
                    "a fire hydrant",
                    "a stop sign",
                    "a parking meter",
                    "a car",
                    "a bench",
                    "a bird",
                    "a cat",
                    "a dog",
                    "a horse",
                    "a sheep",
                    "a cow",
                    "an elephant",
                    "a bear",
                    "a zebra",
                    "a giraffe",
                    "a backpack",
                    "an umbrella",
                    "a handbag",
                    "a tie",
                    "a suitcase",
                    "a frisbee",
                    "skis",
                    "a snowboard",
                    "a sports ball",
                    "a kite",
                    "a baseball bat",
                    "a baseball glove",
                    "a skateboard",
                    "a surfboard",
                    "a tennis racket",
                    "a bottle",
                    "a wine glass",
                    "a cup",
                    "a fork",
                    "a knife",
                    "a spoon",
                    "a bowl",
                    "a banana",
                    "an apple",
                    "a sandwich",
                    "an orange",
                    "broccoli",
                    "a carrot",
                    "a hot dog",
                    "a pizza",
                    "a doughnut",
                    "a cake",
                    "a chair",
                    "a sofa",
                    "a potted plant",
                    "a bed",
                    "a dining table",
                    "a toilet",
                    "a TV monitor",
                    "a laptop",
                    "a computer mouse",
                    "a remote control",
                    "a keyboard",
                    "a cell phone",
                    "a microwave",
                    "an oven",
                    "a toaster",
                    "a sink",
                    "a refrigerator",
                    "a book",
                    "a clock",
                    "a vase",
                    "a pair of scissors",
                    "a teddy bear",
                    "a hair drier",
                    "a toothbrush"
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
        return frame //프레임 리턴
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
