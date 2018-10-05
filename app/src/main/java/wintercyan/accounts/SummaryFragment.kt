package wintercyan.accounts

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SummaryFragment: Fragment() {
    private var passedContext: Context? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_summary, container, false)
        val dayCard = rootView.findViewById<CardView>(R.id.dayCard)
        val lastMonthCard = rootView.findViewById<CardView>(R.id.lastMonthCard)
        val thisMonthCard = rootView.findViewById<CardView>(R.id.thisMonthCard)

        val init = rootView.findViewById<CardView>(R.id.init_db)
        init.setOnClickListener {
            val dbHelper = SQLite(passedContext!!, dbName, 2)
            val db = dbHelper.writableDatabase
//            dbHelper.initDatabase(db)
            db.close()
        }

        dayCard.setOnClickListener {
            lastMonthCard.setOnClickListener{
                val dbHelper = SQLite(passedContext!!, dbName, 2)
                val db = dbHelper.writableDatabase
                var accounts = dbHelper.query(db, ALL)
                db.close()

                // refresh the summary view.
            }
        }

        lastMonthCard.setOnClickListener{
            val dbHelper = SQLite(passedContext!!, dbName, 2)
            val db = dbHelper.writableDatabase
//            var accounts = dbHelper.query(db, LAST_MONTH)
            val lastMonthdata = getMonthData(db)
            db.close()

            // refresh the summary view.
        }

        thisMonthCard.setOnClickListener {
            val dbHelper = SQLite(passedContext!!, dbName, 2)
            val db = dbHelper.writableDatabase
            var accounts = dbHelper.query(db, THIS_MONTH)
            db.close()

            // refresh the summary view.
        }
        return rootView
    }

    fun newInstance(context: Context): Fragment?{
        val fragment = SummaryFragment()
        fragment.passedContext = context
        return fragment
    }

    private fun getMonthData(db: SQLiteDatabase): TreeMap<String, Float>{
        var dateArray = ArrayList<String>()
        var dateAmountMap = TreeMap<String, Float>()
        val c = Calendar.getInstance()
        do {
            dateArray.add(SimpleDateFormat("yyyy-MM-dd").format(c.time))
            c.add(Calendar.DATE, -1)
        } while (c.get(Calendar.DAY_OF_MONTH) != Calendar.getInstance().get(Calendar.DAY_OF_MONTH))

        for (date in dateArray){
            var totalDayAmount = 0.toFloat()
            dateAmountMap[date] = totalDayAmount
            var sql = "select * from item where date='$date';"
            var cursor = db.rawQuery(sql, null)
            var amount: Float
            var name: String
            if (cursor!!.moveToFirst()){
                do {
                    name = cursor.getString(cursor.getColumnIndex("name"))
                    amount = cursor.getFloat(cursor.getColumnIndex("amount"))
                    if (name=="Food") totalDayAmount += amount
                } while (cursor.moveToNext())
                dateAmountMap[date] = totalDayAmount
            }
            cursor.close()
        }

        for (key in dateAmountMap.keys){
            println("$key, ${dateAmountMap[key]}")
        }

        return dateAmountMap
    }
}