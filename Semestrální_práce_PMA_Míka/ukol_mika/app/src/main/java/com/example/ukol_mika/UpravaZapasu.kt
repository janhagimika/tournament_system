package com.example.ukol_mika

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import example.javatpoint.com.kotlinsqlitecrud.StrikerClass
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.pohled_dva.bottomNavigationView
import kotlinx.android.synthetic.main.zapas_uprava.*

class UpravaZapasu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zapas_uprava)
        val PrvniPohled = Intent(this, PrvniPohled::class.java)
        val DruhyPohled = Intent(this, DruhyPohled::class.java)
        val TretiPohled = Intent(this, TretiPohled::class.java)
        bottomNavigationView.setSelectedItemId(R.id.zapasy)
        val dbHandler: MyDBHelper= MyDBHelper(this)
        var helper = MyDBHelper(applicationContext)
        var db = helper.readableDatabase
        val domaci = intent.getStringExtra ("domaci")
        val hoste = intent.getStringExtra("hoste")
        val idzapasu = intent.getStringExtra("match_id")
        val golydomaci = intent.getStringExtra("domacigoly")
        val golyhoste = intent.getStringExtra("hostegoly")
        val dom = findViewById<TextView>(R.id.homeTeam)
        val host = findViewById<TextView>(R.id.hostTeam)
        val domSkore = findViewById<EditText>(R.id.scoreHome)
        val hosteSkore = findViewById<EditText>(R.id.scoreHost)
        dom.setText(domaci)
        host.setText(hoste)
        domSkore.setText(golydomaci)
        hosteSkore.setText(golyhoste)
        var ulozeno = 0
        var ulozenoVtetoAkci = 0
        if(golydomaci != "" || golyhoste != ""){
            ulozeno = 1
        }
        btnUlozitZapas.setOnClickListener {
            if(ulozenoVtetoAkci == 0){
                if(domSkore.text.toString() != "" && hosteSkore.text.toString() != ""){
                    val dskore = Integer.parseInt(domSkore.text.toString())
                    val hskore = Integer.parseInt(hosteSkore.text.toString())
                    if(dskore == hskore){
                        if(ulozeno == 0){
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = GOALSSCORED + " + scoreHome.text.toString() + ", GOALSRECEIVED = GOALSRECEIVED + " + scoreHost.text.toString() + ", POINTS = POINTS + " + 1 + " WHERE TEAMNAME = '" + domaci +"'")
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = GOALSSCORED + " + scoreHost.text.toString() + ", GOALSRECEIVED = GOALSRECEIVED + " + scoreHome.text.toString() + ", POINTS = POINTS + " + 1 + " WHERE TEAMNAME = '" + hoste +"'")
                        }
                        else{
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = " + scoreHome.text.toString() + ", GOALSRECEIVED = " + scoreHost.text.toString() + ", POINTS = " + 1 + " WHERE TEAMNAME = '" + domaci +"'")
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = " + scoreHost.text.toString() + ", GOALSRECEIVED = " + scoreHome.text.toString() + ", POINTS = " + 1 + " WHERE TEAMNAME = '" + hoste +"'")
                        }
                    }
                    else if(dskore > hskore){
                        if(ulozeno == 0){
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = GOALSSCORED + " + scoreHome.text.toString() + ", GOALSRECEIVED = GOALSRECEIVED + " + scoreHost.text.toString() + ", POINTS = POINTS + " + 3 + " WHERE TEAMNAME = '" + domaci +"'")
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = GOALSSCORED + " + scoreHost.text.toString() + ", GOALSRECEIVED = GOALSRECEIVED + " + scoreHome.text.toString() + ", POINTS = POINTS + " + 0 + " WHERE TEAMNAME = '" + hoste +"'")
                        }
                        else{
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = " + scoreHome.text.toString() + ", GOALSRECEIVED = " + scoreHost.text.toString() + ", POINTS = " + 3 + " WHERE TEAMNAME = '" + domaci +"'")
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = " + scoreHost.text.toString() + ", GOALSRECEIVED = " + scoreHome.text.toString() + ", POINTS = " + 0 + " WHERE TEAMNAME = '" + hoste +"'")
                        }
                    }
                    else{
                        if(ulozeno == 0){
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = GOALSSCORED + " + scoreHome.text.toString() + ", GOALSRECEIVED = GOALSRECEIVED + " + scoreHost.text.toString() + ", POINTS = POINTS + " + 0 + " WHERE TEAMNAME = '" + domaci +"'")
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = GOALSSCORED + " + scoreHost.text.toString() + ", GOALSRECEIVED = GOALSRECEIVED + " + scoreHome.text.toString() + ", POINTS = POINTS + " + 3 + " WHERE TEAMNAME = '" + hoste +"'")
                        }
                        else{
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = " + scoreHome.text.toString() + ", GOALSRECEIVED = " + scoreHost.text.toString() + ", POINTS = " + 0 + " WHERE TEAMNAME = '" + domaci +"'")
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = " + scoreHost.text.toString() + ", GOALSRECEIVED = " + scoreHome.text.toString() + ", POINTS = " + 3 + " WHERE TEAMNAME = '" + hoste +"'")
                        }
                    }
                    db?.execSQL("UPDATE MATCHES SET HOMESCORE = " + scoreHome.text.toString() + ", HOSTSCORE = " + scoreHost.text.toString() + " WHERE MATCHID = " + idzapasu)
                }
                else{
                    Toast.makeText(this, "Neuložili jste výsledek. :/", Toast.LENGTH_SHORT).show()

                }
                Toast.makeText(this, "Skóre uloženo do databáze.", Toast.LENGTH_SHORT).show()
                ulozenoVtetoAkci = 1
            }
            else{
                Toast.makeText(this, "Skóre jste již uložili, pokud ho chcete upravit, načtěte zápas znovu.", Toast.LENGTH_SHORT).show()
            }
        }
        val StrikerList:ArrayList<StrikerClass> = ArrayList<StrikerClass>()
        addStrikerHome.setOnClickListener {
            val striker = StrikerClass(name = ethomestriker.text.toString(), team = domaci.toString(), goals = etgoalshomestriker.text.toString(), matchid = idzapasu.toString())
            StrikerList.add(striker)
            findViewById<TextView>(R.id.tvdomacistrelci).setText(findViewById<TextView>(R.id.tvdomacistrelci).text.toString() + ethomestriker.text.toString() + " - " + etgoalshomestriker.text.toString() + "\n")
            findViewById<EditText>(R.id.ethomestriker).setText("")
            findViewById<EditText>(R.id.etgoalshomestriker).setText("")
        }
        addStrikerHost.setOnClickListener {
            val striker = StrikerClass(name = ethoststriker.text.toString(), team = hoste.toString(), goals = etgoalshoststriker.text.toString(), matchid = idzapasu.toString())
            StrikerList.add(striker)
            findViewById<TextView>(R.id.tvhostestrelci).setText(findViewById<TextView>(R.id.tvhostestrelci).text.toString() + ethoststriker.text.toString() + " - " + etgoalshoststriker.text.toString() + "\n")
            findViewById<EditText>(R.id.ethoststriker).setText("")
            findViewById<EditText>(R.id.etgoalshoststriker).setText("")
        }
        btnulozitstrelce.setOnClickListener {
            for (striker in StrikerList){
                db?.execSQL("INSERT INTO STRIKERS(NAME,TEAM,GOALS,MATCHID) VALUES('"+ striker.name + "','"+striker.team+"','"+striker.goals+"','"+striker.matchid+"')")
            }
            StrikerList.clear()
            findViewById<EditText>(R.id.ethomestriker).setText("")
            findViewById<EditText>(R.id.etgoalshomestriker).setText("")
            findViewById<TextView>(R.id.tvdomacistrelci).setText("")
            findViewById<EditText>(R.id.ethoststriker).setText("")
            findViewById<EditText>(R.id.etgoalshoststriker).setText("")
            findViewById<TextView>(R.id.tvhostestrelci).setText("")
            Toast.makeText(this, "Střelci byli uloženi. Při chybném(nebo dvojitém opětovném) uložení lze uložení vše vynulovat tlačítkem SMAZAT STŘELCE", Toast.LENGTH_SHORT).show()
        }
        btnsmazat.setOnClickListener {
            Toast.makeText(this, "Všichni uložení střelci tohoto zápasu byli vynulování.", Toast.LENGTH_SHORT).show()
            StrikerList.clear()
            findViewById<EditText>(R.id.ethomestriker).setText("")
            findViewById<EditText>(R.id.etgoalshomestriker).setText("")
            findViewById<TextView>(R.id.tvdomacistrelci).setText("")
            findViewById<EditText>(R.id.ethoststriker).setText("")
            findViewById<EditText>(R.id.etgoalshoststriker).setText("")
            findViewById<TextView>(R.id.tvhostestrelci).setText("")
            db?.execSQL("DELETE FROM STRIKERS WHERE MATCHID = "+ idzapasu)
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