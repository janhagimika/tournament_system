package com.example.ukol_mika

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ukol_mika.databinding.ActivityMainBinding
import example.javatpoint.com.kotlinsqlitecrud.MatchClass
import example.javatpoint.com.kotlinsqlitecrud.TeamClass
import example.javatpoint.com.kotlinsqlitecrud.TreatmentClass

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //val navHostFragment = supportFragmentManager.findFragmentById(androidx.navigation.fragment.R.id.nav_host_fragment_container) as NavHostFragment
        //val navController = navHostFragment.navController

        val prvniPohled = Intent(this, PrvniPohled::class.java)
        val txtteam = findViewById<EditText>(R.id.teamname)
        val dbHandler: MyDBHelper= MyDBHelper(this)
        var helper = MyDBHelper(applicationContext)
        var db = helper.readableDatabase

        val treatments: List<TreatmentClass> = dbHandler.getTreatment(db)
        for (treatment in treatments){
            if (treatment.running == 1){
                startActivity(prvniPohled)
                finish()
            }
        }

        binding.btnsaveteam.setOnClickListener {
            if (txtteam.text.toString() != ""){
                db?.execSQL("INSERT INTO TEAMS(TEAMNAME,GOALSSCORED,GOALSRECEIVED,POINTS) VALUES('" + txtteam.text.toString() +"','0','0','0')")
                Toast.makeText(this, "Tým s názvem " + txtteam.text + " byl založen.", Toast.LENGTH_LONG).show()
                txtteam.setText("")
            }
        }

        binding.btnresetteams.setOnClickListener {
            db?.execSQL("DELETE FROM TEAMS")
            Toast.makeText(this, "Všechny týmy byly smazány.", Toast.LENGTH_LONG).show()
            txtteam.setText("")
        }

        binding.start.setOnClickListener{
            val teams: List<TeamClass> = dbHandler.getAllTeams(db)
            val MatchList:ArrayList<MatchClass> = ArrayList<MatchClass>()
            var arrteams = Array<String?>(teams.size) { null }
            if(teams.size < 3){
                Toast.makeText(this, "Založte alespoň 3 týmy.", Toast.LENGTH_LONG).show()
            }
            else{
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
                }
                for (round: Int in 0 until arrteams.size - 1) {
                    var posledni = arrteams.size-1
                    //projizdim a kdyz se nerovna tym co je navic, tak pripisuju zapas do listu
                    for (i:Int in 0 until arrteams.size/2){
                        if(arrteams[i] != "TymNavicSudy" && arrteams[posledni] != "TymNavicSudy"){
                            val match= MatchClass(matchId = "", home = arrteams[i].toString(), homeScore = "", host = arrteams[posledni].toString(), hostScore = "")
                            MatchList.add(match)
                        }
                        posledni--
                    }
                    //zde je protočení týmů na pozicích pole, pouze pozice 0 zůstává
                    var last: String = arrteams[arrteams.size-1].toString()
                    for (i in arrteams.size-1 downTo 2 step 1) {
                        arrteams[i]=arrteams[i-1]
                    }
                    arrteams[1] = last
                }
                for (match in MatchList){
                    db?.execSQL("INSERT INTO MATCHES(HOME,HOMESCORE, HOST, HOSTSCORE) VALUES('"+ match.home + "','','"+match.host+"','')")
                }
                db?.execSQL("INSERT INTO TREATMENTS(RUNNING) VALUES('"+1+"')")
                startActivity(prvniPohled)
                finish()
            }
        }
    }
}