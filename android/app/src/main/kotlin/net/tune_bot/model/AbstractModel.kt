package net.tune_bot.model

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.File
import java.io.FileReader
import java.io.Serializable

abstract class AbstractModel: Serializable {
    fun save(file: File) = file.writeText(serialize())

    open fun serialize(): String = Gson().toJson(this)

    companion object {
        inline fun <reified T: AbstractModel> deserialize(raw: String): T =
            Gson().fromJson(raw, T::class.java)

        inline fun <reified T: AbstractModel> load(file: File): T = JsonReader(FileReader(file)).use {
            Gson().fromJson(it, T::class.java)
        }
    }
}