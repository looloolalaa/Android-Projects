package com.example.engquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_voc.*
import kotlinx.android.synthetic.main.activity_intro.*
import kotlinx.android.synthetic.main.activity_intro.addBtn
import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream

class AddVocActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_voc)

        init()
    }



    private fun init() {

        addBtn.setOnClickListener {

            val word = wordText.text.toString()
            val mean = meanText.text.toString()
            writeFile(word,mean)

            val i = Intent()
            i.putExtra("voc",MyData(word,mean))
            setResult(Activity.RESULT_OK,i)
            finish()
        }

        cancelBtn.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun writeFile(word: String, mean: String) {
        val output = PrintStream(openFileOutput("added_words.txt", Context.MODE_APPEND))
        output.println(word)
        output.println(mean)
        output.close()

    }
}
