package models

data class Item (var itemId: Int = 0, var itemContents : String, var isItemComplete: Boolean = false){

    override fun toString() =
        if (isItemComplete)
            "$itemId: $itemContents (Complete)"
        else
            "$itemId: $itemContents (TODO)"

}
