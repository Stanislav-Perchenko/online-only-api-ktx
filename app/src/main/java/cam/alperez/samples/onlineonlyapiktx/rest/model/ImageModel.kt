package cam.alperez.samples.onlineonlyapiktx.rest.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.net.URL

@Parcelize
data class ImageModel(
    val url: URL,
    @field:SerializedName("size")
    val sizeBytes: Int,
    @field:SerializedName(value = "w", alternate = ["width"])
    val width: Int,
    @field:SerializedName(value = "h", alternate = ["height"])
    val height: Int,
    @field:SerializedName(value = "is_vector", alternate = ["isVector"])
    val isVector: Boolean
) : Parcelable