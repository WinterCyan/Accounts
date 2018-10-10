package wintercyan.accounts

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class AccountHolder(accountView: View): RecyclerView.ViewHolder(accountView) {
    private val nameText: TextView = accountView.findViewById(R.id.account_name)
    private val dateText: TextView = accountView.findViewById(R.id.account_date)
    private val amountText: TextView = accountView.findViewById(R.id.account_amount)

    fun updateWithAccount(account: Account) {
        nameText.text = account.name+" "    // keep italic character showing completely.
        dateText.text = account.date
        amountText.text = account.amount.toString()
    }
}