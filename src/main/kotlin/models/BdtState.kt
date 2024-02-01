package models

import Actions.Subdocument.SubdocumentBdtProvider
import models.CandidateXml.DataSource
import models.Content.ContentItemsDb
import models.bdtXml.actions.Action
import models.bdtXml.variables.Var

data class BdtState(
    val variables: ArrayList<Var>,
    val dataSource: DataSource,
    val contentItemsDb: ContentItemsDb,
    val baseSequence: ArrayList<Action>,
    val bdtProvider: SubdocumentBdtProvider
)
