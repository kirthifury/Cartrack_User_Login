package com.cartrack.mobile.codingchallenge.user.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.cartrack.mobile.codingchallenge.R
import com.cartrack.mobile.codingchallenge.user.model.usersItem
import com.google.gson.Gson


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [UserListActivity]
 * in two-pane mode (on tablets) or a [UserDetailActivity]
 * on handsets.
 */
class UserDetailFragment : Fragment(), View.OnClickListener {

    private var item: usersItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                item = Gson().fromJson(it.getString(ARG_ITEM_ID), usersItem::class.java)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)

        item?.let {
            val phoneNumber = rootView.findViewById<TextView>(R.id.phone_number)
            phoneNumber.text = it.phone
            val userEmail = rootView.findViewById<TextView>(R.id.user_email)
            userEmail.text = it.email
            val userAddress = rootView.findViewById<TextView>(R.id.user_address)
            userAddress.text =
                "${it.address.suite}, ${it.address.street}, ${it.address.city} - ${it.address.zipcode}"
            val workDetails = rootView.findViewById<TextView>(R.id.work_details)
            workDetails.text = it.company.name
            val websiteDetails = rootView.findViewById<TextView>(R.id.website_details)
            websiteDetails.text = it.website
            userAddress.setOnClickListener(this)
            phoneNumber.setOnClickListener(this)
            userEmail.setOnClickListener(this)
            websiteDetails.setOnClickListener(this)
            rootView.findViewById<ImageView>(R.id.user_location_icon).setOnClickListener(this)
        }

        return rootView
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.user_address, R.id.user_location_icon -> {
                item?.let {
                    val intent = Intent(v.context, MapsActivity::class.java).apply {
                        putExtra(MapsActivity.LAT_VAL, it.address.geo.lat.toDouble())
                        putExtra(MapsActivity.LONG_VAL, it.address.geo.lng.toDouble())
                    }
                    v.context.startActivity(intent)
                }
            }
            R.id.phone_number -> {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:${(v as TextView).text}")
                startActivity(callIntent)
            }
            R.id.user_email -> {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_EMAIL, "${(v as TextView).text}")
                intent.putExtra(Intent.EXTRA_SUBJECT, "Reg:")
                startActivity(Intent.createChooser(intent, "Send Email"))
            }
            R.id.website_details -> {
                val browserIntent = Intent(Intent.ACTION_VIEW)
                browserIntent.data = Uri.parse("${(v as TextView).text}")
                startActivity(browserIntent)
            }

        }
    }
}