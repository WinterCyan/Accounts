package wintercyan.accounts

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ItemFragment: Fragment() {
    private var passedContext: Context? = null
    private var saveBtn: FloatingActionButton? = null
    var accounts: ArrayList<Account>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_item, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.accountRecyclerView)
        val refreshView: SwipeRefreshLayout = rootView.findViewById(R.id.refreshView)

        saveBtn = rootView.findViewById(R.id.exportFab)

        saveBtn!!.setOnClickListener{
            val dbHelper = SQLite(passedContext!!, "accounts.db", 2)
            val db = dbHelper!!.writableDatabase
            val accounts = dbHelper.query(db,"all")
            var writeContent: String
            passedContext!!.openFileOutput("accounts.txt", Context.MODE_PRIVATE).use {
                for (account in accounts){
                    var name = account.name
                    var date = account.date
                    var amount = account.amount
                    writeContent = "$name $date $amount\n"
                    it.write(writeContent.toByteArray())
                }
                it.close()
            }
            db.close()
        }
        refreshView.setOnRefreshListener {
            updateItemFragment(recyclerView)
            refreshView.isRefreshing = false
        }
        updateItemFragment(recyclerView)

        return rootView
    }

    private fun updateItemFragment(view: RecyclerView){
        val dbHelper = SQLite(passedContext!!, "accounts.db", 2)
        val db = dbHelper!!.writableDatabase
        accounts = dbHelper!!.query(db, "all")
        db.close()
        accounts!!.reverse()

        view.adapter = AccountAdapter(accounts)
        view.layoutManager = LinearLayoutManager(context)
    }

    fun newInstance(context: Context): Fragment?{
        val fragment = ItemFragment()
        fragment.passedContext = context
        return fragment
    }
}