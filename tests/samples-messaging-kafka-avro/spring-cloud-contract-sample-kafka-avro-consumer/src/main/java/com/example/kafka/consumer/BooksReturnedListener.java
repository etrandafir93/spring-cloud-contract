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

package com.example.kafka.consumer;

import com.example.kafka.avro.Book;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
class BooksReturnedListener {

	private final EmailService emailService;

	BooksReturnedListener(EmailService emailService) {
		this.emailService = emailService;
	}

	@KafkaListener(topics = "book.returned")
	public void sendEmailOnBookReturned(Book book) {
		String emailBody = """
				Dear User,

				The book you borrowed has been successfully returned:
				Title: %s, Author: %s, ISBN: %s

				""".formatted(book.getTitle(), book.getAuthor(), book.getIsbn());

		emailService.sendEmail(emailBody);
	}

}
