package com.example.ukol_mika

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ukol_mika.databinding.PohledTretiBinding
import example.javatpoint.com.kotlinsqlitecrud.StrikerClass

private lateinit var binding: PohledTretiBinding
class TretiPohled : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PohledTretiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val PrvniPohled = Intent(this, PrvniPohled::class.java)
        val DruhyPohled = Intent(this, DruhyPohled::class.java)
        val TretiPohled = Intent(this, TretiPohled::class.java)
        binding.bottomNavigationView.setSelectedItemId(R.id.strikers)

        val tabulka = findViewById<TableLayout>(R.id.tableOfStrikers)
        var helper = MyDBHelper(applicationContext)
        var db = helper.readableDatabase
        val dbHandler: MyDBHelper= MyDBHelper(this)
        val query = "SELECT NAME, TEAM, SUM(GOALS) AS GOALS, MATCHID FROM STRIKERS GROUP BY NAME, TEAM ORDER BY GOALS DESC"
        val strikers: List<StrikerClass> = dbHandler.getStrikers(db,query)
        for (strelec in strikers) {
            val tbRow = TableRow(this)
            tbRow.layoutParams= ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            tbRow.setBackgroundColor(Color.rgb(235,242, 235))

            val txtJmeno = TextView(this)
            txtJmeno.setText(strelec.name)
            txtJmeno.setTypeface(null, Typeface.BOLD)
            txtJmeno.setPadding(15, 5, 5, 5)
            tbRow.addView(txtJmeno)

            val txtTeam = TextView(this)
            txtTeam.setText(strelec.team)
            txtTeam.setTypeface(null, Typeface.BOLD)
            txtTeam.setPadding(15, 5, 5, 5)
            tbRow.addView(txtTeam)

            val txtGoly = TextView(this)
            txtGoly.setText(strelec.goals)
            txtGoly.setTypeface(null, Typeface.BOLD)
            txtGoly.setPadding(15, 5, 5, 5)
            tbRow.addView(txtGoly)
            tabulka.addView(tbRow)
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.zapasy -> moveToAnotherActivity(PrvniPohled)
                R.id.tabulka -> moveToAnotherActivity(DruhyPohled)
                R.id.strikers -> moveToAnotherActivity(TretiPohled)
            }
            true
        }
    }
    fun moveToAnotherActivity(intent: Intent){
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.end -> TournamentHelper.endTournament(this)
        }
        return super.onOptionsItemSelected(item)
    }
}