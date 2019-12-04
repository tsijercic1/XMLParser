package com.github.tsijercic1;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class XMLParserTest {
    @Test
    @Order(1)
    void simpleXMLRead() {
        try {
            XMLParser xmlParser = new XMLParser("src/test/test-resources/simple.xml");
            Node root = xmlParser.getDocumentRootNode();
            assertAll(()->
            {
                assertEquals("root", root.getName());
                assertEquals("child",root.getChildNodes().get(0).getName());
                assertEquals("grandchild",root.getChildNodes().get(0).getChildNodes().get(0).getName());
            });
        } catch (IOException | InvalidXMLException e) {
            fail();
        }
    }

    @Test
    @Order(2)
    void doesItDetectInvalidTagClosing() {
        try {
            XMLParser xmlParser = new XMLParser("src/test/test-resources/invalidTagClosing.xml");
            Node root = xmlParser.getDocumentRootNode();
            fail();
        } catch (IOException | InvalidXMLException e) {
            assertTrue(true);
        }
    }

    @Test
    @Order(3)
    void doesItDetectTagCrossing() {
        try {
            XMLParser xmlParser = new XMLParser("src/test/test-resources/tagCrossing.xml");
            Node root = xmlParser.getDocumentRootNode();
            fail();
        } catch (IOException | InvalidXMLException e) {
            assertTrue(true);
        }
    }

    @Test
    @Order(4)
    void doesItReadAttributes() {
        try {
            XMLParser xmlParser = new XMLParser("src/test/test-resources/attributes.xml");
            Node root = xmlParser.getDocumentRootNode();
            StringBuilder keyValuePairs = new StringBuilder();
            for (Node child : root.getChildNodes()) {
                Set<String> keys = child.getAttributeKeys();
                for(String key : keys){
                    keyValuePairs.append(key).append("=").append(child.getAttributes().get(key));
                }
            }
            assertAll(()->
            {
                String temp = keyValuePairs.toString();
                assertTrue(temp.contains("one=1"));
                temp = temp.replace("one=1", "");
                assertTrue(temp.contains("two=2"));
                temp = temp.replace("two=2", "");
                assertTrue(temp.contains("three=3"));
                temp = temp.replace("three=3", "");
                assertTrue(temp.contains("four=4"));
                temp = temp.replace("four=4", "");
                assertTrue(temp.contains("five=5"));
                temp = temp.replace("five=5", "");
                assertTrue(temp.contains("six=6"));
                temp = temp.replace("six=6", "");
                assertTrue(temp.contains("seven=7"));
                temp = temp.replace("seven=7", "");
                assertTrue(temp.contains("eight=8"));
                temp = temp.replace("eight=8", "");
                assertTrue(temp.contains("nine=9"));
                temp = temp.replace("nine=9", "");
                assertTrue(temp.contains("ten=10"));
                temp = temp.replace("ten=10", "");
                assertTrue(temp.contains("eleven=11"));
                temp = temp.replace("eleven=11", "");
                assertTrue(temp.contains("twelwe=12"));
                temp = temp.replace("twelwe=12", "");
                assertEquals("",temp);
            });
        } catch (IOException | InvalidXMLException e) {
            fail();
        }
    }

    @Test
    @Order(5)
    void doesItDetectInvalidQuotes() {
        try {
            XMLParser xmlParser = new XMLParser("src/test/test-resources/invalidQuotes.xml");
            Node root = xmlParser.getDocumentRootNode();
            fail();
        } catch (IOException | InvalidXMLException e) {
            assertTrue(true);
        }
    }

    @Test
    void doesItReadAValidStructure() {
        try {
            XMLParser xmlParser = new XMLParser("src/test/test-resources/people.xml");
            Node root = xmlParser.getDocumentRootNode();
            assertAll(()->
            {
                assertEquals("people",root.getName());
                ArrayList<Node> children = root.getChildNodes();
                assertEquals(2, children.size());
                Map<String, String> secondPersonAttributes = children.get(1).getAttributes();
                String secondPerson = secondPersonAttributes.get("name") + " " + secondPersonAttributes.get("surname");
                assertEquals("Mark Salt", secondPerson);
                assertEquals("Berlin", children.get(1).getChildNode("HomeCity").getContent());
            });
        } catch (IOException | InvalidXMLException e) {
            fail();
        }
    }
}