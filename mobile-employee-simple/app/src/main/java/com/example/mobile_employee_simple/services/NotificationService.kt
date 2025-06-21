package com.example.mobile_employee_simple.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mobile_employee_simple.MainActivity
import com.example.mobile_employee_simple.R

class NotificationService(private val context: Context) {
    
    companion object {
        const val CHANNEL_ID_TASK = "task_notifications"
        const val CHANNEL_ID_INVENTORY = "inventory_notifications"
        const val CHANNEL_ID_SYSTEM = "system_notifications"
        const val CHANNEL_ID_AI = "ai_notifications"
    }
    
    private val notificationManager = NotificationManagerCompat.from(context)
    
    init {
        createNotificationChannels()
    }
    
    /**
     * 创建通知渠道
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    CHANNEL_ID_TASK,
                    "任务通知",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "任务相关的通知"
                    enableVibration(true)
                    enableLights(true)
                },
                NotificationChannel(
                    CHANNEL_ID_INVENTORY,
                    "库存通知",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "库存相关的通知"
                    enableVibration(true)
                },
                NotificationChannel(
                    CHANNEL_ID_SYSTEM,
                    "系统通知",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "系统相关的通知"
                },
                NotificationChannel(
                    CHANNEL_ID_AI,
                    "AI助手通知",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "AI助手相关的通知"
                    enableVibration(true)
                }
            )
            
            notificationManager.createNotificationChannels(channels)
        }
    }
    
    /**
     * 显示任务通知
     */
    fun showTaskNotification(
        id: Int,
        title: String,
        content: String,
        taskId: String? = null
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("task_id", taskId)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_TASK)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(id, notification)
    }
    
    /**
     * 显示库存通知
     */
    fun showInventoryNotification(
        id: Int,
        title: String,
        content: String,
        inventoryId: String? = null
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("inventory_id", inventoryId)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_INVENTORY)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(id, notification)
    }
    
    /**
     * 显示系统通知
     */
    fun showSystemNotification(
        id: Int,
        title: String,
        content: String
    ) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_SYSTEM)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(id, notification)
    }
    
    /**
     * 显示AI助手通知
     */
    fun showAINotification(
        id: Int,
        title: String,
        content: String,
        aiAction: String? = null
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("ai_action", aiAction)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_AI)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(id, notification)
    }
    
    /**
     * 显示进度通知
     */
    fun showProgressNotification(
        id: Int,
        title: String,
        content: String,
        progress: Int,
        max: Int = 100
    ) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_SYSTEM)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setProgress(max, progress, false)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
        
        notificationManager.notify(id, notification)
    }
    
    /**
     * 更新进度通知
     */
    fun updateProgressNotification(
        id: Int,
        title: String,
        content: String,
        progress: Int,
        max: Int = 100
    ) {
        showProgressNotification(id, title, content, progress, max)
    }
    
    /**
     * 完成进度通知
     */
    fun completeProgressNotification(
        id: Int,
        title: String,
        content: String
    ) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_SYSTEM)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(id, notification)
    }
    
    /**
     * 取消通知
     */
    fun cancelNotification(id: Int) {
        notificationManager.cancel(id)
    }
    
    /**
     * 取消所有通知
     */
    fun cancelAllNotifications() {
        notificationManager.cancelAll()
    }
    
    /**
     * 检查通知权限
     */
    fun areNotificationsEnabled(): Boolean {
        return notificationManager.areNotificationsEnabled()
    }
} 