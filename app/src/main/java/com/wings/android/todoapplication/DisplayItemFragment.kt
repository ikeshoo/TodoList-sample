package com.wings.android.todoapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wings.android.todoapplication.data.Item
import com.wings.android.todoapplication.databinding.FragmentDisplayItemBinding
import com.wings.android.todoapplication.presentation.adapter.ItemAdapter
import com.wings.android.todoapplication.presentation.notification.AlarmBroadcastReceiver
import com.wings.android.todoapplication.presentation.viewmodel.TodoViewModel


class DisplayItemFragment : Fragment() {
    private val viewModel by activityViewModels<TodoViewModel>()
    private lateinit var alarmManager: AlarmManager
    private var _displayItemBinding:FragmentDisplayItemBinding?=null
    private val displayItemBinding
    get() = _displayItemBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.display_menu,menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _displayItemBinding = FragmentDisplayItemBinding.inflate(
            inflater,
            container,
            false
        )

        return displayItemBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args:DisplayItemFragmentArgs by navArgs()
        val item = args.item
        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        displayItemBinding.apply{
            tvItemContent.text = item.content
            tvItemDeadline.text = item.deadline.toString()
            //削除ボタンのリスナー
            btDelete.setOnClickListener {
                displayDialog(item)
            }
            //上書きボタンのリスナー
            btUpdate.setOnClickListener {
                val action = DisplayItemFragmentDirections.actionDisplayItemFragmentToMakeItemFragment(item)
                findNavController().navigate(action)
            }
        }
    }

    private fun deleteAlarm(item:Item){
        val intent = Intent(requireContext(),AlarmBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            item.id,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT)
        alarmManager.cancel(pendingIntent)
    }

    private fun displayDialog(item:Item){
        val alertDialog = activity?.let{
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(
                    R.string.dialog_positive_button,
                    DialogInterface.OnClickListener { dialog, which ->
                        viewModel.deleteItem(item)
                        deleteAlarm(item)
                        findNavController().navigate(R.id.action_displayItemFragment_to_homeFragment)
                    })
                setNegativeButton(
                    R.string.dialog_negative_button,
                    DialogInterface.OnClickListener { dialog, which ->

                    }
                )
                setMessage("削除しますか？")
                create()
            }
        }
        alertDialog?.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _displayItemBinding = null
    }
}