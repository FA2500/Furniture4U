package my.fa250.furniture4u.arsv;

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.ar.core.Config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import io.github.sceneview.SceneView
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
import android.content.res.Resources
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Scale
import io.github.sceneview.model.model
import my.fa250.furniture4u.com.HomePageActivity
import kotlin.math.abs

class ARActivity2 : AppCompatActivity(R.layout.activity_arkotlin) {
    lateinit var sceneView: ArSceneView
    lateinit var loadingView: View
    lateinit var statusText: TextView
    lateinit var placeModelButton: ExtendedFloatingActionButton
    lateinit var newModelButton: ExtendedFloatingActionButton
    lateinit var productCardView: CardView
    lateinit var productKotlinName: TextView
    lateinit var productKotlinPrice: TextView
    lateinit var nodestatus: TextView
    lateinit var productAddToCart: Button
    lateinit var productCheckout: Button
    lateinit var productKotlinColour: TextView
    lateinit var productvar: Button
    lateinit var closeAR: ImageButton
    lateinit var closeMenu: ImageButton
    lateinit var ll: LinearLayout
    lateinit var productRemove: Button

    var counter = 0
    var quadrant = 0
    var anchor1 = false
    var anchor1Pos = Position()
    var anchor2 = false
    var anchor2Pos = Position()
    var removeAnc = false

    var indTemp = 0;
    var llm = LinearLayout.LayoutParams(150, 150)

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
        val PKM: ProductKotlinModel?,
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
        llm.setMargins(0,0,10,0)

         roomType = intent.getStringExtra("type").toString()
         roomColour = intent.getStringExtra("colour").toString()
         roomFurn = intent.getStringArrayListExtra("furn") as ArrayList<String>

        if(roomColour == "Gray")
        {
            roomColour = "Grey"
        }

