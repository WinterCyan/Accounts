package wintercyan.accounts

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.io.FileDescriptor
import java.io.PrintWriter

class ItemFragment: Fragment() {
    var accounts: ArrayList<Account>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_item, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.accountRecyclerView)

        accounts = arrayListOf(Account("1", "312", 23.2.toFloat()))

        recyclerView.adapter = AccountAdapter(accounts)
        recyclerView.layoutManager = LinearLayoutManager(context)
        return rootView
    }
}