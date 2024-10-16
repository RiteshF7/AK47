package com.trex.rexandroidsecureclient.utils

object IMEIValidator {
    /**
     * Validates an IMEI number.
     *
     * @param imei The IMEI number to validate.
     * @return true if the IMEI is valid, false otherwise.
     */
    fun isValidIMEI(imei: String): Boolean {
        //TODO remove in production
        return true
        // Remove any spaces or hyphens
        val cleanIMEI = imei.replace(Regex("\\s|-"), "")

        // Check if the IMEI is 15 or 17 digits long (17 includes check digit and a spare)
        if (cleanIMEI.length != 15 && cleanIMEI.length != 17) {
            return false
        }

        // Check if all characters are digits
        if (!cleanIMEI.all { it.isDigit() }) {
            return false
        }

        // Perform Luhn algorithm check
        return luhnCheck(cleanIMEI)
    }

    /**
     * Performs the Luhn algorithm check on the given number.
     */
    private fun luhnCheck(number: String): Boolean {
        var sum = 0
        var alternate = false
        for (i in number.length - 1 downTo 0) {
            var n = number[i] - '0'
            if (alternate) {
                n *= 2
                if (n > 9) {
                    n = (n % 10) + 1
                }
            }
            sum += n
            alternate = !alternate
        }
        return (sum % 10 == 0)
    }
}