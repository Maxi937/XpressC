package models.bdtXml.bdtsolver

import models.bdtXml.variables.Var
import models.bdtassetprovider.BdtAssetProvider
import models.datasource.DataSource
import models.datasource.RecordSet

data class BdtState(
    val name: String,
    val variables: ArrayList<Var>,
    val dataSource: DataSource,
    val activeRecordSet: RecordSet,
    val assetProvider: BdtAssetProvider
)
