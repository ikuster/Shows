package i.kuster.ui.shared

import android.util.Patterns

class EmailRegexCheck {
        companion object {
            fun isValid(email: String): Boolean {
                return Patterns.EMAIL_ADDRESS.toRegex().matches(email);
            }
        }
    }
