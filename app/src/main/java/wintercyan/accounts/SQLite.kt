package wintercyan.accounts

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.sql.Driver
import java.sql.DriverManager
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

const val CREATE_ITEM = "create table item(" +
        "id integer primary key autoincrement," +
        "name text," +
        "date date," +
        "amount real)"
const val DROP_ITEM = "drop table if exists item"
const val ALL: Int = 1
const val LAST_MONTH: Int = 2
const val THIS_MONTH: Int = 3
const val jdbcUrl = "jdbc:sqldroid:/data/data/wintercyan.accounts/databases/accounts.db"
const val dbName = "accounts.db"

class SQLite(context: Context, database: String, version: Int) : SQLiteOpenHelper(context, database, null, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(CREATE_ITEM)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(DROP_ITEM)
    }

    private fun getDates(): ArrayList<String?> {
        try {
            DriverManager.registerDriver(Class.forName("org.sqldroid.SQLDroidDriver").newInstance() as Driver)
        } catch (e: Exception) {
            throw RuntimeException("Failed to register SQLDroidDriver")
        }

        try {
            val conn = DriverManager.getConnection(jdbcUrl)
            val stat = conn.createStatement()
            var set = stat.executeQuery("select date('now');")
            var today: String? = null
            var firstMonthDay: String? = null
            var lastMonthToday: String? = null
            if (set.next()) today= set.getString(1)
            set = stat.executeQuery("select date('now', 'start of month');")
            if (set.next()) firstMonthDay = set.getString(1)
            set = stat.executeQuery("select date('now', '-1 month');")
            if (set.next()) lastMonthToday = set.getString(1)
            set.close()
            stat.close()
            conn.close()

            return arrayListOf(today, firstMonthDay, lastMonthToday)

        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    fun query(db: SQLiteDatabase, kind: Int): ArrayList<Account>{
        val dateList = getDates()
        var today = dateList[0]
        var firstMonthDay = dateList[1]
        var lastMonthToday = dateList[2]

        var sql: String? = null
        when (kind){
            ALL -> sql = "select * from item"
            LAST_MONTH -> sql = "select * from item where date between '$lastMonthToday' and '$today';"
            THIS_MONTH -> sql = "select * from item where date between '$firstMonthDay' and '$today';"
        }
        var cursor = db.rawQuery(sql!!, null)
        var id: Int
        var name: String
        var date: String
        var amount: Float
        var list = ArrayList<Account>()
        if (cursor!!.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                date = cursor.getString(cursor.getColumnIndex("date"))
                amount = cursor.getFloat(cursor.getColumnIndex("amount"))
                list.add(Account(id, name, date, amount))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun initDatabase(db: SQLiteDatabase?){
        var day: Date
        for (i in -70..70) {
            val c = Calendar.getInstance()
            c.add(Calendar.DATE, i)
            day = c.time
            val date = SimpleDateFormat("yyyy-MM-dd").format(day)

            var values = ContentValues().apply {
                put("name", "name")
                put("date", date)
                put("amount", "1.0")
            }
            db!!.insert("item", null, values)
        }
    }
}

