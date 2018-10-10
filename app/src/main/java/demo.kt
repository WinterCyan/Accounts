import java.text.SimpleDateFormat
import java.util.*

fun main(args: Array<String>){
//    var day: Date
//    val today = Date()
//    for (i in 1..50){
//        day = addDays(today, i)
//        println(SimpleDateFormat("yyyy-MM-dd").format(day))
//    }
//    println(Date())
//    println(Calendar.getInstance())
//    getMonthData()
//    val date = SimpleDateFormat("MM/dd/yyyy").parse("10/08/2018")
//    val new = SimpleDateFormat("yyyy-MM-dd").format(date)
    val new = String.format("%.1f", 5.6724f)
    println(new)
}

//fun addDays(d: Date, days: Int): Date {
//    val c = Calendar.getInstance()
//    c.add(Calendar.DATE, days)
//    return c.time
//}
//
//fun getMonthData(){
//    var dateArray = ArrayList<String>()
//    var dateAmount = HashMap<String, Float>()
//    val c = Calendar.getInstance()
//    do {
//        c.add(Calendar.DATE, -1)
//        dateArray.add(SimpleDateFormat("yyyy-MM-dd").format(c.time))
//    } while (c.get(Calendar.DAY_OF_MONTH) != Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
//    for (date in dateArray){
//        println(date)
//    }
//}

//val sql = "select * from item"
//var cursor = db.rawQuery(sql, null)
//var id: Int
//var date: String
//if (cursor!!.moveToFirst()){
//    do {
//        id = cursor.getInt(cursor.getColumnIndex("id"))
//        date = cursor.getString(cursor.getColumnIndex("date"))
//        if (date!="2018-10-05"){
//            val origindate = SimpleDateFormat("MM/dd/yyyy").parse(date)
//            val new = SimpleDateFormat("yyyy-MM-dd").format(origindate)
//            val sql2 = "update item set date='$new' where id=$id;"
//            db.execSQL(sql2)
//        }
//    } while (cursor.moveToNext())
//}
//cursor.close()
