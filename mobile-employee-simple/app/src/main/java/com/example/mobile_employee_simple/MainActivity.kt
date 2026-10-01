package com.example.mobile_employee_simple

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mobile_employee_simple.databinding.ActivityMainBinding
import com.example.mobile_employee_simple.ui.auth.LoginViewModel
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val topLevelDestinations = setOf(
        R.id.nav_workbench,
        R.id.nav_ai_chat,
        R.id.nav_approvals,
        R.id.nav_profile
    )

    private fun getNavController(): NavController {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        return navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // 设置 toolbar
            setSupportActionBar(binding.appBarMain.toolbar)

            val drawerLayout: DrawerLayout = binding.drawerLayout
            val navView: NavigationView = binding.navView
            val bottomNav = binding.appBarMain.contentMain.bottomNavigation
            val navController = getNavController()

            appBarConfiguration = AppBarConfiguration(topLevelDestinations, drawerLayout)
            setupActionBarWithNavController(navController, appBarConfiguration)

            setupNavigationListeners()

            // 检查登录状态
            val isLoggedIn = LoginViewModel.isLoggedIn(this)
            if (!isLoggedIn) {
                appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_login), drawerLayout)
                setupActionBarWithNavController(navController, appBarConfiguration)
                navView.isEnabled = false
                bottomNav.visibility = View.GONE
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                navController.navigate(
                    R.id.nav_login,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.nav_workbench, false).build()
                )
            }
        } catch (e: Exception) {
            Toast.makeText(this, "onCreate异常: " + e.message, Toast.LENGTH_LONG).show()
            Log.e("MainActivity", "onCreate异常", e)
            e.printStackTrace()
        }
    }

    private fun setupNavigationListeners() {
        val navController = getNavController()
        val bottomNav = binding.appBarMain.contentMain.bottomNavigation
        val navView = binding.navView
        val drawerLayout = binding.drawerLayout

        // 底部导航栏点击监听：明确定义 popUpTo 逻辑，避免状态恢复混乱跳到 approvals
        bottomNav.setOnItemSelectedListener { item ->
            val currentId = navController.currentDestination?.id
            if (currentId == item.itemId) {
                return@setOnItemSelectedListener true
            }

            val navOptions = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .apply {
                    if (item.itemId == R.id.nav_workbench) {
                        setPopUpTo(R.id.nav_workbench, inclusive = false)
                    } else {
                        setPopUpTo(R.id.nav_workbench, inclusive = false, saveState = true)
                        setRestoreState(true)
                    }
                }
                .build()

            try {
                navController.navigate(item.itemId, null, navOptions)
                true
            } catch (e: Exception) {
                Log.e("MainActivity", "BottomNav 导航异常", e)
                false
            }
        }

        // 侧边栏抽屉点击监听：与底部导航栏同步联动
        navView.setNavigationItemSelectedListener { menuItem ->
            drawerLayout.closeDrawers()
            when (menuItem.itemId) {
                R.id.nav_workbench,
                R.id.nav_ai_chat,
                R.id.nav_approvals,
                R.id.nav_profile -> {
                    bottomNav.selectedItemId = menuItem.itemId
                    true
                }
                R.id.nav_settings -> {
                    navController.navigate(R.id.nav_settings)
                    true
                }
                R.id.nav_logout -> {
                    logout()
                    true
                }
                else -> false
            }
        }

        // 页面跳转状态监听，精准同步 Toolbar 与底部栏可见性及选中态
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_login -> {
                    supportActionBar?.hide()
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    navView.isEnabled = false
                    bottomNav.visibility = View.GONE
                }
                R.id.nav_camera -> {
                    supportActionBar?.show()
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    bottomNav.visibility = View.GONE
                }
                R.id.nav_workbench,
                R.id.nav_ai_chat,
                R.id.nav_approvals,
                R.id.nav_profile -> {
                    supportActionBar?.show()
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    navView.isEnabled = true
                    bottomNav.visibility = View.VISIBLE

                    // 同步底部导航状态
                    val menu = bottomNav.menu
                    for (i in 0 until menu.size()) {
                        val item = menu.getItem(i)
                        if (item.itemId == destination.id && !item.isChecked) {
                            item.isChecked = true
                            break
                        }
                    }
                    navView.setCheckedItem(destination.id)
                }
                else -> {
                    // 子页面（如库存管理、产品图鉴、设置等）
                    supportActionBar?.show()
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    navView.isEnabled = true
                    bottomNav.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_user_info -> {
                showUserInfoDialog()
                true
            }
            R.id.action_settings -> {
                getNavController().navigate(R.id.nav_settings)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun showUserInfoDialog() {
        val currentUser = LoginViewModel.getCurrentUser(this) ?: "用户"
        val isLoggedIn = LoginViewModel.isLoggedIn(this)
        
        if (isLoggedIn) {
            val options = arrayOf("当前用户: $currentUser", "退出登录")
            AlertDialog.Builder(this)
                .setTitle("用户信息")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> {
                            Toast.makeText(this, "当前用户: $currentUser", Toast.LENGTH_SHORT).show()
                        }
                        1 -> {
                            logout()
                        }
                    }
                }
                .setNegativeButton("取消", null)
                .show()
        } else {
            Toast.makeText(this, "未登录", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun logout() {
        LoginViewModel.logout(this)
        Toast.makeText(this, "已退出登录", Toast.LENGTH_SHORT).show()
        
        val navController = getNavController()
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.mobile_navigation, true)
            .build()
        navController.navigate(R.id.nav_login, null, navOptions)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = getNavController()
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    
    // 登录成功后调用此方法启用导航与底部导航栏
    fun enableNavigationAfterLogin() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val bottomNav = binding.appBarMain.contentMain.bottomNavigation
        val navController = getNavController()

        appBarConfiguration = AppBarConfiguration(topLevelDestinations, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.isEnabled = true
        bottomNav.visibility = View.VISIBLE
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        bottomNav.selectedItemId = R.id.nav_workbench
    }
}