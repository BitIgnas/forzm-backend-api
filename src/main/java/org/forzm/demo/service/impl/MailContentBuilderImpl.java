package org.forzm.demo.service.impl;

import lombok.AllArgsConstructor;
import org.forzm.demo.service.MailContentBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class MailContentBuilderImpl implements MailContentBuilder {

    private final TemplateEngine templateEngine;

    @Override
    public String build(String link) {
        Context context = new Context();
        context.setVariable("link", link);
        return templateEngine.process("mail-template", context);
    }
}
