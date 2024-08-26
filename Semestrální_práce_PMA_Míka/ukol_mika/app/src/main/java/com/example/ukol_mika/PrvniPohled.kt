package com.example.ukol_mika

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.marginLeft
import androidx.fragment.app.Fragment
import example.javatpoint.com.kotlinsqlitecrud.MatchClass
import kotlinx.android.synthetic.main.pohled_dva.*
import kotlinx.android.synthetic.main.pohled_jedna.*
import kotlinx.android.synthetic.main.pohled_jedna.bottomNavigationView
import kotlinx.android.synthetic.main.pohled_treti.*
import kotlinx.android.synthetic.main.zapas_uprava.*
import java.math.RoundingMode.valueOf
import kotlin.reflect.typeOf

class PrvniPohled : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pohled_jedna)
        val PrvniPohled = Intent(this, PrvniPohled::class.java)
        val DruhyPohled = Intent(this, DruhyPohled::class.java)
        val TretiPohled = Intent(this, TretiPohled::class.java)
        bottomNavigationView.setSelectedItemId(R.id.zapasy)

        val table = findViewById<TableLayout>(R.id.tableOfMatches)
        var helper = MyDBHelper(applicationContext)
        var db = helper.readableDatabase
        /*val strSQl = "DROP TABLE STRIKERS"
        db?.execSQL(strSQl)*/
        val dbHandler: MyDBHelper= MyDBHelper(this)
        val match: List<MatchClass> = dbHandler.getAllMatches(db)
        for (zapas in match) {
            val tbRow = TableRow(this)
            tbRow.layoutParams= ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            tbRow.setBackgroundColor(Color.rgb(235,242, 235))

            val txtDomaci = TextView(this)
            txtDomaci.setText(zapas.home)
            txtDomaci.setTypeface(null, Typeface.BOLD)
            txtDomaci.setPadding(15, 5, 5, 5)
            tbRow.addView(txtDomaci)

            val txtHoste = TextView(this)
            txtHoste.setText(zapas.host)
            txtHoste.setTypeface(null, Typeface.BOLD)
            txtHoste.setPadding(15, 5, 5, 5)
            tbRow.addView(txtHoste)

            val txtVysledek = TextView(this)
            txtVysledek.setText(zapas.homeScore + " : " + zapas.hostScore)
            txtVysledek.setTypeface(null, Typeface.BOLD)
            txtVysledek.setPadding(5, 5, 5, 5)
            tbRow.addView(txtVysledek)
            val button = Button(this)

            val btn_id = 1
            button.id = btn_id
            button.text = "Upravit"
            tbRow.addView(button)

            table.addView(tbRow)
            val uprava = Intent(this, UpravaZapasu::class.java)
            uprava.putExtra("match_id",zapas.matchId)
            uprava.putExtra("domaci",txtDomaci.text)
            uprava.putExtra("hoste",txtHoste.text)
            uprava.putExtra("domacigoly",zapas.homeScore)
            uprava.putExtra("hostegoly",zapas.hostScore)
            button.setOnClickListener {
                startActivity(uprava)
            }
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


