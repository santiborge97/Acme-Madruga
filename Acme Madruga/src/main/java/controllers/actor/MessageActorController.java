
package controllers.actor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import services.ActorService;
import services.BoxService;
import services.ConfigurationService;
import services.MessageService;
import domain.Message;
import forms.MessageForm;

@Controller
@RequestMapping("/message/actor")
public class MessageActorController extends AbstractController {

	@Autowired
	private MessageService			messageService;

	@Autowired
	private BoxService				boxService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private ActorService			actorService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int boxId) {
		ModelAndView result;
		final Collection<Message> messages;
		Boolean security;

		final Boolean exist = this.boxService.existId(boxId);

		if (exist) {

			security = this.boxService.boxSecurity(boxId);

			if (security) {

				messages = this.messageService.findMessagesByBoxId(boxId);

				final String banner = this.configurationService.findConfiguration().getBanner();

				result = new ModelAndView("message/list");
				result.addObject("messages", messages);
				result.addObject("banner", banner);
				result.addObject("boxId", boxId);
				result.addObject("requestURI", "message/actor/list.do");

			} else
				result = new ModelAndView("redirect:/welcome/index.do");
		} else
			result = new ModelAndView("misc/notExist");

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int messageId, @RequestParam final int boxId) {
		ModelAndView result;
		final Message message1;
		final Boolean security;

		final Boolean existMessage = this.messageService.existId(messageId);
		final Boolean existBox = this.boxService.existId(boxId);

		if (existMessage && existBox) {

			security = this.messageService.securityMessage(boxId);

			if (security) {

				final String banner = this.configurationService.findConfiguration().getBanner();

				message1 = this.messageService.findOne(messageId);

				result = new ModelAndView("message/display");
				result.addObject("message1", message1);
				result.addObject("box", this.boxService.findOne(boxId));
				result.addObject("banner", banner);
				result.addObject("requestURI", "message/actor/display.do");

			} else
				result = new ModelAndView("redirect:/welcome/index.do");

		} else
			result = new ModelAndView("misc/notExist");

		return result;
	}
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int actorId) {
		final ModelAndView result;

		final Boolean exist = this.actorService.existActor(actorId);
		if (exist) {
			final MessageForm message2 = this.messageService.create(actorId);

			result = this.createEditModelAndView(message2);
		} else
			result = new ModelAndView("misc/notExist");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute(value = "message") final MessageForm message2, final BindingResult binding) {
		final Message message3 = this.messageService.reconstruct(message2, binding);
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(message2);
		else
			try {
				this.messageService.save(message3);
				result = new ModelAndView("redirect:/box/actor/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(message2, "message.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int messageId, @RequestParam final int boxId) {

		ModelAndView result;
		final Message message1;
		final Boolean security;

		final Boolean existMessage = this.messageService.existId(messageId);
		final Boolean existBox = this.boxService.existId(boxId);

		security = this.messageService.securityMessage(boxId);

		if (existMessage && existBox) {
			if (security) {
				final String banner = this.configurationService.findConfiguration().getBanner();

				message1 = this.messageService.findOne(messageId);

				this.messageService.delete(message1);

				result = new ModelAndView("redirect:/welcome/index.do");
				result.addObject("banner", banner);

			} else
				result = new ModelAndView("redirect:/welcome/index.do");
		} else
			result = new ModelAndView("misc/notExist");

		return result;
	}

	protected ModelAndView createEditModelAndView(final MessageForm message2) {
		final ModelAndView result;
		result = this.createEditModelAndView(message2, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final MessageForm message2, final String errorText) {
		final ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();
		final Collection<String> priorities = this.configurationService.findConfiguration().getPriorities();

		result = new ModelAndView("message/create");
		result.addObject("messageError", errorText);
		result.addObject("message", message2);
		result.addObject("banner", banner);
		result.addObject("priorities", priorities);
		result.addObject("enlace", "message/actor/edit.do");

		return result;
	}
}
