package com.example.docscanner.core.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import com.example.docscanner.feature_createpdf.domain.Pdf
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import java.io.File
import java.io.FileOutputStream

class FileManager(
    private val activity: ComponentActivity
) {

    fun copyPdfToInternalDir( resultPdf : GmsDocumentScanningResult?, title: String): String{
        resultPdf?.pdf?.let {pdf->
            val directoryName = Constants.INTERNAL_DIR

            val storageDir = activity.applicationContext.getDir(directoryName, Context.MODE_PRIVATE)

            if(!storageDir.exists()){
                storageDir.mkdir()
            }

            var nameofPdf  = title.replace(" " , "")

            val doesFileAlreadyExist = searchForFile("$nameofPdf.pdf", directoryName)

            if(doesFileAlreadyExist){
                nameofPdf = "$nameofPdf-${System.currentTimeMillis()}"
            }

            val newFile = File(storageDir, "$nameofPdf.pdf")
            val fos = FileOutputStream(newFile)

            activity.applicationContext.contentResolver.openInputStream(pdf.uri)?.use{inputStream->
                inputStream.copyTo(fos)
            }
            return newFile.absolutePath
        }
        return ""
    }

    fun copyImageToInternalDir(resultPdf : GmsDocumentScanningResult?, title: String): String{
        resultPdf?.pages?.get(0)?.let {page->
            val directoryName = Constants.INTERNAL_DIR
            val storageDir = activity.applicationContext.getDir(directoryName, Context.MODE_PRIVATE)

            if(!storageDir.exists()){
                storageDir.mkdir()
            }

            var nameofImage = title.replace(" " , "")

            val doesFileAlreadyExist = searchForFile("$nameofImage.jpg", directoryName)

            if(doesFileAlreadyExist){
                nameofImage = "$nameofImage-${System.currentTimeMillis()}"
            }

            val newFile = File(storageDir, "$nameofImage.jpg")
            val fos = FileOutputStream(newFile)

            activity.applicationContext.contentResolver.openInputStream(page.imageUri)?.use{inputStream->
                inputStream.copyTo(fos)
            }
            return newFile.absolutePath
        }
        return ""
    }

    fun copyToExternalDir(pdf: Pdf) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            saveFileUsingMediaStore(activity.applicationContext, pdf)
        }
        else{
            saveFileUsingFileProvider(activity.applicationContext, pdf)
        }
    }

    fun deleteFileFromDirectory(directory: String, fileNameToDelete: String, isCache: Boolean){

        val fileName = fileNameToDelete.replace(" " , "")
        if(isCache){
            val cacheDir = File(activity.applicationContext.cacheDir.path + "/mlkit_docscan_ui_client")
            cacheDir.deleteRecursively()
        }else {
            val appPdfDir = activity.applicationContext.getDir(directory, Context.MODE_PRIVATE)
            val files = appPdfDir.listFiles()

            if (files != null) {
                for (file in files) {
                    // Check for files you want to delete (based on filename, extension, or other criteria)
                    if (file.name == fileName ) {
                        file.delete()
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveFileUsingMediaStore(context: Context, pdf: Pdf){
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "${pdf.titleName}.pdf")
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + File.separator + "Doc Scanner")
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        try {
            uri?.let { newUri ->
                // Open InputStream from the source URI
                resolver.openInputStream(Uri.fromFile(File(pdf.uri)))?.use { inputStream ->
                    // Open OutputStream to the new URI
                    resolver.openOutputStream(newUri)?.use { outputStream ->
                        // Copy the contents from InputStream to OutputStream
                        inputStream.copyTo(outputStream)
                    }
                }
            }
        }catch (e: Exception){
            Log.e("FileManager", "while file saving using media-store ${e.message.toString()}")
            e.printStackTrace()
        }
    }

    private fun saveFileUsingFileProvider(context: Context, pdf: Pdf){
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val outputFile = File(downloadsDir, "${pdf.titleName}.pdf")

        try {
            context.contentResolver.openInputStream(Uri.fromFile(File(pdf.uri)))?.use { inputStream ->
                FileOutputStream(outputFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }catch (e : Exception){
            Log.e("FileManager" , "while file saving using file provider ${e.message.toString()}")
            e.printStackTrace()
        }
    }

    fun savePdfTemporarily(pdf: Pdf): File{
        val pdfFile = File(activity.cacheDir,"${pdf.titleName}.pdf")
        val fos = FileOutputStream(pdfFile)

        activity.contentResolver.openInputStream(Uri.fromFile(File(pdf.uri)))?.use {ois->
            ois.copyTo(fos)
        }

        return pdfFile
    }

    private fun searchForFile(fileName: String, directory: String): Boolean{
        val appPdfDir = activity.applicationContext.getDir(directory, Context.MODE_PRIVATE)
        val files = appPdfDir.listFiles()

        Log.e("FileManager", "searchForFile: ${files?.size}")

        files?.forEach { file->
            if(file.name == fileName){
                Log.e("FileManager", "searchForFile: $fileName file found")
                return true
            }
        }
        Log.e("FileManager", "searchForFile: $fileName file not found")
        return false
    }
}
