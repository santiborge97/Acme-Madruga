
package controllers.brotherhood;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ConfigurationService;
import services.MessageService;
import services.ProcessionService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Float;
import domain.Procession;

@Controller
@RequestMapping("/procession/brotherhood")
public class ProcessionBrotherhoodController extends AbstractController {

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private ProcessionService		processionService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private MessageService			messageService;


	//List---------------------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		final ModelAndView result;
		final Collection<Procession> processions;
		final Brotherhood b;

		b = this.brotherhoodService.findByPrincipal();

		processions = this.processionService.findProcessionByBrotherhoodId(b.getId());

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("procession/list");
		result.addObject("processions", processions);
		result.addObject("requestURI", "procession/brotherhood/list.do");
		result.addObject("pagesize", 5);
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
		result.addObject("autoridad", "brotherhood");

		return result;

	}

	@RequestMapping(value = "/listByProcession", method = RequestMethod.GET)
	public ModelAndView listByProcession(@RequestParam final int processionId) {

		final ModelAndView result;
		final Collection<Float> floats;

		final String banner = this.configurationService.findConfiguration().getBanner();

		final Procession procession = this.processionService.findOne(processionId);
		if (procession == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {
			floats = procession.getFloats();

			result = new ModelAndView("float/list");
			result.addObject("floats", floats);
			result.addObject("requestURI", "procession/brotherhood/listByProcession.do");
			result.addObject("pagesize", 5);
			result.addObject("banner", banner);
		}

		return result;

	}
	//Display------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int processionId) {
		final ModelAndView result;
		final Brotherhood login;
		final Brotherhood owner;

		final String banner = this.configurationService.findConfiguration().getBanner();
		final Procession processionFound = this.processionService.findOne(processionId);

		if (processionFound == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {

			login = this.brotherhoodService.findByPrincipal();
			owner = processionFound.getBrotherhood();

			if (login.getId() == owner.getId()) {
				result = new ModelAndView("procession/display");
				result.addObject("procession", processionFound);
				result.addObject("banner", banner);

			} else
				result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

	//Create-----------------------------------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;

		final Brotherhood b;
		b = this.brotherhoodService.findByPrincipal();

		if (b.getArea() == null)
			result = new ModelAndView("misc/noArea");
		else {
			final Procession procession = this.processionService.create();
			final String banner = this.configurationService.findConfiguration().getBanner();

			result = new ModelAndView("procession/edit");
			result.addObject("procession", procession);
			result.addObject("banner", banner);
		}
		return result;

	}
	//Editar-------------------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int processionId) {
		ModelAndView result;
		Procession procession;
		Boolean security;

		final Brotherhood b;
		b = this.brotherhoodService.findByPrincipal();
		final Procession processionFind = this.processionService.findOne(processionId);
		final String banner = this.configurationService.findConfiguration().getBanner();

		if (b.getArea() == null) {
			result = new ModelAndView("misc/noArea");
			result.addObject("banner", banner);
		} else if (processionFind == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {

			procession = this.processionService.findOne(processionId);
			security = this.processionService.processionBrotherhoodSecurity(processionId);

			if (security)
				result = this.createEditModelAndView(procession, null);
			else
				result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute(value = "procession") Procession procession, final BindingResult binding) {
		ModelAndView result;

		final Brotherhood b;
		b = this.brotherhoodService.findByPrincipal();

		if (b.getArea() == null)
			result = new ModelAndView("misc/noArea");
		else {

			procession = this.processionService.reconstruct(procession, binding);

			if (binding.hasErrors())
				result = this.createEditModelAndView(procession, null);
			else
				try {
					this.processionService.save(procession);

					if (procession.getFinalMode())
						this.messageService.NotificationNewProcession(procession);

					result = new ModelAndView("redirect:list.do");
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(procession, "procession.commit.error");

				}
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Procession procession, final BindingResult binding) {
		ModelAndView result;

		procession = this.processionService.findOne(procession.getId());

		try {
			this.processionService.delete(procession);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(procession, "procession.commit.error");
		}

		return result;
	}

	//Other business methods------------------------------------------------------------------------------------------
	protected ModelAndView createEditModelAndView(final Procession procession, final String messageCode) {
		final ModelAndView result;

		final Collection<Float> floats = procession.getFloats();

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("procession/edit");
		result.addObject("procession", procession);
		result.addObject("floats", floats);
		result.addObject("messageError", messageCode);
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());

		return result;
	}
}
