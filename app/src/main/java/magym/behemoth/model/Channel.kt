package magym.rssreader.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Entity
@Root(strict = false, name = "channel")
class Channel {

    @PrimaryKey
    var id: Int = 0

    var url: String = "http://"

    var urlIcoChannel: String = "http://"

    @field:Element(required = false, name = "title")
    var titleChannel: String = ""

    @Ignore
    @field:ElementList(inline = true, name = "item")
    var news: List<New>? = null

}