package wintercyan.accounts

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class AccountAdapter(private val accounts: ArrayList<Account>?): RecyclerView.Adapter<AccountHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AccountHolder {
        val accountItem = LayoutInflater.from(p0.context).inflate(R.layout.account_item, p0, false) as CardView
        return AccountHolder(accountItem)
    }

    override fun onBindViewHolder(p0: AccountHolder, p1: Int) {
        p0.updateWithAccount(accounts!![p1])
    }

    override fun getItemCount(): Int {
        return accounts!!.toArray().count()
    }
}