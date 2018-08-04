package magym.rssreader.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false, name = "rss")
class Rss {

    @field:Element(required = false, name = "channel")
    var channel: Channel? = null

}