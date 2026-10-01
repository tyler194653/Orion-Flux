package com.example.mobile_employee_simple.ui.approvals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.data.DataRepositoryProvider
import com.example.mobile_employee_simple.data.model.ApprovalItem
import com.example.mobile_employee_simple.data.model.ApprovalStatus
import com.example.mobile_employee_simple.databinding.FragmentApprovalsBinding
import com.example.mobile_employee_simple.ui.base.BaseFragment
import kotlinx.coroutines.launch

class ApprovalsFragment : BaseFragment<FragmentApprovalsBinding>() {

    private val repository = DataRepositoryProvider.approvalsRepository
    private val items = mutableListOf<ApprovalItem>()
    private lateinit var adapter: ApprovalsAdapter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentApprovalsBinding {
        return FragmentApprovalsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ApprovalsAdapter(
            items,
            onApprove = { item, position ->
                viewLifecycleOwner.lifecycleScope.launch {
                    item.status = ApprovalStatus.COMPLETED
                    repository.updateStatus(item.id, ApprovalStatus.COMPLETED)
                    adapter.notifyItemChanged(position)
                    showToast(getLocalizedString(R.string.approvals_toast_approved, item.title))
                }
            },
            onReject = { item, position ->
                viewLifecycleOwner.lifecycleScope.launch {
                    item.status = ApprovalStatus.REJECTED
                    repository.updateStatus(item.id, ApprovalStatus.REJECTED)
                    adapter.notifyItemChanged(position)
                    showToast(getLocalizedString(R.string.approvals_toast_rejected, item.title))
                }
            }
        )

        binding.approvalsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.approvalsRecyclerView.adapter = adapter

        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            val list = repository.getApprovals()
            items.clear()
            items.addAll(list)
            adapter.notifyDataSetChanged()
        }
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
            val context = holder.itemView.context

            holder.title.text = item.title
            holder.meta.text = item.meta
            holder.desc.text = item.desc

            // Higher-level localization: mapped via ApprovalStatus enum resource ID
            holder.badge.setText(item.status.titleRes)
            holder.badge.setBackgroundResource(item.status.bgDrawableRes)
            holder.badge.setTextColor(ContextCompat.getColor(context, item.status.textColorRes))

            val isActionable = item.status == ApprovalStatus.PENDING || item.status == ApprovalStatus.IN_PROGRESS
            holder.btnApprove.visibility = if (isActionable) View.VISIBLE else View.GONE
            holder.btnReject.visibility = if (isActionable) View.VISIBLE else View.GONE

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
