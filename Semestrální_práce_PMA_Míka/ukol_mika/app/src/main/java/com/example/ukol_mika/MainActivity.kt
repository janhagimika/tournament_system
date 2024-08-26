package com.example.ukol_mika

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import example.javatpoint.com.kotlinsqlitecrud.MatchClass
import example.javatpoint.com.kotlinsqlitecrud.StrikerClass
import example.javatpoint.com.kotlinsqlitecrud.TeamClass
import example.javatpoint.com.kotlinsqlitecrud.TreatmentClass
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.zapas_uprava.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prvniPohled = Intent(this, PrvniPohled::class.java)
        val txtteam = findViewById<EditText>(R.id.teamname)
        val dbHandler: MyDBHelper= MyDBHelper(this)
        var helper = MyDBHelper(applicationContext)
        var db = helper.readableDatabase

        val treatments: List<TreatmentClass> = dbHandler.getTreatment(db)
        for (treatment in treatments){
            if (treatment.running == 1){
                startActivity(prvniPohled)
            }
        }

        btnsaveteam.setOnClickListener {
            if (txtteam.text.toString() != ""){
                db?.execSQL("INSERT INTO TEAMS(TEAMNAME,GOALSSCORED,GOALSRECEIVED,POINTS) VALUES('" + txtteam.text.toString() +"','0','0','0')")
                Toast.makeText(this, "Tým s názvem " + txtteam.text + " byl založen.", Toast.LENGTH_SHORT).show()
                txtteam.setText("")
            }
        }

        btnresetteams.setOnClickListener {
            db?.execSQL("DELETE FROM TEAMS")
            Toast.makeText(this, "Všechny týmy byly smazány.", Toast.LENGTH_SHORT).show()
            txtteam.setText("")
        }

        start.setOnClickListener{
            val teams: List<TeamClass> = dbHandler.getAllTeams(db)
            val MatchList:ArrayList<MatchClass> = ArrayList<MatchClass>()
            var arrteams = Array<String?>(teams.size) { null }
            for (i: Int in 0 until teams.size) {
                for (team in teams) {
                    if (team.scheduleid == i) {
                        arrteams[i] = team.name
                    }
                }
            }
            if(arrteams.size % 2 == 1){
                val list = arrteams.toMutableList()
                list.add("TymNavicSudy")
                arrteams = list.toTypedArray()
                Toast.makeText(this, "Licho", Toast.LENGTH_SHORT).show()
            }
            for (round: Int in 0 until arrteams.size - 1) {
                var odpocet = arrteams.size-1
                for (i:Int in 0 until arrteams.size/2){
                    if(arrteams[i] != "TymNavicSudy" && arrteams[odpocet] != "TymNavicSudy"){
                        val match= MatchClass(matchId = "", home = arrteams[i].toString(), homeScore = "", host = arrteams[odpocet].toString(), hostScore = "")
                        MatchList.add(match)
                    }
                    odpocet--
                }
                var last: String = arrteams[arrteams.size-1].toString()
                for (i in arrteams.size-1 downTo 2 step 1) {
                    arrteams[i]=arrteams[i-1]
                }
                arrteams[1] = last
            }
            for (match in MatchList){
                db?.execSQL("INSERT INTO MATCHES(HOME,HOMESCORE, HOST, HOSTSCORE) VALUES('"+ match.home + "','','"+match.host+"','')")
            }
            db?.execSQL("INSERT INTO TREATMENTS(RUNNING) VALUES('" + 1+"')")
            startActivity(prvniPohled)
        }

    }
}