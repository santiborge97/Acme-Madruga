
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import services.ConfigurationService;
import controllers.AbstractController;
import domain.Area;

@Controller
@RequestMapping("/area/administrator")
public class AdministratorAreaController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public AdministratorAreaController() {
		super();
	}


	// Services ---------------------------------------------------------------
	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private AreaService				areaService;


	// Create Area ------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createArea() {
		final ModelAndView result;
		final Area area = this.areaService.create();

		result = this.createEditModelAndView(area, null);
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Area> areas;

		areas = this.areaService.findAll();

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("administrator/listAreas");
		result.addObject("areas", areas);
		result.addObject("banner", banner);
		result.addObject("requestURI", "area/administrator/list.do");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int areaId) {
		ModelAndView result;
		final Area area;

		final String banner = this.configurationService.findConfiguration().getBanner();
		try {
			area = this.areaService.findOne(areaId);

			result = this.createEditModelAndView(area, null);
		} catch (final IllegalArgumentException e) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);

		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute(value = "area") Area area, final BindingResult binding) {
		ModelAndView result;

		area = this.areaService.reconstruct(area, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(area, null);
		else
			try {
				this.areaService.save(area);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(area, "commit.error");
			}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Area area, final BindingResult binding) {
		ModelAndView result;

		area = this.areaService.reconstruct(area, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(area, "commit.error");
		else
			try {
				this.areaService.delete(area);
				result = new ModelAndView("redirect:/area/administrator/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(area, "commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Area area, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("administrator/createArea");
		result.addObject("area", area);
		result.addObject("banner", banner);
		result.addObject("messageError", messageCode);
		final String countryCode = this.configurationService.findConfiguration().getCountryCode();
		result.addObject("defaultCountry", countryCode);

		return result;
	}
}
