package dev.graansma.tunebot_android

import android.util.Log
import com.stealthcopter.networktools.SubnetDevices
import com.stealthcopter.networktools.subnet.Device
import kotlin.collections.ArrayList

abstract class Network {
    private var devices: Set<String> = mutableSetOf()
    private var isScanning: Boolean = false
    private var scanner: SubnetDevices? = null
    private val listener = object : SubnetDevices.OnSubnetDeviceFound {
        override fun onDeviceFound(device: Device?) {
            if(device != null) {
                Log.i("Network", "mac: ${device.mac}, ip: ${device.ip}, hostname: ${device.hostname}")
            }
        }

        override fun onFinished(devicesFound: ArrayList<Device>?) {
            devices = (devicesFound?.map { it.mac }?.toSet() ?: setOf())
            onScanComplete(devices)
            isScanning = false
            scanner = null
        }
    }

    fun startScan() {
        if(!isScanning) {
            isScanning = true
            scanner = SubnetDevices.fromLocalAddress().findDevices(listener)
        }
    }

    fun endScan() {
        scanner?.cancel()
        isScanning = false
        scanner = null
    }

    fun isConnected(mac: String): Boolean {
        return devices.contains(mac)
    }

    abstract fun onScanComplete(macs: Set<String>)
}