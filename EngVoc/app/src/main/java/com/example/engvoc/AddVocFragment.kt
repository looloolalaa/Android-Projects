package com.example.engvoc

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_add_voc.*
import kotlinx.android.synthetic.main.fragment_add_voc.view.*
import java.io.PrintStream

class AddVocFragment : Fragment() {

    var listener:OnAddVocFragmentInteractionListener?=null
    interface OnAddVocFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onAddVocFragmentInteraction()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_add_voc, container, false)
        init(view)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }

    private fun init(view: View) {

        view.addBtn.setOnClickListener {

            if(wordText.text.isEmpty()||meanText.text.isEmpty()){

            }else {
                val word = wordText.text.toString()
                val mean = meanText.text.toString()
                wordText.text = null
                meanText.text = null
                Toast.makeText(this.context, word+" 가 단어장에 추가되었습니다!", Toast.LENGTH_SHORT).show()
                writeFile(word, mean)

                listener?.onAddVocFragmentInteraction()
            }
        }

        view.cancelBtn.setOnClickListener {
            wordText.text = null
            meanText.text = null
            Toast.makeText(this.context, "다시 입력해주세요!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun writeFile(word: String, mean: String) {
        val output = PrintStream(activity?.openFileOutput("out3.txt", Context.MODE_APPEND))
        output.println(word)
        output.println(mean)
        output.close()

    }
}
