package com.tees.s3186984.whereabout.wutils

class Helpers(){

    companion object {
        fun isValidEmail(email: String): Boolean {
            val emailPattern = Regex(
                "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
            )
            return emailPattern.matches(email)
        }

        fun isValidPassword(password: String): Boolean {
            val minLength = 8
            val specialCharPattern = Regex("[^A-Za-z0-9]")
            return password.length >= minLength && specialCharPattern.containsMatchIn(password)
        }

        fun isValidFullName(fullName: String): Boolean {
            return fullName.isNotBlank() &&
                    fullName.all { it.isLetter() || it.isWhitespace() } &&
                    fullName.length in 3..20
        }

        fun isValidPhrase(securityQuestion: String): Boolean {
            return securityQuestion.isNotBlank() &&
                    securityQuestion.length in 5..100 &&
                    securityQuestion.contains(" ")
        }

    }
}





