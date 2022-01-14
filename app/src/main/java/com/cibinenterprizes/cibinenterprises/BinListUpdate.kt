package com.cibinenterprizes.cibinenterprises

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibinenterprizes.cibinenterprises.Model.BinDetails
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_bin_list_update.*

class BinListUpdate : AppCompatActivity() {

    private lateinit var recview: RecyclerView
    val idAuth = FirebaseAuth.getInstance().currentUser?.uid.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bin_list_update)

        recview = findViewById(R.id.bin_list_update_recyclerview)
        recview.setLayoutManager(LinearLayoutManager(this))

        bin_list_update_back_botton.setOnClickListener {
            finish()
        }
    }
    override fun onStart() {
        super.onStart()
        val options: FirebaseRecyclerOptions<BinDetails> = FirebaseRecyclerOptions.Builder<BinDetails>()
            .setQuery(FirebaseDatabase.getInstance().getReference()
                .child("User Details").child(idAuth).child("BINS"), BinDetails::class.java).build()

        val adapter: FirebaseRecyclerAdapter<BinDetails, myviewholder> =object: FirebaseRecyclerAdapter<BinDetails, myviewholder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewholder {
                val view: View = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bin_update, parent, false)
                return myviewholder(view)
            }

            override fun onBindViewHolder(holder: myviewholder, position: Int, model: BinDetails) {
                holder.area?.setText(model.Area_Village)
                holder.binId?.setText(model.BinId.toString())
                holder.locality?.setText(model.Locality)
                holder.city?.setText(model.District)
                holder.loadType?.setText(model.LoadType)
                holder.collectionPeriod?.setText(model.CollectionPeriod)
                holder.mapLantitude?.setText(model.Lantitude)
                holder.mapLongitude?.setText(model.Longitude)
                holder.verificationStatus?.setText(model.Verification)

                holder.itemView.setOnClickListener {
                    val binId = getRef(position).getKey()
                    val intent = Intent(this@BinListUpdate,UpdateBin::class.java)
                    intent.putExtra("Bin ID",binId)
                    startActivity(intent)
                    finish()
                }
            }

        }
        recview.setAdapter(adapter)
        adapter.startListening()
    }
    class myviewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var area: TextView? = null
        var binId: TextView? = null
        var locality: TextView? = null
        var city: TextView? = null
        var loadType: TextView? = null
        var collectionPeriod: TextView? = null
        var mapLantitude: TextView? = null
        var mapLongitude: TextView? = null
        var verificationStatus: TextView? = null

        init {
            area= itemView.findViewById(R.id.bin_list_update_area)
            binId= itemView.findViewById(R.id.bin_list_update_bin_id)
            locality= itemView.findViewById(R.id.bin_list_update_locality)
            city= itemView.findViewById(R.id.bin_list_update_city)
            loadType= itemView.findViewById(R.id.bin_list_update_load_type)
            collectionPeriod= itemView.findViewById(R.id.bin_list_update_collection_period)
            mapLantitude= itemView.findViewById(R.id.bin_list_update_lantitude)
            mapLongitude= itemView.findViewById(R.id.bin_list_update_longitude)
            verificationStatus= itemView.findViewById(R.id.bin_list_update_verification_status)
            super.itemView
        }
    }
}