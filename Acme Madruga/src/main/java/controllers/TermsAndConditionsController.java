
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;

@Controller
@RequestMapping("/termsAndConditions")
public class TermsAndConditionsController extends AbstractController {

	@Autowired
	private ConfigurationService	configurationService;


	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView termsAndConditions() {
		ModelAndView result;

		final String language = LocaleContextHolder.getLocale().getLanguage();

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("misc/termsAndConditions");
		result.addObject("language", language);
		result.addObject("banner", banner);

		return result;
	}

}
