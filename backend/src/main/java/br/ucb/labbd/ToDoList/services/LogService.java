package br.ucb.labbd.ToDoList.services;

import org.springframework.stereotype.Service;
import br.ucb.labbd.ToDoList.repositories.SessionRepository;
import br.ucb.labbd.ToDoList.entities.SessionDoc;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.bson.Document;
import java.time.Instant;

@Service
public class LogService {
    private final MongoTemplate mongoTemplate;

    public LogService(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public void log(String userId, String action, String details){
        Document doc = new Document();
        doc.append("userId", userId);
        doc.append("action", action);
        doc.append("details", details);
        doc.append("timestamp", Instant.now().toString());
        mongoTemplate.getCollection("app_logs").insertOne(doc);
    }
}
