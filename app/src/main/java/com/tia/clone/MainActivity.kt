package com.tia.clone

import android.Manifest
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Ecran principal : liste les applications installees sur l'appareil.
 *
 * IMPORTANT : ce projet ne contourne AUCUNE permission Android. Chaque acces
 * sensible (GPS, camera, contacts...) demande par une application clonee passe
 * par le dialogue systeme standard (ActivityCompat.requestPermissions), exactement
 * comme pour n'importe quelle app installee normalement. L'utilisateur voit et
 * valide chaque autorisation lui-meme.
 */
class MainActivity : AppCompatActivity() {

    private val locationRequestCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.app_list)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = InstalledAppsAdapter(getUserApps()) { appInfo ->
            onCloneRequested(appInfo)
        }
    }

    /** Recupere la liste des apps utilisateur (hors apps systeme). */
    private fun getUserApps(): List<ApplicationInfo> {
        val pm = packageManager
        return pm.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }
            .sortedBy { pm.getApplicationLabel(it).toString().lowercase() }
    }

    /**
     * Point d'entree du clonage d'une app : cree un conteneur isole (sandbox)
     * pour une 2e instance. Le moteur de virtualisation est a brancher ici
     * (ex: fork de VirtualApp). Chaque permission que l'app clonee demandera
     * ensuite (GPS, camera...) passera par les methodes ci-dessous, jamais
     * par un contournement.
     */
    private fun onCloneRequested(appInfo: ApplicationInfo) {
        val label = packageManager.getApplicationLabel(appInfo)
        Toast.makeText(this, "Clonage de $label (demo)", Toast.LENGTH_SHORT).show()
        // TODO: brancher le moteur de virtualisation (conteneur + classloader isole)
    }

    /** Exemple : demande d'acces GPS via le dialogue systeme standard. */
    private fun requestLocationIfNeeded() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationRequestCode
            )
        }
    }
}
