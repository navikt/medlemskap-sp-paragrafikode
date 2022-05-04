package no.nav.medlemskap.sykepenger.paragrafikode

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.time.delay
import mu.KotlinLogging
import no.nav.medlemskap.sykepenger.paragrafikode.config.Environment
import no.nav.medlemskap.sykepenger.paragrafikode.config.KafkaConfig
import no.nav.medlemskap.sykepenger.paragrafikode.domain.MedlemskapVurdertRecord
import no.nav.medlemskap.sykepenger.paragrafikode.jackson.MedlemskapVurdertParser
import no.nav.medlemskap.sykepenger.paragrafikode.service.MedlemskapVurdertHandler


import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration

class MedlemskapVurdertConsumer(
    environment: Environment,
    private val handler:MedlemskapVurdertHandler = MedlemskapVurdertHandler(),
    private val config: KafkaConfig = KafkaConfig(environment) ,
    private val consumer: KafkaConsumer<String, String> = config.createMedlemskapVurdertConsumer(),
)
{
    private val secureLogger = KotlinLogging.logger("tjenestekall")
    private val logger = KotlinLogging.logger { }

    init {
        consumer.subscribe(listOf(config.medlemskapVurdertTopic))
    }

    fun pollMessages(): List<MedlemskapVurdertRecord> =

        consumer.poll(Duration.ofSeconds(4))
            .map {
                MedlemskapVurdertRecord(
                    it.partition(),
                    it.offset(),
                    it.value(),
                    it.key(),
                    it.topic(),
                    MedlemskapVurdertParser().parse(it.value())
                )
            }
            .also {
                Metrics.incReceivedvurderingTotal(it.count())
            }

    fun flow(): Flow<List<MedlemskapVurdertRecord>> =
        flow {
            while (true) {

                if (config.medlemskapvurdert_enabled != "Ja") {
                    logger.debug("Kafka is disabled. Does not fetch messages from topic")
                    emit(emptyList<MedlemskapVurdertRecord>())
                } else {
                    emit(pollMessages())
                }

                delay(Duration.ofSeconds(1))
            }
        }
            .onEach {
                it.forEach { record -> handler.handle(record) }
        }.onEach {
            consumer.commitAsync()
        }.onEach {
            Metrics.incProcessedVurderingerTotal(it.count())
        }

}