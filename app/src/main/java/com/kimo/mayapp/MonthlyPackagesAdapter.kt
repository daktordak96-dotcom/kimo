package com.kimo.mayapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MonthlyPackagesAdapter(
    private val packagesList: List<MonthlyPackage>,
    private val onActionClick: (phone: String, code: String) -> Unit
) : RecyclerView.Adapter<MonthlyPackagesAdapter.PackageViewHolder>() {

    class PackageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.txtPackageTitle)
        val price: TextView = itemView.findViewById(R.id.txtPackagePrice)
        val desc: TextView = itemView.findViewById(R.id.txtPackageDesc)
        val btnActivate: Button = itemView.findViewById(R.id.btnActivate)
        val btnCheck: Button = itemView.findViewById(R.id.btnCheck)
        val btnDeactivate: Button = itemView.findViewById(R.id.btnDeactivate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_package_card, parent, false)
        return PackageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {
        val pkg = packagesList[position]
        holder.title.text = pkg.title
        holder.price.text = "${pkg.priceAfghani} افغانی"
        holder.desc.text = "${pkg.description}\n\nبرای فعال‌سازی ${pkg.activationCode} به 3378 ارسال نمایید.\nبرای بررسی موجودی ${pkg.checkBalanceCode} را به 606 ارسال نمایید."

        holder.btnActivate.setOnClickListener {
            onActionClick("3378", pkg.activationCode)
        }
        holder.btnCheck.setOnClickListener {
            onActionClick("606", pkg.checkBalanceCode)
        }
        holder.btnDeactivate.setOnClickListener {
            onActionClick("3378", pkg.deactivationCode)
        }
    }

    override fun getItemCount(): Int = packagesList.size
}
