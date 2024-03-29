package models.datasource

data class RecordSet(val name: String, var data: List<Table> = ArrayList()) {
    var index: Int = 0
    var activeRecordSet = data[index]

    fun reset() {
        index = 0
        activeRecordSet = data[index]
    }

    fun isEod(): Boolean {
        return activeRecordSet.name == "EOF"
    }

    fun isNotEod(): Boolean {
        return activeRecordSet.name != "EOF"
    }

    fun getDbField(name: String): String? {
        return activeRecordSet.getField(name)
    }

    fun recordSetMoveNext() {
        index += 1
        activeRecordSet = data[index]
    }
}