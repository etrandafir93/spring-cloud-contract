package org.springframework.cloud.contract.stubrunner

import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierSender
import spock.lang.Specification


import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.verify

class StubRunnerExecutorSendMessageSpec extends Specification {

	private MessageVerifierSender msgVerifierSender
	private StubRunnerExecutor stubRunnerExecutor

	def setup() {
		msgVerifierSender = Mockito.mock(MessageVerifierSender)
		stubRunnerExecutor = new StubRunnerExecutor(new AvailablePortScanner(1, 1_000), msgVerifierSender, [])
	}

	def "send message passing a Map"() {
		given:
		var dsl = Contract.make {
			outputMessage {
				sentTo 'dummy-destination'
				body(
						foo: 'bar'
				)
				headers {
					header('dummy-header-key', 'dummy-header-value')
				}
			}
		}

		when:
		stubRunnerExecutor.sendMessage(dsl)

		then:
		verify(msgVerifierSender).send(
				eq('{"foo":"bar"}'),
				eq(['dummy-header-key': 'dummy-header-value']),
				eq('dummy-destination'),
				ArgumentMatchers.any()
		)
	}

	def "send message passing a String"() {
		given:
		var dsl = Contract.make {
			outputMessage {
				sentTo 'dummy-destination'
				body(
						'dummy String body'
				)
				headers {
					header('dummy-header-key', 'dummy-header-value')
				}
			}
		}

		when:
		stubRunnerExecutor.sendMessage(dsl)

		then:
		verify(msgVerifierSender).send(
				eq('"dummy String body"'),
				eq(['dummy-header-key': 'dummy-header-value']),
				eq('dummy-destination'),
				ArgumentMatchers.any()
		)
	}

}
