package com.example.cqrs_read;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StartListener implements ApplicationListener<ApplicationStartedEvent> {
    private final BookRepository bookRepository;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        List<Book> books = bookRepository.findAll();
        MongoClient mongoClient = MongoClients.create("mongodb://adam:wnddkd@43.200.180.22:27017");
        MongoDatabase database = mongoClient.getDatabase("itstudy");
        MongoCollection<Document> mongo_books = database.getCollection("books");
        mongo_books.drop();
        mongo_books = database.getCollection("books");
        for(Book book : books) {
           Document mongoBook = new Document();
           mongoBook.append("bid", book.getBid());
           mongoBook.append("title", book.getTitle());
           mongoBook.append("author", book.getAuthor());
           mongoBook.append("category", book.getCategory());
           mongoBook.append("pages", book.getPages());
           mongoBook.append("price", book.getPrice());
           mongoBook.append("published_date", book.getPublished_date().toString());
           mongoBook.append("description", book.getDescription());
           mongo_books.insertOne(mongoBook);
        }
        mongoClient.close();
    }
}