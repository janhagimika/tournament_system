package com.example.ukol_mika

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import example.javatpoint.com.kotlinsqlitecrud.StrikerClass
import example.javatpoint.com.kotlinsqlitecrud.TeamClass
import kotlinx.android.synthetic.main.pohled_dva.*
import kotlinx.android.synthetic.main.pohled_dva.bottomNavigationView
import kotlinx.android.synthetic.main.pohled_treti.*

class DruhyPohled : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pohled_dva)
        val PrvniPohled = Intent(this, PrvniPohled::class.java)
        val DruhyPohled = Intent(this, DruhyPohled::class.java)
        val TretiPohled = Intent(this, TretiPohled::class.java)
        bottomNavigationView.setSelectedItemId(R.id.tabulka)
        var helper = MyDBHelper(applicationContext)
        var db = helper.readableDatabase
        val dbHandler: MyDBHelper= MyDBHelper(this)
        val selectQuery = "SELECT * FROM TEAMS ORDER BY POINTS DESC"
        val teams: List<TeamClass> = dbHandler.getAllTeams(db)

        val tabulka = findViewById<TableLayout>(R.id.tableOfTeams)
        for (tym in teams) {
            val tbRow = TableRow(this)
            tbRow.layoutParams= ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            tbRow.setBackgroundColor(Color.rgb(235,242, 235))

            val txtTym = TextView(this)
            txtTym.setText(tym.name)
            txtTym.setTypeface(null, Typeface.BOLD)
            txtTym.setPadding(15, 5, 5, 5)
            tbRow.addView(txtTym)

            val txtGoly = TextView(this)
            txtGoly.setText(tym.goals)
            txtGoly.setTypeface(null, Typeface.BOLD)
            txtGoly.setPadding(15, 5, 5, 5)
            tbRow.addView(txtGoly)

            val txtObdrzene = TextView(this)
            txtObdrzene.setText(tym.received)
            txtObdrzene.setTypeface(null, Typeface.BOLD)
            txtObdrzene.setPadding(15, 5, 5, 5)
            tbRow.addView(txtObdrzene)

            val txtBody = TextView(this)
            txtBody.setText(tym.points)
            txtBody.setTypeface(null, Typeface.BOLD)
            txtBody.setPadding(15, 5, 5, 5)
            tbRow.addView(txtBody)
            tabulka.addView(tbRow)
        }


        bottomNavigationView.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.zapasy -> startActivity(PrvniPohled)
                R.id.tabulka -> startActivity(DruhyPohled)
                R.id.strikers -> startActivity(TretiPohled)
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.end -> endTournament()
        }
        return super.onOptionsItemSelected(item)
    }
    fun endTournament(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.dialogTitle)
        builder.setMessage(R.string.dialogMessage)
        val dbHandler: MyDBHelper= MyDBHelper(this)
        var helper = MyDBHelper(applicationContext)
        var db = helper.readableDatabase
        val MainAct = Intent(this, MainActivity::class.java)
        builder.setPositiveButton("Ano"){dialogInterface, which ->
            db?.execSQL("DELETE FROM MATCHES")
            db?.execSQL("DELETE FROM TEAMS")
            db?.execSQL("DELETE FROM STRIKERS")
            db?.execSQL("DELETE FROM TREATMENTS")
            startActivity(MainAct)
        }
        builder.setNeutralButton("Neukončovat"){dialogInterface , which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}