package com.chatTop.backend.dto.response;


public class MessageResponse {

   String message;
    /**
     * DTO pour transmettre un message de réponse dans les appels REST.
     */


    public MessageResponse(String message) {
        this.message = message;
 
    }

    public MessageResponse() {
		// TODO Auto-generated constructor stub
	}

	// Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
