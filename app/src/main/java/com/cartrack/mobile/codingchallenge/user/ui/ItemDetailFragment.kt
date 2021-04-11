package com.cartrack.mobile.codingchallenge.user.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cartrack.mobile.codingchallenge.R
import com.cartrack.mobile.codingchallenge.user.model.usersItem
import com.google.gson.Gson


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
class ItemDetailFragment : Fragment(), View.OnClickListener {

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
            rootView.findViewById<TextView>(R.id.phone_number).text = it.phone
            rootView.findViewById<TextView>(R.id.user_email).text = it.email
            rootView.findViewById<TextView>(R.id.user_address).text =
                "${it.address.suite}, ${it.address.street}, ${it.address.city} - ${it.address.zipcode}"
        }

        return rootView
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.user_address, R.id.user_location_icon -> {
                item?.let {

                }
            }

        }
    }
}