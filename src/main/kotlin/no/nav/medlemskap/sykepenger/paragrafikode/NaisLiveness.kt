package no.nav.medlemskap.sykepenger.paragrafikode

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.metrics.micrometer.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.exporter.common.TextFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.io.Writer

fun naisLiveness(medlemskalConsumerJob:Job) = embeddedServer(Netty, applicationEngineEnvironment {
    connector { port = 8080 }
    module {

        install(MicrometerMetrics) {
            registry = Metrics.registry
        }

        routing {
            get("/isAlive") {
                if (medlemskalConsumerJob.isActive) {
                    call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
                } else {
                    call.respondText("Not alive :(", ContentType.Text.Plain, HttpStatusCode.InternalServerError)
                }
            }
            get("/isReady") {
                call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
            }
            get("/metrics") {
                call.respondTextWriter(ContentType.parse(TextFormat.CONTENT_TYPE_004)) {
                    writeMetrics004(this, Metrics.registry)
                }
            }
        }
    }
})

suspend fun writeMetrics004(writer: Writer, registry: PrometheusMeterRegistry) {
    withContext(Dispatchers.IO) {
        kotlin.runCatching {
            TextFormat.write004(writer, registry.prometheusRegistry.metricFamilySamples())
        }
    }
}

