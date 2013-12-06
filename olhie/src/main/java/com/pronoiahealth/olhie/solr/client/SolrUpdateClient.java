package com.pronoiahealth.olhie.solr.client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

import com.lowagie.text.pdf.codec.Base64;
import com.pronoiahealth.olhie.solr.xml.Book;
import com.pronoiahealth.olhie.solr.xml.BookAsset;
import com.pronoiahealth.olhie.solr.xml.BookAssetDescription;
import com.pronoiahealth.olhie.solr.xml.ObjectFactory;
import com.pronoiahealth.olhie.solr.xml.User;

public class SolrUpdateClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InitialContext ic = null;
		ConnectionFactory cf = null;
		Connection connection = null;

		try{
	        String content = encodeBase64("/home/nadelson/tmp/olhie/flyer.pdf");
	        //write(content,"/home/nadelson/tmp/olhie/content.txt");
	       
	        
			Properties env = new Properties();
	        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
	        env.put(Context.PROVIDER_URL, "remote://localhost:4447");
	        env.put(Context.SECURITY_PRINCIPAL, "guest");
	        env.put(Context.SECURITY_CREDENTIALS, "guestpw");
	        ic = new InitialContext(env);
	        
			System.out.println("Looking up ConnectionFactory");
			cf = (ConnectionFactory) ic.lookup("java:jms/RemoteConnectionFactory");
			System.out.println("Looking up Destination");
			Queue queue = (Queue) ic.lookup("jms/queue/solr");
			System.out.println("Queue queue/solr exists");

			connection = cf.createConnection("guest","guestpw");
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer sender = session.createProducer(queue);
			
			// create a Book object
			Book book = new Book();
			book.setId("1");
			book.setBookTitle("title");
			book.setIntroduction("introduction");
			book.setKeyWords("keyword");
			book.setCategory("category");
			book.setCoverName("cover");
			book.setCreatedDate("2013-11-13");
			book.setPublishedDate("2013-11-13");
			book.setAuthorId("1");
			book.setActive("true");
			
			// create a User
			User user = new User();
			user.setId("5");
			user.setFirstName("John");
			user.setLastName("Smith");
			user.setRole("admin");
			user.setResetPwd("false");
			book.getUser().add(user);
			
			// create a BookAssetDescription
			BookAssetDescription bookAssetDescription = new BookAssetDescription();
			bookAssetDescription.setId("100");
			bookAssetDescription.setDescription("camp event");
			bookAssetDescription.setRemoved("false");
			bookAssetDescription.setCreatedDate("2013-11-13");
			bookAssetDescription.setBookId("1");
			book.getBookAssetDescription().add(bookAssetDescription);
			
			// create a BookAsset
			BookAsset bookAsset = new BookAsset();
			bookAsset.setId("25");
			bookAsset.setItemName("test.pdf");
			bookAsset.setContentType("application/pdf");
			bookAsset.setSize("1000");
			bookAsset.setBase64Data(content);
			bookAsset.setBookassetdescriptionId("100");
			book.getBookAsset().add(bookAsset);
			
			
			ObjectFactory of = new ObjectFactory();
			JAXBElement<Book> element = of.createBook(book);
			
			// generate XML string with JAXB
			StringWriter writer = new StringWriter();
			JAXBContext jaxbContext = JAXBContext.newInstance("com.pronoiahealth.olhie.solr.xml");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(element, writer);
			
			// send text message to queue
			TextMessage message = session.createTextMessage(writer.toString());
			sender.send(message);
			System.out.println("The message was successfully sent to the " + queue.getQueueName() + " queue");
		} 
		catch (Exception e) {
			e.printStackTrace();

		} 
		finally {

			if (ic != null) {
				try {ic.close();}catch(Exception ignore) {}
			}
			try{connection.close();}catch(Exception e){}
		}

	}
	
	public static String encodeBase64(String path){
		
	    try {
	    	
	    	File file = new File(path);
			FileInputStream fis = new FileInputStream(file);
		    ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    byte[] buf = new byte[1024];
		    
	    	for (int readNum; (readNum = fis.read(buf)) != -1;) {
	    		bos.write(buf, 0, readNum);
	    	}
	    	byte[] bytes = bos.toByteArray();
	        return Base64.encodeBytes(bytes);
	    } 
	    catch (IOException ex) {
	       return null;     
	    }
	        
	}
	
	public static void write(String content, String path) {
		FileOutputStream fop = null;
		File file;
 
		try {
 
			file = new File(path);
			fop = new FileOutputStream(file);
 
			// if file does not exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
 
			System.out.println("Done");
 
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
