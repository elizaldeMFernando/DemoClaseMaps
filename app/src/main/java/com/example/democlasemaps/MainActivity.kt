package com.example.democlasemaps

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap

    companion object{
        const val REQUEST_CODE_LOCATION=0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createFragment()
    }
    private fun enableLocation(){
        if(!::map.isInitialized)return
        if(isLocationPermissionGranted())
        {
            map.isMyLocationEnabled=true
        }else{
            requestLocationPermission()
        }
    }
    private fun createFragment(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        //Este codigo se ejecutara cuando el fragment termine de cargarse
        map=googleMap
        crearMarker()
        //Tratar de Acceder a la ubicación del GPS
        enableLocation()
    }
    private fun crearMarker(){
        val coordenadas = LatLng(19.302135, -99.149138)
        val marker= MarkerOptions().position(coordenadas).title("Este es el estadio azteca")
        map.addMarker(marker)
    }
    private fun isLocationPermissionGranted()=
        ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED

    private fun requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            //Mostrar la ventan de permiso
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE_LOCATION->if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled=true
            }
            else{
                Toast.makeText(this,"Para activar permiso, ve a ajustes y acepta los permisos",Toast.LENGTH_LONG).show()
            }
            else ->{}
        }
    }
}