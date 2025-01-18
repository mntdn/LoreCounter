package com.mntdn.lorecounter

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.mntdn.lorecounter.databinding.FragmentFirstBinding

class MMTimer(var tv: TextView) {
    var tempModif: Int = 0
    val timer = object: CountDownTimer(2000, 1000) {
        override fun onTick(millisUntilFinished: Long) {

        }

        override fun onFinish() {
            tv.text = ""
            tempModif = 0
        }
    }

    fun inc(){
        timer.cancel()
        timer.start()
        tempModif++
        tv.text = (if(tempModif > 0) "+" else "-") + Math.abs(tempModif).toString();
    }

    fun dec(){
        timer.cancel()
        timer.start()
        tempModif--
        tv.text = (if(tempModif > 0) "+" else "-") + Math.abs(tempModif).toString();
    }
}

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    var loreValues = intArrayOf(0 , 0)

    var tempAugmentation = 0

    var timer1: MMTimer? = null
    var timer2: MMTimer? = null

    private var _binding: FragmentFirstBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    fun showDialogFinish(player: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder
            .setMessage("Game finished!")
            .setTitle("Player " + player.toString() + " has won!")
            .setPositiveButton("New game") { dialog, which ->
                // Do something.
                resetGame()
            }
            .setNegativeButton("Oops") { dialog, which ->
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun resetGame(){
        loreValues[0] = 0
        loreValues[1] = 0
        tempAugmentation = 0
        updateText()
    }

    fun inc(player: Int){
        if(loreValues[player - 1] == 19){
            showDialogFinish(player)
            return
        }
        loreValues[player - 1]++
        tempAugmentation++
        if(player == 1)
            timer1!!.inc()
        else
            timer2!!.inc()
    }

    fun dec(player: Int){
        if(loreValues[player - 1] == 0)
            return
        loreValues[player - 1]--
        tempAugmentation--
        if(player == 1)
            timer1!!.dec()
        else
            timer2!!.dec()
    }

    fun updateText() {
        binding.tvP1.text = loreValues[0].toString();
        binding.tvP2.text = loreValues[1].toString();
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timer1 = MMTimer(binding.tvTempP1)
        timer2 = MMTimer(binding.tvTempP2)
        updateText()
        binding.tvPlusP1.setOnClickListener {
            // findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            inc(1)
            updateText();
        }
        binding.tvMinusP1.setOnClickListener {
            dec(1)
            updateText();
        }
        binding.tvPlusP2.setOnClickListener {
            inc(2)
            updateText();
        }
        binding.tvMinusP2.setOnClickListener {
            dec(2)
            updateText();
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}