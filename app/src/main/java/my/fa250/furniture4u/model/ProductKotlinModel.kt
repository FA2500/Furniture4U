package my.fa250.furniture4u.model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
class ProductKotlinModel : Serializable {
    var ID: String? = null
    var description: String? = null
    var name: String? = null
    var rating: Double = 0.0
    var price: Double = 0.0
    var img_url: List<String>? = null
    var category: String? = null
    var type: String? = null
    var varianceList: Map<String, Any>? = null
    var colour: String? = null
    var stock = 0
    var url_3d: String? = null

    constructor() {}
    constructor(
        ID: String?,
        desc: String?,
        name: String?,
        rate: Double,
        price: Double,
        url: List<String>?,
        category: String?,
        type: String?,
        varianceList: Map<String, Any>?,
        colour: String?,
        stock: Int,
        url_3d: String?
    ) {
        this.ID = ID
        this.description = desc
        this.name = name
        this.rating = rate
        this.price = price
        this.img_url = url
        this.category = category
        this.type = type
        this.varianceList = varianceList
        this.colour = colour
        this.stock = stock
        this.url_3d = url_3d
    }


}