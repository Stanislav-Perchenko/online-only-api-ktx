package cam.alperez.samples.onlineonlyapiktx.utils

import android.os.Parcelable
import cam.alperez.samples.onlineonlyapiktx.entity.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
class IntId<T> private constructor(
    val idValue: Int
): Parcelable {
    override fun toString() = idValue.toString()

    companion object {
        fun <E : Entity<*>> valueOf(idValue: Int): IntId<E> {
            return IntId(idValue)
        }
    }
}