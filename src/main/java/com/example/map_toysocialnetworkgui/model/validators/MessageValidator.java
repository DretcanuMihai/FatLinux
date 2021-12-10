package com.example.map_toysocialnetworkgui.model.validators;

import com.example.map_toysocialnetworkgui.model.entities.Message;

import java.util.HashSet;
import java.util.List;

public class MessageValidator implements Validator<Message> {


    static private final int maxSize=100;

    /**
     * validates a message
     * @param entity - said entity
     * @throws ValidationException - if the message is invalid =
     * - if toEmails has emails that repeat or the sender email is in the toEmails
     * - if its text is a null pointer or exceeds maximum size
     */
    @Override
    public void validate(Message entity) throws ValidationException {
        String message="";
        Integer id=entity.getId();
        String fromEmail=entity.getFromEmail();
        List<String> toEmails=entity.getToEmails();
        String text=entity.getMessageText();
        if(id==null)
            message+="Invalid id for message! Must be non null;\n";
        if(toEmails.size()!= new HashSet<>(toEmails).size() || toEmails.contains(fromEmail))
            message+="Invalid list of recipient emails! Emails must be different and shouldn't contain the sender" +
                    " email;\n";
        if(text==null || text.length()>maxSize)
            message+="Invalid message text! Must be non null and not have more than "+maxSize+" characters;\n";
        if(message.length()!=0){
            throw new ValidationException(message);
        }
    }

    /**
     * validates a message id
     * @param id - said id
     * @throws ValidationException if the id is null
     */
    public void validateID(Integer id) throws ValidationException {
        String message="";
        if(id==null)
            message+="Invalid id for message! Must be non null;\n";
        if(message.length()!=0){
            throw new ValidationException(message);
        }
    }

}
