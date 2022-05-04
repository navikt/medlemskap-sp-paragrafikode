package no.nav.medlemskap.sykepenger.paragrafikode.domain

import java.time.LocalDate
import java.time.LocalDateTime

data class LovmeSoknadDTO(
    val id: String,
    val type: SoknadstypeDTO,
    val status: SoknadsstatusDTO,
    val fnr: String,
    val korrigerer: String? = null,
    val startSyketilfelle: LocalDate,
    val sendtNav: LocalDateTime,
    val fom: LocalDate,
    val tom: LocalDate,
    // Kun True eller False hvis bruker har svar JA eller NEI.
    val arbeidUtenforNorge: Boolean? = null)

enum class SoknadsstatusDTO {
    NY,
    SENDT,
    FREMTIDIG,
    KORRIGERT,
    AVBRUTT,
    SLETTET
}
enum class SoknadstypeDTO {
    SELVSTENDIGE_OG_FRILANSERE,
    OPPHOLD_UTLAND,
    ARBEIDSTAKERE,
    ANNET_ARBEIDSFORHOLD,
    ARBEIDSLEDIG,
    BEHANDLINGSDAGER,
    REISETILSKUDD,
    GRADERT_REISETILSKUDD,
}