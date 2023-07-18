package my.fa250.furniture4u.arsv;

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.getDescription
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position
import io.github.sceneview.utils.doOnApplyWindowInsets
import io.github.sceneview.utils.setFullScreen
import my.fa250.furniture4u.NotifReceiver
import my.fa250.furniture4u.R
import my.fa250.furniture4u.com.CartActivity
import my.fa250.furniture4u.ml.ContextActivity
import my.fa250.furniture4u.model.CartModel
import my.fa250.furniture4u.model.ProductKotlinModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ARActivity2 : AppCompatActivity(R.layout.activity_arkotlin) {

    lateinit var sceneView: ArSceneView
    lateinit var loadingView: View
    lateinit var statusText: TextView
    lateinit var placeModelButton: ExtendedFloatingActionButton
    lateinit var newModelButton: ExtendedFloatingActionButton
    lateinit var productCardView: CardView
    lateinit var productKotlinName: TextView
    lateinit var productKotlinPrice: TextView
    lateinit var productAddToCart: Button
    lateinit var productCheckout: Button
    lateinit var productKotlinColour: TextView
    lateinit var closeAR: ImageButton

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var currentUser = auth.currentUser
    var categoryModelList:kotlin.collections.MutableList<ProductKotlinModel> = mutableListOf()

    private lateinit var gestureDetectorCompat: GestureDetectorCompat

    data class Model(
        val name: String,
        val fileLocation: String,
        val scaleUnits: Float? = null,
        val placementMode: PlacementMode = PlacementMode.PLANE_HORIZONTAL,
        val applyPoseRotation: Boolean = true,
        val PKM: ProductKotlinModel,
    )

    var models = mutableListOf<Model>()
    var modelIndex = 0
    var modelNode: ArModelNode? = null

    lateinit var roomType: String
    lateinit var roomColour: String
    lateinit var roomFurn: ArrayList<String>

    val database = Firebase.database("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val cartMap = HashMap<String, Any>()
    var isLoading = false
        set(value) {
            field = value
            loadingView.isGone = !value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        initUI()

         roomType = intent.getStringExtra("type").toString()
         roomColour = intent.getStringExtra("colour").toString()
         roomFurn = intent.getStringArrayListExtra("furn") as ArrayList<String>

        if(roomColour == "Gray")
        {
            roomColour = "Grey"
        }

        database.getReference("product").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                for (snapshot1 in snapshot.children) {
                    val value = snapshot1.getValue<ProductKotlinModel>()
                    if (value != null)
                    {
                        categoryModelList.add(value)
                    }
                }
                for(k in categoryModelList)
                {
                    Log.w("Cat Mod",k.name.toString())
                    if(k.varianceList?.keys?.contains("null") == true && k.colour.equals(capitalize(roomColour)))
                    {
                        k.url_3d?.let {
                            Model(
                                k.name.toString(),
                                fileLocation = it,
                                scaleUnits = 1f,
                                placementMode = if(k.type=="Clock") PlacementMode.PLANE_VERTICAL else PlacementMode.PLANE_HORIZONTAL,
                                applyPoseRotation = true,
                                PKM = k,
                            )

                        }?.let { models.add(it) }
                    }
                    else if(k.varianceList?.keys?.contains(capitalize(roomColour)) == true)
                    {
                        val vl = k.varianceList!![capitalize(roomColour)] as Map<*, *>
                        val vla = vl["url_3d"]
                        k.colour = capitalize(roomColour)
                        models.add(
                            Model(
                                k.name.toString(),
                                fileLocation = vla as String,
                                scaleUnits = 1f,
                                placementMode = if(k.type=="Clock") PlacementMode.PLANE_VERTICAL else PlacementMode.PLANE_HORIZONTAL,
                                applyPoseRotation = true,
                                PKM = k,
                            )
                        )
                    }
                }
                if (models.isEmpty())
                {
                    models.add(Model(
                        "Black Lilly Chair",
                        fileLocation = "https://furniture4u.s3.ap-southeast-1.amazonaws.com/chair/lilly/black/chair_lilly_black.gltf",
                        scaleUnits = 1f,
                        placementMode = PlacementMode.PLANE_VERTICAL,
                        applyPoseRotation = true,
                        PKM = ProductKotlinModel(
                            "OZ2oHNBW4tCWuVmXmcV9",
                            "A simple plastic chair for you.",
                            "Lilly Chair",
                            4.0,
                            55.0,
                            listOf("https://firebasestorage.googleapis.com/v0/b/furniture4u-93724.appspot.com/o/product%2Fchair%2FLilly%2FBlack%2Fchair_lilly_black_left.png?alt=media&token=429aed66-fbf1-4d98-96ec-7309a34c3913"),
                            "Chair",
                            "Chair",
                            mapOf("null" to "null"),
                            "Black",
                            1,
                            "https://furniture4u.s3.ap-southeast-1.amazonaws.com/chair/lilly/black/chair_lilly_black.gltf"
                        )
                    ))
                    Toast.makeText(
                        this@ARActivity2,
                        "No Model in stores are suitable for this room. Showing default model.",
                        Toast.LENGTH_SHORT
                    ).show()
                    newModelNode()
                }
                else
                {
                    Toast.makeText(
                        this@ARActivity2,
                        ""+models.size+" models are suitable for this room. Loading model.",
                        Toast.LENGTH_SHORT
                    ).show()
                    newModelNode()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Kotlin RDB", "Failed to read value.", error.toException())
            }
        })
        initUI2()
    }

    fun initUI()
    {
        productCardView = findViewById<CardView>(R.id.productCardViewKT)
        productKotlinName = findViewById(R.id.productKotlinName)
        productKotlinPrice = findViewById(R.id.productKotlinPrice)
        productKotlinColour = findViewById(R.id.productKotlinColour)
        closeAR = findViewById<ImageButton?>(R.id.closeRec).apply {
            setOnClickListener {
                val intent = Intent(this@ARActivity2, ContextActivity::class.java)
                startActivity(intent)
            }
        }
        productAddToCart = findViewById<Button?>(R.id.AddToCartKt).apply {
            setOnClickListener{ addToCartKotlin() }
        }
        productCheckout = findViewById<Button?>(R.id.checkoutKt).apply {
            setOnClickListener {
                val intent = Intent(this@ARActivity2, CartActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun initUI2()
    {
        setFullScreen(
            findViewById(R.id.rootView),
            fullScreen = true,
            hideSystemBars = false,
            fitsSystemWindows = false
        )

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar)?.apply {
            doOnApplyWindowInsets { systemBarsInsets ->
                (layoutParams as ViewGroup.MarginLayoutParams).topMargin = systemBarsInsets.top
            }
            navigationIcon = null
            title = ""

        })
        statusText = findViewById(R.id.statusText)
        sceneView = findViewById<ArSceneView?>(R.id.sceneView).apply {
            onArTrackingFailureChanged = { reason ->
                statusText.text = reason?.getDescription(context)
                statusText.isGone = reason == null
            }
        }
        loadingView = findViewById(R.id.loadingView)
        newModelButton = findViewById<ExtendedFloatingActionButton>(R.id.newModelButton).apply {
            // Add system bar margins
            val bottomMargin = (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin
            doOnApplyWindowInsets { systemBarsInsets ->
                (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin =
                    systemBarsInsets.bottom + bottomMargin
            }
            setOnClickListener { newModelNode() }
        }
        placeModelButton = findViewById<ExtendedFloatingActionButton>(R.id.placeModelButton).apply {
            setOnClickListener { placeModelNode() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.isChecked = !item.isChecked
        modelNode?.detachAnchor()
        modelNode?.placementMode = when (item.itemId) {
            R.id.menuPlanePlacement -> PlacementMode.PLANE_HORIZONTAL_AND_VERTICAL
            R.id.menuInstantPlacement -> PlacementMode.INSTANT
            R.id.menuDepthPlacement -> PlacementMode.DEPTH
            R.id.menuBestPlacement -> PlacementMode.BEST_AVAILABLE
            else -> PlacementMode.DISABLED
        }
        return super.onOptionsItemSelected(item)
    }

    fun placeModelNode() {
        modelNode?.anchor()
        placeModelButton.isVisible = false
        sceneView.planeRenderer.isVisible = false
    }

    fun getProductInfo(model: Model)
    {
        val cartListener = object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                for(snapshot1 in snapshot.children)
                {
                    val cart = snapshot1.getValue<CartModel>()
                    if (cart != null)
                    {
                        if(cart.productName == model.name && cart.isInCart==true && cart.colour == capitalize(roomColour))
                        {
                            productAddToCart.setText("Already in Cart")
                            productAddToCart.isClickable=false
                            break
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ARActivity2,error.toString(),Toast.LENGTH_SHORT).show()
            }
        }

        database.getReference("user").child(auth.uid.toString()).child("cart").addValueEventListener(cartListener)

        productCardView.isGone = false
        productKotlinName.setText(model.PKM.name)
        productKotlinPrice.setText("RM"+model.PKM.price)
        productKotlinColour.setText(model.PKM.colour)
        productToCart(pkm = model.PKM)

    }

    fun productToCart(pkm: ProductKotlinModel)
    {
        val currentTime: String
        val currentDate: String
        val cal = Calendar.getInstance()

        val currDate = SimpleDateFormat("dd MM yyyy")
        currentDate = currDate.format(cal.time)

        val currTime = SimpleDateFormat("HH:mm:ss a")
        currentTime = currTime.format(cal.time)

        cartMap["productID"] = pkm.ID.toString()
        cartMap["productCat"] = pkm.category.toString()
        cartMap["colour"] = pkm.colour.toString()
        cartMap["description"] = pkm.description.toString()
        cartMap["img_url"] = pkm.img_url as ArrayList<String>
        cartMap["productName"] = pkm.name.toString()
        cartMap["productPrice"] = pkm.price
        cartMap["totalQuantity"] = 1
        cartMap["totalPrice"] = pkm.price
        cartMap["rating"] = pkm.rating
        cartMap["url_3d"] = pkm.url_3d.toString()
        cartMap["currentDate"] = currentDate
        cartMap["currentTime"] = currentTime

        if (pkm.varianceList?.keys?.contains("null") == true) {
            cartMap["variance"] = ArrayList<String>()
            cartMap["varianceList"] = ArrayList<String>()
        } else {
            val empList: MutableList<String> = ArrayList<String>()
            for (i in pkm.varianceList!!.keys) {
                empList.add(i)
            }
            cartMap["variance"] = empList
            cartMap["varianceList"] = pkm.varianceList!!
        }
        cartMap["isInCart"] = true
    }

    fun addToCartKotlin()
    {
        database.getReference("user/" + auth.currentUser!!.uid + "/cart")
            .push()
            .setValue(cartMap)
            .addOnCompleteListener {
                Toast.makeText(
                    this@ARActivity2,
                    "Item successfully added to cart",
                    Toast.LENGTH_SHORT
                ).show()
                scheduleNotif(
                    getNotif("You have items in your cart. Checkout before it's gone!")!!,
                    5000
                )
            }
    }

    private fun scheduleNotif(notif: Notification, delay: Int) {
        val intent = Intent(this, NotifReceiver::class.java)
        intent.putExtra(NotifReceiver.NOTIFICATIONID, 1)
        intent.putExtra(NotifReceiver.NOTIFICAION, notif)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val futureMilis = SystemClock.elapsedRealtime() + delay
        val alarmManager = (getSystemService(ALARM_SERVICE) as AlarmManager)
        alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, futureMilis] = pendingIntent
    }

    private fun getNotif(content: String): Notification {
        val intent = Intent(this, CartActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(this, "1001")
        builder.setContentTitle("Cart Reminder")
        builder.setContentText(content)
        builder.setSmallIcon(R.drawable.baseline_shopping_cart_24)
        builder.setAutoCancel(true)
        builder.setChannelId("1001")
        builder.setContentIntent(pendingIntent)
        return builder.build()
    }

    fun capitalize(str: String): String? {
        return str.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }

    fun newModelNode() {
        isLoading = true
        modelNode?.takeIf { !it.isAnchored }?.let {
            sceneView.removeChild(it)
            it.destroy()
        }
        val model = models[modelIndex]
        modelIndex = (modelIndex + 1) % models.size
        modelNode = ArModelNode(model.placementMode).apply {
            applyPoseRotation = model.applyPoseRotation
            name = model.name
            loadModelGlbAsync(
                glbFileLocation = model.fileLocation,
                autoAnimate = true,
                scaleToUnits = model.scaleUnits,
                // Place the model origin at the bottom center
                centerOrigin = Position(y = -1.0f),
            ) {
                sceneView.planeRenderer.isVisible = true
                isLoading = false
            }
            onAnchorChanged = { anchor ->
                placeModelButton.isGone = anchor != null
                Log.w("AR TEST","onAnchor")
            }
            onHitResult = { node, _ ->
                placeModelButton.isGone = !node.isTracking
                Log.w("AR TEST","onHit")
            }
            onTap = { motion,render ->
                productAddToCart.setText("Add to Cart")
                productAddToCart.isClickable=true
                getProductInfo(model)
                Log.w("AR TEST","onTap")
            }
        }
        sceneView.addChild(modelNode!!)
        // Select the model node by default (the model node is also selected on tap)
        sceneView.selectedNode = modelNode
    }


}