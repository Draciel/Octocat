package pl.draciel.octocat.net.converters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.lang.reflect.Type

internal class LocalDateTimeAdapter : JsonDeserializer<LocalDateTime?> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDateTime? {
        if (json == null) {
            return null
        }
        val instant = Instant.parse(json.asJsonPrimitive.asString)
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    }
}
