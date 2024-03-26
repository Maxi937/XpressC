package models.datasource

data class RecordSet(val name: String, var data: List<Table> = ArrayList()) {
    var activeRecordSet = data[0]

    fun reset() {
        activeRecordSet = data[0]
    }


    fun isEod(): Boolean {
        return activeRecordSet.name == "EOF"
    }

    fun isNotEod(): Boolean {
        return activeRecordSet.name != "EOF"
    }

    fun getIndex(): Int {
        return data.indexOf(activeRecordSet)
    }

    fun getDbField(name: String): String? {
        return activeRecordSet.getField(name)
    }

    fun recordSetMoveNext() {
        val curIndx = data.indexOf(activeRecordSet)
        activeRecordSet = data[curIndx + 1]
    }
}