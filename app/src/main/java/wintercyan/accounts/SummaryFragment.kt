package wintercyan.accounts

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SummaryFragment: Fragment() {
    private var passedContext: Context? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_summary, container, false)
        val dayCard = rootView.findViewById<CardView>(R.id.dayCard)
        val monthCard = rootView.findViewById<CardView>(R.id.monthCard)

        return rootView
    }

    fun newInstance(context: Context): Fragment?{
        val fragment = SummaryFragment()
        fragment.passedContext = context
        return fragment
    }
}