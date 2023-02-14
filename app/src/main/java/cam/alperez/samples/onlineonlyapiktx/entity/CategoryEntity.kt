package cam.alperez.samples.onlineonlyapiktx.entity

import android.os.Parcelable
import cam.alperez.samples.onlineonlyapiktx.rest.model.ImageModel
import cam.alperez.samples.onlineonlyapiktx.utils.IntId
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryEntity(
    private val id: IntId<CategoryEntity>,

    @field:SerializedName("display_name")
    val displayName: String?,
    val order: Int,

    @field:SerializedName("books_link")
    val booksLink: String?,

    @field:SerializedName("icon_png")
    val iconPng: ImageModel?,

    @field:SerializedName("icon_svg")
    val iconSvg: ImageModel?) : Entity<CategoryEntity>, Parcelable
{

    override fun getId(): IntId<CategoryEntity> {
        return id
    }

}