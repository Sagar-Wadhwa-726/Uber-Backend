package com.sagarw.project.uber.uberApp;

import com.sagarw.project.uber.uberApp.services.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UberApplicationTests {

    @Autowired
    private EmailSenderService emailSenderService;


	@Test
	void contextLoads() {
        emailSenderService.sendEmail("libis32887@fanlvr.com",
                "This is the testing Email",
                "Body of the mail !");
	}

    @Test
    void sendEmailMultiple() {
        String[] emails = {
                "libis32887@fanlvr.com",
                "sagarwadhwa726@yahoo.com"
        };
        emailSenderService.sendEmail(emails, "Hello from Uber Application Demo",
                "This is a good boy, keep coding !");
    }
}
