package com.kimo.mayapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kimo.mayapp.R

class PackageAdapter(
    private val packages: List<PackageModel>,
    private val onButtonClick: (PackageModel, String) -> Unit
) : RecyclerView.Adapter<PackageAdapter.PackageViewHolder>() {

    class PackageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.txtPackageTitle)
        val price: TextView = view.findViewById(R.id.txtPackagePrice)
        val desc: TextView = view.findViewById(R.id.txtPackageDesc)
        val btnActivate: Button = view.findViewById(R.id.btnActivate)
        val btnCheck: Button = view.findViewById(R.id.btnCheck)
        val btnDeactivate: Button = view.findViewById(R.id.btnDeactivate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_package_card, parent, false)
        return PackageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {
        val pkg = packages[position]
        holder.title.text = pkg.title
        holder.price.text = pkg.price
        holder.desc.text = pkg.description

        holder.btnActivate.setOnClickListener {
            onButtonClick(pkg, "ACTIVATE")
        }

        holder.btnCheck.setOnClickListener {
            onButtonClick(pkg, "CHECK")
        }

        holder.btnDeactivate.setOnClickListener {
            onButtonClick(pkg, "DEACTIVATE")
        }
    }

    override fun getItemCount() = packages.size
}
