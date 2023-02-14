package cam.alperez.samples.onlineonlyapiktx.ui.categorylist

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cam.alperez.samples.onlineonlyapiktx.R
import cam.alperez.samples.onlineonlyapiktx.entity.CategoryEntity
import cam.alperez.samples.onlineonlyapiktx.ui.common.ApiListResponseUiBundle
import cam.alperez.samples.onlineonlyapiktx.ui.common.OnItemClickListener

const val STATE_PENDING_NAVIGATE_CATEGORY = "pendingNavigateToCategory"

class CategoryListActivity : AppCompatActivity() {
    private lateinit var vRefresher: SwipeRefreshLayout
    private lateinit var tvError: TextView
    private lateinit var viewModel: CategoryListViewModel

    private var pendingNavigateToCategory = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_with_recycler)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setTitle(R.string.screen_title_category_list)

        vRefresher = findViewById(R.id.refresher)
        tvError = findViewById(R.id.text_error)
        tvError.visibility = View.GONE

        val rv = findViewById<RecyclerView>(R.id.items_recycler)
        rv.layoutManager = LinearLayoutManager(this)
        val listAdapter = CategoryListAdapter(this)
        listAdapter.setItemClickListener(object : OnItemClickListener<CategoryEntity> {
            override fun onItemClicked(position: Int, category: CategoryEntity) {
                if (!pendingNavigateToCategory) {
                    pendingNavigateToCategory = true
                    viewModel.fetchBooksForCategory(category)
                }
            }
        })
        rv.adapter = listAdapter

        viewModel = ViewModelProviders.of(this)[CategoryListViewModel::class.java]

        observeCategories(listAdapter)
        observeCategoryBook(listAdapter)

        if (savedInstanceState == null) {
            viewModel.fetchCategories()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        pendingNavigateToCategory = savedInstanceState.getBoolean(STATE_PENDING_NAVIGATE_CATEGORY, false)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        vRefresher.setOnRefreshListener { viewModel.fetchCategories() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_PENDING_NAVIGATE_CATEGORY, pendingNavigateToCategory)
    }

    private fun observeCategories(adapter: CategoryListAdapter) {
        viewModel.categoriesUiState.observe(this) { uiState: ApiListResponseUiBundle<CategoryEntity> ->

            vRefresher.isRefreshing = uiState.isLoading

            if (uiState.isSuccess) {
                tvError.visibility = View.GONE
                adapter.setData(uiState.data)
            } else if (uiState.error != null) {
                adapter.clear()
                tvError.text = uiState.error.detailedDescription
                tvError.visibility = View.VISIBLE
            }

            if (uiState.isErrorMessageShow) {
                if (uiState.error != null) {
                    Toast.makeText(this, uiState.error.displayText, Toast.LENGTH_SHORT).show()
                }
                vRefresher.post { viewModel.clearNeedShowCategoryError() }
            }
        }
    }

    private fun observeCategoryBook(adapter: CategoryListAdapter) {
        viewModel.categoryBooksUiState.observe(this) { uiState: CategoryBooksUiBundle ->

            if (uiState.categoryToDownload != null) {
                adapter.setItemLoadingProgress(uiState.categoryToDownload.id, uiState.isLoading)
            }

            if (!uiState.isLoading) {
                if (uiState.isSuccess && (uiState.data != null) && pendingNavigateToCategory) {
                    viewModel.navigateToCategoryBooksScreen(
                        this,
                        uiState.categoryToDownload!!, uiState.data
                    )
                }
                pendingNavigateToCategory = false
            }

            if (uiState.isErrorMessageShow) {
                if (uiState.error != null) {
                    Toast.makeText(this, uiState.error.displayText, Toast.LENGTH_SHORT).show()
                }
                vRefresher.post { viewModel.clearNeedShowCategoryBooksError() }
            }
        }
    }
}