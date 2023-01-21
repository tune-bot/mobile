package dev.graansma.tunebot_android

class Controller(promotionLevel: PromotionLevel) {
    private val api = Api(promotionLevel)
    private val masterList = mutableMapOf<String, Playlist>()
    private var currentSong = Song("")
    private val previousSongs = arrayListOf<Song>()
    private val nextSongs = arrayListOf<Song>()

    private fun ArrayList<Song>.contains(url: String): Boolean {
        forEach {
            if(it.url == url) {
                return true
            }
        }
        return false
    }

    fun nextSong(): Song {
        if(nextSongs.isEmpty()) {
            throw Error("No songs available.")
        }
        previousSongs.add(0, currentSong)
        currentSong = nextSongs.removeAt(0)
        return currentSong
    }

    fun currentSong(): Song {
        return if(currentSong.equals("")) {
            nextSong()
        } else {
            currentSong
        }
    }

    fun previousSong(): Song {
        if(previousSongs.isEmpty()) {
            throw Error("No songs available.")
        }
        nextSongs.add(0, currentSong)
        currentSong = previousSongs.removeAt(0)
        return currentSong
    }

    fun updateMasterList(macs: Set<String>) {
        // TODO retain relatively recent macs in the event of a network blip
        for(mac in macs) {
            if(!masterList.contains(mac)) {
                masterList[mac] = api.getPlaylist(mac)
            }
        }

        val library = mutableSetOf<String>()
        val blacklist = mutableSetOf<String>()

        for(mac in masterList.keys) {
            if(macs.contains(mac)) {
                masterList[mac]?.blacklist?.let { blacklist.addAll(it) }
                masterList[mac]?.songs?.let { library.addAll(it) }
            } else {
                masterList.remove(mac)
            }
        }

        library.removeAll(blacklist)
        library.removeAll(previousSongs.map { it.url }.toSet())


        nextSongs.forEach {
            if(!library.contains(it.url)) nextSongs.remove(it)
        }

        library.forEach {
            if(!nextSongs.contains(it)) {
                nextSongs.add(Song(it))
            }
        }

        if(nextSongs.isNotEmpty() && (currentSong.url.isBlank() || currentSong.url.isEmpty())) {
            currentSong = nextSongs.removeAt(0)
        }
    }
}

enum class PromotionLevel {
    DEVELOPMENT,
    PRODUCTION
}

class Playlist(val songs: Set<String>, val blacklist: Set<String>)