package ODL.web.authservice.service.email;

import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ThymeleafTemplateService {

    TemplateEngine textTemplateEngine;

    public String process(String template, Locale locale, Map<String, Object> model) {
        Context context = new Context(locale, model);
        return textTemplateEngine.process(template, context);
    }
}
