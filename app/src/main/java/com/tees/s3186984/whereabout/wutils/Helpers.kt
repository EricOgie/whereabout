package com.tees.s3186984.whereabout.wutils

class Helpers(){

    companion object {

        /**
         * Validates an email address format.
         *
         * @param email The email address to validate.
         * @return `true` if the email matches the expected pattern; `false` otherwise.
         */
        fun isValidEmail(email: String): Boolean {
            val emailPattern = Regex(
                "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
            )
            return emailPattern.matches(email)
        }


        /**
         * Validates a password for minimum length 8 and presence of a special character.
         *
         * @param password The password to validate.
         * @return `true` if the password meets the criteria; `false` otherwise.
         */
        fun isValidPassword(password: String): Boolean {
            val minLength = 8
            val specialCharPattern = Regex("[^A-Za-z0-9]")
            return password.length >= minLength && specialCharPattern.containsMatchIn(password)
        }


        /**
         * Validates a full name, allowing only letters and spaces, within a length range.
         *
         * @param fullName The full name to validate.
         * @return `true` if the full name meets the criteria; `false` otherwise.
         */
        fun isValidFullName(fullName: String): Boolean {
            return fullName.isNotBlank() &&
                    fullName.all { it.isLetter() || it.isWhitespace() } &&
                    fullName.length in 3..20
        }

        /**
         * Validates a phrase within a length range.
         *
         * @param phrase The security phrase to validate.
         * @return `true` if the phrase meets the criteria; `false` otherwise.
         */
        fun isValidPhrase(phrase: String): Boolean {
            return phrase.isNotBlank() &&
                    phrase.length in 5..100
        }


        fun capitalizeWords(input: String): String {
            return input.split(" ")
                .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
        }


    }
}





