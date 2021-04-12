package com.cartrack.mobile.codingchallenge.user.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.cartrack.mobile.codingchallenge.CartrackApplication
import com.cartrack.mobile.codingchallenge.R
import com.cartrack.mobile.codingchallenge.user.model.usersItem
import com.cartrack.mobile.codingchallenge.user.viewmodel.UserViewModel
import com.cartrack.mobile.codingchallenge.user.viewmodel.UserViewModelFactory
import com.google.gson.Gson

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [UserDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class UserListActivity : AppCompatActivity() {

    private var twoPane: Boolean = false
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory((application as CartrackApplication).userRepository)
        )
            .get(UserViewModel::class.java)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            twoPane = true
        }
        userViewModel.getUsers()
        userViewModel.userListState.observe(this@UserListActivity, Observer {
            val users = it ?: return@Observer
            setupRecyclerView(findViewById(R.id.item_list), users)

        })
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, users: MutableList<usersItem>) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, users, twoPane)
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: UserListActivity,
        private val values: List<usersItem>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
            val item = v.tag as usersItem
            if (twoPane) {
                val fragment = UserDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(UserDetailFragment.ARG_ITEM_ID, Gson().toJson(item).toString())
                    }
                }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(v.context, UserDetailActivity::class.java).apply {
                    putExtra(UserDetailFragment.ARG_ITEM_ID, Gson().toJson(item).toString())
                    putExtra(UserDetailActivity.ARG_TITLE, item.name)
                }
                v.context.startActivity(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.name
            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.findViewById(R.id.id_text)
            val contentView: TextView = view.findViewById(R.id.content)
        }
    }
}