package cam.alperez.samples.onlineonlyapiktx.entity

import android.os.Parcelable
import cam.alperez.samples.onlineonlyapiktx.rest.model.FileModel
import cam.alperez.samples.onlineonlyapiktx.rest.model.ImageModel
import cam.alperez.samples.onlineonlyapiktx.utils.IntId
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookEntity(
    private val id: IntId<BookEntity>,
    @field:SerializedName("category_id")
    val categoryId: IntId<CategoryEntity>,
    val order: Int,
    val title: String,
    val authors: List<String>,
    val publisher: String,
    val year: Int,
    val isbn: String?,
    val cover: ImageModel?,
    val file: FileModel) : Entity<BookEntity>, Parcelable
{

    override fun getId(): IntId<BookEntity> {
        return id
    }

}