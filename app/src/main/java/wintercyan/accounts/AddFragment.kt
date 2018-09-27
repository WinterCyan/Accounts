package wintercyan.accounts

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.provider.Telephony
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_item.*
import kotlinx.android.synthetic.main.second.*
import java.text.SimpleDateFormat
import java.util.*

class AddFragment: Fragment() {
    private var passedContext: Context? = null
    private var addBtn: Button? = null
    private var nameText: EditText? = null
    private var dateText: EditText? = null
    private var amountText: EditText? = null
    private var dateBtn: Button? = null
    private var calendar = Calendar.getInstance()!!
    private var dbHelper: SQLite? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_add, container, false)

        nameText = rootView.findViewById(R.id.nameText)
        dateText = rootView.findViewById(R.id.dateText)
        amountText = rootView.findViewById(R.id.amountText)
        addBtn = rootView.findViewById(R.id.addButton)
        dateBtn = rootView.findViewById(R.id.dateBtn)

        addBtn!!.setOnClickListener {
            val date = dateText!!.text.toString()
            val name: String = nameText!!.text.toString()
            val amount: Float = amountText!!.text.toString().toFloat()
            dbHelper = SQLite(passedContext!!, "accounts.db", 2)
            val db = dbHelper!!.writableDatabase
            var values = ContentValues().apply {
                put("name", name)
                put("date", date)
                put("amount", amount)
            }
            val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                when(which){
                    DialogInterface.BUTTON_POSITIVE -> {
                        db.insert("item", null, values)
                    }
                }
            }
            val builder = AlertDialog.Builder(passedContext)
            builder.setTitle("Confirmation")
            builder.setMessage("Click OK to add this account")
            builder.setPositiveButton("OK", dialogClickListener)
            builder.setNegativeButton("CANCEL", dialogClickListener)
            builder.create().show()
        }
        dateBtn!!.setOnClickListener {
            DatePickerDialog(passedContext, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val format = SimpleDateFormat("MM/dd/yyyy", Locale.CHINA)
                dateText!!.setText(format.format(calendar.time))
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        return rootView
    }

    companion object {
        fun newInstance(context: Context): AddFragment{
            val fragment = AddFragment()
            fragment.passedContext = context

            return fragment
        }
    }
}