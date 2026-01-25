/*
 * Copyright 2013-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.kafka.producer;

import java.util.Map;

import com.example.kafka.avro.Book;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
class BookService {

	private final KafkaTemplate<String, Book> kafkaTemplate;

	BookService(KafkaTemplate<String, Book> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	void bookReturned(String isbn, String title, String author) {
		Book payload = Book.newBuilder().setIsbn(isbn).setTitle(title).setAuthor(author).build();

		// @formatter:off
		MessageHeaders headers = new MessageHeaders(Map.of(
				KafkaHeaders.TOPIC, "book.returned",
				"X-Correlation-Id", "abc-123-def",
				"X-Source-System", "library-service",
				"X-Event-Type", "BOOK_RETURNED"
		));
		// @formatter:on

		Message<Book> msg = MessageBuilder.createMessage(payload, headers);
		kafkaTemplate.send(msg);
	}

}
