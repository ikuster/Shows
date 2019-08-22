package i.kuster.ui.shared

const val PASSWORD_MIN=5
const val EPISODE_TITLE_MIN=2
const val EPISODE_DESCRIPTION_MIN=50

object Validations {

    fun isUsernameInputValid(username: String): Pair<Boolean, String> {
        return if (username == "") Pair(false, "Username must not be empty")
        else if (!EmailRegexCheck.isValid(username)) Pair(false, "Username is not in the correct format")
        else Pair(true, "")
    }

    fun isPasswordInputValid(password: String): Pair<Boolean, String> {
        return if (password == "") Pair(false, "Password must not be empty")
        else if (password.length < PASSWORD_MIN) Pair(false, "Password must contain at least five characters")
        else Pair(true, "")
    }

    fun isEpisodeTitleInputValid(title: String): Pair<Boolean, String> {
        return if (title == "") Pair(false, "Title must not be empty")
        else if (title.length < EPISODE_TITLE_MIN) Pair(false, "Title must contain at least two characters")
        else Pair(true, "")
    }

    fun isEpisodeDescriptionInputValid(description: String): Pair<Boolean, String> {
        return if (description == "") Pair(false, "Description must not be empty")
        else if (description.length < EPISODE_DESCRIPTION_MIN) Pair(false, "Description must contain at least fifty characters")
        else Pair(true, "")
    }

    fun confirmPasswordCheck(passwordFirst: String, passwordSecond: String): Pair<Boolean, String> {
        return if (passwordFirst != passwordSecond) Pair(false, "Password and confirm password should be the same")
        else Pair(true, "")

    }
    fun isCommentValid(comment:String):Pair<Boolean,String>{
        if(comment=="") {
            return Pair(false,"Comment must not be empty!")
        }
        return Pair(true,"")
    }
}

