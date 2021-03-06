package com.utils

import java.util.regex.Pattern

object ValidationUtils {

    fun isValidImageURL(url: String?): Boolean {
        return if (url != null) {
            url.toLowerCase().contains("jpg") || url.toLowerCase().contains("jpeg") ||
                    url.toLowerCase().contains("png")
        } else false
    }

    fun isValidEmail(email: String): Boolean {
        if (email.trim { it <= ' ' }.isEmpty()) {
            return false
        }
        val regExpn = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
        val pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email.trim())
        return matcher.matches()
    }

    fun isValidPassword(password: String?): Boolean {
        return if (password != null) {
//            val regex = ("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W\\_])[A-Za-z\\d\\W\\_]{8,}\$")
//            val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
//            val matcher = pattern.matcher(password.toString().trim())
//            (matcher.matches())
            password.trim { it <= ' ' }.length >= 6
        } else false
    }

    fun isValidMobile(mobile: String?): Boolean {
        if (mobile != null) {
            return (mobile.trim { it <= ' ' }.length == 11 &&
                    mobile.trim()[0] == '0' &&
                    mobile.trim()[1] == '1')
        }
        return false
    }


    fun isValidMerchantID(text: String?): Boolean {
        if (text != null) {
            return text.trim { it <= ' ' }.length == 6
        }
        return false
    }

    fun isValidLoanAmount(text: String?): Boolean {
        if (text != null) {
            return (text.trim { it <= ' ' }.isNotEmpty() && text.trim().toDouble() > 0)
        }
        return false
    }

    fun isValidIDNumber(idNumber: String?): Triple<Boolean, String, Int> {
        var dateOfBirth = ""
        if (idNumber != null) {
            if (idNumber.trim { it <= ' ' }.length == 14) {
                if (idNumber.trim { it <= ' ' }[0] == '2' || idNumber.trim { it <= ' ' }[0] == '3') {
                    dateOfBirth = "" + idNumber.trim { it <= ' ' }[5] + "" +
                            idNumber.trim { it <= ' ' }[6] + "/" + "" + idNumber.trim { it <= ' ' }[3] +
                            "" + idNumber.trim { it <= ' ' }[4] + "/"
                    if (idNumber.trim { it <= ' ' }[0] == '2') {
                        dateOfBirth += ("19" + idNumber.trim { it <= ' ' }[1]
                                + "" + idNumber.trim { it <= ' ' }[2])
                    } else if (idNumber.trim { it <= ' ' }[0] == '3') {
                        dateOfBirth += ("20" + idNumber.trim { it <= ' ' }[1]
                                + "" + idNumber.trim { it <= ' ' }[2])
                    }
                }
                if (validateDateOfBirth(dateOfBirth)) {
                    return Triple(
                        true, dateOfBirth,
                        if (idNumber[12].toInt() % 2 == 0) 2 else 1
                    )
                }

            }
        }
        return Triple(false, dateOfBirth, 1)
    }


    fun isValidText(text: String?): Boolean {
        if (text != null) {
            if (text.trim { it <= ' ' }.isNotEmpty()) return true
        }
        return false
    }

    fun isValidHomePhone(homePhone: String?): Boolean {
        if (homePhone != null) {
            if (homePhone.trim { it <= ' ' }.length in 8..11) return true
        }
        return false
    }


    //    private static final String DATE_PATTERN = "(0?[1-9]|1[012]) [/.-] (0?[1-9]|[12][0-9]|3[01]) [/.-] ((19|20)\\d\\d)";
    private val DATE_PATTERN = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$"


    /**
     * Validate date format with regular expression
     *
     * @param date date address for validation
     * @return true valid date format, false invalid date format
     */
    fun validateDateOfBirth(date: String): Boolean {

        /** dd/MM/yyyy  */
        val matcher =
            Pattern.compile(DATE_PATTERN, Pattern.CASE_INSENSITIVE)
                .matcher(date.trim { it <= ' ' })

        if (matcher.matches()) {
            matcher.reset()

            if (matcher.find()) {
                val day = date.substring(0, 2)
                val month = date.substring(3, 5)
                val year = Integer.parseInt(date.substring(6))

                return if (day == "31" && (month == "4" || month == "6" || month == "9" ||
                            month == "11" || month == "04" || month == "06" ||
                            month == "09")
                ) {
                    false // only 1,3,5,7,8,10,12 has 31 days
                } else if (month == "2" || month == "02") {
                    //leap year
                    if (year % 4 == 0) {
                        !(day == "30" || day == "31")
                    } else {
                        !(day == "29" || day == "30" || day == "31")
                    }
                } else {
                    true
                }
            } else {
                return false
            }
        } else {
            return false
        }
    }


    fun findMaxChar(str: String): Int {

        val count = IntArray(256)

        for (i in 0 until str.length) {
            count[str[i].toInt()]++
        }

        var max = -1
        var result = ' '

        for (j in 0 until str.length) {
            if (max < count[str[j].toInt()] && count[str[j].toInt()] > 1) {
                max = count[str[j].toInt()]
                result = str[j]
            }
        }

        return max

    }

    fun isValidCVC(cvc: String): Boolean {
        return cvc.length >= 3
    }

    val USERNAME_REGEX = "^[a-zA-Z\\u0621-\\u064A].{2,30}$"
    //public static final String USERNAME_REGEX = "/^[a-zA-Z]+._-/i{3,30}$";

    fun isValidUserName(name: String): Boolean {
        val pattern = Pattern.compile(USERNAME_REGEX, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(name)
        return matcher.matches()
    }
}