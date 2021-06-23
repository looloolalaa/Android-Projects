package com.example.mymap

import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    var fusedLocationClient:FusedLocationProviderClient?=null
    var locationCallback:LocationCallback?=null
    var locationRequest:LocationRequest?=null

    lateinit var googleMap: GoogleMap
    var loc=LatLng(37.554752,126.970631)
    val arrLoc=ArrayList<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initLocation()
        //initMap()
        //initSpinner()
    }

    fun initLocation(){

        //권한 승인이 되어있다면
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            ==PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            ==PackageManager.PERMISSION_GRANTED){

            getuserLocation()
            startLocationUpdates()
            initMap()

        }else{      //권한 승인이 되어있지 않다면
            ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION),100)
        }
    }

    fun getuserLocation(){
        fusedLocationClient=LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient?.lastLocation?.addOnSuccessListener {
            loc= LatLng(it.latitude,it.longitude)
        }
    }

    override fun onRequestPermissionsResult(      //권한 요청 승인,거부에 따른 결과 작업
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==100){  //사용자가 승인 시
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED&&
                grantResults[1]==PackageManager.PERMISSION_GRANTED){

                getuserLocation()
                startLocationUpdates()
                initMap()

            }else{   //사용자가 거부 시
                Toast.makeText(this, "위치정보 제공을 하셔야 합니다.",Toast.LENGTH_SHORT).show()
                initMap()
            }
        }
    }

    fun startLocationUpdates(){

        locationRequest=LocationRequest.create()?.apply {
            interval=10000
            fastestInterval=5000
            priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback=object :LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for(location in locationResult.locations){
                    loc= LatLng(location.latitude,location.longitude)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,16.0f))
                }
            }
        }

        fusedLocationClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }

    fun stopLocationUpdates(){
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    fun initSpinner(){
        val adapter=ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_dropdown_item,ArrayList<String>())
        adapter.add("Hybrid")
        adapter.add("Normal")
        adapter.add("Satellite")
        adapter.add("Terrain")
        spinner.adapter=adapter
        spinner.setSelection(1)
        spinner.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    0->googleMap.mapType=GoogleMap.MAP_TYPE_HYBRID
                    1->googleMap.mapType=GoogleMap.MAP_TYPE_NORMAL
                    2->googleMap.mapType=GoogleMap.MAP_TYPE_SATELLITE
                    3->googleMap.mapType=GoogleMap.MAP_TYPE_TERRAIN
                }
            }

        }
    }

    fun initMap(){
        val mapFragment=supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync{
            googleMap=it
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,16.0f))
            googleMap.setMinZoomPreference(10.0f)
            googleMap.setMaxZoomPreference(18.0f)

            val options=MarkerOptions()
            options.position(loc)
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            //options.title("역")
            //options.snippet("서울역")
            val mk1=googleMap.addMarker(options)
            //mk1.showInfoWindow()

            initMapListener()
        }
    }
    fun initMapListener(){
        googleMap.setOnMapClickListener {
            googleMap.clear()
            arrLoc.add(it)
            val options=MarkerOptions()
            options.position(it)
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            googleMap.addMarker(options)
//            val lineOptions=PolylineOptions().color(Color.BLUE).addAll(arrLoc)
//            googleMap.addPolyline(lineOptions)
            val gonOptions=PolygonOptions().fillColor(Color.argb(100,0,0,255))
                .strokeColor(Color.BLUE).addAll(arrLoc)
            googleMap.addPolygon(gonOptions)
        }
    }
}
