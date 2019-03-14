package com.company;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class ReaderDOM {

    public TaskList getTaskList() {
        TaskList taskList = new TaskList();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("resource/login.xml"));


        }
        catch (ParserConfigurationException e){
            e.printStackTrace();
        }
        catch (SAXException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return taskList;
    }
}
