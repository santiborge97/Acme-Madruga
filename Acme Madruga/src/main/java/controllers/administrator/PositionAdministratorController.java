
package controllers.administrator;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import services.ConfigurationService;
import services.EnrolmentService;
import services.PositionService;
import domain.Enrolment;
import domain.Position;

@Controller
@RequestMapping("/position/administrator")
public class PositionAdministratorController extends AbstractController {

	//Services

	@Autowired
	private PositionService			positionService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private EnrolmentService		enrolmentService;


	// Methods

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int positionId) {

		final String banner = this.configurationService.findConfiguration().getBanner();

		ModelAndView result;
		final Boolean exist = this.positionService.existId(positionId);
		if (exist) {
			final Position position;

			position = this.positionService.findOne(positionId);

			result = new ModelAndView("administrator/displayPosition");
			result.addObject("position", position);
			result.addObject("banner", banner);
		} else {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);

		}

		return result;

	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Position> positions;

		positions = this.positionService.findAll();

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("administrator/listPosition");
		result.addObject("positions", positions);
		result.addObject("banner", banner);
		result.addObject("requestURI", "position/administrator/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final Position position;

		position = this.positionService.create();
		result = this.createEditModelAndView(position);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int positionId) {
		final ModelAndView result;
		final Boolean exist = this.positionService.existId(positionId);
		final Position position;

		final String banner = this.configurationService.findConfiguration().getBanner();

		if (exist) {
			position = this.positionService.findOne(positionId);

			Boolean notEnrol = false;
			final Collection<Enrolment> enrols = this.enrolmentService.findEnrolmentPerPosition(position.getId());

			if (enrols.isEmpty())
				notEnrol = true;

			Assert.notNull(position);
			result = this.createEditModelAndView(position);
			result.addObject("notEnrol", notEnrol);
		} else {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Position position, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(position);
		else
			try {

				this.positionService.save(position);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(position, "position.commit.error");
			}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Position position, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(position);
		else
			try {
				this.positionService.delete(position);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(position, "position.commit.error");
			}
		return result;
	}
	// Ancillary methods

	protected ModelAndView createEditModelAndView(final Position position) {
		ModelAndView result;

		result = this.createEditModelAndView(position, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Position position, final String messageCode) {
		ModelAndView result;
		final Collection<Position> positions;

		positions = this.positionService.findAll();

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("administrator/editPosition");
		result.addObject("position", position);
		result.addObject("positions", positions);
		result.addObject("banner", banner);

		result.addObject("messageError", messageCode);

		return result;
	}

}
