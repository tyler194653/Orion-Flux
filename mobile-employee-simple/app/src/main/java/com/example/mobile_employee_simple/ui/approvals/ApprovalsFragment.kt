package com.example.mobile_employee_simple.ui.approvals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.data.DataRepositoryProvider
import com.example.mobile_employee_simple.data.model.ApprovalItem
import com.example.mobile_employee_simple.data.model.ApprovalStatus
import com.example.mobile_employee_simple.databinding.FragmentApprovalsBinding
import com.example.mobile_employee_simple.ui.auth.LoginViewModel
import com.example.mobile_employee_simple.ui.base.BaseFragment
import com.example.mobile_employee_simple.ui.base.LocaleDialogHelper
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
            onDetails = { item ->
                showDetailsDialog(item)
            },
            onWithdraw = { item, position ->
                handleWithdraw(item, position)
            }
        )

        binding.approvalsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.approvalsRecyclerView.adapter = adapter

        binding.fabSubmitRequest.setOnClickListener {
            showNewRequestDialog()
        }

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

    private fun showDetailsDialog(item: ApprovalItem) {
        val detailsContent = if (item.details.isNotEmpty()) {
            item.details
        } else {
            val isZh = isChinese()
            if (isZh) {
                "【申请单号】${item.id}\n【所属类别】${item.category}\n【内容简述】${item.desc}\n【审批节点】${item.approver}\n【流转记录】${item.timeline}"
            } else {
                "[Request ID] ${item.id}\n[Category] ${item.category}\n[Summary] ${item.desc}\n[Approver] ${item.approver}\n[Timeline] ${item.timeline}"
            }
        }

        LocaleDialogHelper.showInfoDialog(
            context = requireContext(),
            titleRes = R.string.dialog_details_title,
            message = detailsContent,
            buttonRes = R.string.dialog_ok
        )
    }

    private fun handleWithdraw(item: ApprovalItem, position: Int) {
        if (!item.canWithdraw) {
            showToast(getLocalizedString(R.string.toast_withdraw_failed_status))
            return
        }

        LocaleDialogHelper.showConfirmDialog(
            context = requireContext(),
            titleRes = R.string.dialog_withdraw_title,
            messageRes = R.string.dialog_withdraw_message,
            positiveRes = R.string.dialog_withdraw_confirm,
            negativeRes = R.string.cancel,
            onPositive = {
                viewLifecycleOwner.lifecycleScope.launch {
                    val success = repository.withdrawApproval(item.id)
                    if (success) {
                        item.status = ApprovalStatus.WITHDRAWN
                        adapter.notifyItemChanged(position)
                        showToast(getLocalizedString(R.string.toast_withdraw_success))
                    }
                }
            }
        )
    }

    private fun showNewRequestDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_new_request, null)
        val rgType = dialogView.findViewById<RadioGroup>(R.id.rg_request_type)
        val etTitle = dialogView.findViewById<TextInputEditText>(R.id.et_request_title)
        val etDesc = dialogView.findViewById<TextInputEditText>(R.id.et_request_desc)
        val btnCancel = dialogView.findViewById<Button>(R.id.btn_dialog_cancel)
        val btnSubmit = dialogView.findViewById<Button>(R.id.btn_dialog_submit)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSubmit.setOnClickListener {
            val titleText = etTitle.text?.toString()?.trim().orEmpty()
            val descText = etDesc.text?.toString()?.trim().orEmpty()

            if (titleText.isEmpty() || descText.isEmpty()) {
                showToast(getLocalizedString(R.string.toast_input_required))
                return@setOnClickListener
            }

            val checkedRbId = rgType.checkedRadioButtonId
            val selectedRb = dialogView.findViewById<RadioButton>(checkedRbId)
            val categoryName = selectedRb?.text?.toString() ?: getLocalizedString(R.string.dialog_new_request_type_supplies)

            val currentUser = LoginViewModel.getCurrentUser(requireContext()) ?: "test"
            val nowStr = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
            val dateTag = SimpleDateFormat("HHmm", Locale.getDefault()).format(Date())
            val newId = "REQ-2026-$dateTag"

            val newItem = ApprovalItem(
                id = newId,
                title = titleText,
                meta = "$categoryName · ${getLocalizedString(R.string.inventory_item_update_format, nowStr)}",
                desc = descText,
                status = ApprovalStatus.PENDING,
                category = categoryName,
                details = if (isChinese()) {
                    "【申请单号】$newId\n【申请人】$currentUser\n【申请类别】$categoryName\n【申请事由】$titleText\n【详细说明】$descText\n【提交时间】$nowStr\n【审批节点】待直属主管初审"
                } else {
                    "[Request ID] $newId\n[Applicant] $currentUser\n[Category] $categoryName\n[Title] $titleText\n[Details] $descText\n[Submitted At] $nowStr\n[Node] Pending Supervisor Review"
                },
                approver = if (isChinese()) "直属主管" else "Direct Supervisor",
                timeline = "$nowStr ${if (isChinese()) "员工提交申请 ➔ 待初审" else "Submitted by employee ➔ Pending review"}"
            )

            viewLifecycleOwner.lifecycleScope.launch {
                repository.submitApproval(newItem)
                items.add(0, newItem)
                adapter.notifyItemInserted(0)
                binding.approvalsRecyclerView.scrollToPosition(0)
                dialog.dismiss()
                showToast(getLocalizedString(R.string.toast_submit_success))
            }
        }

        dialog.show()
    }

    private class ApprovalsAdapter(
        private val list: List<ApprovalItem>,
        private val onDetails: (ApprovalItem) -> Unit,
        private val onWithdraw: (ApprovalItem, Int) -> Unit
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

            // Dynamic binding via ApprovalStatus enum
            holder.badge.setText(item.status.titleRes)
            holder.badge.setBackgroundResource(item.status.bgDrawableRes)
            holder.badge.setTextColor(ContextCompat.getColor(context, item.status.textColorRes))

            holder.btnDetails.setOnClickListener {
                onDetails(item)
            }

            if (item.canWithdraw) {
                holder.btnWithdraw.visibility = View.VISIBLE
                holder.btnWithdraw.isEnabled = true
                holder.btnWithdraw.text = context.getString(R.string.request_btn_withdraw)
                holder.btnWithdraw.setTextColor(ContextCompat.getColor(context, R.color.status_critical_text))
                holder.btnWithdraw.setOnClickListener {
                    val pos = holder.adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        onWithdraw(item, pos)
                    }
                }
            } else if (item.status == ApprovalStatus.WITHDRAWN) {
                holder.btnWithdraw.visibility = View.VISIBLE
                holder.btnWithdraw.isEnabled = false
                holder.btnWithdraw.text = context.getString(R.string.request_btn_withdrawn)
                holder.btnWithdraw.setTextColor(ContextCompat.getColor(context, R.color.slate_400))
                holder.btnWithdraw.setOnClickListener(null)
            } else {
                holder.btnWithdraw.visibility = View.GONE
                holder.btnWithdraw.setOnClickListener(null)
            }
        }

        override fun getItemCount(): Int = list.size

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val title: TextView = v.findViewById(R.id.approval_title)
            val badge: TextView = v.findViewById(R.id.approval_status_badge)
            val meta: TextView = v.findViewById(R.id.approval_meta)
            val desc: TextView = v.findViewById(R.id.approval_desc)
            val btnDetails: Button = v.findViewById(R.id.btn_details)
            val btnWithdraw: Button = v.findViewById(R.id.btn_withdraw)
        }
    }
}
