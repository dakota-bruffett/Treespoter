package com.example.trees

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.GeoPoint
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [tree_map_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class tree_map_fragment : Fragment() {

private lateinit var addTreeButton: FloatingActionButton

private var locationPromissonGranted = false

private var movedMapToUserLocation = false

private var fusedLocationProvider: FusedLocationProviderClient? = null

    private var map: GoogleMap? = null

    private val TreeMarker = mutableListOf<Marker>()

    private var treelist = (listOf <Tree>())
    private val treeViewModel: Tree_View_model by lazy {
        ViewModelProvider(requireActivity()).get(treeViewModel::class.java)
    }
    private val mapCallback= OnMapReadyCallback {googleMap ->
        Log.d(TAG, "Google is ready")
        map = googleMap
        UpdateMap()
    }

    private fun UpdateMap() {
        drawTrees()
        if (locationPromissonGranted){
            if(!locationPromissonGranted) {
                moveMapToUserLocation()
            }
        }
    }
    private fun setAddTreeButtonEnabled(isEnabled: Boolean){
        addTreeButton.isClickable = isEnabled
        addTreeButton.isEnabled = isEnabled

        if (isEnabled){
            addTreeButton.backgroundTintList = AppCompatResources.getColorStateList(requireActivity(),
            android.R.color.holo_blue_light)
        }else{
            addTreeButton.backgroundTintList = AppCompatResources.getColorStateList(requireActivity(),
            android.R.color.holo_red_dark)
        }
    }
    private fun showSnackBar(message: String){
        Snackbar.make(requireView(), message,Snackbar.LENGTH_LONG)
    }
    private fun requestLocation(){
        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationPromissonGranted = true
            Log.d(TAG, "Permission Granted")
            UpdateMap()
        }else{
            val requestLocationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()
            ){granted -> if (granted){
                Log.d(TAG,"User gets permission ")
                setAddTreeButtonEnabled(true)
                showSnackBar(getString(R.string.give_Promission))
            }

            }
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mainview = inflater.inflate(R.layout.fragment_tree_map, container, false)
        addTreeButton = mainview.findViewById(R.id.floatingActionButton)
        addTreeButton.setOnClickListener{
            addTreesatLocation()
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_View) as SupportMapFragment?
        mapFragment?.getMapAsync(mapCallback)

        setAddTreeButtonEnabled(false)

        requestLocation()

        treeViewModel.latestTree.observe(requireActivity()){ latestTrees ->
            treelist = latestTrees
            drawTrees()
        }

        return mainview
    }

    private fun drawTrees() {
        if (map == null ) {return}

        for (tree in treelist) {

            for (marker in TreeMarker){
                marker.remove()
            }
            tree.location?.let { geoPoint ->

                val markerOptions = MarkerOptions()
                    .position(LatLng(geoPoint.latitude, geoPoint.longitude))
                    .title(tree.name)
                    .snippet("Spotted on ${tree.dateSpotted}")

                map?.addMarker(markerOptions)?.also { marker ->
                    TreeMarker.add(marker)
                        marker.tag = tree
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun moveMapToUserLocation(){
        if (map == null){
            return
        }
        if (locationPromissonGranted){
            map?.isMyLocationEnabled = true
            map?.uiSettings?.isMyLocationButtonEnabled = true
            map?.uiSettings?.isZoomControlsEnabled = true

            fusedLocationProvider?.lastLocation?.addOnCompleteListener{ getLocationTask ->
                val location= getLocationTask.result
                if (location!= null){
                    Log.d(TAG,"Users location $location")
                    val center= LatLng(location.latitude,location.longitude)
                    val zoomLevel = 8f
                    map?.moveCamera(CameraUpdateFactory.newLatLng(center))
                    movedMapToUserLocation= true
                }else{
                    showSnackBar(getString(R.string.no_location))
                }
            }
        }
    }
    @SuppressLint("MissingPermission")
    fun addTreesatLocation(){
        if (map == null) {return}
        if (fusedLocationProvider == null) {return}
        if (!locationPromissonGranted){
            showSnackBar(getString(R.string.Grant_Promission_Please))
            return
        }
          fusedLocationProvider?.lastLocation?.addOnCompleteListener(requireActivity()) {
              locationRequestTask ->
              val location = locationRequestTask.result
              if (location != null) {
                  val treeName = getTreeName()
                  val tree= Tree(
                      name = treeName,
                      dateSpotted = Date(),
                      location = GeoPoint(location.latitude,location.longitude)
                  )
                  treeViewModel.addTree(tree)
                  moveMapToUserLocation()
                  showSnackBar(getString(R.string.Added_Tree , treeName))
              }else{
                  showSnackBar(getString(R.string.no_location))
              }
          }
    }

    private fun getTreeName(): String {
        return listOf("fir", "oak ").random()
    }

    companion object {
        @JvmStatic
        fun newInstance() = tree_map_fragment()

    }
}