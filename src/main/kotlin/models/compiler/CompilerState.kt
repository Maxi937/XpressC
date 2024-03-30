package models.compiler

import interfaces.Var
import interfaces.AssetProviderInterface
import models.datasource.DataSource
import models.datasource.RecordSet

data class CompilerState(
    val variables: ArrayList<Var>,
    val dataSource: DataSource,
    val activeRecordSet: RecordSet,
    val assetProvider: AssetProviderInterface
)
