package com.cibinenterprizes.cibinenterprises

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibinenterprizes.cibinenterprises.Model.ComplainDetails
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_my_complaints.*

class MyComplaints : AppCompatActivity() {

    private lateinit var recview: RecyclerView
    val idAuth = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_complaints)

        recview = findViewById(R.id.my_complaint_recyclerview)
        recview.setLayoutManager(LinearLayoutManager(this))

        my_complaints_back_botton.setOnClickListener {
            finish()
        }

    }
    override fun onStart() {
        super.onStart()
        val options: FirebaseRecyclerOptions<ComplainDetails> =
            FirebaseRecyclerOptions.Builder<ComplainDetails>().setQuery(FirebaseDatabase.getInstance().getReference()
                .child("User Details").child(idAuth).child("Complaints"), ComplainDetails::class.java).build()

        var adapter: FirebaseRecyclerAdapter<ComplainDetails, myviewholder> = object: FirebaseRecyclerAdapter<ComplainDetails, myviewholder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewholder {
                var view= LayoutInflater.from(parent.context).inflate(R.layout.my_complaint_view,parent,false)
                return myviewholder(view)
            }

            override fun onBindViewHolder(
                holder: myviewholder,
                position: Int,
                model: ComplainDetails
            ) {
                holder.email?.setText(model.EmailId)
                holder.area?.setText(model.Area)
                holder.binId?.setText(model.BinID)
                holder.complaint?.setText(model.CompaintDescription)
                holder.status?.setText(model.Status)
                holder.id?.setText(model.ComplaintId)
            }
        }
        recview.setAdapter(adapter)
        adapter.startListening()
    }
    class myviewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var email: TextView? = null
        var binId: TextView? = null
        var area: TextView? = null
        var complaint: TextView? = null
        var status: TextView? = null
        var id: TextView? = null

        init {
            email= itemView.findViewById(R.id.my_complaint_email)
            binId= itemView.findViewById(R.id.my_complaint_bin_id)
            area= itemView.findViewById(R.id.my_complaint_area)
            complaint= itemView.findViewById(R.id.my_complaint_complaints)
            status= itemView.findViewById(R.id.my_complaint_status)
            id= itemView.findViewById(R.id.my_complaint_id)
        }
    }

}