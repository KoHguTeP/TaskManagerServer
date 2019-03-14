package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static ObjectInputStream in;
    private static ObjectOutputStream out;


    public static void main(String[] args) {
        // write your code here
        int port = 1024;

        try {
            /*serverSocket = new ServerSocket(port);
            System.out.println("Сервер запущен");*/
            while (true) {
                try {
                    serverSocket = new ServerSocket(port);
                    System.out.println("Сервер запущен");
                    System.out.println("Ожидание клиента");

                    clientSocket = serverSocket.accept();
                    System.out.println("Подключение установлено");
                    System.out.println("Выполнение программы");

                    try {
                        in = new ObjectInputStream(clientSocket.getInputStream());
                        out = new ObjectOutputStream(clientSocket.getOutputStream());

                        try {
                            Document documentLogin = (Document) in.readObject();
                            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                            documentLogin.getDocumentElement().normalize();

                            Document documentSource = documentBuilder.parse(new File("src/resources/users.xml"));


                            if (((Element) documentLogin.getElementsByTagName("action").item(0)).getAttribute("id").equals("login")) {
                                String login = ((Element) documentLogin.getElementsByTagName("action").item(0)).getAttribute("login");
                                String password = ((Element) documentLogin.getElementsByTagName("action").item(0)).getAttribute("password");
                                //String response = Control.login(((Element) documentLogin.getElementsByTagName("action").item(0)).getAttribute("login"), ((Element) documentLogin.getElementsByTagName("action").item(0)).getAttribute("password"));
                                int k = 0;
                                boolean check = false;
                                Node node;
                                Element element;
                                while ((k  < documentSource.getElementsByTagName("user").getLength()) && (!check)) {
                                    if (login.equals(((Element) documentSource.getElementsByTagName("user").item(k)).getAttribute("login"))) {
                                        check = true;
                                        if (password.equals(((Element) documentSource.getElementsByTagName("user").item(k)).getAttribute("password"))) {

                                            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                                            DocumentBuilder builder = factory.newDocumentBuilder();
                                            Document document = builder.parse(new File("src/resources/" + login + ".xml"));
                                            document.getDocumentElement().normalize();


                                            for (int i = 0; i < document.getElementsByTagName("task").getLength(); i++) {
                                                Task task = new Task(
                                                ((Element) document.getElementsByTagName("task").item(i)).getAttribute("name"),
                                                        ((Element) document.getElementsByTagName("task").item(i)).getAttribute("description"),
                                                        ((Element) document.getElementsByTagName("task").item(i)).getAttribute("date"),
                                                        ((Element) document.getElementsByTagName("task").item(i)).getAttribute("time")
                                                );
                                                System.out.println("check");
                                            }


                                            out.writeObject(document);

                                            /*Document document = documentBuilder.parse(new File("src/resources/" + login + ".xml"));
                                            document.getDocumentElement().normalize();
                                            element = (Element) document.getElementsByTagName("tasks").item(0);*/
                                            //out.writeObject(element);
                                        }
                                        else out.writeObject("Неверный пароль");
                                    }
                                    k++;
                                }
                                if (!check) {
                                    out.writeObject("Неверный логин");
                                }
                                out.flush();
                            }
                            else if (((Element) documentLogin.getElementsByTagName("action").item(0)).getAttribute("id").equals("registration")) {
                                String login = ((Element) documentLogin.getElementsByTagName("action").item(0)).getAttribute("login");
                                String password = ((Element) documentLogin.getElementsByTagName("action").item(0)).getAttribute("password");
                                int i = 0;
                                boolean check = false;
                                while ((i < documentSource.getElementsByTagName("user").getLength()) && (!check)){
                                    if (login.equals(((Element) documentSource.getElementsByTagName("user").item(i)).getAttribute("login"))) {
                                        check = true;
                                    }
                                    i++;
                                }
                                if (check) {
                                    out.writeObject(false);//ПРОВЕРИТЬ
                                }
                                else {//Создание нового пользователя
                                    Node node = documentSource.getElementsByTagName("users").item(0);
                                    Element element = documentSource.createElement("user");
                                    element.setAttribute("login", login);
                                    element.setAttribute("password", password);
                                    element.setAttribute("filename", login + ".xml");
                                    node.appendChild(element);
                                    TransformerFactory.newInstance().newTransformer().transform(new DOMSource(documentSource), new StreamResult("src/resources/users.xml"));

                                    //Создание файла со списком задач для этого пользователя
                                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                                    factory.setNamespaceAware(true);
                                    Document doc = factory.newDocumentBuilder().newDocument();

                                    Element root = doc.createElement("tasks");
                                    doc.appendChild(root);

                                    File file = new File("src/resources/" + login + ".xml");

                                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                                    transformer.transform(new DOMSource(doc), new StreamResult(file));

                                    out.writeObject(true);
                                }
                            }

                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        }
                        catch (TransformerException e){
                            e.printStackTrace();
                        }
                    } finally {
                        clientSocket.close();
                        serverSocket.close();
                        in.close();
                        out.close();
                    }
                } finally {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

