package com.wings.android.todoapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.wings.android.todoapplication.data.Item
import com.wings.android.todoapplication.databinding.FragmentMakeItemBinding
import com.wings.android.todoapplication.presentation.adapter.ItemAdapter
import com.wings.android.todoapplication.presentation.dialog.DatePickerFragment
import com.wings.android.todoapplication.presentation.dialog.TimePickerFragment
import com.wings.android.todoapplication.presentation.notification.AlarmBroadcastReceiver
import com.wings.android.todoapplication.presentation.viewmodel.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MakeItemFragment : Fragment() {
    private val viewModel by activityViewModels<TodoViewModel>()
    @Inject
    lateinit var datePickerFragment: DatePickerFragment
    @Inject
    lateinit var timePickerFragment: TimePickerFragment
    @Inject
    lateinit var itemAdapter: ItemAdapter
    private var receivedItem: Item? = null
    private lateinit var alarmManager: AlarmManager
    private var hour:Int? = null
    private var minute:Int? = null
    private var isReceivedNotification = false
    private var _fragmentMakeItemBinding:FragmentMakeItemBinding? = null
    private val fragmentMakeItemBinding
    get() = _fragmentMakeItemBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _fragmentMakeItemBinding = FragmentMakeItemBinding.inflate(
            inflater,
            container,
            false
        )
        //DataBinding設定
        fragmentMakeItemBinding.viewModel = viewModel
        fragmentMakeItemBinding.lifecycleOwner = this

        return fragmentMakeItemBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //DisplayItemFragmentからItemを受け取る
        val args:MakeItemFragmentArgs? by navArgs()
        receivedItem = args?.item
        //フラグを初期化
        isReceivedNotification = false

        //Dialogから受け取る値
        childFragmentManager.setFragmentResultListener("fromTime",viewLifecycleOwner){ key,bundle ->
            hour = bundle.getInt("hour")
            minute = bundle.getInt("minute")
        }
        //AlarmManager設定
        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager


        //EditTextの表示設定
        if(receivedItem != null){
            viewModel.setMakeItemText(receivedItem!!)
        }else{
            viewModel.initializeMakiItemText()
        }

        fragmentMakeItemBinding.apply{
            //保存ボタンリスナー
            btnSave.setOnClickListener {
                setItem(receivedItem)
            }

            //EditText Focusが外れた時にKeyBordを隠す
            etItemContent.setOnFocusChangeListener { v, hasFocus ->
                if(!hasFocus){
                    val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(view.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
                }
            }
            etDeadline.setOnClickListener {
                showDatePickerDialog(view)
                val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(view.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
            }

            //通知スイッチ
            switchNotification.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    showTimePickerDialog()
                    isReceivedNotification = true
                }else{
                    isReceivedNotification = false
                }
            }
        }

        //View全体にFocusが移るようにする
        view.setOnTouchListener { v, event ->
            if(event.actionMasked == MotionEvent.ACTION_DOWN){
                view.requestFocus()
            }
            v?.onTouchEvent(event) ?: true
        }
        


    }

    //EditTextからItemを作成
    private fun setItem(receivedItem:Item?){
        fragmentMakeItemBinding.apply {
            if(etItemContent.text.isBlank()){
                Snackbar.make(this.root, "内容が空欄です", Snackbar.LENGTH_LONG).show()
            }else if(etDeadline.text.isBlank()){
                Snackbar.make(this.root, "期日が空欄です", Snackbar.LENGTH_LONG).show()
            }else{
                if(receivedItem != null){
                    lifecycleScope.launch {
                        viewModel!!.updateItem(receivedItem)
                        if (isReceivedNotification) {
                            val triggerTime = getTimeInMillis()
                            setAlarm(triggerTime,receivedItem.id)
                        }
                        findNavController().navigate(R.id.action_makeItemFragment_to_homeFragment)
                    }
                }else{
                    lifecycleScope.launch {
                        viewModel!!.saveItem()
                        if (isReceivedNotification) {
                            val triggerTime = getTimeInMillis()
                            setAlarm(triggerTime,viewModel!!.insertRowId!!)
                        }
                        findNavController().navigate(R.id.action_makeItemFragment_to_homeFragment)
                    }
                }
            }
        }
    }

    //DatePickerDialog作成
    private fun showDatePickerDialog(v:View){
        datePickerFragment.show(childFragmentManager,"datePicker")
    }

    //TimePickerDialog作成
    private fun showTimePickerDialog(){
        timePickerFragment
            .show(childFragmentManager,"timePicker")
    }


    //Alarmを起動
    private fun setAlarm(triggerTime:Long,rowId:Int){
        val differ = triggerTime - System.currentTimeMillis()
        val intent = Intent(requireContext(),AlarmBroadcastReceiver::class.java)
        val sendContent = viewModel.displayContent.value
        intent.putExtra("content",sendContent)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            rowId,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + differ,
            pendingIntent
        )
    }

    //ダイアログから受け取った日時をmillisecに変換
    private fun getTimeInMillis():Long{
        val dateText = fragmentMakeItemBinding.etDeadline.text
        val dateStr = dateText.split("-")
        val calendar = Calendar.getInstance().apply {
            set(
                dateStr[0].toInt(),
                dateStr[1].toInt() - 1,
                dateStr[2].toInt(),
                hour!!,
                minute!!
            )
        }
        Log.i("MyTag","year:${dateStr[0]} month:${dateStr[1]} day:${dateStr[2]}")
        return calendar.timeInMillis
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentMakeItemBinding = null
    }



}