package com.ami.webservice;
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * @author Michal Gajdos
 */
@ApplicationPath("/")
public class MyApplication extends ResourceConfig {

    public MyApplication() {
 //       super(ImageProcessing.class, MultiPartFieldInjectedResource.class, MultiPartFeature.class);
    }
}