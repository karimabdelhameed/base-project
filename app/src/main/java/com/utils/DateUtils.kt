package com.utils

import android.content.ContentUris
import android.net.Uri
import android.provider.CalendarContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val format = "yyyy-MM-dd"
    private val formatter = SimpleDateFormat(format, Locale.getDefault())

    fun toDate(str: String): Date {
        var date: Date
        try {
            date = formatter.parse(str)
        } catch (e: ParseException) {
            e.printStackTrace()
            date = Date()
        }

        return date
    }

    fun toString(date: Date): String {
        return formatter.format(date)
    }

    fun toString(date: String): String {
        return formatter.format(toDate(date))
    }

    fun getYear(str: String): Int {
        val date = toDate(str)
        return getYear(date)
    }

    private fun getYear(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.YEAR)
    }


    fun getDateString(calendar: Calendar): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm")
        return sdf.format(calendar.time)
    }

    fun formatCalendar(date: String): Date {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        var convertedDate = Date()
        try {
            convertedDate = dateFormat.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return convertedDate
    }

    fun formatTime(time: String): String {

        var array = time.split(" ")
        var newTime = array[1]

        var res = ""
        try {
            val sdf = SimpleDateFormat("HH:mm")
            val dateObj = sdf.parse(time)
            res = SimpleDateFormat("hh:mm a").format(newTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return res
    }


    fun getDayName(dateStr: String): String {
        // dd/MM/yyyy
        var date: Date? = null
        try {
            date = SimpleDateFormat("dd/MM/yyyy").parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return SimpleDateFormat("EEE", Locale.ENGLISH).format(date)
    }

    fun DeleteCalendarEntry(eventID: Int) {
        var deleteUri: Uri? = null
        deleteUri = ContentUris.withAppendedId(
            CalendarContract.Events.CONTENT_URI, java.lang.Long.parseLong(
                eventID.toString()
            )
        )
        val rows = Domain.application.getContentResolver().delete(deleteUri, null, null)
    }

    fun addDurationToTime(time: String, duration: Int): String {
        val df = SimpleDateFormat("HH:mm")
        var d: Date? = null
        try {
            d = df.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val cal = Calendar.getInstance()
        cal.time = d!!
        cal.add(Calendar.MINUTE, duration)
        return df.format(cal.time)
    }

    fun getDifferenceBetweenTwoDatesInHours(start: String, end: String): Long {
        var endDate: Date? = null
        var startDate: Date? = null
        try {
            startDate = SimpleDateFormat("yyyy-MM-dd hh:mm").parse(start)
            endDate = SimpleDateFormat("yyyy-MM-dd hh:mm").parse(end)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        var different = endDate!!.time - startDate!!.time

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedDays = different / daysInMilli
        different = different % daysInMilli
/*different = different % hoursInMilli;
        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        long elapsedSeconds = different / secondsInMilli;*/

        return different / hoursInMilli + elapsedDays * 24
    }

    fun getMonthName(monthNumber: Int): String {
        val cal = Calendar.getInstance()
        lateinit var locale: Locale
        runBlocking {
            val job = CoroutineScope(IO).launch {
                locale = Locale(preferencesGateway.load(Constants.LANGUAGE, "ar"))
            }
            job.join()
        }
        val monthDate = SimpleDateFormat("MMM", locale)
        cal.set(Calendar.MONTH, monthNumber)
        return monthDate.format(cal.time)
    }

    fun addMonthsToDate(strDate: String, numberOfMonths: Int): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var date: Date? = null
        try {
            date = Date(sdf.parse(strDate).time)
            date.month = date.month + numberOfMonths
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val x = sdf.format(date)
        return sdf.format(date)
    }

    fun getCurrentDate(): String {
        return (Calendar.getInstance().get(Calendar.YEAR).toString() + "-" + Calendar.getInstance().get(
            Calendar.MONTH
        )
                + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
    }


    fun getCurrentDateName(): String {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        return year.toString() + "-" + (month + 1) + "-" + day
    }


//    fun formatDate(date: String): String {
//
//        var array = date.split(" ")
//        var newDate = array[0]
//
//        var res = ""
//        try {
//            val sdf = SimpleDateFormat("yyyy-MM-dd")
//            val dateObject = sdf.parse(newDate)
//            res = SimpleDateFormat("dd MMMM YYYY").format(dateObject)
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//        return res
//    }


    fun formatDateWithSlashes(mDate: String): String {
        // dd/MM/yyyy
        val dateArr = mDate.split("/")
        return "${dateArr[0]} ${getMonthName(dateArr[1].toInt() - 1)}" //${dateArr[2]}
    }

    fun formatDayMonthWithSlashes(mDate: String): String {
        val dateArr = mDate.split("/")
        return "${dateArr[0]} ${getMonthName(dateArr[1].toInt() - 1)}"
    }

    fun formatDayMonthOnly(mDate: String?): String {
        val dateArr = mDate?.split("-")
        return "${dateArr?.get(2)} ${getMonthName((dateArr?.get(1)?.toInt() ?: 1) - 1)}"
    }

    fun formatTransactionDate(mDate: String): String {
        val dateArr = mDate.split(" ")
        val newDate = dateArr[0]
        val dateArr2 = newDate.split("-")
        return "${dateArr2.get(2)} ${getMonthName(dateArr2.get(1).toInt() - 1)} ${dateArr2[0]} "
    }

    fun formatTransactionTimeToAMOrPM(mDate: String): String {
        val dateArr = mDate.split(" ")
        val newDate = dateArr[1]
        val dateArr2 = newDate.split(":")
        val pm = if (dateArr.get(2).toLowerCase().contains("pm")) "م" else "ص"

        return "${dateArr2[0]}:${dateArr2[1]} ${pm}"
    }

    fun formatTimeToAMOrPM(mtime: String): String {

        var array = mtime.split(" ")
        var newTime = array[1] + array[2]

        var res = ""
        try {
            val sdf = SimpleDateFormat("HH:mm")
            val dateObj = sdf.parse(newTime)
            res = SimpleDateFormat("hh:mm a", Locale("ar")).format(dateObj)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return res
    }


    fun formatDate(mDate: String): String {
        val dateArr = mDate.split(" ")
        return "${dateArr[0]} ${getMonthName(dateArr[1].toInt() - 1)} ${dateArr[2]}"
    }


    fun formatDayName(mDate: String): String {
        // dd/MM/yyyy
        var date: Date? = null
        try {
            date = SimpleDateFormat("dd/MM/yyyy").parse(mDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return SimpleDateFormat("EEE", Locale(Utils.getLocaleLanguage())).format(date)
    }

}