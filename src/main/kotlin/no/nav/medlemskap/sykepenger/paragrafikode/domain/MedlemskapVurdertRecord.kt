package no.nav.medlemskap.sykepenger.paragrafikode.domain

import com.fasterxml.jackson.databind.JsonNode

data class MedlemskapVurdertRecord(val partition:Int,val offset:Long,val value : String, val key:String?,val topic:String,val medlemskapVurdert:JsonNode)
