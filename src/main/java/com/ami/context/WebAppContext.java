package com.ami.context;

/**
 * Created on 11/3/2015, 9:55 PM
 * MongoDBContextListener.java
 *
 * @author akhandelwal
 */

import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.UnknownHostException;

public class WebAppContext implements ServletContextListener {

    private static Logger LOGGER = LoggerFactory.getLogger(WebAppContext.class);

   // public static final String WEBAPP_CONTEXT = "helix.context";

    public static ServletContext WEBAPP_CONTEXT = null;


    public void contextDestroyed(ServletContextEvent sce) {
        MongoClient mongo = (MongoClient) sce.getServletContext()
                .getAttribute("MONGO_CLIENT");
        mongo.close();
        LOGGER.error("MongoClient closed successfully");
    }

    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.debug("Web-App Context initializing");
        WEBAPP_CONTEXT = sce.getServletContext();
        MongoClient mongo = null;
        try {
            mongo = new MongoClient(
                    WEBAPP_CONTEXT.getInitParameter("MONGODB_HOST"),
                    Integer.parseInt(WEBAPP_CONTEXT.getInitParameter("MONGODB_PORT")));
        } catch (UnknownHostException e) {
            LOGGER.error("exception in mongo init : " + e.getMessage());
            throw new RuntimeException("MongoClient init failed");
        }
        LOGGER.debug("MongoClient initialized successfully");
        WEBAPP_CONTEXT.setAttribute("MONGO_CLIENT", mongo);
        LOGGER.debug("web-app initialization successful");

    }

}
