package no.nav.medlemskap.sykepenger.paragrafikode

import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.Metrics
import io.micrometer.core.instrument.Timer
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.Counter
import java.util.concurrent.atomic.AtomicInteger

object Metrics {
    val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT, CollectorRegistry.defaultRegistry, Clock.SYSTEM)

    fun incReceivedvurderingTotal(count: Int = 1) =
        receivedVurderingerTotal.inc(count.toDouble())


    fun incProcessedVurderingerTotal(count: Int = 1) =
        processedVurderingerTotal.inc(count.toDouble())

    private val receivedVurderingerTotal: Counter = Counter.build()
        .name("medlemskap_sp_paragrafikode_vurderinger_received")
        .help("Totalt mottatte vurdernger")
        .register()


    private val processedVurderingerTotal: Counter = Counter.build()
        .name("medlemskap_sp_paragrafikode_vurderinger_processed_counter")
        .help("Totalt prosesserte vurderinger")
        .register()

}
