package com.example.ukol_mika

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog

class TournamentHelper {
    companion object {
        fun endTournament(context: Context) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Přejete si ukončit turnaj?")
            builder.setMessage("Odsohlasením ukončíte turnaj a smažete všechny záznamy.")
            
            var helper = MyDBHelper(context)
            var db = helper.readableDatabase

            builder.setPositiveButton("Ano") { _, _ ->
                // Execute the deletion of all relevant data from the database
                db?.execSQL("DELETE FROM MATCHES")
                db?.execSQL("DELETE FROM TEAMS")
                db?.execSQL("DELETE FROM STRIKERS")
                db?.execSQL("DELETE FROM TREATMENTS")

                // Start the MainActivity after deletion
                val mainActIntent = Intent(context, MainActivity::class.java)
                context.startActivity(mainActIntent)
                if (context is DruhyPohled) {
                    context.finish()
                }
            }

            builder.setNeutralButton("Ne") { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
    }
}
