package cam.alperez.samples.onlineonlyapiktx.ui.categorydetail

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cam.alperez.samples.onlineonlyapiktx.R
import cam.alperez.samples.onlineonlyapiktx.entity.BookEntity
import cam.alperez.samples.onlineonlyapiktx.entity.CategoryEntity
import cam.alperez.samples.onlineonlyapiktx.rest.model.ImageModel
import cam.alperez.samples.onlineonlyapiktx.ui.utils.UiUtils
import com.squareup.picasso.Picasso

class CategoryBooksAdapter(ctx: Context, category: CategoryEntity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val inflater: LayoutInflater
    private val category: CategoryEntity
    private val books: MutableList<BookEntity> = ArrayList()

    init {
        inflater = LayoutInflater.from(ctx)
        this.category = category
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(books: Collection<BookEntity>?) {
        if (this.books.isNotEmpty()) {
            this.books.clear()
        }
        books?.also {
            this.books.addAll(it)
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        if (books.isNotEmpty()) {
            books.clear()
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return 1 + books.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ITEM_TYPE_CATEGORY else ITEM_TYPE_BOOK
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_CATEGORY -> VHCategory(inflater.inflate(R.layout.category_header_list_item, parent, false))
            ITEM_TYPE_BOOK -> VHBook(inflater.inflate(R.layout.book_list_item, parent, false))
            else -> throw RuntimeException("View type not supported: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM_TYPE_CATEGORY -> (holder as VHCategory).bingData(category)
            ITEM_TYPE_BOOK -> (holder as VHBook).bingData(getBookByAdapterPosition(position))
        }
    }

    private fun getBookByAdapterPosition(position: Int): BookEntity {
        return books[position - 1]
    }

    internal class VHCategory(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivCategoryLogo: ImageView
        private val tvCategoryTitle: TextView
        private val logoScaledSize = Point()

        init {
            ivCategoryLogo = itemView.findViewById(R.id.img_category_header)
            tvCategoryTitle = itemView.findViewById(R.id.category_title_header)
        }

        fun bingData(category: CategoryEntity) {
            category.iconPng?.also { img: ImageModel ->
                UiUtils.calculateScaledImageSize(
                    img.width, img.height,
                    ivCategoryLogo.layoutParams.width, ivCategoryLogo.layoutParams.height,
                    logoScaledSize
                )
                Picasso.get()
                    .load(img.url.toString())
                    .resize(logoScaledSize.x, logoScaledSize.y)
                    .placeholder(R.drawable.image_placeholder)
                    .into(ivCategoryLogo)
            } ?: ivCategoryLogo.setImageDrawable(null)

            tvCategoryTitle.text = category.displayName
        }
    }

    internal class VHBook(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivBookCover: ImageView
        private val tvBookTitle: TextView
        private val tvAuthors: TextView
        private val tvYear: TextView
        private val tvPublisher: TextView
        private val tvIsbnTitle: TextView
        private val tvIsbnValue: TextView
        private val coverScaledSize = Point()

        init {
            ivBookCover = itemView.findViewById(R.id.img_book_cover)
            tvBookTitle = itemView.findViewById(R.id.txt_book_title)
            tvAuthors = itemView.findViewById(R.id.txt_book_authors)
            tvYear = itemView.findViewById(R.id.txt_book_year)
            tvPublisher = itemView.findViewById(R.id.txt_book_publisher)
            tvIsbnTitle = itemView.findViewById(R.id.book_isbn_title)
            tvIsbnValue = itemView.findViewById(R.id.book_isbn_value)
        }

        @SuppressLint("SetTextI18n")
        fun bingData(book: BookEntity) {
            book.cover?.also { img: ImageModel ->
                val (_, _, width, height) = img
                UiUtils.calculateScaledImageSize(
                    width, height,
                    ivBookCover.layoutParams.width, ivBookCover.layoutParams.height,
                    coverScaledSize
                )
                Picasso.get()
                    .load(book.cover.url.toString())
                    .resize(coverScaledSize.x, coverScaledSize.y)
                    .placeholder(R.drawable.image_placeholder)
                    .into(ivBookCover)
            }

            tvBookTitle.text = book.title
            tvAuthors.text = buildDisplayedAuthors(book.authors)
            tvYear.text = book.year.toString()
            tvPublisher.text = book.publisher
            if (TextUtils.isEmpty(book.isbn)) {
                tvIsbnTitle.visibility = View.INVISIBLE
                tvIsbnValue.text = ""
            } else {
                tvIsbnTitle.visibility = View.VISIBLE
                tvIsbnValue.text = book.isbn
            }
        }

        private fun buildDisplayedAuthors(authors: List<String>): CharSequence {
            return if (authors.isEmpty()) {
                ""
            } else if (authors.size == 1) {
                authors[0]
            } else {
                val sb = StringBuilder()
                authors.forEach { author ->
                    if (sb.isNotEmpty()) sb.append(", ")
                    sb.append(author) }
                sb.toString()
            }
        }
    }

    companion object {
        const val ITEM_TYPE_CATEGORY = 1
        const val ITEM_TYPE_BOOK = 2
    }
}