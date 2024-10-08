package com.example.ukol_mika

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ukol_mika.databinding.ZapasUpravaBinding
import example.javatpoint.com.kotlinsqlitecrud.StrikerClass

private lateinit var binding: ZapasUpravaBinding
class UpravaZapasu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ZapasUpravaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val PrvniPohled = Intent(this, PrvniPohled::class.java)
        val DruhyPohled = Intent(this, DruhyPohled::class.java)
        val TretiPohled = Intent(this, TretiPohled::class.java)
       // bottomNavigationView.setSelectedItemId(R.id.zapasy)
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
        val StrikerList:ArrayList<StrikerClass> = ArrayList<StrikerClass>()
        var ulozeno = 0
        var ulozenoVtetoAkci = 0
        if(golydomaci != "" || golyhoste != ""){
            ulozeno = 1
        }

        val queryHome = "SELECT NAME, TEAM, GOALS, MATCHID FROM STRIKERS WHERE MATCHID='"+idzapasu.toString()+"' AND TEAM = '"+domaci.toString()+"'"
        val strikersHome: List<StrikerClass> = dbHandler.getStrikers(db,queryHome)
        val queryHost = "SELECT NAME, TEAM, GOALS, MATCHID FROM STRIKERS WHERE MATCHID='"+idzapasu.toString()+"' AND TEAM = '"+hoste.toString()+"'"
        val strikersHost: List<StrikerClass> = dbHandler.getStrikers(db,queryHost)
        var outcomeHome: String = ""
        var outcomeHost: String = ""
        for (strelec in strikersHome){
            outcomeHome = outcomeHome + strelec.name + " - " + strelec.goals + "\n"
        }
        findViewById<TextView>(R.id.tvdomacistrelci).setText(outcomeHome)

        for (strelec in strikersHost){
            outcomeHost = outcomeHost + strelec.name + " - " + strelec.goals + "\n"
        }
        findViewById<TextView>(R.id.tvhostestrelci).setText(outcomeHost)

        binding.btnUlozitZapas.setOnClickListener {
            if(ulozenoVtetoAkci == 0){
                if(domSkore.text.toString() != "" && hosteSkore.text.toString() != ""){
                    val dskore = Integer.parseInt(domSkore.text.toString())
                    val hskore = Integer.parseInt(hosteSkore.text.toString())
                    if(dskore == hskore){
                        if(ulozeno == 0){
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = GOALSSCORED + " + binding.scoreHome.text.toString() + ", GOALSRECEIVED = GOALSRECEIVED + " + binding.scoreHost.text.toString() + ", POINTS = POINTS + " + 1 + " WHERE TEAMNAME = '" + domaci +"'")
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = GOALSSCORED + " + binding.scoreHost.text.toString() + ", GOALSRECEIVED = GOALSRECEIVED + " + binding.scoreHome.text.toString() + ", POINTS = POINTS + " + 1 + " WHERE TEAMNAME = '" + hoste +"'")
                        }
                        else{
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = " + binding.scoreHome.text.toString() + ", GOALSRECEIVED = " + binding.scoreHost.text.toString() + ", POINTS = " + 1 + " WHERE TEAMNAME = '" + domaci +"'")
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = " + binding.scoreHost.text.toString() + ", GOALSRECEIVED = " + binding.scoreHome.text.toString() + ", POINTS = " + 1 + " WHERE TEAMNAME = '" + hoste +"'")
                        }
                    }
                    else if(dskore > hskore){
                        if(ulozeno == 0){
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = GOALSSCORED + " + binding.scoreHome.text.toString() + ", GOALSRECEIVED = GOALSRECEIVED + " + binding.scoreHost.text.toString() + ", POINTS = POINTS + " + 3 + " WHERE TEAMNAME = '" + domaci +"'")
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = GOALSSCORED + " + binding.scoreHost.text.toString() + ", GOALSRECEIVED = GOALSRECEIVED + " + binding.scoreHome.text.toString() + ", POINTS = POINTS + " + 0 + " WHERE TEAMNAME = '" + hoste +"'")
                        }
                        else{
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = " + binding.scoreHome.text.toString() + ", GOALSRECEIVED = " + binding.scoreHost.text.toString() + ", POINTS = " + 3 + " WHERE TEAMNAME = '" + domaci +"'")
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = " + binding.scoreHost.text.toString() + ", GOALSRECEIVED = " + binding.scoreHome.text.toString() + ", POINTS = " + 0 + " WHERE TEAMNAME = '" + hoste +"'")
                        }
                    }
                    else{
                        if(ulozeno == 0){
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = GOALSSCORED + " + binding.scoreHome.text.toString() + ", GOALSRECEIVED = GOALSRECEIVED + " + binding.scoreHost.text.toString() + ", POINTS = POINTS + " + 0 + " WHERE TEAMNAME = '" + domaci +"'")
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = GOALSSCORED + " + binding.scoreHost.text.toString() + ", GOALSRECEIVED = GOALSRECEIVED + " + binding.scoreHome.text.toString() + ", POINTS = POINTS + " + 3 + " WHERE TEAMNAME = '" + hoste +"'")
                        }
                        else{
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = " + binding.scoreHome.text.toString() + ", GOALSRECEIVED = " + binding.scoreHost.text.toString() + ", POINTS = " + 0 + " WHERE TEAMNAME = '" + domaci +"'")
                            db?.execSQL("UPDATE TEAMS SET GOALSSCORED = " + binding.scoreHost.text.toString() + ", GOALSRECEIVED = " + binding.scoreHome.text.toString() + ", POINTS = " + 3 + " WHERE TEAMNAME = '" + hoste +"'")
                        }
                    }
                    db?.execSQL("UPDATE MATCHES SET HOMESCORE = " + binding.scoreHome.text.toString() + ", HOSTSCORE = " + binding.scoreHost.text.toString() + " WHERE MATCHID = " + idzapasu)
                }
                else{
                    Toast.makeText(this, "Neuložili jste výsledek. :/", Toast.LENGTH_LONG).show()

                }
                Toast.makeText(this, "Skóre uloženo do databáze.", Toast.LENGTH_LONG).show()
                ulozenoVtetoAkci = 1
            }
            else{
                Toast.makeText(this, "Skóre jste již uložili, pokud ho chcete upravit, načtěte zápas znovu.", Toast.LENGTH_LONG).show()
            }
        }
        binding.addStrikerHome.setOnClickListener {
            db?.execSQL("INSERT INTO STRIKERS(NAME,TEAM,GOALS,MATCHID) VALUES('"+ binding.ethomestriker.text.toString() + "','"+domaci.toString()+"','"+binding.etgoalshomestriker.text.toString()+"','"+idzapasu.toString()+"')")
            val strikersHome: List<StrikerClass> = dbHandler.getStrikers(db,queryHome)
            outcomeHome = ""
            var countOfGoals: Int = 0
            for (strelec in strikersHome){
                outcomeHome += strelec.name + " - " + strelec.goals + "\n"
                countOfGoals += strelec.goals.toInt()
            }
            findViewById<TextView>(R.id.tvdomacistrelci).setText(outcomeHome)
            findViewById<EditText>(R.id.ethomestriker).setText("")
            findViewById<EditText>(R.id.etgoalshomestriker).setText("")
            var dskore: Int
            if(binding.scoreHome.text.toString() == ""){
                dskore = 0
            }
            else{
                dskore = binding.scoreHome.text.toString().toInt()
            }
            if (dskore == countOfGoals){
                Toast.makeText(this, "Góly domácích střelců odpovídají počtu gólů. :)", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this, "Góly domácích střelců neodpovídají počtu gólů. :/", Toast.LENGTH_LONG).show()
            }

        }
        binding.addStrikerHost.setOnClickListener {
            db?.execSQL("INSERT INTO STRIKERS(NAME,TEAM,GOALS,MATCHID) VALUES('"+ binding.ethoststriker.text.toString() + "','"+hoste.toString()+"','"+binding.etgoalshoststriker.text.toString()+"','"+idzapasu.toString()+"')")
            val strikersHost: List<StrikerClass> = dbHandler.getStrikers(db,queryHost)
            outcomeHost = ""
            var countOfGoals: Int = 0
            for (strelec in strikersHost){
                outcomeHost = outcomeHost + strelec.name + " - " + strelec.goals + "\n"
                countOfGoals += strelec.goals.toInt()
            }
            findViewById<TextView>(R.id.tvhostestrelci).setText(outcomeHost)
            findViewById<EditText>(R.id.ethoststriker).setText("")
            findViewById<EditText>(R.id.etgoalshoststriker).setText("")
            var hskore: Int
            if(binding.scoreHost.text.toString() == ""){
                hskore = 0
            }
            else{
                hskore = binding.scoreHost.text.toString().toInt()
            }
            if (hskore == countOfGoals){
                Toast.makeText(this, "Góly hostujících střelců odpovídají počtu gólů. :)", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this, "Góly hostujících střelců neodpovídají počtu gólů. :/", Toast.LENGTH_LONG).show()
            }
        }
        binding.btnsmazat.setOnClickListener {
            Toast.makeText(this, "Všichni uložení střelci tohoto zápasu byli vynulování.", Toast.LENGTH_LONG).show()
            StrikerList.clear()
            findViewById<EditText>(R.id.ethomestriker).setText("")
            findViewById<EditText>(R.id.etgoalshomestriker).setText("")
            findViewById<TextView>(R.id.tvdomacistrelci).setText("")
            findViewById<EditText>(R.id.ethoststriker).setText("")
            findViewById<EditText>(R.id.etgoalshoststriker).setText("")
            findViewById<TextView>(R.id.tvhostestrelci).setText("")
            db?.execSQL("DELETE FROM STRIKERS WHERE MATCHID = "+ idzapasu)
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