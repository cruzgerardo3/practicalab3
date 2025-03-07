package com.example.practica3

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var buttonSelectImage: Button
    private lateinit var buttonRotate: Button
    private lateinit var buttonZoom: Button
    private lateinit var buttonMirror: Button
    private lateinit var buttonGrayscale: Button
    private lateinit var buttonCrop: Button
    private lateinit var buttonSave: Button

    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setInitComponents()


        setActionEvents()
    }

    private fun setInitComponents() {
        imageView = findViewById(R.id.imageView)
        buttonSelectImage = findViewById(R.id.buttonSelectImage)
        buttonRotate = findViewById(R.id.buttonRotate)
        buttonZoom = findViewById(R.id.buttonZoom)
        buttonMirror = findViewById(R.id.buttonMirror)
        buttonGrayscale = findViewById(R.id.buttonGrayscale)
        buttonCrop = findViewById(R.id.buttonCrop)
        buttonSave = findViewById(R.id.buttonSave)
    }

    private fun setActionEvents() {

        buttonSelectImage.setOnClickListener {
            openGallery()
        }


        buttonRotate.setOnClickListener {
            imageView.rotation = (imageView.rotation + 90f) % 360
        }


        buttonZoom.setOnClickListener {
            imageView.scaleX *= 1.5f
            imageView.scaleY *= 1.5f
        }


        buttonMirror.setOnClickListener {
            imageView.scaleX = -imageView.scaleX
        }


        buttonGrayscale.setOnClickListener {
            applyGrayscale()
        }


        buttonCrop.setOnClickListener {
            cropImage()
        }


        buttonSave.setOnClickListener {
            saveImage()
        }
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            selectedImageUri?.let {
                imageView.setImageURI(it)
            }
        }
    }


    private fun applyGrayscale() {
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        imageView.colorFilter = ColorMatrixColorFilter(matrix)
    }


    private fun cropImage() {
        val drawable = imageView.drawable as? BitmapDrawable
        drawable?.let {
            val bitmap = it.bitmap
            val croppedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width / 2, bitmap.height / 2)
            imageView.setImageBitmap(croppedBitmap)
        }
    }


    private fun saveImage() {
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = imageView.drawingCache
        try {
            val fileName = "imagen_guardada_${System.currentTimeMillis()}.png"
            openFileOutput(fileName, MODE_PRIVATE).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            }
            println("Imagen guardada como $fileName")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        imageView.isDrawingCacheEnabled = false
    }
}

