package com.tothenew.sharda.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PageController {

    @Autowired
    MessageSource messageSource;

    @GetMapping(path="/international")
    public String apiInternationalized() {
        return messageSource.getMessage("greeting.messages", null, LocaleContextHolder.getLocale());
    }
}
