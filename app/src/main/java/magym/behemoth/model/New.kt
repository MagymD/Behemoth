package magym.rssreader.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Entity
@Root(strict = false, name = "item")
class New {

    @PrimaryKey
    var id: Int = 0

    var idChannel: Int = 0

    @field:Element(required = false, name = "title")
    var title: String = ""

    @field:Element(required = false, name = "link")
    var link: String = "http://"

    @field:Element(required = false, name = "description")
    var description: String = ""

    @field:Element(required = false, name = "pubDate")
    var date: String = ""

    var dateLong: Long = 0

    @field:Path("enclosure")
    @field:Attribute(required = false, name = "url")
    var urlImage: String = "http://"

}