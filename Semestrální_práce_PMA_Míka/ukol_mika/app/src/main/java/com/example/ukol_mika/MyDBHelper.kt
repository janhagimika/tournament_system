package com.example.ukol_mika

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getStringOrNull
import example.javatpoint.com.kotlinsqlitecrud.MatchClass
import example.javatpoint.com.kotlinsqlitecrud.StrikerClass
import example.javatpoint.com.kotlinsqlitecrud.TeamClass
import example.javatpoint.com.kotlinsqlitecrud.TreatmentClass

class MyDBHelper(context: Context) : SQLiteOpenHelper(context, "TOURNAMENTDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS MATCHES(MATCHID INTEGER PRIMARY KEY AUTOINCREMENT, HOME TEXT, HOMESCORE TEXT, HOST TEXT, HOSTSCORE TEXT)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS TEAMS(TEAMID INTEGER PRIMARY KEY AUTOINCREMENT, TEAMNAME TEXT, GOALSSCORED TEXT, GOALSRECEIVED TEXT, POINTS TEXT)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS STRIKERS(STRIKERID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, TEAM TEXT, GOALS TEXT, MATCHID INTEGER, FOREIGN KEY(MATCHID) REFERENCES MATCHES (MATCHID))")
        db?.execSQL("CREATE TABLE IF NOT EXISTS TREATMENTS(TREATMENTID INTEGER PRIMARY KEY AUTOINCREMENT, RUNNING INTEGER)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
    @SuppressLint("Range")
    fun getStrikers(db: SQLiteDatabase?, query: String): ArrayList<StrikerClass>{
        db?.execSQL("CREATE TABLE IF NOT EXISTS STRIKERS(STRIKERID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, TEAM TEXT, GOALS TEXT, MATCHID INTEGER, FOREIGN KEY(MATCHID) REFERENCES MATCHES (MATCHID))")
        val strikerList:ArrayList<StrikerClass> = ArrayList<StrikerClass>()
        //db?.execSQL("CREATE TABLE IF NOT EXISTS STRIKERS(STRIKERID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, TEAM TEXT, GOALS TEXT, MATCHID INTEGER, FOREIGN KEY(MATCHID) REFERENCES MATCHES(MATCHID))")
        var name: String
        var team: String
        var goals: String
        var matchid: String
        var cursor: Cursor? = null
        try {
            cursor = db?.rawQuery(query, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    name = cursor.getString(cursor.getColumnIndex("NAME"))
                    team = cursor.getString(cursor.getColumnIndex("TEAM"))
                    goals = cursor.getString(cursor.getColumnIndex("GOALS"))
                    matchid = cursor.getString(cursor.getColumnIndex("MATCHID"))
                    val striker = StrikerClass(name = name, team = team, goals = goals, matchid = matchid)
                    strikerList.add(striker)
                }
            }
            return strikerList
        }
        catch (e: SQLiteException) {
            db?.execSQL(query)
            return ArrayList()
        }
    }

    @SuppressLint("Range")
    fun getAllTeams(db: SQLiteDatabase?): ArrayList<TeamClass> {
        db?.execSQL("CREATE TABLE IF NOT EXISTS TEAMS(TEAMID INTEGER PRIMARY KEY AUTOINCREMENT, TEAMNAME TEXT, GOALSSCORED TEXT, GOALSRECEIVED TEXT, POINTS TEXT)")
        val teamList:ArrayList<TeamClass> = ArrayList<TeamClass>()
        val selectQuery = "SELECT * FROM TEAMS ORDER BY POINTS DESC"
        var cursor: Cursor? = null
        var name: String
        var goals: String
        var received: String
        var points: String
        var scheduleid: Int = 0
        try {
            cursor = db?.rawQuery(selectQuery, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    name = cursor.getString(cursor.getColumnIndex("TEAMNAME"))
                    goals = cursor.getString(cursor.getColumnIndex("GOALSSCORED"))
                    received = cursor.getString(cursor.getColumnIndex("GOALSRECEIVED"))
                    points = cursor.getString(cursor.getColumnIndex("POINTS"))
                    val team= TeamClass(name = name, goals = goals, received = received, points = points, scheduleid = scheduleid)
                    teamList.add(team)
                    scheduleid++
                }
            }
            return teamList
        }
        catch (e: SQLiteException) {
            db?.execSQL(selectQuery)
            return ArrayList()
        }
    }

    @SuppressLint("Range")
    fun getAllMatches(db: SQLiteDatabase?): ArrayList<MatchClass> {
        db?.execSQL("CREATE TABLE IF NOT EXISTS MATCHES(MATCHID INTEGER PRIMARY KEY AUTOINCREMENT, HOME TEXT, HOMESCORE TEXT, HOST TEXT, HOSTSCORE TEXT)")
        val matchList:ArrayList<MatchClass> = ArrayList<MatchClass>()
        val selectQuery = "SELECT * FROM MATCHES"
        var cursor: Cursor? = null
        var mId: String
        var home: String
        var homescore: String
        var host: String
        var hostscore: String
        try {
            cursor = db?.rawQuery(selectQuery, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    mId = cursor.getString(cursor.getColumnIndex("MATCHID"))
                    home = cursor.getString(cursor.getColumnIndex("HOME"))
                    homescore = cursor.getStringOrNull(cursor.getColumnIndex("HOMESCORE")).toString()
                    host = cursor.getString(cursor.getColumnIndex("HOST"))
                    hostscore = cursor.getStringOrNull(cursor.getColumnIndex("HOSTSCORE")).toString()
                    val match= MatchClass(matchId = mId, home = home, homeScore = homescore, host = host, hostScore = hostscore)
                    matchList.add(match)
                }
            }
            return matchList
        }
        catch (e: SQLiteException) {
            db?.execSQL(selectQuery)
            return ArrayList()
        }
    }
    @SuppressLint("Range")
    fun getTreatment(db: SQLiteDatabase?): ArrayList<TreatmentClass> {
        db?.execSQL("CREATE TABLE IF NOT EXISTS TREATMENTS(TREATMENTID INTEGER PRIMARY KEY AUTOINCREMENT, RUNNING INTEGER)")
        val treatmentList:ArrayList<TreatmentClass> = ArrayList<TreatmentClass>()
        var running: Int
        var cursor: Cursor? = null
        val query = "SELECT * FROM TREATMENTS"
        try {
            cursor = db?.rawQuery(query, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    running = cursor.getInt(cursor.getColumnIndex("RUNNING"))
                    val treatment = TreatmentClass(running = running)
                    treatmentList.add(treatment)
                }
            }
            return treatmentList
        }
        catch (e: SQLiteException) {
            db?.execSQL(query)
            return ArrayList()
        }
    }
}
