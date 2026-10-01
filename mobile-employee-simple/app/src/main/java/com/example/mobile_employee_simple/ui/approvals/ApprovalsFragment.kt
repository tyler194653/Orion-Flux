package com.example.mobile_employee_simple.ui.approvals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.databinding.FragmentApprovalsBinding

data class ApprovalItem(
    val id: String,
    val title: String,
    val meta: String,
    val desc: String,
    var status: ApprovalStatus
)

enum class ApprovalStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    REJECTED
}

class ApprovalsFragment : Fragment() {

    private var _binding: FragmentApprovalsBinding? = null
    private val binding get() = _binding!!
    private val items = mutableListOf<ApprovalItem>()
    private lateinit var adapter: ApprovalsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApprovalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (items.isEmpty()) {
            items.addAll(
                listOf(
                    ApprovalItem(
                        "PR-2026-088",
                        "紧急采购补货申请",
                        "仓储一部 · 提交于 2026-09-30",
                        "申请紧急补充 SKU002 (减速齿轮箱总成) 50 箱，预算 ¥24,500。",
                        ApprovalStatus.PENDING
                    ),
                    ApprovalItem(
                        "SO-2026-042",
                        "货损出库核销复核",
                        "质检中心 · 提交于 2026-09-30",
                        "申请复核并下架 A区-01 货架受潮纸箱包装配件 2 件，作退厂返工处理。",
                        ApprovalStatus.PENDING
                    ),
                    ApprovalItem(
                        "TR-2026-015",
                        "跨库精密轴承调拨",
                        "调度中心 · 提交于 2026-09-29",
                        "从中央一号库向分拨二号库调配 100 套工业精密轴承，支援产线急需。",
                        ApprovalStatus.IN_PROGRESS
                    ),
                    ApprovalItem(
                        "MT-2026-003",
                        "输送机易损备件申领",
                        "运维工程部 · 提交于 2026-09-28",
                        "申领高压液压接头 10 包用于 3 号传送辊道日常维保保养。",
                        ApprovalStatus.COMPLETED
                    )
                )
            )
        }

        adapter = ApprovalsAdapter(
            items,
            onApprove = { item, position ->
                item.status = ApprovalStatus.COMPLETED
                adapter.notifyItemChanged(position)
                Toast.makeText(context, "已通过审批: ${item.title}", Toast.LENGTH_SHORT).show()
            },
            onReject = { item, position ->
                item.status = ApprovalStatus.REJECTED
                adapter.notifyItemChanged(position)
                Toast.makeText(context, "已驳回工单: ${item.title}", Toast.LENGTH_SHORT).show()
            }
        )

        binding.approvalsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.approvalsRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class ApprovalsAdapter(
        private val list: List<ApprovalItem>,
        private val onApprove: (ApprovalItem, Int) -> Unit,
        private val onReject: (ApprovalItem, Int) -> Unit
    ) : RecyclerView.Adapter<ApprovalsAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_approval, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = list[position]
            holder.title.text = item.title
            holder.meta.text = item.meta
            holder.desc.text = item.desc

            when (item.status) {
                ApprovalStatus.PENDING -> {
                    holder.badge.text = "待审批"
                    holder.badge.setBackgroundResource(R.drawable.bg_badge_pending)
                    holder.badge.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.status_pending_text))
                    holder.btnApprove.visibility = View.VISIBLE
                    holder.btnReject.visibility = View.VISIBLE
                }
                ApprovalStatus.IN_PROGRESS -> {
                    holder.badge.text = "流转中"
                    holder.badge.setBackgroundResource(R.drawable.bg_badge_in_progress)
                    holder.badge.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.status_in_progress_text))
                    holder.btnApprove.visibility = View.VISIBLE
                    holder.btnReject.visibility = View.VISIBLE
                }
                ApprovalStatus.COMPLETED -> {
                    holder.badge.text = "已通过"
                    holder.badge.setBackgroundResource(R.drawable.bg_badge_completed)
                    holder.badge.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.status_completed_text))
                    holder.btnApprove.visibility = View.GONE
                    holder.btnReject.visibility = View.GONE
                }
                ApprovalStatus.REJECTED -> {
                    holder.badge.text = "已驳回"
                    holder.badge.setBackgroundResource(R.drawable.bg_badge_pending)
                    holder.badge.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.status_critical_text))
                    holder.btnApprove.visibility = View.GONE
                    holder.btnReject.visibility = View.GONE
                }
            }

            holder.btnApprove.setOnClickListener {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onApprove(item, pos)
                }
            }
            holder.btnReject.setOnClickListener {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onReject(item, pos)
                }
            }
        }

        override fun getItemCount(): Int = list.size

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val title: TextView = v.findViewById(R.id.approval_title)
            val badge: TextView = v.findViewById(R.id.approval_status_badge)
            val meta: TextView = v.findViewById(R.id.approval_meta)
            val desc: TextView = v.findViewById(R.id.approval_desc)
            val btnApprove: Button = v.findViewById(R.id.btn_approve)
            val btnReject: Button = v.findViewById(R.id.btn_reject)
        }
    }
}
