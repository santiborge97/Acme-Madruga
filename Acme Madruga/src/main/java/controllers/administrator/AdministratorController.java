/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdministratorService;
import services.ConfigurationService;
import controllers.AbstractController;
import domain.Actor;

@Controller
@RequestMapping("/actor/administrator")
public class AdministratorController extends AbstractController {

	//Services

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private AdministratorService	administratorService;


	//Methods

	@RequestMapping(value = "/score/list", method = RequestMethod.GET)
	public ModelAndView listScore() {

		final ModelAndView result;
		final Collection<Actor> actors;

		actors = this.actorService.findAll();

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("administrator/listActor");
		result.addObject("score", true);
		result.addObject("spam", false);
		result.addObject("profile", false);
		result.addObject("actors", actors);
		result.addObject("requestURI", "actor/administrator/score/list.do");
		result.addObject("banner", banner);

		return result;

	}

	@RequestMapping(value = "/spammer/list", method = RequestMethod.GET)
	public ModelAndView listSpammer() {

		final ModelAndView result;
		final Collection<Actor> actors;

		actors = this.actorService.findAll();

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("administrator/listActor");
		result.addObject("score", false);
		result.addObject("spam", true);
		result.addObject("profile", false);
		result.addObject("actors", actors);
		result.addObject("requestURI", "actor/administrator/spammer/list.do");
		result.addObject("banner", banner);

		return result;

	}

	@RequestMapping(value = "/score/calculate", method = RequestMethod.GET)
	public ModelAndView calculate() {
		ModelAndView result;

		this.administratorService.calculateScore();
		result = new ModelAndView("redirect:/actor/administrator/score/list.do");

		return result;
	}

	@RequestMapping(value = "/spammer/calculate", method = RequestMethod.GET)
	public ModelAndView spammer() {
		ModelAndView result;

		this.administratorService.spammer();
		result = new ModelAndView("redirect:/actor/administrator/spammer/list.do");

		return result;
	}

	@RequestMapping(value = "/score/banActor", method = RequestMethod.GET)
	public ModelAndView banScore(@RequestParam final int actorId) {
		ModelAndView result;
		Actor actor;
		final Boolean exist = this.actorService.existActor(actorId);

		final String banner = this.configurationService.findConfiguration().getBanner();

		actor = this.actorService.findOne(actorId);

		if (!exist) {
			result = new ModelAndView("misc/notExist");
			result.addObject("id", actorId);
		} else if (actor.getScore() < -0.5) {
			this.actorService.banOrUnBanActor(actor);

			result = new ModelAndView("redirect:/actor/administrator/score/list.do");
			result.addObject("banner", banner);
		} else
			result = new ModelAndView("redirect:/welcome/index.do");

		return result;

	}

	@RequestMapping(value = "/spammer/banActor", method = RequestMethod.GET)
	public ModelAndView banSpammer(@RequestParam final int actorId) {
		ModelAndView result;
		Actor actor;
		final Boolean exist = this.actorService.existActor(actorId);

		final String banner = this.configurationService.findConfiguration().getBanner();

		if (exist) {
			actor = this.actorService.findOne(actorId);
			if (actor.getSpammer()) {
				this.actorService.banOrUnBanActor(actor);

				result = new ModelAndView("redirect:/actor/administrator/score/list.do");

			} else
				result = new ModelAndView("redirect:/welcome/index.do");

		} else {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		}

		return result;

	}

	@RequestMapping(value = "/profile/list", method = RequestMethod.GET)
	public ModelAndView listProfile() {

		final ModelAndView result;
		final Collection<Actor> actors;

		actors = this.actorService.findAll();

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("administrator/listActor");
		result.addObject("score", false);
		result.addObject("spam", false);
		result.addObject("admin", true);
		result.addObject("actors", actors);
		result.addObject("requestURI", "actor/administrator/profile/list.do");
		result.addObject("banner", banner);

		return result;

	}

	@RequestMapping(value = "profile/displayActor", method = RequestMethod.GET)
	public ModelAndView displayProfile(@RequestParam final int actorId) {
		ModelAndView result;
		Actor actor;

		final Actor principal = this.actorService.findByPrincipal();
		final Boolean exist = this.actorService.existActor(actorId);

		if (exist) {
			actor = this.actorService.findOne(actorId);
			Assert.notNull(actor);

			final String banner = this.configurationService.findConfiguration().getBanner();

			result = new ModelAndView("actor/display");
			result.addObject("actor", actor);
			result.addObject("principal", principal);
			result.addObject("banner", banner);
			result.addObject("admin", true);
		} else
			result = new ModelAndView("misc/notExist");

		return result;

	}

	@RequestMapping(value = "profile/deleteProfile", method = RequestMethod.GET)
	public ModelAndView deleteProfile(@RequestParam final int actorId) {
		ModelAndView result;

		final Actor principal = this.actorService.findByPrincipal();

		final String banner = this.configurationService.findConfiguration().getBanner();

		final Boolean exist = this.actorService.existActor(actorId);

		if (exist) {
			final Actor delete = this.actorService.findOne(actorId);
			if (!delete.equals(principal))
				try {
					this.actorService.masterDelete(actorId);
					result = new ModelAndView("redirect:/welcome/index.do");
				} catch (final Throwable oops) {
					result = new ModelAndView("redirect:/actor/administrator/profile/displayActor.do?actorId=" + actorId);
				}
			else
				result = new ModelAndView("redirect:/welcome/index.do");
		} else {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		}

		return result;
	}
}
