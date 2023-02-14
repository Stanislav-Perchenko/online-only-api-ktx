package cam.alperez.samples.onlineonlyapiktx.ui.categorydetail

import android.app.Application
import androidx.lifecycle.*
import cam.alperez.samples.onlineonlyapiktx.entity.CategoryEntity
import cam.alperez.samples.onlineonlyapiktx.ui.common.ErrorUiMessageMapper
import cam.alperez.samples.onlineonlyapiktx.utils.ReplaceableSourceMediatorLiveData
import cam.alperez.samples.onlineonlyapiktx.rest.utils.ApiResponse
import cam.alperez.samples.onlineonlyapiktx.entity.BookEntity
import cam.alperez.samples.onlineonlyapiktx.ui.common.ApiListResponseUiBundle
import androidx.lifecycle.viewmodel.CreationExtras
import cam.alperez.samples.onlineonlyapiktx.rest.ApplicationRestService
import cam.alperez.samples.onlineonlyapiktx.utils.MapMutableLiveData
import java.lang.RuntimeException
import java.util.ArrayList

class CategoryBooksViewModel(
    app: Application,
    val category: CategoryEntity,
    private val errorMsgMapper: ErrorUiMessageMapper
) : AndroidViewModel(app) {

    private val booksResponseMediator: ReplaceableSourceMediatorLiveData<ApiResponse<List<BookEntity>>> = ReplaceableSourceMediatorLiveData()

    private val _booksUiState: MutableLiveData<ApiListResponseUiBundle<BookEntity>>
    init {
        _booksUiState = MapMutableLiveData.create(booksResponseMediator) { input: ApiResponse<List<BookEntity>> ->
            if (input.isSuccessful() && input.getResponseData() != null)
                ApiListResponseUiBundle.createSuccess(input.getResponseData())
            else
                ApiListResponseUiBundle.createError(errorMsgMapper.apply(input))
        }
        _booksUiState.value = ApiListResponseUiBundle.createSuccess(emptyList())
    }
    val booksUiState: LiveData<ApiListResponseUiBundle<BookEntity>> = _booksUiState

    fun setBooks(books: Collection<BookEntity>) {
        val items: MutableList<BookEntity> = ArrayList(books.size)
        items.addAll(books)
        _booksUiState.value = ApiListResponseUiBundle.createSuccess(items)
    }

    fun fetchBooks() {
        _booksUiState.value?.also { currentState: ApiListResponseUiBundle<BookEntity> ->
            _booksUiState.value = currentState.withIsLoading(true)
        }
        val result = ApplicationRestService.INSTANCE.getBooksForCategory(category.booksLink)
        booksResponseMediator.setSource(result)
    }

    fun clearNeedShowBookError() {
        val currentState = _booksUiState.value
        if (currentState != null && currentState.isErrorMessageShow) {
            _booksUiState.value = currentState.withErrorShow(false)
        }
    }

    /****************************  Factory for this ViewModel  ************************************/
    class Factory(private val category: CategoryEntity) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

            val app: Application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                ?: throw RuntimeException("Application is null when ViewModel creation")
            return CategoryBooksViewModel(app, category, ErrorUiMessageMapper(app)) as T
        }
    }
}