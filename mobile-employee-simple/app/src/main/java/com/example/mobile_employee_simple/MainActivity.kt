package com.example.mobile_employee_simple

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile_employee_simple.databinding.ActivityMainBinding
import com.example.mobile_employee_simple.ui.auth.LoginViewModel
import com.example.mobile_employee_simple.ui.auth.LoginFragment
import androidx.fragment.app.commit
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // 设置 toolbar
            setSupportActionBar(binding.appBarMain.toolbar)

            binding.appBarMain.fab.setOnClickListener { view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .setAnchorView(R.id.fab).show()
            }
            
            val drawerLayout: DrawerLayout = binding.drawerLayout
            val navView: NavigationView = binding.navView
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            
            // 检查登录状态
            val isLoggedIn = LoginViewModel.isLoggedIn(this)
            
            if (isLoggedIn) {
                // 已登录：启用侧边导航，设置所有页面为顶级目标
                appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.nav_home, R.id.nav_workbench, R.id.nav_camera, R.id.nav_inventory, R.id.nav_settings, R.id.nav_user_info
                    ), drawerLayout
                )
                
                // 设置 ActionBar 导航
                setupActionBarWithNavController(navController, appBarConfiguration)
                navView.setupWithNavController(navController)
                
                // 如果当前在登录页面，跳转到Home页面
                if (navController.currentDestination?.id == R.id.nav_login) {
                    navController.navigate(R.id.nav_home)
                }
            } else {
                // 未登录：禁用侧边导航，只允许登录页面
                appBarConfiguration = AppBarConfiguration(
                    setOf(R.id.nav_login), drawerLayout
                )
                
                // 设置 ActionBar 导航
                setupActionBarWithNavController(navController, appBarConfiguration)
                
                // 禁用侧边导航
                navView.setEnabled(false)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
            
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.nav_login) {
                    // 登录页面：完全隐藏ActionBar，锁定抽屉
                    supportActionBar?.hide()
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    
                    // 禁用侧边导航
                    navView.setEnabled(false)
                } else {
                    // 其他页面：显示ActionBar，解锁抽屉
                    supportActionBar?.show()
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    
                    // 启用侧边导航
                    navView.setEnabled(true)
                    
                    // 重新配置AppBarConfiguration，设置所有页面为顶级目标
                    appBarConfiguration = AppBarConfiguration(
                        setOf(
                            R.id.nav_home, R.id.nav_workbench, R.id.nav_camera, R.id.nav_inventory, R.id.nav_settings, R.id.nav_user_info
                        ), drawerLayout
                    )
                    setupActionBarWithNavController(navController, appBarConfiguration)
                }
            }
            
            navView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.nav_user_info -> {
                        val navController = findNavController(R.id.nav_host_fragment_content_main)
                        navController.navigate(R.id.nav_user_info)
                        binding.drawerLayout.closeDrawers()
                        true
                    }
                    R.id.nav_logout -> {
                        logout()
                        binding.drawerLayout.closeDrawers()
                        true
                    }
                    else -> {
                        // 默认交给Navigation组件处理
                        val handled = try {
                            val navController = findNavController(R.id.nav_host_fragment_content_main)
                            navController.navigate(menuItem.itemId)
                            true
                        } catch (e: Exception) {
                            false
                        }
                        binding.drawerLayout.closeDrawers()
                        handled
                    }
                }
            }
            
        } catch (e: Exception) {
            Toast.makeText(this, "onCreate异常: " + e.message, Toast.LENGTH_LONG).show()
            Log.e("MainActivityTest", "onCreate异常", e)
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if is present.
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
                // 跳转到设置页面
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.nav_settings)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun showUserInfoDialog() {
        val currentUser = LoginViewModel.getCurrentUser(this) ?: "用户"
        val isLoggedIn = LoginViewModel.isLoggedIn(this)
        
        if (isLoggedIn) {
            // 显示用户信息和登出选项
            val options = arrayOf("当前用户: $currentUser", "退出登录")
            android.app.AlertDialog.Builder(this)
                .setTitle("用户信息")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> {
                            // 显示用户详细信息
                            Toast.makeText(this, "当前用户: $currentUser", Toast.LENGTH_SHORT).show()
                        }
                        1 -> {
                            // 退出登录
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
        
        // 跳转到登录页面
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val navOptions = androidx.navigation.NavOptions.Builder()
            .setPopUpTo(R.id.nav_home, true)
            .build()
        navController.navigate(R.id.nav_login, null, navOptions)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    
    // 登录成功后调用此方法启用侧边导航
    fun enableNavigationAfterLogin() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        
        // 重新配置导航
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_workbench, R.id.nav_camera, R.id.nav_inventory, R.id.nav_settings, R.id.nav_user_info
            ), drawerLayout
        )
        
        // 重新设置 ActionBar 导航
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        
        // 启用侧边导航
        navView.setEnabled(true)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }
}