package rocks.zipcode.io.web.rest;

import com.codahale.metrics.annotation.Timed;
import rocks.zipcode.io.domain.Message;
import rocks.zipcode.io.service.MessageService;
import rocks.zipcode.io.web.rest.errors.BadRequestAlertException;
import rocks.zipcode.io.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Message.
 */
@RestController
@RequestMapping("/api")
public class MessageResource {

    private final Logger log = LoggerFactory.getLogger(MessageResource.class);

    private static final String ENTITY_NAME = "message";

    private final MessageService messageService;

    public MessageResource(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * POST  /messages : Create a new message.
     *
     * @param message the message to create
     * @return the ResponseEntity with status 201 (Created) and with body the new message, or with status 400 (Bad Request) if the message has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/messages")
    @Timed
    public ResponseEntity<Message> createMessage(@RequestBody Message message) throws URISyntaxException {
        log.debug("REST request to save Message : {}", message);
        if (message.getId() != null) {
            throw new BadRequestAlertException("A new message cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Message result = messageService.save(message);
        return ResponseEntity.created(new URI("/api/messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /messages : Updates an existing message.
     *
     * @param message the message to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated message,
     * or with status 400 (Bad Request) if the message is not valid,
     * or with status 500 (Internal Server Error) if the message couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/messages")
    @Timed
    public ResponseEntity<Message> updateMessage(@RequestBody Message message) throws URISyntaxException {
        log.debug("REST request to update Message : {}", message);
        if (message.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Message result = messageService.save(message);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, message.getId().toString()))
            .body(result);
    }

    /**
     * GET  /messages : get all the messages.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of messages in body
     */
    @GetMapping("/messages")
    @Timed
    public List<Message> getAllMessages(@RequestParam(required = false) String filter) {
        if ("appuser-is-null".equals(filter)) {
            log.debug("REST request to get all Messages where appUser is null");
            return messageService.findAllWhereAppUserIsNull();
        }
        log.debug("REST request to get all Messages");
        return messageService.findAll();
    }

    /**
     * GET  /messages/:id : get the "id" message.
     *
     * @param id the id of the message to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the message, or with status 404 (Not Found)
     */
    @GetMapping("/messages/{id}")
    @Timed
    public ResponseEntity<Message> getMessage(@PathVariable Long id) {
        log.debug("REST request to get Message : {}", id);
        Optional<Message> message = messageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(message);
    }

    /**
     * DELETE  /messages/:id : delete the "id" message.
     *
     * @param id the id of the message to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/messages/{id}")
    @Timed
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        log.debug("REST request to delete Message : {}", id);
        messageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
