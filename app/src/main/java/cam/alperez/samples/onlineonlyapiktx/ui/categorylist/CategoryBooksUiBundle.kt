package cam.alperez.samples.onlineonlyapiktx.ui.categorylist

import cam.alperez.samples.onlineonlyapiktx.entity.BookEntity
import cam.alperez.samples.onlineonlyapiktx.entity.CategoryEntity
import cam.alperez.samples.onlineonlyapiktx.ui.common.ApiListResponseUiBundle
import cam.alperez.samples.onlineonlyapiktx.ui.common.ErrorUiMessage


class CategoryBooksUiBundle(
    val categoryToDownload: CategoryEntity?,
    isLoading: Boolean,
    isSuccess: Boolean,
    data: List<BookEntity>?,
    error: ErrorUiMessage?,
    isErrorMessageShow: Boolean
) : ApiListResponseUiBundle<BookEntity>(isLoading, isSuccess, data, error, isErrorMessageShow) {

    fun withCategoryId(categoryToDownload: CategoryEntity): CategoryBooksUiBundle {
        return CategoryBooksUiBundle(
            categoryToDownload,
            isLoading,
            isSuccess,
            data,
            error,
            isErrorMessageShow
        )
    }

    override fun withIsLoading(newIsLoading: Boolean): CategoryBooksUiBundle {
        return CategoryBooksUiBundle(
            categoryToDownload,
            newIsLoading,
            isSuccess,
            data,
            error,
            isErrorMessageShow
        )
    }

    override fun withErrorShow(newIsErrShow: Boolean): CategoryBooksUiBundle {
        return CategoryBooksUiBundle(
            categoryToDownload,
            isLoading,
            isSuccess,
            data,
            error,
            newIsErrShow
        )
    }


    companion object {
        fun createSuccess(
            categoryToDownload: CategoryEntity?,
            data: List<BookEntity>?
        ): CategoryBooksUiBundle {
            return CategoryBooksUiBundle(
                categoryToDownload,
                isLoading = false,
                isSuccess = true,
                data,
                error = null,
                isErrorMessageShow = false
            )
        }

        fun createError(
            categoryToDownload: CategoryEntity?,
            err: ErrorUiMessage?
        ): CategoryBooksUiBundle {
            return CategoryBooksUiBundle(
                categoryToDownload,
                isLoading = false,
                isSuccess = false,
                data = null,
                err,
                isErrorMessageShow = true
            )
        }
    }

}