@file:Suppress("DEPRECATION")

package dev.shreyaspatil.celllocationfinder.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoWcdma
import android.telephony.TelephonyManager
import dev.shreyaspatil.celllocationfinder.model.request.Cell
import dev.shreyaspatil.celllocationfinder.model.request.CellInfo
import dev.shreyaspatil.celllocationfinder.model.request.RadioType

@SuppressLint("MissingPermission")
fun getCurrentCellInfo(context: Context): List<CellInfo> {
    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val allCellInfo = telephonyManager.allCellInfo

    return allCellInfo.mapNotNull {
        when (it) {
            is CellInfoGsm -> getCellInfo(it)
            is CellInfoWcdma -> getCellInfo(it)
            is CellInfoLte -> getCellInfo(it)
            else -> null
        }
    }
}

fun getCellInfo(info: CellInfoGsm): CellInfo {
    val cellInfo = CellInfo()
    cellInfo.radio = RadioType.GSM

    info.cellIdentity.let {
        val (mcc, mnc) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Pair(it.mccString?.toInt() ?: 0, it.mncString?.toInt() ?: 0)
        } else {
            Pair(it.mcc, it.mnc)
        }
        cellInfo.mcc = mcc
        cellInfo.mnc = mnc
        cellInfo.cells = listOf(Cell(it.lac, it.cid, it.psc))
    }

    return cellInfo
}

fun getCellInfo(info: CellInfoWcdma): CellInfo {
    val cellInfo = CellInfo()

    cellInfo.radio = RadioType.CDMA

    info.cellIdentity.let {
        val (mcc, mnc) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Pair(it.mccString?.toInt() ?: 0, it.mncString?.toInt() ?: 0)
        } else {
            Pair(it.mcc, it.mnc)
        }
        cellInfo.mcc = mcc
        cellInfo.mnc = mnc
        cellInfo.cells = listOf(Cell(it.lac, it.cid, it.psc))
    }

    return cellInfo
}

fun getCellInfo(info: CellInfoLte): CellInfo {
    val cellInfo = CellInfo()

    cellInfo.radio = RadioType.LTE

    info.cellIdentity.let {
        val (mcc, mnc) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Pair(it.mccString?.toInt() ?: 0, it.mncString?.toInt() ?: 0)
        } else {
            Pair(it.mcc, it.mnc)
        }
        cellInfo.mcc = mcc
        cellInfo.mnc = mnc
        cellInfo.cells = listOf(Cell(it.tac, it.ci, it.pci))
    }

    return cellInfo
}
