
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import services.ConfigurationService;
import services.MessageService;
import domain.Message;
import forms.MessageForm;

@Controller
@RequestMapping("/broadcast/administrator")
public class BroadcastAdministratorController extends AbstractController {

	@Autowired
	private MessageService			messageService;

	@Autowired
	private ConfigurationService	configurationService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;

		final MessageForm message2 = this.messageService.create2();

		result = this.createEditModelAndView(message2);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute(value = "message") final MessageForm message1, final BindingResult binding) {
		final Message message2 = this.messageService.reconstruct(message1, binding);
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(message1);
		else
			try {
				this.messageService.broadcastSystem(message2);
				this.messageService.save(message2);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(message1, "message.commit.error");
			}
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
		result.addObject("enlace", "broadcast/administrator/create.do");

		return result;
	}
}
