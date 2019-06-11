
package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import services.ConfigurationService;
import domain.Administrator;
import forms.RegisterAdministratorForm;

@Controller
@RequestMapping("/administrator")
public class RegisterAdministratorController {

	//Services

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ConfigurationService	configurationService;


	// Methods

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final RegisterAdministratorForm administrator;

		administrator = new RegisterAdministratorForm();
		result = this.createEditModelAndView(administrator);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("administrator") final RegisterAdministratorForm form, final BindingResult binding) {
		ModelAndView result;

		final Administrator adminReconstruct = this.administratorService.reconstruct(form, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(form);
		else if (!form.checkPassword())
			result = this.createEditModelAndView(form, "administrator.commit.error");
		else
			try {
				this.administratorService.save(adminReconstruct);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(form, "administrator.commit.error");
			}
		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final RegisterAdministratorForm administrator) {
		ModelAndView result;

		result = this.createEditModelAndView(administrator, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final RegisterAdministratorForm administrator, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("administrator/signUpAdministrator");
		result.addObject("administrator", administrator);
		result.addObject("banner", banner);
		result.addObject("messageError", messageCode);
		final String countryCode = this.configurationService.findConfiguration().getCountryCode();
		result.addObject("defaultCountry", countryCode);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Administrator administrator) {
		ModelAndView result;

		result = this.createEditModelAndView(administrator, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Administrator administrator, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		final RegisterAdministratorForm administratorForm = new RegisterAdministratorForm();

		administratorForm.setId(administrator.getId());
		administratorForm.setVersion(administrator.getVersion());
		administratorForm.setName(administrator.getName());
		administratorForm.setMiddleName(administrator.getMiddleName());
		administratorForm.setSurname(administrator.getSurname());
		administratorForm.setPhoto(administrator.getPhoto());
		administratorForm.setEmail(administrator.getEmail());
		administratorForm.setPhone(administrator.getPhone());
		administratorForm.setAddress(administrator.getAddress());
		administratorForm.setUsername(administrator.getUserAccount().getUsername());

		result = new ModelAndView("administrator/signUpAdministrator");
		result.addObject("administrator", administratorForm);
		result.addObject("banner", banner);
		result.addObject("messageError", messageCode);
		final String countryCode = this.configurationService.findConfiguration().getCountryCode();
		result.addObject("defaultCountry", countryCode);

		return result;
	}
}
