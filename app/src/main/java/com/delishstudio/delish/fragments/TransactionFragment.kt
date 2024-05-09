package com.delishstudio.delish.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delishstudio.delish.databinding.FragmentTransactionBinding
import com.delishstudio.delish.model.UserManager
import com.delishstudio.delish.activities.adapters.TransactionListAdapter

class TransactionFragment : Fragment() {

    private lateinit var mBinding: FragmentTransactionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        mBinding = FragmentTransactionBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = mBinding.rcTransactions
        recyclerView.layoutManager = LinearLayoutManager(activity)

        recyclerView.adapter = TransactionListAdapter(UserManager.Main.transactionList)
    }
}