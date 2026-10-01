package com.example.mobile_employee_simple.data.repository

import com.example.mobile_employee_simple.utils.LanguageManager

data class QuickPrompt(
    val id: String,
    val displayLabel: String,
    val queryText: String
)

interface AIChatRepository {
    fun getWelcomeMessage(): String
    fun getQuickPrompts(): List<QuickPrompt>
    suspend fun generateResponse(query: String): String
}

class MockAIChatRepositoryImpl : AIChatRepository {

    override fun getWelcomeMessage(): String {
        return if (LanguageManager.isChinese()) {
            "您好！我是供应链 AI 智能工作助理。支持实时库存状态查询、盘点工单下发与智能出库路径规划，请问今天有什么可以帮您？"
        } else {
            "Hello! I am your Supply Chain AI Assistant. I support real-time inventory queries, stocktake work order dispatch, and intelligent picking route optimization. How can I assist you today?"
        }
    }

    override fun getQuickPrompts(): List<QuickPrompt> {
        val isZh = LanguageManager.isChinese()
        return if (isZh) {
            listOf(
                QuickPrompt("stock_alert", "⚠️ 库存预警", "查询当前库存预警"),
                QuickPrompt("inventory_summary", "📊 盘点汇总", "总结今日盘点进度"),
                QuickPrompt("route_optimize", "⚡ 路径优化", "获取智能拣货与出库路径优化建议"),
                QuickPrompt("qa_check", "🎯 质检分析", "查看最新批次质检分析简报")
            )
        } else {
            listOf(
                QuickPrompt("stock_alert", "⚠️ Stock Alert", "Query current inventory warnings"),
                QuickPrompt("inventory_summary", "📊 Stocktake", "Summarize today's stocktaking progress"),
                QuickPrompt("route_optimize", "⚡ Route Opt", "Get intelligent picking route optimization"),
                QuickPrompt("qa_check", "🎯 QA Analysis", "View latest batch QA inspection report")
            )
        }
    }

    override suspend fun generateResponse(query: String): String {
        val isZh = LanguageManager.isChinese()
        return if (isZh) {
            when {
                query.contains("预警") || query.contains("库存") ->
                    "⚠️ 【低库存预警提示】\n• 减速齿轮箱总成 (SKU002)：当前库存 45 箱，已触及安全水位线（50箱）。\n建议立即向采购部门发起补货流程，或在工作台审批流提交采购单。"
                query.contains("盘点") || query.contains("进度") ->
                    "📊 【今日盘点进度快报】\n• 已完成库区：A区（轴承/齿轮箱共 165 件，账实相符 100%）。\n• 进行中库区：B区（传感器 310 件，已核 200 件）。\n• 预计耗时：还需约 45 分钟完成全仓核实。"
                query.contains("路径") || query.contains("优化") || query.contains("出库") ->
                    "⚡ 【智能出库与拣货优化】\n根据当前订单结构，推荐最优走行拣货路线：\n1. [A区-01-01] 工业精密轴承\n2. [A区-01-02] 减速齿轮箱总成\n3. [B区-02-01] 数字压力传感器\n预计可减少约 35% 的仓内往返折返时间。"
                query.contains("质检") || query.contains("质量") ->
                    "🎯 【批次质检分析】\n最近入库的 5 个批次综合合格率为 98.5%。异常主要为轻微包装磨损，核心零部件参数检测均达标，可正常流转出库。"
                else ->
                    "收到您的指令：\"$query\"。\nAI 助理已完成供应链系统数据检索，各项运营参数正常。如需更详细的工单操作，可前往工作台查看对应功能模块。"
            }
        } else {
            when {
                query.contains("warning", ignoreCase = true) || query.contains("stock", ignoreCase = true) || query.contains("inventory", ignoreCase = true) ->
                    "⚠️ [Low Stock Alert]\n• Speed Reducer Gearbox Assembly (SKU002): Current stock 45 boxes, reaching safety threshold (50 boxes).\nRecommended action: Initiate replenishment in Approvals Center."
                query.contains("stocktake", ignoreCase = true) || query.contains("progress", ignoreCase = true) ->
                    "📊 [Today's Stocktake Flash Report]\n• Completed Zone: Zone A (Bearings/Gearboxes 165 items, 100% verified match).\n• Active Zone: Zone B (Sensors 310 items, 200 items checked).\n• Estimated time remaining: ~45 minutes to complete whole warehouse."
                query.contains("route", ignoreCase = true) || query.contains("optimi", ignoreCase = true) ->
                    "⚡ [Smart Picking Route Optimization]\nBased on current picking batch orders, recommended routing:\n1. [Zone A-01-01] Industrial Precision Bearings\n2. [Zone A-01-02] Speed Reducer Gearbox Assembly\n3. [Zone B-02-01] Digital Pressure Sensor\nEstimated ~35% transit time reduction."
                query.contains("qa", ignoreCase = true) || query.contains("quality", ignoreCase = true) || query.contains("inspection", ignoreCase = true) ->
                    "🎯 [Batch Quality Analysis]\nThe last 5 inbound batches achieved an overall pass rate of 98.5%. Minor deviations were limited to cosmetic carton scuffs; all critical tolerances meet enterprise specs."
                else ->
                    "Command received: \"$query\".\nAI Assistant has queried live supply chain metrics; all operating indicators are nominal. For specific workflows, visit the Workbench modules."
            }
        }
    }
}
