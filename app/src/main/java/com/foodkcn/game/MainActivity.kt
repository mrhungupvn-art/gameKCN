package com.foodkcn.game

import android.app.Activity
import android.graphics.*
import android.os.Bundle
import android.view.*
import android.widget.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

private const val API_BASE = "https://game.foodkcn.com/api/"

class MainActivity : Activity() {
    private val executor = Executors.newSingleThreadExecutor()
    private var token: String? = null
    private var characterId: Int? = null
    private var root: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLogin()
    }

    override fun onDestroy() {
        executor.shutdownNow()
        super.onDestroy()
    }

    private fun baseLayout(): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(24, 24, 24, 24)
        setBackgroundColor(Color.rgb(17, 24, 39))
    }

    private fun title(text: String) = TextView(this).apply {
        this.text = text
        textSize = 28f
        setTextColor(Color.WHITE)
        setPadding(0, 0, 0, 20)
    }

    private fun input(hint: String) = EditText(this).apply {
        this.hint = hint
        setTextColor(Color.WHITE)
        setHintTextColor(Color.LTGRAY)
        setSingleLine(true)
        setPadding(18, 12, 18, 12)
    }

    private fun button(text: String, action: () -> Unit) = Button(this).apply {
        this.text = text
        setOnClickListener { action() }
    }

    private fun showLogin() {
        val box = baseLayout()
        box.addView(title("🏭 GAME KCN"))
        box.addView(TextView(this).apply { text = "Xây dựng cuộc sống của chính bạn trong KCN"; setTextColor(Color.LTGRAY); textSize = 16f })
        val user = input("Tên tài khoản")
        val pass = input("Mật khẩu")
        box.addView(user); box.addView(pass)
        box.addView(button("ĐĂNG NHẬP") { auth(false, user.text.toString(), pass.text.toString(), null, null, null) })
        box.addView(button("TẠO TÀI KHOẢN GAME") { showRegister() })
        setContentView(box)
    }

    private fun showRegister() {
        // Registration is wrapped in a ScrollView because the app runs in
        // landscape. This keeps the submit button visible/accessible on small screens.
        val scroll = ScrollView(this).apply {
            isFillViewport = true
            setBackgroundColor(Color.rgb(17, 24, 39))
        }
        val box = baseLayout().apply {
            layoutParams = ScrollView.LayoutParams(-1, -2)
            setPadding(24, 20, 24, 32)
        }

        box.addView(title("Tạo tài khoản Game KCN"))
        box.addView(TextView(this).apply {
            text = "Tạo tài khoản riêng cho Game KCN. Sau khi đăng ký, bạn sẽ chọn KCN và bắt đầu cuộc sống của nhân vật."
            setTextColor(Color.LTGRAY)
            textSize = 15f
            setPadding(0, 0, 0, 16)
        })

        val user = input("Tên tài khoản")
        val pass = input("Mật khẩu")
        pass.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        val name = input("Tên nhân vật")

        val gender = Spinner(this).apply {
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item,
                arrayOf("nam", "nu"))
        }
        val role = Spinner(this).apply {
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item,
                arrayOf("cong_nhan", "nguoi_ban"))
        }
        val personality = Spinner(this).apply {
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item,
                arrayOf("cham_chi", "nang_dong", "than_thien", "tham_vong"))
        }

        fun addLabeledField(label: String, view: View) {
            box.addView(TextView(this).apply {
                text = label
                setTextColor(Color.WHITE)
                textSize = 14f
                setPadding(0, 8, 0, 4)
            })
            box.addView(view, LinearLayout.LayoutParams(-1, 52).apply {
                bottomMargin = 6
            })
        }

        box.addView(user)
        box.addView(pass)
        box.addView(name)
        addLabeledField("Giới tính", gender)
        addLabeledField("Vai trò", role)
        addLabeledField("Tính cách", personality)

        val registerButton = button("ĐĂNG KÝ & TẠO NHÂN VẬT") {
            val username = user.text.toString().trim()
            val password = pass.text.toString()
            val characterName = name.text.toString().trim()
            if (username.isBlank() || password.isBlank() || characterName.isBlank()) {
                toast("Vui lòng nhập tài khoản, mật khẩu và tên nhân vật")
                return@button
            }
            auth(
                true,
                username,
                password,
                characterName,
                gender.selectedItem?.toString() ?: "nam",
                role.selectedItem?.toString() ?: "cong_nhan",
                personality.selectedItem?.toString() ?: "cham_chi"
            )
        }.apply {
            textSize = 17f
            minHeight = 60
            isAllCaps = false
        }
        box.addView(registerButton, LinearLayout.LayoutParams(-1, 64).apply {
            topMargin = 16
            bottomMargin = 12
        })

        box.addView(button("← Quay lại đăng nhập") { showLogin() }, LinearLayout.LayoutParams(-1, 56))

        box.addView(TextView(this).apply {
            text = "Bạn chưa cần tài khoản Food KCN. Game KCN sử dụng tài khoản riêng."
            setTextColor(Color.GRAY)
            textSize = 13f
            setPadding(0, 18, 0, 12)
        })

        scroll.addView(box)
        setContentView(scroll)
    }

    private fun auth(register: Boolean, user: String, pass: String, name: String?, gender: String?, role: String? = null, personality: String? = null) {
        if (user.isBlank() || pass.isBlank() || (register && name.isNullOrBlank())) return toast("Vui lòng nhập đủ thông tin")
        val body = JSONObject().apply {
            put("username", user); put("password", pass)
            if (register) { put("characterName", name); put("gender", gender ?: "nam"); put("role", role ?: "cong_nhan"); put("personality", personality ?: "cham_chi") }
        }
        api(if (register) "auth/register" else "auth/login", "POST", body) { json ->
            token = json.optString("token", null)
            characterId = json.optInt("characterId").takeIf { it > 0 }
            if (token.isNullOrBlank()) return@api toast("API không trả token")
            showKcnSelection()
        }
    }

    private fun showKcnSelection() {
        val box = baseLayout()
        box.addView(title("Chọn KCN"))
        val status = TextView(this).apply { text = "Đang tải danh sách KCN..."; setTextColor(Color.LTGRAY) }
        box.addView(status)
        api("kcn", "GET", null) { json ->
            box.removeView(status)
            val data = json.optJSONArray("data") ?: JSONArray()
            for (i in 0 until data.length()) {
                val k = data.getJSONObject(i)
                val id = k.optInt("id")
                val b = button("🏭 ${k.optString("name")}") {
                    api("kcn/$id/join", "POST", null) { showGame() }
                }
                box.addView(b)
            }
        }
        setContentView(box)
    }

    private fun showGame() {
        val frame = FrameLayout(this)
        val game = KcnWorldView()
        frame.addView(game, FrameLayout.LayoutParams(-1, -1))
        val panel = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(12, 8, 12, 8)
            setBackgroundColor(0xDD111827.toInt())
        }
        val actions = listOf(
            "💼 Làm việc" to { work() },
            "🪙 Ví" to { loadWallet() },
            "🛵 Xe" to { loadVehicles() },
            "🏠 Nhà" to { loadHouses() },
            "🛒 Mua sắm" to { loadShop() },
            "⭐ Kỹ năng" to { loadSkills() },
            "❤️ Kết duyên" to { loadRomance() }
        )
        actions.forEach { (text, action) -> panel.addView(button(text, action).apply { textSize = 11f }) }
        frame.addView(panel, FrameLayout.LayoutParams(-1, -2, Gravity.TOP))
        setContentView(frame)
    }

    private fun work() {
        api("career", "GET", null) { json ->
            val arr = json.optJSONArray("data") ?: JSONArray()
            if (arr.length() == 0) return@api toast("Chưa có công việc")
            val c = arr.getJSONObject(0)
            val id = c.optInt("id")
            api("career/$id/apply", "POST", null) {
                api("career/$id/work", "POST", null) { r -> toast("💰 Nhận ${r.optJSONObject("data")?.optInt("salary") ?: 0} Xu") }
            }
        }
    }

    private fun loadWallet() { api("wallet", "GET", null) { r -> toast("🪙 Xu: ${r.optJSONObject("data")?.optInt("coin") ?: 0}") } }

    private fun loadVehicles() {
        api("vehicle", "GET", null) { r ->
            val a = r.optJSONArray("data") ?: JSONArray()
            if (a.length() == 0) return@api toast("Chưa có xe")
            val v = a.getJSONObject(0)
            val id = v.optInt("id")
            toast("🚲 ${v.optString("name")} • ${v.optInt("price")} Xu")
            api("vehicle/$id/buy", "POST", null) { toast("Đã mua phương tiện") }
        }
    }

    private fun loadHouses() {
        api("house", "GET", null) { r ->
            val a = r.optJSONArray("data") ?: JSONArray()
            if (a.length() > 0) {
                val h = a.getJSONObject(0); api("house/${h.optInt("id")}/buy", "POST", null) { toast("🏠 Đã mua ${h.optString("name")}") }
            }
        }
    }

    private fun loadShop() {
        api("shop/products", "GET", null) { r ->
            val a = r.optJSONArray("data") ?: JSONArray()
            if (a.length() > 0) {
                val p = a.getJSONObject(0)
                val body = JSONObject().put("productId", p.optInt("id")).put("quantity", 1)
                api("shop/buy", "POST", body) { toast("🛒 Đã mua ${p.optString("name")}") }
            }
        }
    }

    private fun loadSkills() { api("skill", "GET", null) { r -> toast("⭐ Kỹ năng đã tải") } }
    private fun loadRomance() { api("romance/candidates", "GET", null) { r -> toast("❤️ Có ${(r.optJSONArray("data")?.length() ?: 0)} nhân vật để tìm hiểu") } }

    private fun api(path: String, method: String, body: JSONObject?, callback: (JSONObject) -> Unit) {
        val authToken = token
        executor.execute {
            try {
                val conn = URL(API_BASE + path).openConnection() as HttpURLConnection
                conn.requestMethod = method
                conn.connectTimeout = 10000
                conn.readTimeout = 15000
                conn.setRequestProperty("Accept", "application/json")
                conn.setRequestProperty("Content-Type", "application/json")
                if (!authToken.isNullOrBlank()) conn.setRequestProperty("Authorization", "Bearer $authToken")
                if (body != null) { conn.doOutput = true; conn.outputStream.use { it.write(body.toString().toByteArray()) } }
                val stream = if (conn.responseCode in 200..299) conn.inputStream else conn.errorStream
                val text = stream.bufferedReader().use { it.readText() }
                val json = JSONObject(text)
                runOnUiThread {
                    if (!json.optBoolean("success", false)) toast(json.optString("message", "API lỗi")) else callback(json)
                }
            } catch (e: Exception) {
                runOnUiThread { toast("Không kết nối được game.foodkcn.com") }
            }
        }
    }

    private fun toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

    private inner class KcnWorldView : View(this@MainActivity) {
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        override fun onDraw(c: Canvas) {
            super.onDraw(c)
            c.drawColor(Color.rgb(226, 232, 240))
            val w = width.toFloat(); val h = height.toFloat()
            paint.color = Color.rgb(190, 220, 180)
            c.drawRect(0f, 90f, w, h, paint)
            drawIsoBuilding(c, w * .25f, h * .45f, 170f, 110f, Color.rgb(59, 130, 246), "🏭 CÔNG TY")
            drawIsoBuilding(c, w * .62f, h * .42f, 150f, 100f, Color.rgb(234, 179, 8), "🏪 CỬA HÀNG")
            drawIsoBuilding(c, w * .45f, h * .70f, 130f, 90f, Color.rgb(168, 85, 247), "🏠 NHÀ")
            paint.color = Color.rgb(30, 64, 175)
            c.drawCircle(w * .50f, h * .56f, 18f, paint)
            paint.color = Color.DKGRAY; paint.textSize = 22f
            c.drawText("👤", w * .50f - 14, h * .56f + 8, paint)
            paint.textSize = 18f
            paint.color = Color.rgb(17,24,39)
            c.drawText("KCN LIFE", 18f, h - 22f, paint)
        }
        private fun drawIsoBuilding(c: Canvas, x: Float, y: Float, bw: Float, bh: Float, color: Int, label: String) {
            val top = Path().apply { moveTo(x, y-bh); lineTo(x+bw/2, y-bh+35); lineTo(x, y-bh+70); lineTo(x-bw/2, y-bh+35); close() }
            paint.color = color; c.drawPath(top, paint)
            val left = Path().apply { moveTo(x-bw/2,y-bh+35); lineTo(x,y-bh+70); lineTo(x,y); lineTo(x-bw/2,y-35); close() }
            paint.color = color.darker(); c.drawPath(left, paint)
            val right = Path().apply { moveTo(x,y-bh+70); lineTo(x+bw/2,y-bh+35); lineTo(x+bw/2,y-35); lineTo(x,y); close() }
            paint.color = color.lighter(); c.drawPath(right, paint)
            paint.color = Color.WHITE; paint.textSize = 18f; c.drawText(label, x-bw/2, y+28f, paint)
        }
    }

    private fun Int.darker(): Int = Color.rgb((Color.red(this)*.75).toInt(), (Color.green(this)*.75).toInt(), (Color.blue(this)*.75).toInt())
    private fun Int.lighter(): Int = Color.rgb(minOf(255,(Color.red(this)*1.2).toInt()), minOf(255,(Color.green(this)*1.2).toInt()), minOf(255,(Color.blue(this)*1.2).toInt()))
}
