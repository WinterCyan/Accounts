package wintercyan.accounts

import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ItemFragment: Fragment() {
    private var passedContext: Context? = null
    var accounts: ArrayList<Account>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_item, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.accountRecyclerView)


        val dbHelper = SQLite(passedContext!!, "accounts.db", 2)
        val db = dbHelper!!.writableDatabase
        accounts = dbHelper!!.query(db)

        recyclerView.adapter = AccountAdapter(accounts)
        recyclerView.layoutManager = LinearLayoutManager(context)
        return rootView
    }

    fun newInstance(context: Context): Fragment?{
        val fragment = ItemFragment()
        fragment.passedContext = context
        return fragment
    }
}