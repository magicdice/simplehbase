package com.alipay.simplehbase.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alipay.simplehbase.exception.SimpleHBaseException;

/**
 * XmlUtil.
 * 
 * @author xinzhi
 * */
public class XmlUtil {

    /** log. */
    private static Logger log = Logger.getLogger(XmlUtil.class);

    /**
     * Find top level node.
     * */
    public static Node findTopLevelNode(InputStream inputStream, String nodeName) {
        Util.checkNull(inputStream);
        Util.checkEmptyString(nodeName);

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setIgnoringComments(true);

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputStream);
            Node rootNode = doc.getDocumentElement();
            NodeList childNodes = rootNode.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                if (childNodes.item(i).getNodeName().equals(nodeName)) {
                    return childNodes.item(i);
                }
            }
        } catch (Exception e) {
            log.error("parse error.", e);
            throw new SimpleHBaseException("parse error.", e);
        }

        return null;
    }

    /**
     * Find top level node.
     * */
    public static Node findTopLevelNode(String filePath, String nodeName) {
        Util.checkEmptyString(filePath);
        try {
            return findTopLevelNode(new BufferedInputStream(
                    new FileInputStream(new File(filePath))), nodeName);
        } catch (FileNotFoundException e1) {
            throw new SimpleHBaseException("error.", e1);
        }
    }

    /**
     * Find top level node.
     * */
    public static Node findTopLevelNodeInString(String content, String nodeName) {
        Util.checkEmptyString(content);
        Util.checkEmptyString(nodeName);

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setIgnoringComments(true);

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db
                    .parse(new ByteArrayInputStream(content.getBytes()));

            Node rootNode = doc.getDocumentElement();
            NodeList childNodes = rootNode.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                if (childNodes.item(i).getNodeName().equals(nodeName)) {
                    return childNodes.item(i);
                }
            }
        } catch (Exception e) {
            log.error("parse error.", e);
            throw new SimpleHBaseException("parse error.", e);
        }

        return null;
    }

    /**
     * Get attribute node value of node or null if attribute doesn't exist.
     * */
    public static String getAttr(Node node, String attrName) {

        Util.checkNull(node);
        Util.checkEmptyString(attrName);

        NamedNodeMap columnAttrs = node.getAttributes();
        if (columnAttrs == null) {
            return null;
        }
        Node attrNode = columnAttrs.getNamedItem(attrName);
        if (attrNode == null) {
            return null;
        }
        return attrNode.getNodeValue();
    }

    private XmlUtil() {
    }
}
