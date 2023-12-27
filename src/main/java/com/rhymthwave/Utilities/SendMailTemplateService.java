package com.rhymthwave.Utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Arrays;


@Service
public class SendMailTemplateService {
	@Autowired
	GetHostByRequest host;

	private static final String MAIL_TEMPLATE_BASE_NAME = "templateMail";
	private static final String MAIL_TEMPLATE_PREFIX = "/templates/Mail/";
	private static final String MAIL_TEMPLATE_SUFFIX = ".html";
	private static final String UTF_8 = "UTF-8";

	private static TemplateEngine templateEngine;

	static {
		templateEngine = emailTemplateEngine();
	}

	private static TemplateEngine emailTemplateEngine() {
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(htmlTemplateResolver());
		templateEngine.setTemplateEngineMessageSource(emailMessageSource());
		return templateEngine;
	}

	private static ResourceBundleMessageSource emailMessageSource() {
		final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename(MAIL_TEMPLATE_BASE_NAME);
		return messageSource;
	}

	private static ITemplateResolver htmlTemplateResolver() {
		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix(MAIL_TEMPLATE_PREFIX);
		templateResolver.setSuffix(MAIL_TEMPLATE_SUFFIX);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding(UTF_8);
		templateResolver.setCacheable(false);
		return templateResolver;
	}

	// For artist and podcaster
	public String getContentForConfirm(String owner, String templateName, String type, String address) {
		final Context context = new Context();
		context.setVariable("owner", owner);
		if (type.equalsIgnoreCase("podcast")) {
			context.setVariable("content",
					"is your email address, you’ll be able to publish episodes and distribute your podcast.");
			context.setVariable("address", address);
			context.setVariable("type", "Pobcaster");
		}else {
			context.setVariable("content",
					"is your email address, you’ll be received from us. Please follow your email to receive latest notifications.");
			context.setVariable("address", address);
			context.setVariable("type", "Artist");
		}
		return templateEngine.process(templateName, context);
	}

	// Send template email  For Role
	public String getContentForNews(String title, String templateName, String content, String url) {
		final Context context = new Context();
		String urls[] = url.split(";");
		context.setVariable("title", title);
		context.setVariable("content", content);
		context.setVariable("img", urls[0]);
		context.setVariable("toUrl", urls[1]);

		return templateEngine.process(templateName, context);
	}
	
	public String getAdvertisingResults(Long listened ,  Long clicked  , Long approach, String templateName) {
		final Context context = new Context();
		context.setVariable("listened", listened);
		context.setVariable("clicked", clicked);
		context.setVariable("approach", approach);

		return templateEngine.process(templateName, context);
	}
	
	public String getContentForReport(String templateName) {
		final Context context = new Context();

		return templateEngine.process(templateName, context);
	}

	public String getContentForArtist(String artistName, String string) {
		final Context context = new Context();
		context.setVariable("artistName", artistName);
		return templateEngine.process(string, context);
	}

	public String getContentForConfirmAccount(String templateName, String url,String email) {
		final Context context = new Context();
		context.setVariable("url", url);
		context.setVariable("email", email);
		return templateEngine.process(templateName, context);
	}
    

}
