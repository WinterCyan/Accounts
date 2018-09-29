package wintercyan.accounts

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

private val CREATE_ITEM = "create table item(" +
        "id integer primary key autoincrement," +
        "name text," +
        "date date," +
        "amount real)"
private val QUERY_ITEM = "select * from item"
private val DROP_ITEM = "drop table if exists item"

class SQLite(context: Context, database: String, version: Int) : SQLiteOpenHelper(context, database, null, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(CREATE_ITEM)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(DROP_ITEM)
    }

    fun query(db: SQLiteDatabase): ArrayList<Account>{
        var cursor = db.rawQuery("select * from item", null)
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
}

