package api.models

import com.google.gson.annotations.SerializedName

data class XpressionDocumentModel(
    val doc_gen_sys_cd: String,
    val last_updt_dtm: String,
    val last_updt_user_id: String,
    val mdl_cd: String,
    val mdl_cstmz_ind: String,
    val mdl_dsc: String,
    val mdl_grp_cd: String,
    val mdl_loc_txt: String,
    val mdl_nm: String,
    val mdl_restrict_on_err_ind: String,
    val mdl_schema_url_txt: String,
    val mdl_send_to_xquery_ind: String
)