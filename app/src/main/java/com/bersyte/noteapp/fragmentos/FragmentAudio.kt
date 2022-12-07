package com.bersyte.noteapp.fragmentos

import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bersyte.noteapp.databinding.FragmentAudioBinding
import com.bersyte.noteapp.db.NoteDatabase
import com.bersyte.noteapp.model.Multimedia
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


class FragmentAudio : Fragment(){

    private var mStartRecording: Boolean = true
    private var fileName: String = ""
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private lateinit var miContexto: Context
    var audioGuardado = false
    private var mStartPlaying = true

   // llamado una vez que el fragmento está asociado con su actividad.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        miContexto = context
    }
    //crea la jerarquia de vistas asociadas al fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAudioBinding.inflate(layoutInflater)

        binding.btnStart.setOnClickListener {   //boton de incio de grabacion
            Toast.makeText(context, "Grabando", Toast.LENGTH_LONG).show()
            revisarPermisos()
            audioGuardado=true
        }

        binding.btnStop.setOnClickListener { //boton de fin de grabacion
            if (audioGuardado==false){
                Toast.makeText(context, "Primero debes grabar un audio", Toast.LENGTH_LONG).show()
            }else{
                stopRecording() //detiene la grabacion
                Toast.makeText(context, "Grabacion Detenida", Toast.LENGTH_SHORT).show()
            }

        }
        binding.btnplay.setOnClickListener {//boton para reproducir grabacion
            if (audioGuardado==false){
                Toast.makeText(context, "Primero debes grabar un audio", Toast.LENGTH_LONG).show()
            }else{
                onPlay(mStartPlaying) //reproduce el audio
                mStartPlaying = !mStartPlaying
            }
        }

       binding.btnGuardar.setOnClickListener {   //boton que guarda el audio
           if (audioGuardado == false) {
               Toast.makeText(context, "Primero debes grabar un audio", Toast.LENGTH_LONG).show()
           } else {


               val file = Multimedia(
                   arguments?.getString("id")!!.toInt(),
                   "audio",
                   fileName,
                   binding.txtDescripcionNota.text.toString()
               )
               //Insert
               NoteDatabase.getInstance(requireActivity().applicationContext).MultimediaDao()
                   .insert(file)

               binding.btnStart.visibility = View.INVISIBLE
               binding.btnStop.visibility = View.INVISIBLE
               binding.btnGuardar.visibility = View.INVISIBLE
               binding.txtDescripcionNota.isEnabled = false
               Toast.makeText(context, "Se guardo con exito el audio", Toast.LENGTH_SHORT).show()
           }
       }
        return binding.root
    }

    private fun onPlay(start: Boolean) = if (start) {
        startPlaying()
    } else {
        stopPlaying()
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
                //Log.e(LOG_TAG, "prepare() failed")
            }
        }
    }



    private fun onRecord(start: Boolean) = if (start) {
        iniciarGrabacion()
    } else {
        stopRecording()
    }

    private fun revisarPermisos() {
        when {
            ContextCompat.checkSelfPermission(
                miContexto,
                "android.permission.RECORD_AUDIO"
            ) == PackageManager.PERMISSION_GRANTED -> {
                onRecord(mStartRecording)
                mStartRecording = !mStartRecording
            }
            shouldShowRequestPermissionRationale("android.permission.RECORD_AUDIO") -> {
                MaterialAlertDialogBuilder( miContexto
                )
                    .setTitle("Title")
                    .setMessage("Debes dar perimso para grabar audios")
                    .setNegativeButton("Cancel") { _, _ ->
                    }
                    .setPositiveButton("OK") { _, _ ->
                        requestPermissions(
                            arrayOf("android.permission.RECORD_AUDIO",
                                "android.permission.WRITE_EXTERNAL_STORAGE"),
                            1001)
                    }
                    .show()
            }
            else -> {
                requestPermissions(
                    arrayOf("android.permission.RECORD_AUDIO",
                        "android.permission.WRITE_EXTERNAL_STORAGE"),
                    1001)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray

    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1001 -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[1] == PackageManager.PERMISSION_GRANTED
                            )) {
                    onRecord(mStartRecording)
                    mStartRecording = !mStartRecording
                } else {

                }
                return
            }
            else -> {
            }
        }
    }

    private fun iniciarGrabacion() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            createAudioFile()
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            try {
                prepare()
            } catch (e: IOException) {
                //Log.e(LOG_TAG, "prepare() failed")
            }
            start()
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    @Throws(IOException::class)
    fun createAudioFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(null)
        return File.createTempFile(
            "AUDIO_${timeStamp}_",
            ".mp3",
            storageDir
        ).apply {
            fileName = absolutePath
        }
    }
}