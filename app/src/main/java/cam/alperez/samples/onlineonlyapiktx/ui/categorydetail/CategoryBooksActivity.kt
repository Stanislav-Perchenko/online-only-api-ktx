package cam.alperez.samples.onlineonlyapiktx.ui.categorydetail

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cam.alperez.samples.onlineonlyapiktx.R
import cam.alperez.samples.onlineonlyapiktx.entity.BookEntity
import cam.alperez.samples.onlineonlyapiktx.entity.CategoryEntity
import cam.alperez.samples.onlineonlyapiktx.ui.Navigation
import cam.alperez.samples.onlineonlyapiktx.ui.common.ApiListResponseUiBundle

class CategoryBooksActivity : AppCompatActivity() {

    private lateinit var vRefresher: SwipeRefreshLayout
    private lateinit var tvError: TextView

    private lateinit var viewModel: CategoryBooksViewModel

    lateinit var listAdapter: CategoryBooksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_with_recycler)

        setSupportActionBar(findViewById(R.id.toolbar))

        vRefresher = findViewById(R.id.refresher)
        tvError = findViewById(R.id.text_error)
        tvError.visibility = View.GONE

        val screenTitle: String
        if (savedInstanceState == null) {
            // Initial creating ViewModel with constructor parameter via custom factory
            val category: CategoryEntity = intent.getParcelableExtra<CategoryEntity>(Navigation.EXTRA_BOOK_CATEGORY) ?: throw java.lang.IllegalStateException()
            viewModel = ViewModelProvider(this, CategoryBooksViewModel.Factory(category))[CategoryBooksViewModel::class.java]
            screenTitle = getString(R.string.screen_title_books_for_category, category?.displayName)
        } else {
            // Find existing ViewModel instance
            viewModel = ViewModelProviders.of(this).get(CategoryBooksViewModel::class.java)
            screenTitle = getString(R.string.screen_title_books_for_category, viewModel.category.displayName)
        }
        supportActionBar?.title = screenTitle

        val rv = findViewById<RecyclerView>(R.id.items_recycler)
        rv.layoutManager = LinearLayoutManager(this)
        listAdapter = CategoryBooksAdapter(this, viewModel.category)
        rv.adapter = listAdapter

        observeUiState()

        // Initial fetching data
        if (savedInstanceState == null) {
            val argBooks: List<BookEntity>? = intent.getParcelableArrayListExtra(Navigation.EXTRA_BOOKS)
            if (argBooks != null && argBooks.isNotEmpty()) {
                viewModel.setBooks(argBooks)
            } else {
                viewModel.fetchBooks()
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        vRefresher.setOnRefreshListener { viewModel.fetchBooks() }
    }

    private fun observeUiState() {
        viewModel.booksUiState.observe(this) { uiState: ApiListResponseUiBundle<BookEntity> ->

            vRefresher.isRefreshing = uiState.isLoading

            if (uiState.isSuccess) {
                tvError.visibility = View.GONE
                listAdapter.setData(uiState.data)
            } else if (uiState.error != null) {
                listAdapter.clear()
                tvError.text = uiState.error.detailedDescription
                tvError.visibility = View.VISIBLE
            }

            if (uiState.isErrorMessageShow) {
                if (uiState.error != null) {
                    Toast.makeText(this, uiState.error.displayText, Toast.LENGTH_SHORT).show()
                }
                vRefresher.post { viewModel.clearNeedShowBookError() }
            }
        }
    }
}