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
import my.fa250.furniture4u.com.HomePageActivity
import my.fa250.furniture4u.ml.ContextActivity
import my.fa250.furniture4u.model.CartModel
import my.fa250.furniture4u.model.ProductKotlinModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ARActivity3 : AppCompatActivity(R.layout.activity_arkotlin2) {

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
    lateinit var closeMenu: ImageButton
    lateinit var tempPKM: ProductKotlinModel

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var currentUser = auth.currentUser

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

        val productID = intent.getStringExtra("ID")
        val productColor = intent.getStringExtra("color")
        initUI()


        database.getReference("product").child(productID!!).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                val value = snapshot.getValue<ProductKotlinModel>()
                if (value != null && value.varianceList?.keys?.contains("null") == true )
                {
                    Log.d("AR VAR","AR NULL")
                    tempPKM = value
                    models.add(
                        Model(
                            tempPKM.name.toString(),
                            fileLocation = tempPKM.url_3d.toString(),
                            scaleUnits = 1f,
                            placementMode = if(tempPKM.type=="Clock") PlacementMode.PLANE_VERTICAL else PlacementMode.PLANE_HORIZONTAL,
                            applyPoseRotation = true,
                            PKM = tempPKM,
                        )
                    )
                    Toast.makeText(
                        this@ARActivity3,
                        "Loading model.",
                        Toast.LENGTH_SHORT
                    ).show()
                    newModelNode()
                }
                else
                {
                    if(value != null)
                    {
                        Log.d("AR VAR","AR VAR")
                        tempPKM = value
                        val vl = tempPKM.varianceList!![capitalize(productColor!!)] as Map<*,*>
                        val vla = vl["url_3d"]
                        models.add(
                            Model(
                                tempPKM.name.toString(),
                                fileLocation = vla as String,
                                scaleUnits = 1f,
                                placementMode = if(tempPKM.type=="Clock") PlacementMode.PLANE_VERTICAL else PlacementMode.PLANE_HORIZONTAL,
                                applyPoseRotation = true,
                                PKM = tempPKM,
                            )
                        )
                        Toast.makeText(
                            this@ARActivity3,
                            "Loading model.",
                            Toast.LENGTH_SHORT
                        ).show()
                        newModelNode()
                    }

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
                //val intent = Intent(this@ARActivity3, ContextActivity::class.java)
                //startActivity(intent)
                /*modelNode?.takeIf { !it.isAnchored }?.let {
                    sceneView.removeChild(it)
                    it.destroy()
                }*/
                //finish()
                val intent = Intent(this@ARActivity3, HomePageActivity::class.java)
                startActivity(intent)
            }
        }
        productAddToCart = findViewById<Button?>(R.id.AddToCartKt).apply {
            setOnClickListener{ addToCartKotlin() }
        }
        productCheckout = findViewById<Button?>(R.id.checkoutKt).apply {
            setOnClickListener {
                val intent = Intent(this@ARActivity3, CartActivity::class.java)
                startActivity(intent)
            }
        }
        closeMenu = findViewById<ImageButton?>(R.id.cardkotlinclose).apply {
            setOnClickListener {
                productCardView.isGone = true
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
                        if(cart.productName == model.name && cart.isInCart==true && cart.colour == capitalize(tempPKM.colour.toString()))
                        {
                            productAddToCart.setText("Already in Cart")
                            productAddToCart.isClickable=false
                            break
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ARActivity3,error.toString(),Toast.LENGTH_SHORT).show()
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
                    this@ARActivity3,
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