package com.example.ukol_mika

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ukol_mika.databinding.PohledDvaBinding
import example.javatpoint.com.kotlinsqlitecrud.TeamClass

private lateinit var binding: PohledDvaBinding

class DruhyPohled : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PohledDvaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val PrvniPohled = Intent(this, PrvniPohled::class.java)
        val DruhyPohled = Intent(this, DruhyPohled::class.java)
        val TretiPohled = Intent(this, TretiPohled::class.java)
        binding.bottomNavigationView.selectedItemId = R.id.tabulka

        var helper = MyDBHelper(applicationContext)
        var db = helper.readableDatabase
        val dbHandler: MyDBHelper = MyDBHelper(this)
        val selectQuery = "SELECT * FROM TEAMS ORDER BY POINTS DESC"
        val teams: List<TeamClass> = dbHandler.getAllTeams(db)

        val tabulka = findViewById<TableLayout>(R.id.tableOfTeams)
        for (tym in teams) {
            val tbRow = TableRow(this)
            tbRow.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            tbRow.setBackgroundColor(Color.rgb(235, 242, 235))

            val txtTym = TextView(this)
            txtTym.text = tym.name
            txtTym.setTypeface(null, Typeface.BOLD)
            txtTym.setPadding(15, 5, 5, 5)
            tbRow.addView(txtTym)

            val txtGoly = TextView(this)
            txtGoly.text = tym.goals
            txtGoly.setTypeface(null, Typeface.BOLD)
            txtGoly.setPadding(15, 5, 5, 5)
            tbRow.addView(txtGoly)

            val txtObdrzene = TextView(this)
            txtObdrzene.text = tym.received
            txtObdrzene.setTypeface(null, Typeface.BOLD)
            txtObdrzene.setPadding(15, 5, 5, 5)
            tbRow.addView(txtObdrzene)

            val txtBody = TextView(this)
            txtBody.text = tym.points
            txtBody.setTypeface(null, Typeface.BOLD)
            txtBody.setPadding(15, 5, 5, 5)
            tbRow.addView(txtBody)
            tabulka.addView(tbRow)
        }
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.zapasy -> moveToAnotherActivity(PrvniPohled)
                R.id.tabulka -> moveToAnotherActivity(DruhyPohled)
                R.id.strikers -> moveToAnotherActivity(TretiPohled)
            }
            true
        }
    }

    fun moveToAnotherActivity(intent: Intent) {
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.end -> TournamentHelper.endTournament(this)
        }
        return super.onOptionsItemSelected(item)
    }
}