package com.simulation.dice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.simulation.dice.domain.Sequence;

@Component
public class SequenceGenerator {
	
	@Autowired
	private MongoOperations mongoOperations;


    public Long getNextSequenceId(String key) {
        Query query = new Query(Criteria.where("id").is(key));
        Update update = new Update();
        update.inc("sequence", 1);

        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);

        Sequence sequence = mongoOperations.findAndModify(query, update, options, Sequence.class);

        if (sequence == null) {
            sequence = setSequenceId(key, 1L);
        }

        return sequence.getCurrent();
    }

    public void cleanSequences(String key) {
        Query query = new Query(Criteria.where("id").is(key));
        if (mongoOperations.exists(new Query(), Sequence.class)) {
            mongoOperations.remove(query, Sequence.class);
        }
    }

    public Sequence setSequenceId(String key, Long id) {
        Sequence sequence = new Sequence();
        sequence.setId(key);
        sequence.setCurrent(id);
        mongoOperations.save(sequence);
        return sequence;
    }

    public void updateSequence(String key, Long delta) {
        Query query = new Query(Criteria.where("id").is(key));
        Update update = new Update();
        update.inc("sequence", delta);

        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);

        mongoOperations.findAndModify(query, update, options, Sequence.class);
    }

}