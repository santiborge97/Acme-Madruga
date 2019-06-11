
package controllers.actor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import services.ActorService;
import services.BoxService;
import services.ConfigurationService;
import domain.Actor;
import domain.Box;

@Controller
@RequestMapping("/box/actor")
public class BoxActorController extends AbstractController {

	@Autowired
	private ActorService			actorService;

	@Autowired
	private BoxService				boxService;

	@Autowired
	private ConfigurationService	configurationService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		final ModelAndView result;
		final Collection<Box> boxes;
		Actor a;

		a = this.actorService.findByPrincipal();

		final String banner = this.configurationService.findConfiguration().getBanner();

		boxes = this.boxService.findAllBoxByActor(a.getId());

		result = new ModelAndView("box/list");
		result.addObject("boxes", boxes);
		result.addObject("banner", banner);
		result.addObject("requestURI", "box/actor/list.do");
		return result;

	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Collection<Box> boxes;
		Actor a;
		a = this.actorService.findByPrincipal();
		final Box box = this.boxService.create();
		boxes = this.boxService.findAllBoxByActor(a.getId());

		result = this.createEditModelAndView(box);
		result.addObject("boxes", boxes);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int boxId) {
		ModelAndView result;
		Box box;
		Boolean security;

		if (this.boxService.existId(boxId)) {
			security = this.boxService.boxSecurity(boxId);

			if (security) {
				box = this.boxService.findOne(boxId);
				Assert.notNull(box);

				final Collection<Box> boxes;
				Actor a;
				a = this.actorService.findByPrincipal();
				boxes = this.boxService.findAllBoxByActor(a.getId());
				boxes.remove(box);

				final String banner = this.configurationService.findConfiguration().getBanner();

				result = new ModelAndView("box/edit");
				result.addObject("boxes", boxes);
				result.addObject("box", box);
				result.addObject("banner", banner);
				result.addObject("messageError", null);

			} else
				result = new ModelAndView("redirect:/welcome/index.do");

		} else {

			result = new ModelAndView("misc/notExist");
			final String banner = this.configurationService.findConfiguration().getBanner();
			result.addObject("banner", banner);

		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Box box, final BindingResult binding) {
		ModelAndView result;

		final Collection<Box> boxes;
		Actor a;
		a = this.actorService.findByPrincipal();
		boxes = this.boxService.findAllBoxByActor(a.getId());

		final Box boxReconstruct = this.boxService.reconstruct(box, binding);

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(box);
			result.addObject("boxes", boxes);
		} else
			try {

				this.boxService.save(boxReconstruct);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(box, "box.commit.error");
				result.addObject("boxes", boxes);
			}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Box box, final BindingResult binding) {
		ModelAndView result;
		final Box boxReconstruct = this.boxService.reconstruct(box, binding);

		try {
			this.boxService.delete(boxReconstruct);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {

			final Collection<Box> boxes;
			Actor a;
			a = this.actorService.findByPrincipal();
			boxes = this.boxService.findAllBoxByActor(a.getId());

			final String banner = this.configurationService.findConfiguration().getBanner();

			result = new ModelAndView("box/edit");
			result.addObject("box", box);
			result.addObject("banner", banner);
			result.addObject("messageError", "box.commit.error");
			result.addObject("boxes", boxes);

		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Box box) {

		ModelAndView result;

		result = this.createEditModelAndView(box, null);

		return result;
	}

	private ModelAndView createEditModelAndView(final Box box, final String messageCode) {

		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("box/edit");
		result.addObject("box", box);
		result.addObject("banner", banner);
		result.addObject("messageError", messageCode);

		return result;
	}

}
