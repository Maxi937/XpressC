package models.CandidateXml

data class RecordSet(val name: String, val data: List<Table> = ArrayList()) {
    private var activeRecordSet = data[0]

    fun isEod(): Boolean {
        val currentIndx = data.indexOf(activeRecordSet)

        if(currentIndx + 1 > data.size) {
            return true
        }
        return false
    }

    fun isNotEod(): Boolean {
        val currentIndx = data.indexOf(activeRecordSet)

        if(currentIndx + 1 < data.size) {
            return true
        }
        return false
    }

    fun getDbField(name: String): String? {
        return activeRecordSet.getField(name)
    }
    fun recordSetMoveNext() {
        data.forEach {
            println(it)
        }
        val curIndx = data.indexOf(activeRecordSet)
        activeRecordSet = data[curIndx + 1]
    }
}