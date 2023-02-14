package cam.alperez.samples.onlineonlyapiktx.ui.categorylist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import cam.alperez.samples.onlineonlyapiktx.entity.CategoryEntity
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.view.View
import cam.alperez.samples.onlineonlyapiktx.utils.IntId
import android.view.ViewGroup
import android.widget.ImageView
import cam.alperez.samples.onlineonlyapiktx.R
import android.widget.TextView
import cam.alperez.samples.onlineonlyapiktx.rest.model.ImageModel
import cam.alperez.samples.onlineonlyapiktx.ui.common.OnItemClickListener
import cam.alperez.samples.onlineonlyapiktx.ui.utils.UiUtils
import com.squareup.picasso.Picasso
import java.util.ArrayList
import java.util.HashSet

class CategoryListAdapter(ctx: Context?) : RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {
    private val inflater: LayoutInflater
    private val data: MutableList<CategoryEntity>
    private var itemClickListener: OnItemClickListener<CategoryEntity>? = null
    private val withProgressItemIds: MutableSet<Int> = HashSet(10)

    init {
        inflater = LayoutInflater.from(ctx)
        data = ArrayList(10)
    }

    fun setItemClickListener(itemClickListener: OnItemClickListener<CategoryEntity>?) {
        this.itemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<CategoryEntity>?) {
        this.data.clear()
        data?.let {
            this.data.addAll(it)
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        if (data.isNotEmpty()) {
            data.clear()
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItemLoadingProgress(categoryId: IntId<CategoryEntity>, isProgress: Boolean) {
        val changed = if (isProgress) {
            withProgressItemIds.add(categoryId.idValue)
        } else {
            withProgressItemIds.remove(categoryId.idValue)
        }
        if (changed) notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long {
        return data[position].id.idValue.toLong()
    }

    fun getItem(position: Int): CategoryEntity {
        return data[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.category_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = data[position]
        holder.bindData(position, model)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var pos = 0
        private val ivCategory: ImageView
        private val tvTitle: TextView
        private val vProgress: View
        private val logoScaledSize = Point()

        init {
            ivCategory = itemView.findViewById(R.id.category_icon)
            tvTitle = itemView.findViewById(R.id.category_title)
            vProgress = itemView.findViewById(R.id.item_load_progress)
            itemView.findViewById<View>(R.id.clickable_container).setOnClickListener { _ ->
                itemClickListener?.also {
                    it.onItemClicked(pos, getItem(pos))
                }
            }
        }

        fun bindData(position: Int, category: CategoryEntity) {
            this.pos = position
            tvTitle.text = category.displayName
            val isProgress = withProgressItemIds.contains(category.id.idValue)
            vProgress.visibility = if (isProgress) View.VISIBLE else View.INVISIBLE

            category.iconPng?.also { img: ImageModel ->
                UiUtils.calculateScaledImageSize(
                    img!!.width, img.height,
                    ivCategory.layoutParams.width, ivCategory.layoutParams.height,
                    logoScaledSize
                )
                Picasso.get()
                    .load(img.url.toString())
                    .resize(logoScaledSize.x, logoScaledSize.y)
                    .placeholder(R.drawable.image_placeholder)
                    .into(ivCategory)
            } ?: ivCategory.setImageDrawable(null)
        }
    }
}