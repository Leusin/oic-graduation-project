package com.project.oic_android.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.project.oic_android.R
import com.project.oic_android.databinding.FragmentArBinding
import com.project.oic_android.databinding.FragmentSearchBinding
import kotlinx.android.synthetic.main.fragment_ar.*
import org.opencv.android.*
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2
import org.opencv.core.*
import org.opencv.core.Core.FONT_HERSHEY_SIMPLEX
import org.opencv.dnn.Dnn
import org.opencv.dnn.Net
import org.opencv.imgproc.Imgproc
import org.opencv.utils.Converters
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.Executors

class ArFragment : Fragment(), CvCameraViewListener2 {

    var cameraBridgeViewBase: CameraBridgeViewBase? = null
    var baseLoaderCallback: BaseLoaderCallback? = null
    var startYolo = false
    var firstTimeYolo = false
    var tinyYolo: Net? = null

    private var _binding: FragmentArBinding? = null
    private val binding get() = _binding!!

    private lateinit var safeContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentArBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraBridgeViewBase = binding.CameraView as JavaCameraView
        cameraBridgeViewBase!!.visibility = SurfaceView.VISIBLE
        cameraBridgeViewBase!!.setCvCameraViewListener(this)


        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        baseLoaderCallback = object : BaseLoaderCallback(safeContext) {
            override fun onManagerConnected(status: Int) {
                super.onManagerConnected(status)
                when (status) {
                    SUCCESS -> cameraBridgeViewBase!!.enableView()
                    else -> super.onManagerConnected(status)
                }
            }
        }
        setListener()
    }

    private fun setListener(){ binding.button.setOnClickListener { YOLO() } }

    fun YOLO() {
        if (startYolo == false) {
            startYolo = true
            if (firstTimeYolo == false) {
                firstTimeYolo = true
                val tinyYoloCfg = getPath("yolov3-tiny.cfg", safeContext) //핸드폰내 외부 저장소 경로
                val tinyYoloWeights = getPath("yolov3-tiny.weights", safeContext)
                tinyYolo = Dnn.readNetFromDarknet(tinyYoloCfg, tinyYoloWeights)
            }
        } else {
            startYolo = false
        }
    }
/*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_ar)
        Log.d("err", "ERror : oncreate")
        cameraBridgeViewBase = findViewById<View>(R.id.CameraView) as JavaCameraView
        cameraBridgeViewBase!!.visibility = SurfaceView.VISIBLE
        cameraBridgeViewBase!!.setCvCameraViewListener(this)


        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        baseLoaderCallback = object : BaseLoaderCallback(this) {
            override fun onManagerConnected(status: Int) {
                super.onManagerConnected(status)
                when (status) {
                    SUCCESS -> cameraBridgeViewBase!!.enableView()
                    else -> super.onManagerConnected(status)
                }
            }
        }
    }
*/
    override fun onCameraViewStarted(width: Int, height: Int) {
        //카메라 뷰 시작될때
        if (true.also { startYolo = it }) {
            val tinyYoloCfg = getPath("yolov3-tiny.cfg", safeContext) //핸드폰내 외부 저장소 경로
            val tinyYoloWeights = getPath("yolov3-tiny.weights", safeContext)
            tinyYolo = Dnn.readNetFromDarknet(tinyYoloCfg, tinyYoloWeights)
        }
    }

    override fun onCameraViewStopped() {}
    override fun onCameraFrame(inputFrame: CvCameraViewFrame): Mat {
        //가장 중요한 함수, 여기서 캡쳐하거나 다른 이미지를 삽입하거나 rgb 바꾸거나 등등 수행(여러 트리거를 줄 수 있음)
        //Mat을 활용하여 이미지를 파이썬의 매트릭스 배열처럼 저장할 수 있다
        val frame = inputFrame.rgba() //프레임 받기
        if (startYolo == true) {
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
                        FONT_HERSHEY_SIMPLEX,
                        2.0,
                        Scalar(255.0, 255.0, 0.0),
                        2
                    )
                    Imgproc.rectangle(frame, box.tl(), box.br(), Scalar(255.0, 0.0, 0.0), 2)
                }
            }
        }
        return frame //프레임 리턴
    }

    override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            Toast.makeText(context, "There's a problem, yo!", Toast.LENGTH_SHORT).show()
        } else {
            baseLoaderCallback!!.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    override fun onPause() {
        //카메라뷰 중지
        super.onPause()
        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase!!.disableView()
        }
    }

    override fun onDestroy() {
        //카메라뷰 종료
        super.onDestroy()
        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase!!.disableView()
        }
    }

    companion object {
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
}