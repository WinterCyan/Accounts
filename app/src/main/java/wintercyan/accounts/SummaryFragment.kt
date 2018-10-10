package wintercyan.accounts

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_item.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

const val TYPE_DAY: Int = 1
const val TYPE_MONTH: Int = 2

class SummaryFragment: Fragment() {
    private var passedContext: Context? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_summary, container, false)
//        val dayCard = rootView.findViewById<CardView>(R.id.dayCard)
//        val lastMonthCard = rootView.findViewById<CardView>(R.id.lastMonthCard)
//        val thisMonthCard = rootView.findViewById<CardView>(R.id.thisMonthCard)
        val refreshLayout: SwipeRefreshLayout = rootView.findViewById(R.id.refreshView)

//        val init = rootView.findViewById<CardView>(R.id.init_db)
//        init.setOnClickListener {
//            val dbHelper = SQLite(passedContext!!, dbName, 2)
//            val db = dbHelper.writableDatabase
////            dbHelper.initDatabase(db)
//            db.close()
//        }

//        dayCard.setOnClickListener {
//            lastMonthCard.setOnClickListener{
//                val dbHelper = SQLite(passedContext!!, dbName, 2)
//                val db = dbHelper.writableDatabase
//                var accounts = dbHelper.query(db, ALL)
//                db.close()
//
//                // refresh the summary view.
//            }
//        }
//
//        lastMonthCard.setOnClickListener{
//            val dbHelper = SQLite(passedContext!!, dbName, 2)
//            val db = dbHelper.writableDatabase
////            var accounts = dbHelper.query(db, LAST_MONTH)
//            val lastMonthdata = getRecentMonthDay(db)
//            db.close()
//
//            // refresh the summary view.
//        }
//
//        thisMonthCard.setOnClickListener {
//            val dbHelper = SQLite(passedContext!!, dbName, 2)
//            val db = dbHelper.writableDatabase
//            var accounts = dbHelper.query(db, THIS_MONTH)
//            db.close()
//
//        }

        var chart1 = rootView.findViewById<BarChart>(R.id.chart)
        var description = Description()
        description.text = "Recent Month Figure"
        chart1.description = description

        var chart2 = rootView.findViewById<PieChart>(R.id.pieChart)
        var description2 = Description()
        description2.text = "Percentage"
        chart2.description = description2

        val monthText = rootView.findViewById<TextView>(R.id.monthText)
        val todayText = rootView.findViewById<TextView>(R.id.todayText)

        refreshLayout.setOnRefreshListener {
            refresh(todayText, monthText, chart1, chart2)
            refreshView.isRefreshing = false
        }

        refresh(todayText, monthText, chart1, chart2)

        return rootView
    }

    private fun refresh(todayText: TextView, monthText: TextView, chart1: BarChart, chart2: PieChart){
        val dbHelper = SQLite(passedContext!!, dbName, 2)
        val db = dbHelper.writableDatabase

        val barChartData = getRecentMonthDay(db)
        val barEntries = ArrayList<Entry>()
        var f: Float = 0.toFloat()
        for (key in barChartData.keys){
            var amount = barChartData[key]
            barEntries.add(BarEntry(f++, amount!!))
        }

        val barSet = BarDataSet(barEntries.toList() as MutableList<BarEntry>?, "Day Food Amount")
        val barData = BarData(barSet)
        barData.barWidth = 0.9f
        chart1.data = barData
        chart1.setFitBars(true)
        chart1.invalidate()


        val pieEntries = ArrayList<Entry>()
        val pieDataArray = getPercent(db)

        pieEntries.add(PieEntry(pieDataArray[0], "Food"))
        pieEntries.add(PieEntry(pieDataArray[2], "Others"))

        val pieSet = PieDataSet(pieEntries.toList() as MutableList<PieEntry>, "Percentage")
        pieSet.colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
        pieSet.valueTextSize = 16f
        val data = PieData(pieSet)
        chart2.data = data
        chart2.invalidate()

        todayText.text = String.format("%.1f", getTodayOrMonth(db, TYPE_DAY))
        monthText.text = String.format("%.1f", getTodayOrMonth(db, TYPE_MONTH))

        db.close()
    }

    private fun getRecentMonthDay(db: SQLiteDatabase): TreeMap<String, Float>{
        var dateArray = ArrayList<String>()
        var dateAmountMap = TreeMap<String, Float>()
        val c = Calendar.getInstance()
        var days = 0
        do {
            dateArray.add(SimpleDateFormat("yyyy-MM-dd").format(c.time))
            c.add(Calendar.DATE, -1)
            days ++
        } while (c.get(Calendar.DAY_OF_MONTH) != Calendar.getInstance().get(Calendar.DAY_OF_MONTH)&&days<=31)

        for (date in dateArray){
            var totalDayAmount = 0f
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

    private fun getPercent(db: SQLiteDatabase): Array<Float>{
        val sql1 = "select sum(amount) from item where name='Food';"
        val sql2 = "select sum(amount) from item where name!='Food';"
        var cursor1 = db.rawQuery(sql1, null)
        var cursor2 = db.rawQuery(sql2, null)
        var foodAmount = 0f
        if (cursor1.moveToFirst()) foodAmount = cursor1.getFloat(0)
        var otherAmount = 0f
        if (cursor2.moveToFirst()) otherAmount = cursor2.getFloat(0)
        cursor1.close()
        cursor2.close()

        if (foodAmount==0f&&otherAmount==0f) return arrayOf(0f,0f,0f,0f)
        return arrayOf(foodAmount, foodAmount/(foodAmount+otherAmount), otherAmount, otherAmount/(foodAmount+otherAmount))
    }

    private fun getTodayOrMonth(db: SQLiteDatabase, type: Int): Float{
        var sql: String? = null
        when (type){
            TYPE_DAY -> {
                sql = "select sum(amount) from item where date>=date('now');"

            }
            TYPE_MONTH -> {
                sql = "select sum(amount) from item where date>=date('now','start of month');"
            }
        }
        var cursor = db.rawQuery(sql, null)
        var amount = 0f
        if (cursor.moveToFirst()) amount = cursor.getFloat(0)
        cursor.close()

        return amount
    }

    fun newInstance(context: Context): Fragment?{
        val fragment = SummaryFragment()
        fragment.passedContext = context
        return fragment
    }
}