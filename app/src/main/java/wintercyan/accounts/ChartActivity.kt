package wintercyan.accounts

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry

class ChartActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        val chart = findViewById<LineChart>(R.id.chart)
        var entries = ArrayList<Entry>()
    }
}