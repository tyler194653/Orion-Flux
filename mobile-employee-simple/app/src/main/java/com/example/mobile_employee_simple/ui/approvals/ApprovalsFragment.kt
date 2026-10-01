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
import com.example.mobile_employee_simple.data.repository.ApprovalsRepository
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
                        val isZh = isChinese()
                        if (isZh) {
                            if (!item.details.contains("撤回说明")) {
                                item.details = "${item.details}\n【撤回说明】申请人已主动撤回该申请"
                            }
                            if (!item.timeline.contains("撤回")) {
                                item.timeline = "${item.timeline} ➔ 员工主动撤回申请"
                            }
                        } else {
                            if (!item.details.contains("Withdrawal Note") && !item.details.contains("Withdrawal Reason")) {
                                item.details = "${item.details}\n[Withdrawal Note] Withdrawn by applicant"
                            }
                            if (!item.timeline.contains("Withdrawn")) {
                                item.timeline = "${item.timeline} ➔ Withdrawn by applicant"
                            }
                        }
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

        fun updateTemplate(rbId: Int) {
            val isZh = isChinese()
            when (rbId) {
                R.id.rb_type_supplies -> {
                    etTitle.setText(if (isZh) "工器具与防护耗材申领" else "Supplies & PPE Requisition")
                    etDesc.setText(if (isZh) "申领手持工业扫码枪 1 台及防静电劳保手套，用于盘点作业。" else "Requisition for handheld barcode scanner and PPE gloves for inventory operations.")
                }
                R.id.rb_type_overtime -> {
                    etTitle.setText(if (isZh) "月末大盘点延时加班调休申请" else "Cycle Count Overtime Comp Time Request")
                    etDesc.setText(if (isZh) "申请延时加班工时折算存入个人调休假期账户。" else "Overtime work compensatory time application for warehouse cycle counting.")
                }
                R.id.rb_type_reimbursement -> {
                    etTitle.setText(if (isZh) "紧急外勤差旅与交通费报销" else "Emergency Transport & Expense Claim")
                    etDesc.setText(if (isZh) "紧急跨库调拨打车费用发票报销申请。" else "Out-of-pocket transportation expense claim with receipts attached.")
                }
                R.id.rb_type_incident -> {
                    etTitle.setText(if (isZh) "进料上架外箱微损异常报备" else "Inbound Damaged Cargo Incident Report")
                    etDesc.setText(if (isZh) "货物上架复核发现外包装破损异常，已贴隔离标并拍照留存。" else "Outer package minor damage exception report with isolation photos filed.")
                }
            }
        }

        updateTemplate(rgType.checkedRadioButtonId)

        rgType.setOnCheckedChangeListener { _, checkedId ->
            updateTemplate(checkedId)
        }

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
            val categoryType = when (checkedRbId) {
                R.id.rb_type_supplies -> "supplies"
                R.id.rb_type_overtime -> "overtime"
                R.id.rb_type_reimbursement -> "reimbursement"
                R.id.rb_type_incident -> "incident"
                else -> "supplies"
            }

            val currentUser = LoginViewModel.getCurrentUser(requireContext()) ?: "test"
            val bilingualItem = ApprovalsRepository.createBilingualRequest(
                categoryType = categoryType,
                userTitle = titleText,
                userDesc = descText,
                applicant = currentUser
            )

            viewLifecycleOwner.lifecycleScope.launch {
                repository.submitApproval(bilingualItem)
                val displayItem = bilingualItem.localized(isChinese())
                items.add(0, displayItem)
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
