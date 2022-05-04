package no.nav.medlemskap.sykepenger.lytter

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import no.nav.medlemskap.sykepenger.paragrafikode.MedlemskapVurdertConsumer
import no.nav.medlemskap.sykepenger.paragrafikode.config.Environment
import no.nav.medlemskap.sykepenger.paragrafikode.naisLiveness
import org.slf4j.Logger
import org.slf4j.LoggerFactory


fun main() {
    Application().start()
}

class Application(private val env: Environment = System.getenv(),
                  private val medlemskapVurdertConsumer: MedlemskapVurdertConsumer = MedlemskapVurdertConsumer(env)
) {
    companion object {
        val log: Logger = LoggerFactory.getLogger(Application::class.java)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun start() {
        log.info("Start application")
        val consumeJob = medlemskapVurdertConsumer.flow().launchIn(GlobalScope)

        naisLiveness(consumeJob).start(wait = true)
    }
}