package exceptions

// Exception where the ContentDb cannot be read
class ContentException(override val message: String?) : Exception(message) {
}