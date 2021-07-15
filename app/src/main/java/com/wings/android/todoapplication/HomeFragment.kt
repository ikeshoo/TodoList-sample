package com.wings.android.todoapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.core.view.MenuItemCompat.setOnActionExpandListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.wings.android.todoapplication.data.Item
import com.wings.android.todoapplication.databinding.FragmentHomeBinding
import com.wings.android.todoapplication.presentation.adapter.ItemAdapter
import com.wings.android.todoapplication.presentation.notification.AlarmBroadcastReceiver
import com.wings.android.todoapplication.presentation.viewmodel.TodoViewModel
import com.wings.android.todoapplication.presentation.viewmodel.TodoViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: TodoViewModelFactory
    @Inject
    lateinit var itemAdapter: ItemAdapter
    private val viewModel by activityViewModels<TodoViewModel> {viewModelFactory}
    private lateinit var searchView:SearchView
    private lateinit var alarmManager: AlarmManager
    private var _homeFragmentBinding:FragmentHomeBinding? = null
    private val homeFragmentBinding
    get() = _homeFragmentBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    //action barの作成
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu,menu)
        val searchViewItem = menu.findItem(R.id.action_search)
        searchViewItem.setOnActionExpandListener(object :MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                //show SoftKey
                item?.actionView!!.requestFocus()
                val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                //hide SoftKey
                val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                //set observer
                viewModel.getSavedItems().observe(viewLifecycleOwner, Observer {
                    itemAdapter.differ.submitList(it)
                })
                return true
            }

        })
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.isIconified = false
    }

    //action barをタッチした時の処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            //作成日時でソート
            R.id.action_sort_by_date ->{
                viewModel.getSavedItemsByDate().observe(viewLifecycleOwner, Observer {
                    itemAdapter.differ.submitList(it)
                })
                true
            }
            //期限でソート
            R.id.action_sort_by_deadline ->{
                viewModel.getSavedItemsByDeadline().observe(viewLifecycleOwner, Observer {
                    itemAdapter.differ.submitList(it)
                })
                true
            }
            //メモを検索
            R.id.action_search ->{
                searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        viewModel.getSearchedItems(query.toString()).observe(viewLifecycleOwner, Observer {
                            itemAdapter.differ.submitList(it)
                        })
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        MainScope().launch{
                            delay(2000)
                            viewModel.getSearchedItems(newText.toString()).observe(viewLifecycleOwner, Observer {
                                itemAdapter.differ.submitList(it)
                            })

                        }
                        return false
                    }
                })
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _homeFragmentBinding = FragmentHomeBinding.inflate(inflater,container,false)

        return homeFragmentBinding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //RecyclerViewの設定
        initRecyclerView()
        //observerの設定
        viewModel.getSavedItems().observe(viewLifecycleOwner, Observer {
            itemAdapter.differ.submitList(it)
        })

        //RecyclerViewのリスナー設定
        itemAdapter.setOnItemClickListener(object :ItemAdapter.OnItemClickListener{
            override fun onItemClick(item: Item) {
                val action = HomeFragmentDirections.actionHomeFragmentToDisplayItemFragment(item)
                findNavController().navigate(action)
            }
        })

        //RecyclerViewのスワイプ時の設定
        val itemTouchHelperCallback = object:ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.ACTION_STATE_IDLE,
            ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = itemAdapter.differ.currentList[position]
                viewModel.deleteItem(item)
                deleteAlarm(item)
                Snackbar.make(homeFragmentBinding.root,"削除しました",Snackbar.LENGTH_LONG).show()
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(homeFragmentBinding.rvItem)
        }

        //FABのリスナーの設定
        homeFragmentBinding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_makeItemFragment)
        }
    }

    //RecyclerViewの初期化
    private fun initRecyclerView(){
        homeFragmentBinding.apply {
            rvItem.adapter = itemAdapter
            rvItem.layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun deleteAlarm(item:Item){
        val intent = Intent(requireContext(), AlarmBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            item.id,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT)
        alarmManager.cancel(pendingIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _homeFragmentBinding = null
    }

}