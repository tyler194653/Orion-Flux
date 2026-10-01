package com.example.mobile_employee_simple.ui.chat

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.data.DataRepositoryProvider
import com.example.mobile_employee_simple.data.model.ChatMessage
import com.example.mobile_employee_simple.databinding.FragmentAiChatBinding
import com.example.mobile_employee_simple.ui.base.BaseFragment
import kotlinx.coroutines.launch

class AIChatFragment : BaseFragment<FragmentAiChatBinding>() {

    private val chatRepository = DataRepositoryProvider.aiChatRepository
    private val messages = mutableListOf<ChatMessage>()
    private lateinit var adapter: ChatAdapter
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAiChatBinding {
        return FragmentAiChatBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ChatAdapter(messages)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        binding.chatRecyclerView.layoutManager = layoutManager
        binding.chatRecyclerView.adapter = adapter

        if (messages.isEmpty()) {
            val welcome = chatRepository.getWelcomeMessage()
            messages.add(ChatMessage(System.currentTimeMillis(), welcome, false))
            adapter.notifyItemInserted(0)
        }

        binding.btnSend.setOnClickListener {
            val text = binding.inputMessage.text.toString().trim()
            if (text.isNotEmpty()) {
                sendMessage(text)
                binding.inputMessage.setText("")
            }
        }

        setupQuickPrompts()
    }

    private fun setupQuickPrompts() {
        val prompts = chatRepository.getQuickPrompts()
        val promptMap = prompts.associateBy { it.id }

        promptMap["stock_alert"]?.let { p ->
            binding.chipStockAlert.text = p.displayLabel
            binding.chipStockAlert.setOnClickListener { sendMessage(p.queryText) }
        }
        promptMap["inventory_summary"]?.let { p ->
            binding.chipInventorySummary.text = p.displayLabel
            binding.chipInventorySummary.setOnClickListener { sendMessage(p.queryText) }
        }
        promptMap["route_optimize"]?.let { p ->
            binding.chipBatchOptimize.text = p.displayLabel
            binding.chipBatchOptimize.setOnClickListener { sendMessage(p.queryText) }
        }
        promptMap["qa_check"]?.let { p ->
            binding.chipQualityCheck.text = p.displayLabel
            binding.chipQualityCheck.setOnClickListener { sendMessage(p.queryText) }
        }
    }

    private fun sendMessage(text: String) {
        val userMsg = ChatMessage(System.currentTimeMillis(), text, true)
        messages.add(userMsg)
        adapter.notifyItemInserted(messages.size - 1)
        binding.chatRecyclerView.smoothScrollToPosition(messages.size - 1)

        mainHandler.postDelayed({
            if (!isAdded) return@postDelayed
            viewLifecycleOwner.lifecycleScope.launch {
                val response = chatRepository.generateResponse(text)
                val aiMsg = ChatMessage(System.currentTimeMillis(), response, false)
                messages.add(aiMsg)
                adapter.notifyItemInserted(messages.size - 1)
                binding.chatRecyclerView.smoothScrollToPosition(messages.size - 1)
            }
        }, 500)
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
