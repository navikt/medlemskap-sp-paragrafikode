package no.nav.medlemskap.sykepenger.paragrafikode.service


import com.fasterxml.jackson.databind.JsonNode

import mu.KotlinLogging
import no.nav.medlemskap.sykepenger.paragrafikode.domain.MedlemskapVurdertRecord


class MedlemskapVurdertHandler(
) {
    companion object {
        private val log = KotlinLogging.logger { }

    }

    fun handle(vurdertRecord: MedlemskapVurdertRecord) {
        val ytelse =  vurdertRecord.medlemskapVurdert.get("datagrunnlag").get("ytelse").textValue()
        if ("SYKEPENGER" == ytelse){
                val newMessage = mapFromMedlemskapVurdertToParagrafIKode(vurdertRecord.medlemskapVurdert)
            }
            else{
                log.info("Dropper alle meldinger som ikke er SYKEPENGER : key ${vurdertRecord.key}, ytelse : $ytelse")
            }

    }

    private fun mapFromMedlemskapVurdertToParagrafIKode(medlemskapVurdert: JsonNode): Any {
        throw NotImplementedError("Not yet implemented")
        return "To be implemented"
    }


}
