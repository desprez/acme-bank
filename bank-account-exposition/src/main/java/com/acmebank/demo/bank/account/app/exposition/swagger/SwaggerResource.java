package com.acmebank.demo.bank.account.app.exposition.swagger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Resource redirection to swagger api documentation
 */
@Controller
public class SwaggerResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerResource.class);

    /**
     * Default constructor
     */
    public SwaggerResource() {
        super();
    }

    @RequestMapping(value = "/")
    public String index() {
        LOGGER.info("swagger-ui.html");
        return "redirect:swagger-ui.html";
    }

}
