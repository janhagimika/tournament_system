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
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ukol_mika.databinding.PohledJednaBinding
import example.javatpoint.com.kotlinsqlitecrud.MatchClass

private lateinit var binding: PohledJednaBinding
class PrvniPohled : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PohledJednaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val PrvniPohled = Intent(this, PrvniPohled::class.java)
        val DruhyPohled = Intent(this, DruhyPohled::class.java)
        val TretiPohled = Intent(this, TretiPohled::class.java)
        binding.bottomNavigationView.setSelectedItemId(R.id.zapasy)

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
                finish()
            }
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


