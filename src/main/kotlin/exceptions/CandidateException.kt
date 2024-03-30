package exceptions

// Exceptions where the Candidate file cannot be read and sourced into a Datasource
class CandidateException(override val message: String?) : Exception(message) {
}