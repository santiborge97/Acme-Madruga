
package controllers.administrator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import domain.Configuration;

@Controller
@RequestMapping("/configuration/administrator")
public class ConfigurationAdministratorController {

	// Services

	@Autowired
	private ConfigurationService	configurationService;


	// Methods

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		final ModelAndView result;
		final Configuration configuration;

		configuration = this.configurationService.findConfiguration();
		Assert.notNull(configuration);
		result = this.createEditModelAndView(configuration);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Configuration configuration, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(configuration);
		else
			try {
				this.configurationService.save(configuration);
				result = new ModelAndView("redirect:edit.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(configuration, "configuration.commit.error");
			}
		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final Configuration configuration) {
		ModelAndView result;

		result = this.createEditModelAndView(configuration, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Configuration configuration, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("administrator/editConfiguration");
		result.addObject("configuration", configuration);
		result.addObject("messageError", messageCode);

		return result;
	}
}
