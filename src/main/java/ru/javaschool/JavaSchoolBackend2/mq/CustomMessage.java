package ru.javaschool.JavaSchoolBackend2.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomMessage {

    private String messageId;
    private String message;
    private String messageDate;

    public CustomMessage(String message, String messageDate) {
        this.message = message;
        this.messageDate = messageDate;
    }


}
