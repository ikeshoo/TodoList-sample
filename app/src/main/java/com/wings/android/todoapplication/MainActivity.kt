package com.wings.android.todoapplication


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.Menu
import androidx.activity.viewModels
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.wings.android.todoapplication.data.Item
import com.wings.android.todoapplication.presentation.adapter.ItemAdapter
import com.wings.android.todoapplication.presentation.viewmodel.TodoViewModel
import com.wings.android.todoapplication.presentation.viewmodel.TodoViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: TodoViewModelFactory
    @Inject
    lateinit var itemAdapter: ItemAdapter
    val viewModel by viewModels<TodoViewModel> {viewModelFactory}
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up Action Bar
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment? ?: return
        val navController = host.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBar(navController,appBarConfiguration)


    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.fragmentContainerView).navigateUp(appBarConfiguration)
    }

    private fun setupActionBar(
        navController: NavController,
        appBarConfiguration: AppBarConfiguration
    ){
        setupActionBarWithNavController(
            navController,
            appBarConfiguration
        )
    }


}