package cam.alperez.samples.onlineonlyapiktx.rest.model

import android.os.Parcelable
import cam.alperez.samples.onlineonlyapiktx.utils.FileType
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.net.URL

@Parcelize
data class FileModel(
    val url: URL,
    @field:SerializedName("type")
    val fileType: FileType,
    @field:SerializedName("size")
    val sizeBytes: Int) : Parcelable