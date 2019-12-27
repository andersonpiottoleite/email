package com.email.email;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@SpringBootApplication
public class EmailApplication implements CommandLineRunner {

	@Autowired
	private JavaMailSender javaMailSender;
	 
	public static void main(String[] args) {
		SpringApplication.run(EmailApplication.class, args);
	}
	
	@Override
    public void run(String... args) throws MessagingException, IOException {
        System.out.println("Sending Email...");

        //sendEmail();
		sendEmailWithAttachment();

        System.out.println("Done");
    }
    // em caso de problemas http://www.codersdesks.com/javax-mail-authenticationfailedexception-535-5-7-8-username-and-password-not-accepted/
	// lista de emails: //http://listadeemailmarketinggratis.blogspot.com/2013/10/lista-1-6857-e-mails.html
		
    private void sendEmail() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("piottok10@gmail.com");

        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World \n Spring Boot Email");

        javaMailSender.send(msg);
    }
    
    private void sendEmailWithAttachment() throws MessagingException, IOException {
    	File arquivoEmails  = new File("C:\\Users\\anderson.leite\\Desktop\\emails2.txt");
    	
    	Scanner scanner  = new Scanner(arquivoEmails);
    	
    	StringBuilder emails = new StringBuilder();
    	
    	int qtdeLimitEmails = 50; 
    	
    	while (scanner.hasNext()) {
    		emails.append(scanner.nextLine());
    		
			if(scanner.hasNext()) {
				emails.append(",");
			}
			
			if(emails.length() > qtdeLimitEmails) {
				
				envia(emails);
				
				emails.setLength(0);
			}
		}
    	
    	envia(emails);
    	
    	scanner.close();
    }

	private void envia(StringBuilder emails) throws MessagingException, AddressException {
        MimeMessage msg = javaMailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setFrom("casadoevangelho2018@gmail.com");
        
       // helper.setTo("casadoevangelho2018@gmail.com");
        
        // esconde os de mais destinarários
        helper.setBcc(InternetAddress.parse(emails.toString()));

        helper.setSubject("Casa do Evangelho | Reflexão | A glória da segunda casa!");

        // default = text/plain
        //helper.setText("Check attachment for image!");

        // true = text/html
        helper.setText(getConteudoEmail(), true);
        
        //enviar anexo
        //FileSystemResource foto = new FileSystemResource("C:\\Users\\anderson.leite\\Desktop\\LogoCasaDoEvangelho.jpg");
        // helper.addAttachment("LogoCasaDoEvangelho.jpg", foto);
        
        helper.addInline("LogoCasaDoEvangelho", new File("C:\\Users\\anderson.leite\\Desktop\\Imagens\\Logo andersona casa do en.jpg"));
        
        javaMailSender.send(msg);
	}

	private String getConteudoEmail() {
		return "<html>"
    		
			+ "<body>"
				
				+ "<h2 style='color:black font-family: 'Verdana', Times, serif;'>Olá!</h2>"
				
				+ "<h1 style='color:black font-family: 'Georgia', Times, serif;'>Somos a <b style='color:#CD6600'><i>Casa do Evangelho</i></b>, uma família que pela graça, vive e prega o verdadeiro Evangelho de <b style='color:#CD6600'><i>Jesus Cristo</i></b>!</h1>"
				
				+ "</br>"
				
				+ "<h3 style='color:black font-family: 'Lucida Console', Times, serif;'>Queremos te convidar para assitir uma de nossas reflexões:</h3>"
				
				+ "</br>"
				
				+ "<a style='color:#CD6600' href=\"https://www.youtube.com/watch?v=avyiSZs4-Lw&feature=youtu.be\"><h3><b style='color:#CD3333;'>YouTube</b> - A glória da segunda casa!</h3></a>"
				
				+ "<h4 style='color:black font-family: 'Lucida Console', Times, serif;'>De seu <b style='color:#CD6600'><i>LIKE</i></b>, se <b style='color:#CD6600'><i>increva</i></b> em nosso Canal e <b style='color:#CD6600'><i>compartilhe!</i></b></h4>"
				
				+ "</br>"
				
				+ "</br>"
				
				+ "<h4 style='color:black font-family: 'Lucida Console', Times, serif;'>Deus te abençoe!</h4>"

				+ "<img src='cid:LogoCasaDoEvangelho' style='float:left;width:200px;height:200px;'/>"
				
			+ "</body>"
			
		+"</html>";
	}

}
