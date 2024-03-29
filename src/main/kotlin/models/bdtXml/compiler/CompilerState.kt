package models.bdtXml.compiler

import models.bdtXml.variables.Var
import models.bdtassetprovider.AssetProviderInterface
import models.datasource.DataSource
import models.datasource.RecordSet

data class CompilerState(
    val variables: ArrayList<Var>,
    val dataSource: DataSource,
    val activeRecordSet: RecordSet,
    val assetProvider: AssetProviderInterface
)
