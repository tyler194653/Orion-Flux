package com.example.mobile_employee_simple.ui.chat

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.databinding.FragmentAiChatBinding

data class ChatMessage(
    val id: Long,
    val text: String,
    val isUser: Boolean
)

class AIChatFragment : Fragment() {

    private var _binding: FragmentAiChatBinding? = null
    private val binding get() = _binding!!
    private val messages = mutableListOf<ChatMessage>()
    private lateinit var adapter: ChatAdapter
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAiChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ChatAdapter(messages)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        binding.chatRecyclerView.layoutManager = layoutManager
        binding.chatRecyclerView.adapter = adapter

        if (messages.isEmpty()) {
            messages.add(
                ChatMessage(
                    System.currentTimeMillis(),
                    "您好！我是供应链 AI 智能工作助理。支持实时库存状态查询、盘点工单下发与智能出库路径规划，请问今天有什么可以帮您？",
                    false
                )
            )
            adapter.notifyItemInserted(0)
        }

        binding.btnSend.setOnClickListener {
            val text = binding.inputMessage.text.toString().trim()
            if (text.isNotEmpty()) {
                sendMessage(text)
                binding.inputMessage.setText("")
            }
        }

        binding.chipStockAlert.setOnClickListener {
            sendMessage("查询当前库存预警")
        }

        binding.chipInventorySummary.setOnClickListener {
            sendMessage("总结今日盘点进度")
        }

        binding.chipBatchOptimize.setOnClickListener {
            sendMessage("获取智能拣货与出库路径优化建议")
        }

        binding.chipQualityCheck.setOnClickListener {
            sendMessage("查看最新批次质检分析简报")
        }
    }

    private fun sendMessage(text: String) {
        val userMsg = ChatMessage(System.currentTimeMillis(), text, true)
        messages.add(userMsg)
        adapter.notifyItemInserted(messages.size - 1)
        binding.chatRecyclerView.smoothScrollToPosition(messages.size - 1)

        mainHandler.postDelayed({
            if (!isAdded) return@postDelayed
            val response = generateResponse(text)
            val aiMsg = ChatMessage(System.currentTimeMillis(), response, false)
            messages.add(aiMsg)
            adapter.notifyItemInserted(messages.size - 1)
            binding.chatRecyclerView.smoothScrollToPosition(messages.size - 1)
        }, 500)
    }

    private fun generateResponse(query: String): String {
        return when {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class ChatAdapter(private val items: List<ChatMessage>) :
        RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_message, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val layoutAi: View = itemView.findViewById(R.id.layout_ai_message)
            private val textAi: TextView = itemView.findViewById(R.id.text_ai_content)
            private val layoutUser: View = itemView.findViewById(R.id.layout_user_message)
            private val textUser: TextView = itemView.findViewById(R.id.text_user_content)

            fun bind(msg: ChatMessage) {
                if (msg.isUser) {
                    layoutUser.visibility = View.VISIBLE
                    layoutAi.visibility = View.GONE
                    textUser.text = msg.text
                } else {
                    layoutUser.visibility = View.GONE
                    layoutAi.visibility = View.VISIBLE
                    textAi.text = msg.text
                }
            }
        }
    }
}
