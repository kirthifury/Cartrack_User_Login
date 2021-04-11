package com.cartrack.mobile.codingchallenge.user.ui

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.cartrack.mobile.codingchallenge.R
import com.cartrack.mobile.codingchallenge.user.utils.AppBarStateChangeListener
import com.cartrack.mobile.codingchallenge.user.utils.convertDpToPixel
import com.cartrack.mobile.codingchallenge.user.utils.getDisplayMetrics
import com.cartrack.mobile.codingchallenge.user.utils.getTextWidth
import com.google.android.material.appbar.AppBarLayout
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ItemListActivity].
 */
class ItemDetailActivity : AppCompatActivity() {
    companion object {
        const val ARG_TITLE = "title"
        private const val EXPAND_AVATAR_SIZE_DP = 80f
        private const val COLLAPSED_AVATAR_SIZE_DP = 32f
    }

    private val mAvatarPoint = FloatArray(2)
    private val mSpacePoint = FloatArray(2)
    private var mTitleTextSize = 0f
    private var mAvatarImageView: CircleImageView? = null
    private var mToolbarTextView: TextView? = null
    private var mTitleTextView: TextView? = null
    private var mContainerView: View? = null
    private val mToolbarTextPoint = FloatArray(2)
    private val mTitleTextViewPoint = FloatArray(2)
    private var mAppBarStateChangeListener: AppBarStateChangeListener? = null
    private var mAppBarLayout: AppBarLayout? = null
    private var mToolBar: Toolbar? = null
    private var mSpace: Space? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        findViews()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val fragment = ItemDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(
                        ItemDetailFragment.ARG_ITEM_ID,
                        intent.getStringExtra(ItemDetailFragment.ARG_ITEM_ID)
                    )
                }
            }
            if (intent.hasExtra(ARG_TITLE)) {
                mTitleTextView!!.text = intent.getStringExtra(ARG_TITLE)
            }

            mTitleTextView!!.post(Runnable { resetPoints(true) })
            supportFragmentManager.beginTransaction()
                .add(R.id.item_detail_container, fragment)
                .commit()
        }
        mTitleTextSize = mTitleTextView!!.textSize
        setUpToolbar()
        setUpAmazingAvatar()
    }

    private fun setUpToolbar() {
        mAppBarLayout!!.layoutParams.height = getDisplayMetrics(this).widthPixels * 9 / 16
        mAppBarLayout!!.requestLayout()
        setSupportActionBar(mToolBar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun findViews() {
        mContainerView = findViewById(R.id.view_container)
        mAvatarImageView = findViewById(R.id.imageView_avatar)
        mToolbarTextView = findViewById(R.id.toolbar_title)
        mTitleTextView = findViewById(R.id.textView_title)
        mAppBarLayout = findViewById(R.id.app_bar)
        mToolBar = findViewById(R.id.toolbar)
        mSpace = findViewById(R.id.space)


    }

    private fun setUpAmazingAvatar() {
        mAppBarStateChangeListener = object : AppBarStateChangeListener() {
            override fun onStateChanged(
                appBarLayout: AppBarLayout?,
                state: AppBarStateChangeListener.State?
            ) {
            }

            override fun onOffsetChanged(state: AppBarStateChangeListener.State?, offset: Float) {
                translationView(offset)
            }
        }
        mAppBarLayout?.addOnOffsetChangedListener(mAppBarStateChangeListener)
    }

    private fun translationView(offset: Float) {
        val newAvatarSize = convertDpToPixel(
            EXPAND_AVATAR_SIZE_DP - (EXPAND_AVATAR_SIZE_DP - COLLAPSED_AVATAR_SIZE_DP) * offset,
            this
        )
        val expandAvatarSize = convertDpToPixel(EXPAND_AVATAR_SIZE_DP, this)
        val xAvatarOffset =
            (mSpacePoint[0] - mAvatarPoint[0] - (expandAvatarSize - newAvatarSize) / 2f) *
                    offset
        val yAvatarOffset =
            (mSpacePoint[1] - mAvatarPoint[1] - (expandAvatarSize - newAvatarSize)) * offset
        mAvatarImageView!!.layoutParams.width = Math.round(newAvatarSize)
        mAvatarImageView!!.layoutParams.height = Math.round(newAvatarSize)
        mAvatarImageView!!.translationX = xAvatarOffset
        mAvatarImageView!!.translationY = yAvatarOffset
        val newTextSize = mTitleTextSize - (mTitleTextSize - mToolbarTextView!!.textSize) * offset
        val paint = Paint(mTitleTextView!!.paint)
        paint.textSize = newTextSize
        val newTextWidth = getTextWidth(paint, mTitleTextView!!.text.toString())
        paint.textSize = mTitleTextSize
        val originTextWidth = getTextWidth(paint, mTitleTextView!!.text.toString())
        val isRTL = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) ==
                View.LAYOUT_DIRECTION_RTL ||
                mContainerView!!.layoutDirection == View.LAYOUT_DIRECTION_RTL
        val xTitleOffset: Float =
            (mToolbarTextPoint[0] + if (isRTL) mToolbarTextView!!.width.toFloat() else 0f -
                    (mTitleTextViewPoint[0] + if (isRTL) mTitleTextView!!.width else 0) -
                    (if (mToolbarTextView!!.width > newTextWidth) {
                        (originTextWidth - newTextWidth) / 2f

                    } else {
                        0f
                    })) * offset
        val yTitleOffset = (mToolbarTextPoint[1] - mTitleTextViewPoint[1]) * offset
        mTitleTextView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize)
        mTitleTextView!!.translationX = xTitleOffset
        mTitleTextView!!.translationY = yTitleOffset
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!hasFocus) {
            return
        }
        resetPoints(false)
    }

    private fun resetPoints(isTextChanged: Boolean) {
        val offset: Float = mAppBarStateChangeListener?.currentOffset ?: 0f
        val newAvatarSize = convertDpToPixel(
            EXPAND_AVATAR_SIZE_DP - (EXPAND_AVATAR_SIZE_DP - COLLAPSED_AVATAR_SIZE_DP) * offset,
            this
        )
        val expandAvatarSize = convertDpToPixel(
            EXPAND_AVATAR_SIZE_DP,
            this
        )
        val avatarPoint = IntArray(2)
        mAvatarImageView!!.getLocationOnScreen(avatarPoint)
        mAvatarPoint[0] =
            avatarPoint[0] - mAvatarImageView!!.translationX - (expandAvatarSize - newAvatarSize) / 2f
        // If avatar center in vertical, just half `(expandAvatarSize - newAvatarSize)`
        mAvatarPoint[1] = avatarPoint[1] - mAvatarImageView!!.translationY -
                (expandAvatarSize - newAvatarSize)
        val spacePoint = IntArray(2)
        mSpace!!.getLocationOnScreen(spacePoint)
        mSpacePoint[0] = spacePoint[0].toFloat()
        mSpacePoint[1] = spacePoint[1].toFloat()
        val toolbarTextPoint = IntArray(2)
        mToolbarTextView!!.getLocationOnScreen(toolbarTextPoint)
        mToolbarTextPoint[0] = toolbarTextPoint[0].toFloat()
        mToolbarTextPoint[1] = toolbarTextPoint[1].toFloat()
        val paint = Paint(mTitleTextView!!.paint)
        val newTextWidth = getTextWidth(paint, mTitleTextView!!.text.toString())
        paint.textSize = mTitleTextSize
        val originTextWidth = getTextWidth(paint, mTitleTextView!!.text.toString())
        val titleTextViewPoint = IntArray(2)
        mTitleTextView!!.getLocationOnScreen(titleTextViewPoint)
        mTitleTextViewPoint[0] = titleTextViewPoint[0] - mTitleTextView!!.translationX -
                if (mToolbarTextView!!.width > newTextWidth) (originTextWidth - newTextWidth) / 2f else 0f
        mTitleTextViewPoint[1] = titleTextViewPoint[1] - mTitleTextView!!.translationY
        if (isTextChanged) {
            Handler().post { translationView(offset) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                navigateUpTo(Intent(this, ItemListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}