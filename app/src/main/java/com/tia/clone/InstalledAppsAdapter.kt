package com.tia.clone

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InstalledAppsAdapter(
    private val apps: List<ApplicationInfo>,
    private val onClone: (ApplicationInfo) -> Unit
) : RecyclerView.Adapter<InstalledAppsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.app_icon)
        val name: TextView = view.findViewById(R.id.app_name)
        val cloneButton: Button = view.findViewById(R.id.clone_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appInfo = apps[position]
        val pm = holder.itemView.context.packageManager
        holder.icon.setImageDrawable(pm.getApplicationIcon(appInfo))
        holder.name.text = pm.getApplicationLabel(appInfo)
        holder.cloneButton.setOnClickListener { onClone(appInfo) }
    }

    override fun getItemCount(): Int = apps.size
}
