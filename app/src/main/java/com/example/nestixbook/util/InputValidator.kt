package com.example.nestixbook.util

class InputValidator {

    companion object {
        @JvmStatic
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"

        val NAME_REGEX = "^[\\p{L} .'-]+$"

        val INPUT_REGEX = "^[A-Za-z0-9 ]+$"

        val PASS_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!\\-_?&])(?=\\S+$).{8,}"

        fun isEmailValid(email: String): Boolean {
            return EMAIL_REGEX.toRegex().matches(email);
        }

        fun isInputValid(name: String): Boolean {
            return NAME_REGEX.toRegex().matches(name);
        }

        fun isInputNameValid(name: String): Boolean {
            return INPUT_REGEX.toRegex().matches(name);
        }

        fun isInputPassValid(name: String): Boolean {
            return PASS_REGEX.toRegex().matches(name);
        }
    }

}