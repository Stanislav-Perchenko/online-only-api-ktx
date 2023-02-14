package cam.alperez.samples.onlineonlyapiktx.ui.common

interface OnItemClickListener<T> {
    fun onItemClicked(position: Int, item: T)
}