        createCustomAlertDialog(this@ARActivity2);

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
                        val a = ImageButton(this@ARActivity2)
                        a.contentDescription = indTemp.toString()
                        indTemp += 1
                        Glide.with(this@ARActivity2).load(k.img_url!!.get(0)).centerCrop()
                            .into(a)
                        a.setOnClickListener {
                            newModelNode2(a)
                        }
                        ll.addView(a,llm)

                    }
                    else if(k.varianceList?.keys?.contains(capitalize(roomColour)) == true)
                    {
                        val vl = k.varianceList!![capitalize(roomColour)] as Map<*, *>
                        val vla = vl["url_3d"]
                        val list: MutableList<String> = vl["img_url"] as MutableList<String>
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

                        val a = ImageButton(this@ARActivity2)
                        a.contentDescription = indTemp.toString()
                        indTemp += 1
                        Glide.with(this@ARActivity2).load(list[0]).centerCrop()
                            .into(a)
                        a.setOnClickListener {
                            newModelNode2(a)
                        }
                        ll.addView(a,llm)
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
                            "https://furniture4u.s3.ap-southeast-1.amazonaws.com/chair/lilly/black/chair_lilly_black.gltf",
                            0.52,
                            0.56,
                            0.83,
                        ),
                    ))
                    Toast.makeText(
                        this@ARActivity2,
                        "No Model in stores are suitable for this room. Showing default model.",
                        Toast.LENGTH_SHORT
                    ).show()
                    //newModelNode()
                }
                else
                {
                    Toast.makeText(
                        this@ARActivity2,
                        ""+models.size+" models are suitable for this room. Loading model.",
                        Toast.LENGTH_SHORT
                    ).show()
                    //newModelNode()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Kotlin RDB", "Failed to read value.", error.toException())
            }
        })
        initUI2()
        measureModelNode()
    }

    fun initUI()
    {
        productCardView = findViewById<CardView>(R.id.productCardViewKT)
        productKotlinName = findViewById(R.id.productKotlinName)
        productKotlinPrice = findViewById(R.id.productKotlinPrice)
        productKotlinColour = findViewById(R.id.productKotlinColour)
        ll = findViewById(R.id.ProductLL)
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
        closeMenu = findViewById<ImageButton?>(R.id.cardkotlinclose).apply {
            setOnClickListener {
                productCardView.isGone = true
            }
        }
        productvar = findViewById<Button?>(R.id.colourKT).apply {
            setOnClickListener {
                showVarianceList()
            }
        }
        productRemove = findViewById<Button?>(R.id.removeKT).apply {
            setOnClickListener {
                //test
                /*modelNode?.let {
                    sceneView.removeChild(it)
                    it.destroy()
                    productCardView.isGone = true
                }*/
                if(sceneView.selectedNode?.isSelected == true)
                {
                    sceneView.selectedNode?.let {
                        sceneView.removeChild(it)
                        productCardView.isGone = true
                        nodestatus.text = "Current selected model = None"
                    }
                }
                else
                {
                    Toast.makeText(this@ARActivity2,"Drag the model a bit to delete it and reclick the button",Toast.LENGTH_SHORT).show()
                }

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
        nodestatus = findViewById(R.id.multiNodeStat)
        statusText = findViewById(R.id.statusText)
        sceneView = findViewById<ArSceneView?>(R.id.sceneView).apply {
            lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
            depthEnabled = true
            cameraNode.isScaleEditable = false
            instantPlacementEnabled = true

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
            setOnClickListener {
                if(counter == 1 && !anchor1)
                {
                    Toast.makeText(this@ARActivity2,"Place anchor 1 first", Toast.LENGTH_SHORT ).show()
                }
                else if(counter == 2 && !anchor2)
                {
                    Toast.makeText(this@ARActivity2,"Place anchor 1 first", Toast.LENGTH_SHORT ).show()
                }
                else
                {
                    newModelNode()
                }
            }
        }
        placeModelButton = findViewById<ExtendedFloatingActionButton>(R.id.placeModelButton).apply {
            setOnClickListener {
                placeModelNode()
                if(counter==1)
                {
                    anchor1 = true;
                    anchor1Pos = sceneView.selectedNode!!.position
                    measureModelNode()
                }
                else if(counter==2)
                {
                    placeModelNode()
                    counter++
                    anchor2 = true;
                    anchor2Pos = sceneView.selectedNode!!.position
                    calcPos()
                    placeModelButton.text = "Place Object"

                    //newModelNode()
                }
            }
        }
        placeModelButton.text = "Place anchor"
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
        if(counter <= 2)
        {
            modelNode?.anchor()
            placeModelButton.isVisible = false
            sceneView.planeRenderer.isVisible = false
        }
        else
        {
            Log.w("Counter","Model Pos = "+modelNode!!.position)
            if(quadrant == 1)
            {
                if(sceneView.selectedNode!!.position.x > anchor2Pos.x)
                {
                    Toast.makeText(this@ARActivity2, "The object doesn't fit here, try move it to the left about "+(sceneView.selectedNode!!.position.x - anchor2Pos.x)+" meter", Toast.LENGTH_SHORT).show()
                    return
                }
                else if(sceneView.selectedNode!!.position.x < anchor1Pos.x )
                {
                    Toast.makeText(this@ARActivity2, "The object doesn't fit here, try move it to the right about "+(anchor1Pos.x - sceneView.selectedNode!!.position.x)+" meter", Toast.LENGTH_SHORT).show()
                    return
                }
                else if(sceneView.selectedNode!!.position.z > anchor2Pos.z)
                {
                    Toast.makeText(this@ARActivity2, "The object doesn't fit here, try move it further from device about "+(sceneView.selectedNode!!.position.z - anchor2Pos.z)+" meter", Toast.LENGTH_SHORT).show()
                    return
                }
                else if(sceneView.selectedNode!!.position.z < anchor1Pos.z)
                {
                    Toast.makeText(this@ARActivity2, "The object doesn't fit here, try move it closer to device about "+(anchor1Pos.z - sceneView.selectedNode!!.position.z)+" meter", Toast.LENGTH_SHORT).show()
                    return
                }
            }
            else if(quadrant == 2)
            {
                if(sceneView.selectedNode!!.position.x > anchor1Pos.x)
                {
                    Toast.makeText(this@ARActivity2, "The object doesn't fit here, try move it to the left about "+(sceneView.selectedNode!!.position.x - anchor1Pos.x)+" meter", Toast.LENGTH_SHORT).show()
                    return
                }
                else if(sceneView.selectedNode!!.position.x < anchor2Pos.x)
                {
                    Toast.makeText(this@ARActivity2, "The object doesn't fit here, try move it to the right about "+(anchor2Pos.x - sceneView.selectedNode!!.position.x)+" meter", Toast.LENGTH_SHORT).show()
                    return
                }
                else if(sceneView.selectedNode!!.position.z > anchor2Pos.z)
                {
                    Toast.makeText(this@ARActivity2, "The object doesn't fit here, try move it further from device about "+(sceneView.selectedNode!!.position.z - anchor2Pos.z)+" meter", Toast.LENGTH_SHORT).show()
                    return
                }
                else if(sceneView.selectedNode!!.position.z < anchor1Pos.z)
                {
                    Toast.makeText(this@ARActivity2, "The object doesn't fit here, try move it closer to device about "+(anchor1Pos.z - sceneView.selectedNode!!.position.z)+" meter", Toast.LENGTH_SHORT).show()
                    return
                }

            }
            else if(quadrant == 3)
            {
                if(sceneView.selectedNode!!.position.x > anchor1Pos.x)
                {
                    Toast.makeText(this@ARActivity2, "The object doesn't fit here, try move it to the left about "+(sceneView.selectedNode!!.position.x - anchor1Pos.x)+" meter", Toast.LENGTH_SHORT).show()
                    return
                }
                else if(sceneView.selectedNode!!.position.x < anchor2Pos.x)
                {
                    Toast.makeText(this@ARActivity2, "The object doesn't fit here, try move it to the right about "+(anchor2Pos.x - sceneView.selectedNode!!.position.x)+" meter", Toast.LENGTH_SHORT).show()
                    return
                }
                else if(sceneView.selectedNode!!.position.z > anchor1Pos.z)
                {
                    Toast.makeText(this@ARActivity2, "The object doesn't fit here, try move it further from device about "+(sceneView.selectedNode!!.position.z - anchor1Pos.z)+" meter", Toast.LENGTH_SHORT).show()
                    return
                }
                else if(sceneView.selectedNode!!.position.z < anchor2Pos.z)
                {
                    Toast.makeText(this@ARActivity2, "The object doesn't fit here, try move it closer to device about "+(anchor2Pos.z - sceneView.selectedNode!!.position.z)+" meter", Toast.LENGTH_SHORT).show()
                    return
                }

            }
            else if(quadrant == 4)
            {
                if(sceneView.selectedNode!!.position.x > anchor2Pos.x)
                {
                    Toast.makeText(this@ARActivity2, "The object doesn't fit here, try move it to the left about "+(sceneView.selectedNode!!.position.x - anchor2Pos.x)+" meter", Toast.LENGTH_SHORT).show()
                    return
                }
                else if(sceneView.selectedNode!!.position.x < anchor1Pos.x )
                {
                    Toast.makeText(this@ARActivity2, "The object doesn't fit here, try move it to the right about "+(anchor1Pos.x - sceneView.selectedNode!!.position.x)+" meter", Toast.LENGTH_SHORT).show()
                    return
                }
                else if(sceneView.selectedNode!!.position.z > anchor1Pos.z)
                {
                    Toast.makeText(this@ARActivity2, "The object doesn't fit here, try move it further from device about "+(sceneView.selectedNode!!.position.z - anchor1Pos.z)+" meter", Toast.LENGTH_SHORT).show()
                    return
                }
                else if(sceneView.selectedNode!!.position.z < anchor2Pos.z)
                {
                    Toast.makeText(this@ARActivity2, "The object doesn't fit here, try move it closer to device about "+(anchor2Pos.z - sceneView.selectedNode!!.position.z)+" meter", Toast.LENGTH_SHORT).show()
                    return
                }
            }
            modelNode?.anchor()
            placeModelButton.isVisible = false
            sceneView.planeRenderer.isVisible = false
        }
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
        if (model.PKM?.varianceList!!.size>1)
        {
            productvar.visibility = View.VISIBLE
        }
        productKotlinName.text = model.PKM.name
        productKotlinPrice.text = "RM"+ model.PKM.price
        productKotlinColour.text = model.PKM.colour
        model.PKM.let { productToCart(pkm = it) }

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

    fun showVarianceList()
    {
        val varList = cartMap["variance"] as MutableList<String>
        val charSequenceArray = varList.map { it as CharSequence }.toTypedArray()
        val builder = AlertDialog.Builder(this@ARActivity2)
        builder.setTitle("Choose Variance Model")
        builder.setItems(
            charSequenceArray
        ) { dialog, which ->
            addMoreVar(charSequenceArray[which])
        }
        builder.create().show()
    }

    fun createCustomAlertDialog(context: Context) {
        val dialogView: View = LayoutInflater.from(context).inflate(R.layout.context_alert_dialog_layout, null)

        val imageView1: ImageView = dialogView.findViewById(R.id.onDragIV)
        val textView1: TextView = dialogView.findViewById(R.id.onDragTV)

        val imageView2: ImageView = dialogView.findViewById(R.id.onTouchIV)
        val textView2: TextView = dialogView.findViewById(R.id.onTouchTV)

        val imageView3: ImageView = dialogView.findViewById(R.id.onRotateIV)
        val textView3: TextView = dialogView.findViewById(R.id.onRotateTV)

        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setView(dialogView)
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }


        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun onItemnotFit(context: Context)
    {
        val dialogView: View = LayoutInflater.from(context).inflate(R.layout.context_alert_product_unfit, null)
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setView(dialogView)
        alertDialogBuilder.setPositiveButton("Resize designated space ")
        { dialog, _ ->
            dialog.dismiss()
            finish();
            startActivity(intent);
        }
        alertDialogBuilder.setNegativeButton("Browse other items")
        { dialog, _ ->
            dialog.dismiss()
            val intent = Intent(this@ARActivity2, HomePageActivity::class.java)
            startActivity(intent)
        }


        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun addMoreVar(a: CharSequence)
    {
        for( k in models.indices)
        {
            if(models[k].name == cartMap["productName"])
            {
                val mode = models[k]
                models.removeAt(k)
                ll.removeViewAt(k)
                for(l in categoryModelList.indices)
                {
                    if(categoryModelList.get(l).name == mode.name)
                    {
                        val vl = categoryModelList.get(l).varianceList!![capitalize(a.toString())] as Map<*, *>
                        val vla = vl["url_3d"]
                        val list: MutableList<String> = vl["img_url"] as MutableList<String>
                        categoryModelList.get(l).colour = capitalize(a.toString())
                        models.add(k,Model(
                            categoryModelList.get(l).name.toString(),
                            fileLocation = vla as String,
                            scaleUnits = 1f,
                            placementMode = if(categoryModelList.get(l).type=="Clock") PlacementMode.PLANE_VERTICAL else PlacementMode.PLANE_HORIZONTAL,
                            applyPoseRotation = true,
                            PKM = categoryModelList.get(l),
                        ))
                        val a = ImageButton(this@ARActivity2)
                        a.contentDescription = k.toString()
                        Glide.with(this@ARActivity2).load(list[0]).centerCrop()
                            .into(a)
                        a.setOnClickListener {
                            newModelNode2(a)
                        }
                        ll.addView(a,llm)
                    }
                }
            }
        }
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
        modelNode?.takeIf { !it.isAnchored || it.isAnchored }?.let {
            sceneView.removeChild(it)
            it.destroy()
        }
        val model = models[modelIndex]
        if(!removeAnc)
        {
            models.clear()
            models.add(model)
            removeAnc=true
        }
        modelIndex = (modelIndex + 1) % models.size
        modelNode = ArModelNode(sceneView.engine, PlacementMode.BEST_AVAILABLE).apply {
            isSmoothPoseEnable = true
            isScaleEditable = false
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
                sceneView.planeRenderer.isVisible = anchor == null
            }
            onHitResult = { node, _ ->
                placeModelButton.isGone = !node.isTracking
                Log.w("AR TEST","onHit")
                sceneView.selectedNode = node
                nodestatus.text = "Current selected model = "+ (sceneView.selectedNode?.name ?: "")
            }
            onTap = { motion,render ->
                sceneView.selectedNode = this
                nodestatus.text = "Current selected model = "+ (sceneView.selectedNode?.name ?: "")
                productAddToCart.setText("Add to Cart")
                productAddToCart.isClickable=true
                getProductInfo(model)
                Log.w("AR TEST","onTap")
                //this.position.xyz
            }
            //onRotate
        }
        sceneView.addChild(modelNode!!)
        // Select the model node by default (the model node is also selected on tap)
        sceneView.selectedNode = modelNode
        nodestatus.isGone=false;
        nodestatus.text = "Current selected model = "+ (sceneView.selectedNode?.name +" position = " + (sceneView.selectedNode?.position ?: "")?: "");
    }

    fun newModelNode2(v: View) {
        isLoading = true
        val model = models[v.contentDescription.toString().toInt()]
        modelNode = ArModelNode(sceneView.engine, PlacementMode.BEST_AVAILABLE).apply {
            isSmoothPoseEnable = true
            isScaleEditable = false
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
                sceneView.planeRenderer.isVisible = anchor == null
            }
            onHitResult = { node, _ ->
                placeModelButton.isGone = !node.isTracking
                Log.w("AR TEST","onHit")
                sceneView.selectedNode = node
                nodestatus.text = "Current selected model = "+ (sceneView.selectedNode?.name +" position = " + (sceneView.selectedNode?.position ?: "")?: "");
            }
            onTap = { motion,render ->
                sceneView.selectedNode = this
                nodestatus.text = "Current selected model = "+ (sceneView.selectedNode?.name +" position = " + (sceneView.selectedNode?.position ?: "")?: "");
                productAddToCart.setText("Add to Cart")
                productAddToCart.isClickable=true
                getProductInfo(model)
                Log.w("AR TEST","onTap")

            }
        }
        sceneView.addChild(modelNode!!)
        // Select the model node by default (the model node is also selected on tap)
        sceneView.selectedNode = modelNode
        nodestatus.isGone=false;
        nodestatus.text = "Current selected model = "+ (sceneView.selectedNode?.name ?: "");
    }

    fun measureModelNode() {
        ++counter
        Log.w("Counter",counter.toString())
        isLoading = true
        val model = Model(
            "Anchor $counter",
            fileLocation = "models/pin.gltf",
            scaleUnits = 0.5f,
            placementMode = PlacementMode.PLANE_HORIZONTAL,
            applyPoseRotation = true,
            PKM = null,
        )
        modelNode = ArModelNode(sceneView.engine, PlacementMode.BEST_AVAILABLE).apply {
            isSmoothPoseEnable = true
            isScaleEditable = false
            applyPoseRotation = model.applyPoseRotation
            name = model.name
            loadModelGlbAsync(
                glbFileLocation = model.fileLocation,
                autoAnimate = false,
                scaleToUnits = model.scaleUnits,
                centerOrigin = Position(),// Position(x = 0.0f, y = 0.0f, z = 0.0f)
            ) {
                sceneView.planeRenderer.isVisible = true
                isLoading = false
            }
            onAnchorChanged = { anchor ->
                placeModelButton.isGone = anchor != null
                sceneView.planeRenderer.isVisible = anchor == null
            }
            onHitResult = { node, _ ->
                placeModelButton.isGone = !node.isTracking
                sceneView.selectedNode = node
                nodestatus.text = "Current "+model.name +" position = " + (sceneView.selectedNode?.position ?: "")
            }
            onTap = { motion,render ->
                sceneView.selectedNode = this
                nodestatus.text = "Current "+model.name +" position = " + (sceneView.selectedNode?.position ?: "")
            }
        }

        sceneView.addChild(modelNode!!)
        sceneView.selectedNode = modelNode
        nodestatus.isGone=false;
        nodestatus.text = "Current "+model.name +" position = " + (sceneView.selectedNode?.position ?: "")
    }

    fun calcPos() {
        Log.w("Counter 1", anchor1Pos.toString())
        Log.w("Counter 2", anchor2Pos.toString())
        var tempL = 0f
        var tempW = 0f
        if (anchor2Pos.x > anchor1Pos.x && anchor2Pos.z > anchor1Pos.z) {
            //first quadrant
            //x +ve z +ve
            tempL = anchor2Pos.x - anchor1Pos.x
            tempW = anchor2Pos.z - anchor1Pos.z
            quadrant = 1
            Log.w("Counter", "1st Quadrant")
        } else if (anchor2Pos.x > anchor1Pos.x && anchor2Pos.z < anchor1Pos.z) {
            //second quadrant
            //x +ve z -ve
            tempL = anchor1Pos.x - anchor2Pos.x
            tempW = anchor2Pos.z - anchor1Pos.z
            quadrant = 2
            Log.w("Counter", "2nd Quadrant")
        } else if (anchor2Pos.x < anchor1Pos.x && anchor2Pos.z < anchor1Pos.z) {
            //third quadrant
            //x -ve z -ve
            tempL = anchor1Pos.x - anchor2Pos.x
            tempW = anchor1Pos.z - anchor2Pos.z
            quadrant = 3
            Log.w("Counter", "3rd Quadrant")
        } else if (anchor2Pos.x < anchor1Pos.x && anchor2Pos.z > anchor1Pos.z) {
            //fourth quadrant
            //x -ve z +ve
            tempL = anchor2Pos.x - anchor1Pos.x
            tempW = anchor1Pos.z - anchor2Pos.z
            quadrant = 4
            Log.w("Counter", "4th Quadrant")
        }
        Log.w("Counter", "Anchor Length = $tempL")
        Log.w("Counter", "Anchor Width = $tempW")
        if (isFit(abs(tempL), abs(tempW))) {
            Toast.makeText(
                this@ARActivity2,
                "Place object inside the anchor parameter",
                Toast.LENGTH_SHORT
            ).show()
            newModelNode()
        } else {
            onItemnotFit(this@ARActivity2)
        }
    }

    fun isFit(l:Float , w:Float): Boolean
    {
        Log.w("Counter", "Product Length = "+models[0].PKM!!.length)
        Log.w("Counter", "Product Width = "+models[0].PKM!!.width)
        if(models[0].PKM!!.length > l)
        {
            return false
        }
        else if(models[0].PKM!!.width > w)
        {
            return false
        }
        return true
    }

    fun lineNode(x:Float , y:Float, z:Float, rotate:Float) {
        isLoading = true
        val model = Model(
            "Line",
            fileLocation = "models/line.gltf",
            scaleUnits = 0.5f*z,
            placementMode = PlacementMode.PLANE_HORIZONTAL,
            applyPoseRotation = true,
            PKM = null,
        )
        modelNode = ArModelNode(sceneView.engine, PlacementMode.BEST_AVAILABLE).apply {
            isSmoothPoseEnable = true
            isScaleEditable = false
            applyPoseRotation = model.applyPoseRotation
            name = model.name
            loadModelGlbAsync(
                glbFileLocation = model.fileLocation,
                autoAnimate = true,
                scaleToUnits = model.scaleUnits,
                centerOrigin = Position(x,y,z),// Position(x = 0.0f, y = 0.0f, z = 0.0f)
            ) {
                isLoading = false
                placeModelButton.isVisible = false
                sceneView.planeRenderer.isVisible = false
            }
        }
        sceneView.addChild(modelNode!!)
        placeModelNode()
        modelNode!!.rotation = Rotation(rotate,0f,0f)
    }
}