package com.example.richmindep.alertemergency

import android.content.Intent
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.requestPermissions
import android.widget.ArrayAdapter
import android.widget.Toast


import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    var map: HashMap<String, String>? = null
    var contacts: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        map = HashMap()
        contacts = ArrayList()


        requestPermissions(this@MainActivity, arrayOf(Manifest.permission.CALL_PHONE), 1)


        try {

            val scan = Scanner(resources.openRawResource(R.raw.emergencynumbers))
            while (scan.hasNextLine()) {
                val line = scan.nextLine()
                val parts: List<String> = line.split("\t")
                map!!.put(parts[0], parts[1])
                contacts!!.add(parts[0])
            }
        }catch (e: Exception) {
            //error
        }

        val adaptor = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, contacts)

        contactsListView.adapter = adaptor

        contactsListView.setOnItemClickListener { parent, _, position, _ ->
            val contactClicked = parent.getItemAtPosition(position).toString()
            val phoneNumber = map!!.get(contactClicked)

            val phoneIntent = Intent(Intent.ACTION_CALL)
            phoneIntent.data = Uri.parse("tel:"+phoneNumber)

            if (ActivityCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Please Enable Call Permissions", Toast.LENGTH_SHORT).show()
            }
            startActivity(phoneIntent)
        }
    }
}
