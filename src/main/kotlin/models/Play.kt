package models

data class Play(var playId: Int = 0, var playContents: String, var isPlayComplete: Boolean = false) {

    override fun toString() =
        if (isPlayComplete)
            "$playId: $playContents (Played)"
        else
            "$playId: $playContents (To-Be Played)"
}
