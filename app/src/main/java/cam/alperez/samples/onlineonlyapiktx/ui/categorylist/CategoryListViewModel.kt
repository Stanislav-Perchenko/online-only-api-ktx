package cam.alperez.samples.onlineonlyapiktx.ui.categorylist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import cam.alperez.samples.onlineonlyapiktx.utils.ReplaceableSourceMediatorLiveData
import cam.alperez.samples.onlineonlyapiktx.rest.utils.ApiResponse
import cam.alperez.samples.onlineonlyapiktx.entity.CategoryEntity
import androidx.lifecycle.MutableLiveData
import cam.alperez.samples.onlineonlyapiktx.ui.common.ApiListResponseUiBundle
import cam.alperez.samples.onlineonlyapiktx.entity.BookEntity
import cam.alperez.samples.onlineonlyapiktx.ui.common.ErrorUiMessageMapper
import androidx.lifecycle.LiveData
import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.Observer
import cam.alperez.samples.onlineonlyapiktx.rest.ApplicationRestService
import cam.alperez.samples.onlineonlyapiktx.ui.Navigation
import cam.alperez.samples.onlineonlyapiktx.utils.MapMutableLiveData
import java.util.*

const val TAG = "CategoryListViewModel"

class CategoryListViewModel(app: Application) : AndroidViewModel(app) {

    private val categoriesResponseMediator: ReplaceableSourceMediatorLiveData<ApiResponse<List<CategoryEntity>>>
    private lateinit var _categoriesUiState: MutableLiveData<ApiListResponseUiBundle<CategoryEntity>>

    private val categoryBooksResponseMediator: ReplaceableSourceMediatorLiveData<ApiResponse<List<BookEntity>>>
    private lateinit var _categoryBooksUiState: MutableLiveData<CategoryBooksUiBundle>

    private val errorMsgMapper = ErrorUiMessageMapper(app)

    init {
        Log.i("CategoryListActivity", "----> ViewModel create!")
        categoriesResponseMediator = ReplaceableSourceMediatorLiveData()
        categoryBooksResponseMediator = ReplaceableSourceMediatorLiveData()

        _categoriesUiState = MapMutableLiveData.create(categoriesResponseMediator) { input: ApiResponse<List<CategoryEntity>> ->
            if (input.isSuccessful() && input.getResponseData() != null)
                ApiListResponseUiBundle.createSuccess(input.getResponseData())
            else
                ApiListResponseUiBundle.createError(errorMsgMapper.apply(input))
        }

        _categoryBooksUiState = MapMutableLiveData.create(categoryBooksResponseMediator) { input: ApiResponse<List<BookEntity>> ->
            val category: CategoryEntity? = _categoryBooksUiState.value?.categoryToDownload
            if (input.isSuccessful() && (input.getResponseData() != null) && (category != null)) {
                CategoryBooksUiBundle.createSuccess(category, input.getResponseData())
            } else {
                CategoryBooksUiBundle.createError(category, errorMsgMapper.apply(input))
            }
        }
        _categoriesUiState.value = ApiListResponseUiBundle.createSuccess(ArrayList())
        _categoryBooksUiState.value = CategoryBooksUiBundle.createSuccess(null, null)
    }

    val categoriesUiState: LiveData<ApiListResponseUiBundle<CategoryEntity>> = _categoriesUiState
    val categoryBooksUiState: LiveData<CategoryBooksUiBundle> = _categoryBooksUiState

    fun fetchCategories() {
        Log.i("CategoryListActivity", "----> ViewModel enter fetch")
        _categoriesUiState.value?.also { currentState: ApiListResponseUiBundle<CategoryEntity> ->
            _categoriesUiState.value = currentState.withIsLoading(true)
        }
        val result: LiveData<ApiResponse<List<CategoryEntity>>> = ApplicationRestService.INSTANCE.getCategories()
        categoriesResponseMediator.setSource(result)
        Log.i("CategoryListActivity", "<---- ViewModel exit fetch")
    }

    fun clearNeedShowCategoryError() {
        val currentState = _categoriesUiState.value
        if (currentState != null && currentState.isErrorMessageShow) {
            _categoriesUiState.value = currentState.withErrorShow(false)
        }
    }

    fun fetchBooksForCategory(category: CategoryEntity) {
        _categoryBooksUiState.value?.also { currentState: CategoryBooksUiBundle ->
            _categoryBooksUiState.value = currentState
                .withCategoryId(category)
                .withIsLoading(true)
        }
        val result: LiveData<ApiResponse<List<BookEntity>>> = ApplicationRestService.INSTANCE.getBooksForCategory(category.booksLink)
        categoryBooksResponseMediator.setSource(result)
    }

    fun clearNeedShowCategoryBooksError() {
        val currentState = _categoryBooksUiState.value
        if (currentState != null && currentState.isErrorMessageShow) {
            _categoryBooksUiState.value = currentState.withErrorShow(false)
        }
    }

    fun navigateToCategoryBooksScreen(
        currentActivity: Activity,
        category: CategoryEntity,
        optBooks: List<BookEntity>?)
    {
        val launcher = Intent(Navigation.ACTION_SCREEN_CATEGORY_BOOKS)
        launcher.putExtra(Navigation.EXTRA_BOOK_CATEGORY, category)
        if (optBooks != null) {
            if (optBooks is ArrayList<*>) {
                launcher.putParcelableArrayListExtra(
                    Navigation.EXTRA_BOOKS,
                    optBooks as ArrayList<out Parcelable>?
                )
            } else {
                val extras = ArrayList<Parcelable>(optBooks.size)
                extras.addAll(optBooks)
                launcher.putParcelableArrayListExtra(Navigation.EXTRA_BOOKS, extras)
            }
        }
        currentActivity.startActivity(launcher)
    }

}